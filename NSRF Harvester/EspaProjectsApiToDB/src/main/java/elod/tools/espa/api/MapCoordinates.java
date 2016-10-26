package elod.tools.espa.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 * this class was used to get the map coordinates(GEO Data) for the project that were already harvested. 
 * Afterwards this was empeded as a method on the main application
 */
public class MapCoordinates {
	String DB_ADDRS="";
	String DB_USER="";
	String DB_PASS=	"";
	Connection conn = null;		
	public static void main(String[] args) throws SQLException, FileNotFoundException, IOException, InterruptedException{

		if(args.length<1){
			System.out.println("No properies file");
			System.exit(1);
		}else if(!new File(args[0]).exists()){
			System.out.println("No properies file");
			System.exit(1);
		}
		MapCoordinates mc=new MapCoordinates();
		mc.loadProp(args[0]);
		mc.conn = DriverManager.getConnection(mc.DB_ADDRS,mc.DB_USER,mc.DB_PASS);
		int c=0;
		while(!mc.getProjects(c++)){}
		System.exit(99);
		
	}
	/**
	 * read the file passed as a parameter and loads the necessary properties.
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void loadProp(String file) throws FileNotFoundException, IOException{
		Properties prop = new Properties();			
		prop.load(new FileInputStream(file));	
		DB_ADDRS=prop.getProperty("DataBase");
		DB_USER=prop.getProperty("Username");	
		DB_PASS=prop.getProperty("Password");
	}
	
	/**
	 * read from the database an offset of 100 projects whose map coordinates are null
	 * then use the project code (OPS) to connect to the anaptyxi website and get the map coordinates
	 * and update the database
	 * @param counter which offset to use
	 * @return true if there are no more records to get, false otherwise
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public boolean getProjects(int counter) throws SQLException, InterruptedException{
		try {
			conn.setAutoCommit(false);
			Statement getProjectsNullMap= conn.createStatement();
			ResultSet projectKey=getProjectsNullMap.executeQuery(
					"SELECT `Overview`.`id`,`Overview`.`kodikos`FROM `Overview`" +
						"where  `Overview`.`map_coordinates` is null limit 100 offset "+counter+";");
			if(projectKey.isAfterLast()){
				//no results
				return true;
			}
			while(projectKey.next()){
				System.out.println("OPS:"+projectKey.getString(2));
				try {
					for(StringBuilder s:getMap(projectKey.getString(2))){
						//for every coordinate pack(might be one pair (point) or muliple (line)
						//insert to the coordinates table
						PreparedStatement insertCoordinates = null;
						insertCoordinates= conn.prepareStatement("INSERT INTO `Coordinates`" +
								" (`coordinates`) VALUES(?);",Statement.RETURN_GENERATED_KEYS);
						insertCoordinates.setString(1,s.toString());
						insertCoordinates.executeUpdate();	
						ResultSet coordinateKeys=insertCoordinates.getGeneratedKeys();
						while(coordinateKeys.next()){
							//for every key returned, create a record on Coordinates_Project table
							PreparedStatement insertProjectCoordinates = null;
							insertProjectCoordinates= conn.prepareStatement("INSERT INTO `Coordinates_Project`" +
									" (`projectId`,`coordinatesId`)" +
									" VALUES(?,?);");
							insertProjectCoordinates.setInt(1,projectKey.getInt(1) );
							insertProjectCoordinates.setInt(2, coordinateKeys.getInt(1));
							insertProjectCoordinates.executeUpdate();
							insertProjectCoordinates.close();
						}
						coordinateKeys.close();
						insertCoordinates.close();
					}
					//update project table and change the map column to denote that this project's coordinates are done
			
					Statement UpdateMap=conn.createStatement();
					UpdateMap.executeUpdate(
							"update `Overview` set map_coordinates=1 where id="+projectKey.getInt(1));
					UpdateMap.close();	
					conn.commit();
					System.out.println("OK");
					
				} catch (IOException e) {
					e.printStackTrace();
					conn.rollback();
					return false;
				}catch(StringIndexOutOfBoundsException s){
					//there is no map on this project
					//do nothing and move to the next
				}
				Thread.sleep(1000);
			}
			projectKey.close();
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
			return false;
		}
		
		
		return false;
	}
	
	/**
	 * get the project code and connect to the anaptyxi webpage to read the geo data.
	 * store them in a List of StringBuilder and return it
	 * @param ops the project code to search for
	 * @return a List of StringBuilder
	 * @throws IOException
	 */
	public List<StringBuilder> getMap(String ops) throws IOException{
		Document doc= Jsoup.parse(new URL("http://anaptyxi.gov.gr/ergopopup.aspx?mis="+ops),20000);
		List<StringBuilder> coordinateList=new ArrayList<StringBuilder>();
	
		String body=doc.body().toString();
		String map=null;
		try{
			//if there is not a point on the map it throws a StringIndexOutOfBoundsException
			//handle it on the reader
			map=body.substring(body.indexOf("<kml"),body.lastIndexOf("</kml"));
		}catch(StringIndexOutOfBoundsException d){
			throw d;
		}	
		System.out.println(map);
		Document doc2= Jsoup.parse(map);
		for(Element e:doc2.select("coordinates")){
			System.out.println("---------");
			String[] coordinates=e.text().replace(", ",",").split(" ");
			StringBuilder sb=new StringBuilder();
			for(int i=0;i<coordinates.length-1;i++){			
				String[] longLat=coordinates[i].split(",");
				sb.append(longLat[1]+" "+longLat[0]+",");	
			}
			String[] longLat=coordinates[coordinates.length-1].split(",");
			sb.append(longLat[1]+" "+longLat[0]);
			coordinateList.add(sb);
			
		}
		return coordinateList;

	}
}
