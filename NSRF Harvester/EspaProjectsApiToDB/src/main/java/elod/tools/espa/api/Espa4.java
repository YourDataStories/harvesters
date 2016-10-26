package elod.tools.espa.api;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Espa4 {
	String DB_ADDRS="";
	String DB_USER="";
	String DB_PASS=	"";
	/*
	 delete from Projects_Sellers where id>=1;
alter table Projects_Sellers auto_increment=1;
delete from SubProject_Project where id>=1;
alter table SubProject_Project auto_increment=1;
delete from Details where id>=1;
alter table  Details auto_increment=1;
delete from Sub_Projects where id>=1;
alter table Sub_Projects auto_increment=1;
delete from Status where id>=1;
alter table Status auto_increment=1;
delete from Project_Files where id>=1;
alter table Project_Files auto_increment=1;
delete from Files where id>=1;
alter table Files auto_increment=1;
delete from Overview where id>=1;
alter table Overview auto_increment=1;
delete from Diaxeirisi where id>=1;
alter table Diaxeirisi auto_increment=1;
delete from Dikaioyxos where id>=1;
alter table Dikaioyxos auto_increment=1;
delete from Egrish where id>=1;
alter table Egrish auto_increment=1;
delete from Leitoyrgia where id>=1;
alter table Leitoyrgia auto_increment=1;
delete from Parakolouthish where id>=1;
alter table Parakolouthish auto_increment=1;
delete from Prosklhsh where id>=1;
alter table Prosklhsh auto_increment=1;
delete from Protash where id>=1;
alter table Protash auto_increment=1;
delete from Sellers where id>=1;
alter table Sellers auto_increment=1;
delete from Xrimatodotish where id>=1;
alter table Xrimatodotish auto_increment=1;
	 */
	
	int FROM_PAGE=1;
	int TO_PAGE=1;
	int SIZE=1;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Espa4 start=new Espa4();			
		//get the dbconnection info
		try {
			start.loadProp(args[0]);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.out.println("Exception reading the properies file. Termination");
			System.exit(1);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Exception reading the properies file. Termination");
			System.exit(1);
		}
		

		 start.FROM_PAGE=Integer.parseInt(args[1]);
		String type=args[4];
		
		if(start.FROM_PAGE<1){
			start.FROM_PAGE=1;
		}
		

		if(args[2].equals("-")){
			start.TO_PAGE=Integer.MAX_VALUE;
		}
		else
			start.TO_PAGE=Integer.parseInt(args[2]);
		start.SIZE=Integer.parseInt(args[3]);

		

		int i=start.FROM_PAGE;
		String url;
		Object obj;
		
		if(type.equalsIgnoreCase("enisxIn")|| type.equalsIgnoreCase("all")){
			start.start(args[0],"3",false);
		}
		if(type.equalsIgnoreCase("enisxOut")|| type.equalsIgnoreCase("all")){
			start.start(args[0],"3",true);
			}	

if(type.equalsIgnoreCase("ergaIn")|| type.equalsIgnoreCase("all")){
	start.start(args[0],"1",false);
}
if(type.equalsIgnoreCase("ergaOut")|| type.equalsIgnoreCase("all")){
	start.start(args[0],"1",true);
}		
		
		
		
	}
	
	/**
	 * depending on the options passed to the main method, this method will call the API
	 * for the desired projects
	 * @param file the properties file
	 * @param mode the type of projects we want
	 * @param apentagmena (boolean) get the non included
	 */
	public void start(String file,String mode,boolean apentagmena){
		Object obj;
		
		String table;
		if(mode.equals("1")){
			table="erga";
		}else{
			table="enisxiseis";
		}
		int i=FROM_PAGE;
//		start.notif2.addMessage("PAGE mode 3 from page "+FROM_PAGE+" till "+TO_PAGE, "1", false);
		String url="http://anaptyxi.gov.gr/" +
				"GetData.ashx?queryType=projects&pagesize="+SIZE+"&pageMode="+mode+"&pagenum=";
		if(apentagmena){
			url+="&includeApentagmena=true";
		}
		try {
			while((obj=getPage(url+i))!=null){
//						System.out.println("URL:"+url+i);
				i++;
				JSONArray jar=(JSONArray) obj;
//			start.notif2.addMessage(url+i, "6", false,"Erga");
//			start.notif2.runInsert();
				for(int element=0;element<jar.size();element++){
				//	jsons.add((JSONObject)jar.get(element));
//					System.out.println("JSON:\n&@:"+jar.get(element).toString());
					Espa4 start2 =new Espa4();
					try {
						start2.loadProp(file);
						Project pr=start2.getOverview((JSONObject)jar.get(element),table);	
						System.out.println("TRY");
						if(pr!=null){
							System.out.println("IF");
							start2.insertProject(pr);
						}
					}catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
						System.out.println("CONN PROB");e.printStackTrace();
					} catch (java.net.ConnectException e) {System.out.println("CONN PROB");
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 

					
					 }
				if(i>=TO_PAGE){
					break;
				}
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * get the coordinates from the list, and store them to the database
	 * @param coordinates A List of StringBuilder that holds the coordinates. 
	 * @param projectKey the ID of the project that this coordinates are mapped to
	 * @throws SQLException
	 */
	public void storeMapToDb(List<StringBuilder> coordinates,int projectKey) throws SQLException{
		Connection conn = null;				
		conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		for(StringBuilder s:coordinates){
	
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
			insertProjectCoordinates.setInt(1,projectKey );
			insertProjectCoordinates.setInt(2, coordinateKeys.getInt(1));
			insertProjectCoordinates.executeUpdate();
			insertProjectCoordinates.close();
		}
		coordinateKeys.close();
		insertCoordinates.close();
		}
	}
	
	/**
	 * Using the project Code (ops) the method is connected to the website http://anaptyxi.gov.gr
	 * and gets the information of the project. The page is parsed using JSOUP and the coordinates are 
	 * stored and returned in a StringBuilder List. Each point is stored seperatly, but a line(road) 
	 * is stored as a group of coordinates.
	 * @param ops The Project code as a String
	 * @return a StringBuilder List containing the coordinates
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
	 * parse the String url and parse it as a Json Object and return it
	 * @param url
	 * @return Object
	 * @throws IOException
	 */
	public Object getPage(String url) throws IOException{
		System.out.println("GET PAGE\n"+url);
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		// add request header
		request.addHeader("User-Agent", "MySuperUserAgent");

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));

		String line = "";
		
		
		 HttpEntity entity = response.getEntity();
	        if (entity != null) {
	         line=EntityUtils.toString(entity);
	            EntityUtils.consume(entity);    
	            JSONParser parser = new JSONParser();
	    		Object obj ;
	    		
	    		try{
	    		//	System.out.println("TRY"+line);
	    		obj= parser.parse(line);
	    		}
	    		catch(Exception e){			
	    			System.out.println("catch"+e.getStackTrace());
	    			e.printStackTrace();
	    			return null;
	    		}
	    		
	    		try{
	    			JSONArray jar=(JSONArray) obj;
	    		//empty the stringbuilder
	    	
	    		if(jar.size()==0){
	    			return null;
	    		}
	    		}catch(ClassCastException e){
	    			//obj is not array	    		
	    		}
	    		return obj;
	        }
	      
		return null;
	}
	int retries=5;
	
	/**
	 * call all the methods that do the insertions
	 * @param project
	 * @throws SQLException
	 */
	public void insertProject(Project project) throws SQLException{
		
			List<Sub_Projects> subs=project.details.subs;
			List<Body> bodies=project.details.bodies;
			List<Integer> subProjectsIds=new ArrayList<Integer>();
			int overViewId=0;int detailsId=0;
			insertBody(bodies);
			
			subProjectsIds=insertSubProject(subs);	
			overViewId=	insertOverview(project.overview);
			try {
				List<StringBuilder> coordinates=getMap(project.overview.kodikos);
				storeMapToDb(coordinates,overViewId) ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			project.details.projectId=overViewId;
			 detailsId=insertDetails(project.details);
			insertSubProject_Project(detailsId,subProjectsIds);			
			insertFiles(project.details.files);
		
	}
	
	/**
	 * insert into SubProject_Project table of the database.
	 * @param project the projectId from the database
	 * @param subs a list of int with the sub-project ids
	 * @throws SQLException
	 */
	public void insertSubProject_Project(int project,List<Integer> subs)throws SQLException{
				Connection conn = null;				
	try{	conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		PreparedStatement insertProject_Sub=null;
		insertProject_Sub=conn.prepareStatement("INSERT INTO SubProject_Project " +
				"(`subProjectId`,`detailsId`,`publishDate`)" +
				"VALUES(?,?,?);");
		insertProject_Sub.setInt(2,project);
		insertProject_Sub.setString(3, new SimpleDateFormat("YYYY/MM/dd HH:mm:ss", new Locale("el","GR")).format(new Date().getTime()));
		for(int sub:subs){
			insertProject_Sub.setInt(1,sub);
			insertProject_Sub.addBatch();
		}
		
		insertProject_Sub.executeBatch();
		insertProject_Sub.close();
	
	 }catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
			//there was a connection problem with the sql server
			//try again
			if(retries>=0){
				retries--;
				insertSubProject_Project(project, subs);
				}else{
				System.out.println("Error");
				throw e;
			}
		}finally{
		 conn.close();
		 }
	}
	
	/**
	 * insert into Project_Files tables of the DB
	 * @param fileIds a list of int, with the files IDs
	 * @param detailId the details table ID for the project
	 * @throws SQLException
	 */
	public void insertProject_Files(List<Integer> fileIds,int detailId)throws SQLException{
			Connection conn = null;				
	try{	
		conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		PreparedStatement insertProject_Files=null;
		insertProject_Files=conn.prepareStatement("INSERT INTO `Project_Files`" +
				"(`projectDetails`,`fileId`,`publishDate`)" +
				"VALUES(?,?,?);");
		for(int fileId:fileIds){
			
		
			insertProject_Files.setInt(1, detailId);
			insertProject_Files.setInt(2,fileId);
			insertProject_Files.setString(3, new SimpleDateFormat("YYYY/MM/dd HH:mm:ss", new Locale("el","GR")).format(new Date().getTime()));
			insertProject_Files.addBatch();
		}
		insertProject_Files.executeBatch();
		
		insertProject_Files.close();
	
	 }catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
			//there was a connection problem with the sql server
			//try again
			if(retries>=0){
				retries--;				
				insertProject_Files(fileIds, detailId);
			}else{
				System.out.println("Error");
				throw e;
			}
		}finally{
		 conn.close();
		 }
	}
	
	/**
	 * insert into Files table of the database
	 * @param files a List of Files that hold the file information of the project
	 * @return a list of integer with the IDs of the inserted rows
	 * @throws SQLException
	 */
	public List<Integer> insertFiles(List<Files> files)throws SQLException{
		Connection conn = null;				
		conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		PreparedStatement insertFiles=null;
		insertFiles=conn.prepareStatement("INSERT INTO `Files`" +
				"(`filename`,`filecode`,`projectcode`," +
				"`filetype`,`fileurl`,`date`)" +
				"VALUES(?,?,?,?,?,?);", new String[] {"id"}); 
		
		
		for(Files file:files){
			insertFiles.setString(1,file.filename);
			insertFiles.setString(2,file.filecode);
			insertFiles.setString(3,file.projectcode);
			insertFiles.setString(4,file.filetype);
			insertFiles.setString(5,file.fileurl);
			insertFiles.setString(6,file.date);
			insertFiles.addBatch();
		}
		insertFiles.executeBatch();
		int id=0;
		ResultSet generatedKeys = insertFiles.getGeneratedKeys() ;
		List<Integer> ids=new ArrayList<Integer>();
		if(generatedKeys.isBeforeFirst()){
			generatedKeys.next();	
			ids.add(generatedKeys.getInt(1));
		}
		generatedKeys.close();
		insertFiles.close();
		conn.close();
		return ids;
	}
	
	/**
	 * insert into overview table of the Database
	 * @param overviewId The data of the overview Api call, in an Overview object
	 * @return Int, the id of the inserted row
	 * @throws SQLException
	 */
	public int insertOverview(Overview overviewId) throws SQLException{
			Connection conn = null;				
		conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		ResultSet rs=null;
		Statement selectOverview= conn.createStatement();
		rs=selectOverview.executeQuery("select publishDate as date,id from Overview where kodikos="+overviewId.kodikos);
				
		 //if the specific project is been harvested less than a week ago (diffDays<6) skip
		 while(rs.next()){
			 long diff=new Date().getTime()-rs.getDate("date").getTime();
//			 long now=new Date().getTime();
			 long diffDays = diff / (24 * 60 * 60 * 1000);
			if(recentlyInserted(overviewId.kodikos)){//diffDays<6){
				 //if last harvest was less than 6 days ago, skip this project
//				rs.close();
//				conn.close();
				 return rs.getInt("id");				 
			 }
		 }
		
		 rs.close();
			
			
		
		
		
		PreparedStatement OverviewtInsert = null;
		OverviewtInsert= conn.prepareStatement("INSERT INTO `Overview`" +
					"(`kodikos`,`publishDate`,`title`," +
					"`description`,`budget`,`contracts`," +
					"`payments`,`completition_percent`,`start`," +
					"`finish`,`perifereia`,`epKodikos`," +
					"`countIpoergon`,`map_coordinates`,`type`," +
					"`trexousaKatastash`)" +
					"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
		OverviewtInsert.setString(1,overviewId.kodikos);
		
		OverviewtInsert.setString(2,overviewId.publishDate);
		OverviewtInsert.setString(3,overviewId.title);
		OverviewtInsert.setString(4,overviewId.description);
		OverviewtInsert.setString(5,overviewId.budget);
		OverviewtInsert.setString(6,overviewId.contracts);
		OverviewtInsert.setString(7,overviewId.payments);
		OverviewtInsert.setInt(8,overviewId.completition_percent);
		OverviewtInsert.setString(9,overviewId.start);
		OverviewtInsert.setString(10,overviewId.finish);
		OverviewtInsert.setString(11,overviewId.perifereia);
		OverviewtInsert.setString(12,overviewId.epKodikos);
		OverviewtInsert.setString(13,overviewId.countIpoergon);
		OverviewtInsert.setString(14,overviewId.map_coordinates);
		OverviewtInsert.setString(15,overviewId.type);
		OverviewtInsert.setString(16,overviewId.trexousaKatastash);
		
		
	
		
		
		
		
		
		OverviewtInsert.executeUpdate();
		int id=0;
		ResultSet generatedKeys = OverviewtInsert.getGeneratedKeys() ;
		if(generatedKeys.isBeforeFirst()){
			generatedKeys.next();
			id=generatedKeys.getInt(1);
		}
		generatedKeys.close();
		OverviewtInsert.close();
		conn.close();
		
		return id;
	}

	/**
	 * insert into Details table of the database
	 * @param details the data of the details Api call, in a Details Object
	 * @return Int, the id of the inserted row
	 * @throws SQLException
	 */
	public int insertDetails(Details details) throws SQLException{
			//TODO
		Connection conn = null;				
	try{	conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		
	//	ResultSet rs=null;
		PreparedStatement detailsInsert = null;
		detailsInsert= conn.prepareStatement("INSERT INTO `Details`" +
				"(`kodikos`,`title`,`body`," +
				"`budget`,`payments`,`completion`," +
				"`startDate`,`endDate`,`description`," +
				"`statusReportDate`,`statusReport`,`publishDate`," +
				"`projectId`,`titleEnglish`," +
				"`dikeouxos`," +
				"`protash`,`xrimatodotish`,`leitoyrgia`," +
				"`diaxeirisi`,`egrish`,`parakolouthish`," +
				"`prosklhsh`)" +
				"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
		detailsInsert.setString(1, details.kodikos);
		detailsInsert.setString(2, details.title);
		detailsInsert.setString(3, details.body.name);
		detailsInsert.setString(4, details.budget);
		detailsInsert.setString(5, details.payments);
		detailsInsert.setString(6, details.completion);
		detailsInsert.setString(7, details.startDate);
		detailsInsert.setString(8, details.endDate);
		detailsInsert.setString(9, details.description);
		detailsInsert.setString(10, details.statusReportDate);
		detailsInsert.setString(11, details.statusReport);
		detailsInsert.setString(12, details.publishDate);
		detailsInsert.setInt(13, details.projectId);
		detailsInsert.setString(14, details.titleEnglish);
//		System.out.details.body.name);
		detailsInsert.setString(15,null);// AAselectBody(details.body.name));
		try{
			detailsInsert.setInt(16, selectBody(details.protash.name,details.protash.type));
		}catch(NullPointerException e){			
			detailsInsert.setString(16,null);}
		try{
			detailsInsert.setInt(17, selectBody(details.xrimatodotish.name,details.xrimatodotish.type));
		}catch(NullPointerException e){			
			detailsInsert.setString(17,null);}
		
		try{
			System.out.println("££!£$"+selectBody(details.leitoyrgia.name,details.leitoyrgia.type)+".."+details.leitoyrgia.name+".."+details.leitoyrgia.type);
			detailsInsert.setInt(18, selectBody(details.leitoyrgia.name,details.leitoyrgia.type));
		}catch(NullPointerException e){	
			System.out.println("££!£$insert null");
			detailsInsert.setString(18,null);}
		try{
			detailsInsert.setInt(19, selectBody(details.diaxeirisi.name,details.diaxeirisi.type));
		}catch(NullPointerException e){			
			detailsInsert.setString(19,null);}
		
		
		
		try{			
			detailsInsert.setInt(20,selectBody(details.egrish.name,details.egrish.type) );
		}catch(NullPointerException e){			
			detailsInsert.setString(20,null );
		}
		
		try{
			detailsInsert.setInt(21,selectBody(details.parakolouthish.name,details.parakolouthish.type));
		}catch(NullPointerException e){
			detailsInsert.setString(21,null);
		}
		
		try{detailsInsert.setInt(22,selectBody(details.prosklhsh.name,details.prosklhsh.type) );
		}catch(NullPointerException e){
			detailsInsert.setString(22,null );
		}
		detailsInsert.executeUpdate();
//		(`espa_Api4`.`Details`, CONSTRAINT `fk_Details_Egrish` 
//				FOREIGN KEY (`egrish`) REFERENCES `Egrish` (`id`) 
//				ON DELETE NO ACTION ON UPDATE NO ACTION)

		int id=0;
		ResultSet generatedKeys = detailsInsert.getGeneratedKeys() ;
		if(generatedKeys.isBeforeFirst()){
			generatedKeys.next();
			id=generatedKeys.getInt(1);
			generatedKeys.close();
			detailsInsert.close();
			}
		generatedKeys.close();
		detailsInsert.close();
	
		return id;
	 }catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
			//there was a connection problem with the sql server
			//try again
			if(retries>=0){
				retries--;insertDetails(details);
			}else{
				System.out.println("Error");
				throw e;
			}
		}finally{
		 conn.close();
		 }
	return 0;
	}
	
	/**
	 * Insert into Sub_Projects table of the Database
	 * @param subs a List of Sub_Projects Object
	 * @return a List of int with the ids of the inserted rows
	 * @throws SQLException
	 */
	public List<Integer> insertSubProject(List<Sub_Projects> subs) throws SQLException{
				Connection conn = null;		
		List<Integer> ids=new ArrayList<Integer>();		
		try{		
			conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);//"jdbc:mysql://127.0.0.1:3306/messenger","root","salonika");
	
			PreparedStatement subProjectInsert = null;
			subProjectInsert= conn.prepareStatement("INSERT INTO `Sub_Projects`" +
				" (`title`,`budget`,`start`,`finish`,`estimatedStatus`,`actualStatus`,`publishDate`,implementorId)" +
					" VALUES (?,?,?,?,?,?,?,?);", new String[] {"id"});//,Statement.RETURN_GENERATED_KEYS);
			for(Sub_Projects sub:subs){
				subProjectInsert.setString(1,sub.title );
				subProjectInsert.setString(2,sub.budget );
				subProjectInsert.setString(3,sub.start );
				subProjectInsert.setString(4,sub.finish );
				if(sub.estimatedStatusId!=0){
					subProjectInsert.setInt(5,sub.estimatedStatusId);//AAinsertStatus(sub.estimatedStatus) );
				}else{
					subProjectInsert.setString(5, null);					
				}
				if(sub.actualStatusId!=0){
					subProjectInsert.setInt(6,sub.actualStatusId);//AAinsertStatus(sub.estimatedStatus) );
				}else{
					subProjectInsert.setString(6, null);					
				}
				subProjectInsert.setString(7,sub.publishDate);// new SimpleDateFormat("YYYY/MM/dd HH:mm:ss", new Locale("el","GR")).format(sub.publishDate));
				
				subProjectInsert.setString(8,null);
				subProjectInsert.addBatch();
			}
			subProjectInsert.executeBatch();
		
			ResultSet rs = subProjectInsert.getGeneratedKeys();
		
		 
			//use the sellers name to make the connection with table "projects_Sellers"
			PreparedStatement insertSellerSub=null;
			insertSellerSub=conn.prepareStatement("insert into Projects_Sellers(subProjectID,constructorID) " +
					"values(?,?);");
			int i=0;
			while (rs.next()) {
			
				// save the id somewhere or update the items list 
				int id=rs.getInt(1);
				ids.add(id);
			
				insertSellerSub.setInt(1,id);
				if(subs.get(i).implementors.split(",").length>1){
					//the implementor is a comma separated list
					for(String seller:subs.get(i).implementors.split(",")){
						insertSellerSub.setInt(2,insertBody(seller));
						insertSellerSub.addBatch();
					}
				}else{
					//single implementor
					insertSellerSub.setInt(2,insertBody(subs.get(i).implementors));
					insertSellerSub.addBatch();
				}
				i++;
				//we have the sub projectID			
			}
			insertSellerSub.executeBatch();
			insertSellerSub.close();
			subProjectInsert.close();
		
			return ids;
		}catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
			//there was a connection problem with the sql server
			//try again
			if(retries>=0){
				retries--;insertSubProject(subs);
			}else{
				System.out.println("Error");
				throw e;
			}
		}finally{
			conn.close();
		}
		return ids;
	}
	
	/**
	 * insert into status table of the database
	 * @param status the data of the status Api call, in a Status Object
	 * @return
	 * @throws SQLException
	 */
	public int insertStatus(Status status) throws SQLException{
			Connection conn = null;				
	try{	conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);//"jdbc:mysql://127.0.0.1:3306/messenger","root","salonika");
		PreparedStatement subProjectInsert = null;
		subProjectInsert= conn.prepareStatement("INSERT INTO `Status`" +
				"(submissionDate,entranceDate,auctionApproveDate,auctionDate,contractApproveDate,contractDate,completionDate,publicationDate)" +
				"values (?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
		subProjectInsert.setString(1,status.submissionDate);
		subProjectInsert.setString(2,status.entranceDate);
		subProjectInsert.setString(3,status.auctionApproveDate);
		subProjectInsert.setString(4,status.auctionDate);
		subProjectInsert.setString(5,status.contractApproveDate);
		subProjectInsert.setString(6,status.contractDate);
		subProjectInsert.setString(7,status.completionDate);
		subProjectInsert.setString(8,status.publicationDate);
		subProjectInsert.executeUpdate();
		//the id of the status 
		int id=-1;
		ResultSet generatedKeys = subProjectInsert.getGeneratedKeys() ;
		if(generatedKeys.isBeforeFirst()){
			generatedKeys.next();
			id=generatedKeys.getInt(1);
			generatedKeys.close();
			subProjectInsert.close();
			
			return id;
		}
		else {
			subProjectInsert.close();
			
			return 0;	
		} }catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
			//there was a connection problem with the sql server
			//try again
			if(retries>=0){
				retries--;
				insertStatus(status);
			}else{
				System.out.println("Error");
				throw e;
			}
		}finally{
		 conn.close();
		 }
	return 0;
		
		
	}
	
	/**
	 * Insert into sellers table of the database
	 * @param bodyName  the name of the seller
	 * @return the id of the inserted row
	 * @throws SQLException
	 * @throws com.mysql.jdbc.exceptions.jdbc4.CommunicationsException
	 */
	public int insertBody(String bodyName) throws SQLException,com.mysql.jdbc.exceptions.jdbc4.CommunicationsException{
		Connection conn = null;				
	try{	
		conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		int id=0;
		if((id=selectBody(bodyName,"Sellers"))!=0){
		
			return id;			
		}
		else{
			//no data, insert
			PreparedStatement inserBody=null;
			inserBody=conn.prepareStatement("insert into Sellers (name)" +
					"values(?)" , Statement.RETURN_GENERATED_KEYS);
			inserBody.setString(1,bodyName );
			
			inserBody.executeUpdate();
			//the id of the body 
			 id=0;
			ResultSet generatedKeys = inserBody.getGeneratedKeys() ;
			if(generatedKeys.isBeforeFirst()){
				generatedKeys.next();
				id=generatedKeys.getInt(1);
				generatedKeys.close();
				inserBody.close();
				
				return id;
			}
			else{				
				return 0;	
			}
		}
	 }catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
			//there was a connection problem with the sql server
			//try again
			if(retries>=0){
				retries--;
				insertBody( bodyName);
			}else{
				System.out.println("Error");
				throw e;
			}
		}finally{
		 conn.close();
		 }
	return 0;
	
	}
	
	
	/**
	 * Insert into one of the bodies tables of the database, depending on the type as stored in
	 * the body Object
	 * @param bodies a List of Body Objects to be inserted
	 * @throws SQLException
	 */
	public void insertBody(List<Body> bodies) throws SQLException{
		try{	Connection conn = null;		
		conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		PreparedStatement inserBody=null;
		for(Body body:bodies){
			if(selectBody(body)==0){
				String sql="insert into "+body.type+" (name,representative,address,telephone,email,fax)" +			
				"values(?,?,?,?,?,?)";
				inserBody=conn.prepareStatement(sql);
		
				inserBody.setString(1,body.name );
				inserBody.setString(2,body.representative );
				inserBody.setString(3,body.address );
				inserBody.setString(4,body.telephone );
				inserBody.setString(5,body.email );
				inserBody.setString(6,body.fax );
				inserBody.executeUpdate();
		}//if
			}//for
		if(inserBody!=null&&!inserBody.isClosed()){
			inserBody.close();
			}
		if(!conn.isClosed()){
			conn.close();}
	}catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
		//there was a connection problem with the sql server
		//try again
		if(retries>=0){
			retries--;
			insertBody(bodies);
		}else{
			System.out.println("Error");
			throw e;
		}
	}
	}
	
	/**
	 * get the ID of the body with the given name and type
	 * @param bodyName the name of the body to search
	 * @param type the type of the body we are searching for
	 * @return int, the id of the body
	 * @throws SQLException
	 */
	public int selectBody(String bodyName,String type) throws SQLException{
		try{	
		Connection conn = null;				
		conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		
		String[] types=new String[]{
				"Sellers","Diaxeirisi","Leitoyrgia","Protash","Xrimatodotish","Egrish","Parakolouthish","Prosklhsh"};
		
			Statement selectBody=conn.createStatement();
			ResultSet rs=	selectBody.executeQuery("select id from "+type+" where name='"+bodyName+"'") ;
			
			int id=0;
			if(rs.isBeforeFirst()){		
				rs.next();
				id=rs.getInt(1);
				conn.close();
				return id;			
			}
			rs.close();
			selectBody.close();
			 
		
		
		conn.close();
		
	}catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
		//there was a connection problem with the sql server
		//try again
		if(retries>=0){
			retries--;
			selectBody( bodyName,type);
		}else{
			System.out.println("Error");
			throw e;
		}
	}
	return 0;
	}
	
	/**
	 * get the ID of the body passed as a Body Object
	 * @param body A Body Object
	 * @return int, the id of the body
	 * @throws SQLException
	 */
	public int selectBody(Body body) throws SQLException{
		try{		Connection conn = null;				
		conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		PreparedStatement selectBody=null;
			selectBody=conn.prepareStatement("select id from "+body.type+" where name=? and representative=?") ;
		selectBody.setString(1, body.name);
		selectBody.setString(2, body.representative);
		ResultSet bodyResult=selectBody.executeQuery();
		if(bodyResult.isBeforeFirst()){
			bodyResult.next();
			int id=bodyResult.getInt(1);
			bodyResult.close();
			selectBody.close();
			conn.close();
			return id;			
		}else {
			conn.close();
			return 0;
		}
}catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
	//there was a connection problem with the sql server
	//try again
	if(retries>=0){
		retries--;
		selectBody(body);
	}else{
		System.out.println("Error");
		throw e;
	}
	return 0;
}
	}
	
	/**
	 * check if the project is inserted into the database in the last week. 
	 * this is used to prevent inserting projects that are soon harvested a second time
	 * @param kodikos the project code in question
	 * @return true if the project is harvested less than a week ago, false otherwise
	 * @throws SQLException
	 */
	public boolean recentlyInserted(String kodikos) throws SQLException{
		System.out.println("RecentlyInserted:"+kodikos);
		Connection conn = null;				
		conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		ResultSet rs=null;
		Statement selectOverview= conn.createStatement();
		rs=selectOverview.executeQuery("select publishDate as date,id from Overview where kodikos="+kodikos);
				
		 //if the specific project is been harvested less than a week ago (diffDays<6) skip
		 while(rs.next()){
		//	 System.out.println("RINS DATE:"+rs.getDate("date").getTime());
			 long diff=new Date().getTime()-rs.getDate("date").getTime();
		//	 System.out.println("RINS DATE diff"+diff);
//			 long now=new Date().getTime();
			 long diffDays = diff / (24 * 60 * 60 * 1000);
		//	 System.out.println("RINS DATE diffDays"+diffDays);
			if(diffDays<6){
				 //if last harvest was less than 6 days ago, skip this project
				rs.close();
				conn.close();
				 return true				 ;
			 }
		 }
		
		 rs.close();
		 conn.close();
		 return false;
			
	}
	
	/**	
	 * Insert into one of the bodies tables of the database, depending on the type as stored in
	 * the body Object
	 * @param bodies the Body Objects to be inserted
	 * @throws SQLException	
	 */
	public int insertBody(Body body) throws SQLException{
			Connection conn = null;				
		try{conn = DriverManager.getConnection(DB_ADDRS,DB_USER,DB_PASS);
		int id=0;
		if((id=selectBody(body))!=0){
			conn.close();
			return id;			
		}
		else{
			//no data, insert
			PreparedStatement inserBody=null;
			inserBody=conn.prepareStatement("insert into "+body.type+" (name,representative,address,telephone,email,fax)" +
					"values(?,?,?,?,?,?)" , Statement.RETURN_GENERATED_KEYS);
			inserBody.setString(1,body.name );
			inserBody.setString(2,body.representative );
			inserBody.setString(3,body.address );
			inserBody.setString(4,body.telephone );
			inserBody.setString(5,body.email );
			inserBody.setString(6,body.fax );
			
			inserBody.executeUpdate();
			//the id of the body 
			 id=0;
			ResultSet generatedKeys = inserBody.getGeneratedKeys() ;
			if(generatedKeys.isBeforeFirst()){
				generatedKeys.next();	
				id=generatedKeys.getInt(1);
				generatedKeys.close();
				inserBody.close();
			
				return id;
			}
			else{ 
			
				return 0;			
			}
		}
		}catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e){
			//there was a connection problem with the sql server
			//try again
			if(retries>=0){
				retries--; 
				insertBody(body);
			}else{
				System.out.println("Error");
				throw e;
			}
		}finally{
			conn.close();
			}
		return 0;
	
	}
	
	/**
	 * get the overview data from the API and store it to a Project object. 
	 * That Includes everything, the Details and the Overview data
	 * @param jsonOb
	 * @param table
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public Project getOverview(JSONObject jsonOb,String table) throws IOException, SQLException{
		
		Overview over=new Overview();
		over.type=table;
		over.kodikos=jsonOb.get("kodikos").toString();
		if(recentlyInserted(over.kodikos)){
			
			return null;
		}
		
		Details details=getDetails(over.kodikos);
		System.out.println("1");
		if((over.trexousaKatastash=jsonOb.get("trexousaKatastash").toString()).length()<1){
			over.trexousaKatastash=null;
		}
		if((over.title=jsonOb.get("title").toString()).length()<1){
			over.title=null;
		}
		if((details.body.name=jsonOb.get("body").toString()).length()<1){
			details.body=null;
		}
		if((over.budget=jsonOb.get("budget").toString()).length()<1){
			over.budget=null;
		}
		if((over.contracts=jsonOb.get("contracts").toString()).length()<1){
			over.contracts=null;
		}
		if((over.payments=jsonOb.get("payments").toString()).length()<1){
			over.payments=null;
		}
		if((over.perifereia=jsonOb.get("perifereia").toString()).length()<1){
			over.perifereia=null;
		}
	
		if((details.startDate=jsonOb.get("startDate").toString()).length()<1){
			details.startDate=null;
		}else{
			//transform the date in the format YYYY/MM/DD 
			details.startDate=	getValidDateFormat(details.startDate,"dd/MM/yyyy");
		}
		System.out.println("2");
		if((details.endDate=jsonOb.get("endDate").toString()).length()<1){
			details.endDate=null;
		}else{
			//transform the date in the format YYYY/MM/DD 
			details.endDate=getValidDateFormat(details.endDate,"dd/MM/yyyy");
		}
		if((over.countIpoergon=jsonOb.get("countIpoergon").toString()).length()<1){
			over.countIpoergon=null;
		}
		if((over.epKodikos=jsonOb.get("epKodikos").toString()).length()<1){
			over.epKodikos=null;
		}
		System.out.println("3");
		over.publishDate=new SimpleDateFormat("YYYY/MM/dd HH:mm:ss", new Locale("el","GR")).format(new Date().getTime()).toString();
		Project project =new Project();
		project.details=details;
		project.overview=over;
		System.out.println("return project");
		
		
		return project;
	}
	
	/**
	 * get the Body details from the API and store it to a Body Object.
	 * @param foreisObject a JSONObject, containing the body information
	 * @param type the type of the body
	 * @return a Body Object
	 */
	public Body getBodyDetails(JSONObject foreisObject,String type){
		
		Body body=new Body();
		body.name=foreisObject.get("name").toString();
		String rep=foreisObject.get("representative").toString();
		if(rep.length()<1){
			body.representative="";
		}else{
			body.representative=rep;
		}
	
		body.address=foreisObject.get("address").toString();
	body.telephone=foreisObject.get("telephone").toString();
	body.email=foreisObject.get("email").toString();
		body.fax=foreisObject.get("fax").toString();
		body.type=type;
		
//		System.out.println("!!£!!"+body.type);

		return body;
	}
	
	/**
	 * connect to the API using the project code and parse the returned Json to a Details Object
	 * @param ops the Project code
	 * @return a Details Object
	 * @throws IOException
	 * @throws SQLException
	 */
	public Details getDetails(String ops) throws IOException, SQLException{
	
		String url="http://anaptyxi.gov.gr/GetData.ashx?queryType=projectDetails&queryArgument="+ops;
		//get the json for the project code (ops) supplied
		JSONObject jo=(JSONObject) getPage(url);	
		//get bodies from object
		JSONArray foreisArray=(JSONArray) jo.get("foreis");
		
		Details details = new Details();
		List<Body> bodies= new ArrayList<Body>();
		details.subs=getSub_Projects((JSONArray) jo.get("subprojects"));
		for(Object o:foreisArray){
			
			JSONObject foreisObject=(JSONObject) o; 
			String bodyCategory=foreisObject.get("bodyCategory").toString();	
			if(bodyCategory.equals("ΑΝΑΔΟΧΟΣ")){								
				details.sellers.add( getBodyDetails(foreisObject,"Sellers"));
				
			}else if(bodyCategory.equals("ΦΟΡΕΑΣ ΔΙΑΧΕΙΡΙΣΗΣ")){
				details.diaxeirisi= getBodyDetails(foreisObject,"Diaxeirisi");
			}else if(bodyCategory.equals("ΦΟΡΕΑΣ ΛΕΙΤΟΥΡΓΙΑΣ")){
				details.leitoyrgia=getBodyDetails(foreisObject,"Leitoyrgia");
			}else if(bodyCategory.equals("ΦΟΡΕΑΣ ΠΡΟΤΑΣΗΣ")){
				details.protash=getBodyDetails(foreisObject,"Protash");
			}else if(bodyCategory.equals("ΦΟΡΕΑΣ ΧΡΗΜΑΤΟΔΟΤΗΣΗΣ")){
				details.xrimatodotish=getBodyDetails(foreisObject,"Xrimatodotish");
			}else if(bodyCategory.equals("ΦΟΡΕΑΣ ΕΓΚΡΙΣΗΣ")){
				details.egrish=(Egrish) getBodyDetails(foreisObject,"Egrish");
			}else if(bodyCategory.equals("ΦΟΡΕΑΣ ΠΑΡΑΚΟΛΟΥΘΗΣΗΣ")){
				details.parakolouthish=getBodyDetails(foreisObject,"Parakolouthish");
			}else if(bodyCategory.equals("ΦΟΡΕΑΣ ΠΡΟΣΚΛΗΣΗΣ")){
				details.prosklhsh= getBodyDetails(foreisObject,"Prosklhsh");
			}
		}
		details.bodies=bodies;
		details.files=getFiles(jo.get("files"));
		Dikaioyxos dikaioyxos = new Dikaioyxos();
		dikaioyxos.name=jo.get("body").toString();
		
		details.kodikos=ops;
	
		details.title=jo.get("title").toString();
		details.body=dikaioyxos;
		details.budget=jo.get("budget").toString();
		details.payments=jo.get("payments").toString();
		details.completion=jo.get("completion").toString();
		details.startDate=getValidDateFormat(jo.get("startDate").toString(),"dd/MM/yyyy HH:mm:ss a");
		
		details.endDate=getValidDateFormat(jo.get("endDate").toString(),"dd/MM/yyyy HH:mm:ss a");
		details.description=jo.get("description").toString();
		details.statusReportDate=jo.get("statusReportDate").toString();
		details.statusReport=jo.get("statusReport").toString();
		details.publishDate=new SimpleDateFormat("YYYY/MM/dd HH:mm:ss", new Locale("el","GR")).format(new Date().getTime());
		details.projectId=-100;
		details.titleEnglish=getEnglishProjectTitle(ops);
			System.out.println("return details");
		return details;
	}

	/**
	 * store the sub-projects from the JSONArray to a List of Sub_Project Objects
	 * @param subPrArr The sub projects in Json Array format
	 * @return a List of Sub_Projects Object
	 * @throws SQLException
	 */
	public List<Sub_Projects> getSub_Projects(JSONArray subPrArr) throws SQLException{
		List<Sub_Projects> subProject=new ArrayList<Sub_Projects>();
		
		for(Object o:subPrArr){
			Sub_Projects sub=new Sub_Projects();			
			JSONObject subProjObject=(JSONObject) o;
			
			sub.title=subProjObject.get("title").toString();
		
			
			sub.implementors=subProjObject.get("implementors").toString();
			if (sub.implementors.length()<1)
				sub.implementors="";
			
			sub.budget=subProjObject.get("budget").toString();
			
			if((sub.start=subProjObject.get("startDate").toString()).length()<1){
				sub.start=null;
			}else{
				//transform the date in the format YYYY/MM/DD 
				sub.start=	getValidDateFormat(sub.start,"dd/MM/yyyy");
			}
			
			if((sub.finish=subProjObject.get("endDate").toString()).length()<1){
				sub.finish=null;
			}else{
				//transform the date in the format YYYY/MM/DD 
				sub.finish=	getValidDateFormat(sub.finish,"dd/MM/yyyy");
			}
			
			//read the estimated and actual status information and insert them to the DB
//			There is a case that status is null, handle it
			JSONObject jsonob=(JSONObject)subProjObject.get("estimatedStatus");
			
			
			if(jsonob!=null){
				sub.estimatedStatus=getSubProjectStatus(jsonob);
				sub.estimatedStatusId=insertStatus(sub.estimatedStatus);
			}
			
			jsonob=(JSONObject)subProjObject.get("actualStatus");
			
			
			if(jsonob!=null){
				sub.actualStatus=getSubProjectStatus(jsonob);
				sub.actualStatusId=insertStatus(sub.actualStatus);
				}
			sub.publishDate=new SimpleDateFormat("YYYY/MM/dd HH:mm:ss", new Locale("el","GR")).format(new Date().getTime());
			subProject.add(sub);
				
			
		}
		
		return subProject;
	}
	
	/**
	 * store the sub-project status from the API to a Status Object
	 * @param statusOb a Json Object with the sub project status
	 * @return a Status Object
	 */
	public Status getSubProjectStatus(JSONObject statusOb){
 		Status status=new Status();
		if((status.submissionDate=statusOb.get("submissionDate").toString()).length()<1){
			status.submissionDate=null;
		}else{
			//transform the date in the format YYYY/MM/DD 
			status.submissionDate=	getValidDateFormat(status.submissionDate,"dd/MM/yyyy HH:mm:ss a");
		}
				 
		if((status.entranceDate=statusOb.get("entranceDate").toString()).length()<1){
			status.entranceDate=null;
		}else{
			//transform the date in the format YYYY/MM/DD 
			status.entranceDate=	getValidDateFormat(status.entranceDate,"dd/MM/yyyy HH:mm:ss a");
		}
		 
		if((status.auctionApproveDate=statusOb.get("auctionApproveDate").toString()).length()<1){
			status.auctionApproveDate=null;
		}else{
			//transform the date in the format YYYY/MM/DD 
			status.auctionApproveDate=	getValidDateFormat(status.auctionApproveDate,"dd/MM/yyyy HH:mm:ss a");
		}
		 
		if((status.auctionDate=statusOb.get("auctionDate").toString()).length()<1){
			status.auctionDate=null;
		}else{
			//transform the date in the format YYYY/MM/DD 
			status.auctionDate=	getValidDateFormat(status.auctionDate,"dd/MM/yyyy HH:mm:ss a");
		} 
		if((status.contractApproveDate=statusOb.get("contractApproveDate").toString()).length()<1){
			status.contractApproveDate=null;
		}else{
			//transform the date in the format YYYY/MM/DD 
			status.contractApproveDate=	getValidDateFormat(status.contractApproveDate,"dd/MM/yyyy HH:mm:ss a");
		}
		 
		if((status.contractDate=statusOb.get("contractDate").toString()).length()<1){
			status.contractDate=null;
		}else{
			//transform the date in the format YYYY/MM/DD 
			status.contractDate=	getValidDateFormat(status.contractDate,"dd/MM/yyyy HH:mm:ss a");
		}
		 
		if((status.completionDate=statusOb.get("completionDate").toString()).length()<1){
			status.completionDate=null;
		}else{
			//transform the date in the format YYYY/MM/DD 
			status.completionDate=	getValidDateFormat(status.completionDate,"dd/MM/yyyy HH:mm:ss a");
		}
		status.publicationDate=new SimpleDateFormat("YYYY/MM/dd HH:mm:ss", new Locale("el","GR")).format(new Date().getTime());
	
		
		return status;
	}
	
	/**
	 * read the Object item and get the files of the project
	 * @param object A Json Object
	 * @return a List of Files
	 */
	public List<Files> getFiles(Object object){
 		List<Files> files=new ArrayList<Files>();
		Files file=new Files();
		
		JSONArray a=(JSONArray) object;
		for(Object ob:a){
			
			JSONObject fileObject=(JSONObject) ob;
			if(fileObject==null)
				return null;
			else
			if(fileObject.get("filename")==null)
				return null;
		//	System.out.println("@@"+fileObject);
//			System.out.println(fileObject.get("filename"));
//			System.out.println(fileObject.get("filename").toString());
//			System.out.println(fileObject.get("filename").toString().equals(""));
			if((file.filename=fileObject.get("filename").toString()).equals("")){
				file.filename=null;
			}
			
			if((file.filecode=fileObject.get("filecode").toString()).equals("")){
				file.filecode=null;
			}
			if((file.projectcode=fileObject.get("projectcode").toString()).equals("")){
				file.projectcode=null;
			}
			if((file.filetype=fileObject.get("filetype").toString()).equals("")){
				file.filetype=null;
			}
			if((file.fileurl=fileObject.get("fileurl").toString()).equals("")){
				file.fileurl=null;
			}
			files.add(file);
		}
		return files;
	}
	
	/**
	 * English title is obtained by calling the API again with a different set of arguments.
	 * @param ops The project Code
	 * @return a String with the title in English
	 * @throws IOException
	 */
	public String getEnglishProjectTitle(String ops) throws IOException{
		String url="http://anaptyxi.gov.gr/GetData.ashx?queryType=projectDetails&queryArgument="+ops+"&lang=en-GB";
		
		JSONObject jsonob=(JSONObject)getPage(url);
		String engTitle=	jsonob.get("title").toString();
		Pattern p = Pattern.compile("[\\p{IsLatin}]+$");

	
		Matcher m = p.matcher(engTitle);
		if(m.find()){
			if(engTitle.matches("^\\p{IsGreek}+$")){
				return null;
			}
			return engTitle;
		}else
			return null;
	}

	/**
	 * return the given date of the specified format to the format yyyy/MM/dd
	 * @param value the date value in a string format
	 * @param inputFormat the format the date is in
	 * @return the date in the yyyy/MM/dd format or null if there is a problem
	 */
	public  String getValidDateFormat( String value,String inputFormat) {
		 SimpleDateFormat fromUser = new SimpleDateFormat(inputFormat, new Locale("el","GR"));
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("el","GR"));

			if(value.equals("")){
				return null;
			}
			try {

			    return myFormat.format(fromUser.parse(value));
			} catch (ParseException e) {
			    e.printStackTrace();
			    return null;
			}
	    }
}

/**
 * Class that holds the information of the Details Api call
 */
class Details{
	
	/**The project Code*/
	public String  kodikos;
	/**The title of the project*/
	public String title;
	/**The dikaioyxos of the project*/
	public Dikaioyxos body;
	/**The projects budget*/
	public String budget;
	/**Payments so far*/
	public String payments;
	/**completion percent*/
	public String completion;
	/**The start date of the project*/
	public String startDate;
	/**estimated end date*/
	public String endDate;
	/**description*/
	public String description;
	public String statusReportDate;
	public String statusReport;
	/**the date of the harvest*/
	public String publishDate;
	/**the overview id that this project is connected with at the database*/
	public 	int projectId;
	public String titleEnglish;
	public List<Body> dikeouxos;
	public Body protash;
	public Body xrimatodotish;
	public Body leitoyrgia;
	public Body diaxeirisi;
	public Body egrish;
	public Body parakolouthish;
	public Body prosklhsh;	
	/**a list of Sellers Objects*/
	public List<Body> sellers=new ArrayList<Body>();	
	/**a list with all the bodies*/
	public List<Body> bodies;
	/**a list with all the sub-projects*/
	public List<Sub_Projects> subs;
	/** a List with all the Files of the project*/
	public List<Files> files;

	public void addSubProject(Sub_Projects sub){
		subs.add(sub);
	}
}

/**
 * a class that can hold information about the project bodies
 */
class Body{
	/**name/title*/
	String name;
	String address;
	String representative;
	String telephone;
	String email;
	String fax;	
	String type;
}
/**
 * a class that holds all the information from the API, the Overview and the Details Object
 *
 */
class Project{
	public Overview overview;
	public Details details;
	
}
class Diaxeirisi extends Body{}
class Dikaioyxos extends Body{}
class Egrish extends Body{}
class Leitoyrgia extends Body{}
class Parakolouthish extends Body{}
class Prosklhsh extends Body{}
class Protash extends Body{}
class Sellers extends Body{}
class Xrimatodotish extends Body{}

/**
 * a class that holds all the information of the Overview Api call
 */
class Overview{
	/**the project code*/
	public String kodikos;
	/**the date of the harvest*/
	public String publishDate;
	public String title;
	public String description;
	public String budget;
	public String contracts;
	public String payments;
	public int completition_percent;
	public String start;
	public String finish;
	public String perifereia;
	public String epKodikos;
	public String countIpoergon;
	public String map_coordinates;
	public String type;
	public String trexousaKatastash;
	public Details details;
}
	
/**a class that holds the information to make the connection between the seller and the subproject*/
class Projects_Sellers{
	public int id;
	public Sub_Projects subProjectID;
	public Body constructorID;
	public String ops; 
}
		
/**
 * a class that holds the information of the project status
 */
class Status{
	public int id;
	public String submissionDate;	
	public String entranceDate;
	public String auctionApproveDate;
	public String auctionDate;
	public String contractApproveDate;
	public String contractDate;
	public String completionDate;
	public String publicationDate;
}
		
/**
 * a class that makes the connection between the subpoject and the project
 */
class SubProject_Project{
	public int id;
	public Sub_Projects subProjectID;
	public Details detailsId;
	public Date publishDate;
}
/**
 * a class that holds all the information about the sub project
 */
class Sub_Projects{
	public int id;
	public String ops; 
	public String title; 
	public String budget; 
	public String start; 
	public String finish; 	
	public Status estimatedStatus;
	public Status actualStatus;
	public String publishDate; 
	public String implementors; 
	public int estimatedStatusId;
	public int actualStatusId;
}
/**
 * a class that holds the information about the project files
 */
class Files{
	public String filename;
	public String filecode;
	public String projectcode;
	public String filetype;
	public String fileurl;
	public String date;
}
		
