package elod.harvest.espa.projects;

import elod.msg.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;
/*
 * args=
/home/ilias/skaros/Dropbox/EspaProjects/ids /home/ilias/skaros/Dropbox/EspaProjects/2DBvEng /home/ilias/skaros/Dropbox/EspaProjects/email /home/ilias/skaros/Dropbox/EspaProjects/db
 * 
 * 
 * 
 delete from Espa_Projects_Eng.Administration_Body where id>=0;         
	 delete from Espa_Projects_Eng.Funding_Body        where id>=0;         
	 delete from Espa_Projects_Eng.Implementation_Body    where id>=0;      
	 delete from Espa_Projects_Eng.Operation_Body       where id>=0;        
	         
	 delete from Espa_Projects_Eng.Projects_Sellers     where id>=0;        
	 delete from Espa_Projects_Eng.Proposal_Body        where id>=0;        
	 delete from Espa_Projects_Eng.Sellers             where id>=0;         
	 delete from Espa_Projects_Eng.Sub_Projects   where id>=0;
	 
	 delete from Espa_Projects_Eng.Overview             where id>=0;
	 
	 insert into Espa_Projects_Eng.Sellers (`title`,`address`) VALUES (" "," ");

	 
	 Password= espa@69
Username= espa
DataBase= jdbc:mysql://83.212.86.161/Espa_Projects2?useUnicode=true&characterEncoding=UTF-8

 



*/

public class EspaToDB {
	public static List<String[]> REVIEW_TAB = new ArrayList<String[]>();
	//public static List<String[]> SUBWORKS_TAB = new ArrayList<String[]>();
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static String DB_URL ="";// "jdbc:mysql://127.0.0.1:3306/";//market_Harvest?useUnicode=true&characterEncoding=UTF-8";
//	   static final String DB_URL = "jdbc:mysql://83.212.86.161/Espa_Projects?useUnicode=true&characterEncoding=UTF-8";  
	   
	//  Database credentials
	   static String USER = "";
	   static String PASS = "";
	   
	   //email credentials
	   static String USER_NAME="", PASSWORD="";
	   static String[] RECIPIENT;
	   
	   static final int SLEEP=5000;
	   static boolean SEND_EMAIL=false;
	   static String logDir="";
	   
	   
	   static Notifications notif;
	   
	   
	public static void readArgs(String[] args){
		if (args.length<4){
			String msg="Parameter Error: \nUsage: \n\tjava -jar EspaProjects.jar [file with project ids] [download dir] [email setting file] [Database Setting file]";
			System.out.println(msg);
			System.exit(1);
		}
	}
	
	/**
	 * when there are more than one pages of sub-projects, this method will read all the pages and returns a list with the harvested data
	 * @param document the page to be read
	 * @return List<Element> with the data harvested
	 * @throws InterruptedException
	 */
	public static List<Element> readMultipleSubs(Document document) throws InterruptedException{
		List<Element> rows=new ArrayList<Element>();
		List<Element> pages=
				//get the page
				document.query("#dnn_ctr521_View_episkopisiGrid_ctl00")
				//find the footer
				.query("tfoot")
				//find footers body
				.query("tbody")
				//find the columns
				.query("tr").queryAll("td")
				//the second column (1) is the links to the other pages
				.get(1).queryAll("div")
				//the items are 
				//0: first/prev page
				//1: individual page links
				//0: last/next page
				//we get the list with the pages links
				.get(1).queryAll("a");
		//read and add the first page on the list
		rows.addAll(document.query("#dnn_ctr521_View_episkopisiGrid_ctl00").queryAll("tbody").get(2).queryAll("tr"));
		//read and add the rest of the pages
		long retry=SLEEP;
		for(int i=1;i<pages.size();i++){
			pages.get(i).click();	
			//keep retrying till the current page is the one that is clicked. This is a way to make sure the page is loaded with the results we want.
			//on our third iteration,we are clicking on the 3rd page (pages.get(i).getText=3),so this should be equal to i.
			do{
				Thread.sleep(retry);
				//increase the wait time
				retry=retry*2;
				pages=document.query("#dnn_ctr521_View_episkopisiGrid_ctl00").query("tfoot").query("tbody").query("tr").queryAll("td").get(1).queryAll("div").get(1).queryAll("a");
			}while(!pages.get(i).getText().equals(i+1+""));
			rows.addAll(document.query("#dnn_ctr521_View_episkopisiGrid_ctl00").queryAll("tbody").get(2).queryAll("tr"));
		}
		return rows;
		
	}
	
	/**
	 * will use the file passed as a parameter to read the project ids (OPS) of the ESPA projects. 
	 * For the ids found will get all the information, update the database and create a csv file
	 * @param args the path to the ids file, the download dir, the email authentication information and the database authantication information
	 */
	public static void main(String[] args) {
		//args=new String[]{"/home/ilias/Documents/test/ids","/home/ilias/Documents/test/EspaProjects/2DBvEng","/home/ilias/Documents/test/email","/home/ilias/Documents/test/db"};
		
		notif=new Notifications("espaProjects", "Skaros","EspaProjectHarvest","skaros");
		

	
		if (args.length<4){
			String msg="Parameter Error: \nUsage: \n\tjava -jar EspaProjects.jar [file with project ids] [download dir] [email setting file] [Database Setting file]";
			System.out.println(msg);
			notif.DbCredentials("messenger", "messenger@69", "jdbc:mysql://83.212.86.161/Messenger1?useUnicode=true&characterEncoding=UTF-8");
			notif.addError(msg+"Terminating");
			notif.runInsert();
			System.exit(1);
		}
		try {
			if(!new File(args[1]).exists()){
				if(!new File(args[1]).mkdirs()){
					String msg="Download directory Error: The path you entered "+args[1]+" is not a valid directory and can not be created";
					notif.DbCredentials("messenger", "messenger@69", "jdbc:mysql://83.212.86.161/Messenger1?useUnicode=true&characterEncoding=UTF-8");
					notif.addError(msg+"Terminating");
					System.out.println(msg);
					notif.runInsert();
					System.exit(1);
				}
			}
			logDir=new File(args[1]).getParent();
			//+File.separator+input);
			if (!new File(args[2]).exists()){
				//The db msn file doesnt exist
			//TODO	
			}
			else{
				notif.DbCredentials(new File(args[2]));
			}
		
			/*
			 * The email settings file. Not used, we now use the messenger option
			 */
//			if (!new File(args[2]).exists()){
//				SEND_EMAIL=false;
//				log("The email settings file "+args[2] +" doesnt exist. no email will be sent");
//				 notif.addWarning("The email settings file "+args[2] +" doesnt exist. no email will be sent");
//			}else{
//				SEND_EMAIL=true;
//				USER_NAME=new GetCredential(args[2]).getProp("Username");
//				PASSWORD=new GetCredential(args[2]).getProp("Password");						
//				RECIPIENT=new GetCredential(args[2]).getProp("Recipients").split(" ");		
//			}


			if (!new File(args[3]).exists()){
				
				log("The database settings file "+args[3] +" doesnt exist.Terminating");
				System.out.println("The database settings file "+args[3] +" doesnt exist.Terminating");
				 notif.addError("The database settings file "+args[3] +" doesnt exist.Terminating");
				System.exit(1);
			}else{
				PASS=new GetCredential(args[3]).getProp("Password");
				USER=new GetCredential(args[3]).getProp("Username");
				DB_URL=new GetCredential(args[3]).getProp("DataBase");
				}
		
			
			if(new File(args[0]).exists()){
				System.setProperty("ui4j.headless", "true");
					BufferedReader fr;
					fr = new BufferedReader(new FileReader(new File(args[0])));
				
					String input;
				//	StringBuilder data=new StringBuilder();
					BrowserEngine webkit = BrowserFactory.getWebKit();
					
					//Page page = null;
					Document document = null;
					while ((input=fr.readLine())!=null){
						
						String url="http://anaptyxi.gov.gr/ergopopup.aspx?mis="+input;
						String fileName;
						//String log;
						//files will be saved under the provided path at a tree structure as:
						//path/perHarvestDate/[Year]-[Month]/[Date]/[project Code].csv
						if(args[1].charAt(args[1].length()-1)== File.separatorChar)
						{ 				
							fileName = args[1]+"perHarvestDate"+File.separator+new SimpleDateFormat("yyyy-MM").format(new Date())+File.separator+new SimpleDateFormat("dd").format(new Date())+File.separator+input;

//									fileName = args[1]+input+File.separator+new SimpleDateFormat("dd-MM-yyyy").format(new Date())+"-"+input;
//								log=args[1]+input+"_log.txt";
						}
						else {
							fileName = args[1]+File.separator+"perHarvestDate"+File.separator+new SimpleDateFormat("yyyy-MM").format(new Date())+File.separator+new SimpleDateFormat("dd").format(new Date())+File.separator+input;
//								fileName = args[1]+File.separator+input+File.separator+new SimpleDateFormat("dd-MM-yyyy").format(new Date())+"-"+input;
//								log=args[1]+"/"+input+"_log.txt";
						}
//						
//						
//						java.io.PrintWriter loger = new PrintWriter(log, "UTF-8");
//						loger.append("link"+url+"\n");
						new File(fileName).getParentFile().mkdirs();
						java.io.PrintWriter writer = new PrintWriter(fileName+".csv", "UTF-8");
						 document = webkit.navigate(url).getDocument();

						 
						 
						 //read the review tab
						 List<String> review=readOverview(document);
				 
						 
						 //read the subworks
						List<String[]> subWorks=readSubWorks(document,input);
					
						 List<String> proposalBody=getForeisSubTable(document.query("#dnn_ctr521_View_treeProtasis"));
						
						 List<String> fundingBody=getForeisSubTable(document.query("#dnn_ctr521_View_treeXrimatodotisis"));
						 List<String> administrationBody=getForeisSubTable(document.query("#dnn_ctr521_View_treeDiaxeirisis"));
						 List<String> implementationBody=getForeisSubTable(document.query("#dnn_ctr521_View_treeIlopoiisis"));
						 List<String> operationBody=getForeisSubTable(document.query("#dnn_ctr521_View_treeLeitourgias"));
						 
						
						List<List<String>> sellers=getSellersTable(document);
		
						String[] titles ={"Έργο","Δικαιούχος Φορέας","Επισκόπηση","Προϋπ/σμός Δημόσιας Δαπάνης","Πληρωμές","Ολοκλήρωση","Έναρξη","Λήξη","Υποέργα","Κρατικές Ενισχύσεις","Αρ. Επιχειρησιακού Προγράμματος","Επιχειρησιακό Πρόγραμμα","Τίτλος ΟΠΣ","Χάρτης"};
						String[] EngTitles ={"Title","Beneficiary Institution","Overview","Public Expenditure Budget","Payments","Completion","Start","End","Sub-Projects","Public Aid","Νο. Operational Programme","Operational Programme","title MIS","Map"};
						
													
						String msg="";
						
					
						
						/*
						 * save to file and upload database for data about
						 * Επισκόπηση
						 */
						writer.append("Επισκόπηση\n");
						writer.append("Overview\n");

						try{
							//if the manual titles are of the same amount as data use them
							if(titles.length==review.size()){
								try {	for(int i=0;i<titles.length;i++){
									writer.append(titles[i]+"\n"+EngTitles[i]+":\n"+review.get(i).toString()+"\n");}
								}catch(Exception e){
									log("exception in Overview FOR"+e.getStackTrace());
									 notif.addError("exception in OverView FOR"+e.getStackTrace());
								}
							}else
								//the titles can not be used
								for(String info:review){
									writer.append(info+"\n");
								}
							try{
							updateOverview(review,input);
						}catch(Exception e){
							log("exception in REVIEW update"+e.getLocalizedMessage());
							 notif.addError("exception in REVIEW update"+e.getLocalizedMessage());
						}
							writer.flush();
						}catch(Exception e){
							log("There was an error while processing the Επισκόπηση (episkopish) data. \n Can not proceed. Terminating.");
							 notif.addError("There was an error while processing the Επισκόπηση (episkopish) data. \n Can not proceed. Terminating.");
							System.exit(1);
						}
						
						writer.append("Φορείς\nBodies");
						writer.append("\n");
						try{	
							for(List<String> rows:sellers){
								for(String col:rows){
									writer.append(col+"\t\n");}
								writer.append("\n");
							}
							writer.flush();
							updateSellers(sellers,input); 
							
						}catch(NullPointerException e){
							msg+="There was an error while processing ΑΝΑΔΟΧΟΣ (anadoxos) for project "+input+".\n might be empty data, will try to continue.\n";
							notif.addWarning("There was an error while processing ΑΝΑΔΟΧΟΣ (anadoxos) for project "+input+".\n might be empty data, will try to continue.\n");
							}
						/*
						 * save to file and upload database for data about
						 * Yποέργα
						 */
						try{
							writer.append("\nYποέργα\nSub-Projects\n");
						
							for(String[] row:subWorks){
								for (String cell:row){
									writer.append(cell+"\t\n");	}	
								writer.append("\n");
								
							}
							V2_updateSubWorks(subWorks,input);
							//updateSubWorks(subWorks,input);
							writer.flush();
						}
						catch(NullPointerException e){
							msg+="There was an error while processing Yποέργα (ypoerga) for project "+input+".\n might be empty data, will try to continue.\n";
							notif.addWarning("There was an error while processing Yποέργα (ypoerga) for project "+input+".\n might be empty data, will try to continue.\n");
						}
						
						
						/*
						 * save to file and upload database for data about
						 * ΦΟΡΕΑΣ ΠΡΟΤΑΣΗΣ
						 */
						writer.append("ΦΟΡΕΑΣ ΠΡΟΤΑΣΗΣ \n Proposal Body\n");
						try{
							for(String body:fundingBody){
								writer.append(body+"\n");
								}
							writer.flush();
							updateProposalBody(proposalBody,input);
						}
						catch(NullPointerException e){
							msg+="There was an error while processing ΦΟΡΕΑΣ ΠΡΟΤΑΣΗΣ (Proposal Body) for project "+input+".\n might be empty data, will try to continue.\n";
							notif.addWarning("There was an error while processing ΦΟΡΕΑΣ ΠΡΟΤΑΣΗΣ (Proposal_Body) for project "+input+".\n might be empty data, will try to continue.");
						}
						
						

						/*
						 * save to file and upload database for data about
						 * ΦΟΡΕΑΣ ΧΡΗΜΑΤΟΔΟΤΗΣΗΣ
						 */
						writer.append("ΦΟΡΕΑΣ ΧΡΗΜΑΤΟΔΟΤΗΣΗΣ\n");
						writer.append("Funding Body\n");
						try{	
							for(String body:fundingBody){
								writer.append(body+"\n");}
							writer.flush();
							updateFundingBody(fundingBody,input);
						}catch(NullPointerException e){
							msg+="There was an error while processing  ΦΟΡΕΑΣ ΧΡΗΜΑΤΟΔΟΤΗΣΗΣ (Foreas xrhmatodotishs) for project "+input+".\n might be empty data, will try to continue.\n";
							notif.addWarning("There was an error while processing  ΦΟΡΕΑΣ ΧΡΗΜΑΤΟΔΟΤΗΣΗΣ (Foreas xrhmatodotishs) for project "+input+".\n might be empty data, will try to continue.\n");
						}

						/*
						 * save to file and upload database for data about
						 * ΦΟΡΕΑΣ ΔΙΑΧΕΙΡΙΣΗΣ
						 */
						writer.append("ΦΟΡΕΑΣ ΔΙΑΧΕΙΡΙΣΗΣ\nAdministration Body\n");
						try{
							for(String dixirish :administrationBody){
								writer.append(dixirish+"\n");}
							writer.flush();
							updateAdministrationBody(administrationBody,input);
						}catch(NullPointerException e){
							msg+="There was an error while processing ΦΟΡΕΑΣ ΔΙΑΧΕΙΡΙΣΗΣ (Administration Body) for project "+input+".\n might be empty data, will try to continue.\n";
							notif.addWarning("There was an error while processing ΦΟΡΕΑΣ ΔΙΑΧΕΙΡΙΣΗΣ (Administration Body) for project "+input+".\n might be empty data, will try to continue.\n");
						}

						/*
						 * save to file and upload database for data about
						 * ΦΟΡΕΑΣ ΙΛΟΠΟΙΗΣΗΣ
						 */
						writer.append("ΦΟΡΕΑΣ ΙΛΟΠΟΙΗΣΗΣ\nImplementation Body\n");

						try{
							for(String ilopoiish: implementationBody){
								writer.append(ilopoiish+"\n");}
							writer.flush();
							updateImplementationBody(implementationBody,input);
						}catch(NullPointerException e){
							msg+="There was an error while processing ΦΟΡΕΑΣ ΥΛΟΠΟΙΗΣΗΣ (Foreas Ylopoiishs) for project "+input+".\n might be empty data, will try to continue.\n";
							notif.addWarning("There was an error while processing ΦΟΡΕΑΣ ΥΛΟΠΟΙΗΣΗΣ (Foreas Ylopoiishs) for project "+input+".\n might be empty data, will try to continue.\n");
						}

						/*
						 * save to file and upload database for data about
						 * ΦΟΡΕΑΣ ΛΕΙΤΟΥΡΓΙΑΣ
						 */	
						writer.append("ΦΟΡΕΑΣ ΛΕΙΤΟΥΡΓΙΑΣ\nOperationBody\n");

						try{
							for(String administration : operationBody){
								writer.append(administration+"\n");}
							writer.flush();
							updateOperationBody(operationBody,input);
						}catch(NullPointerException e){
							msg+="There was an error while processing ΦΟΡΕΑΣ ΛΕΙΤΟΥΡΓΙΑΣ (Operation_Body) for project "+input+".\n might be empty data, will try to continue.\n";
							notif.addWarning("There was an error while processing ΦΟΡΕΑΣ ΛΕΙΤΟΥΡΓΙΑΣ (Operation_Body) for project "+input+".\n might be empty data, will try to continue.\n");
						}
						
					
						writer.flush();
						writer.close();
						if(msg!=""){
						 log("process completly succesfully BUT\n"+msg);
//						 notif.addWarning(msg);
						 notif.addSuccess("process completly succesfully for ops:"+input+"but with Warnings");
						}else{
							log("process completly succesfully");
							notif.addSuccess("process completly succesfully for ops:"+input);
						}
//						 loger.append("\n\nCLOSE\n");
//					loger.close();
					
					}
					fr.close();
			
			}else{
				System.out.println("IDs dont exist");
			}
		
			
		} catch (Exception e) {
			log("Exception on Main "+e.getStackTrace());
			 notif.addWarning("Exception on Main "+e.getStackTrace());
			e.printStackTrace();
		}
		notif.addSuccess("Process Completed");
		notif.addPublic("Harvesting the Espa project was completed");
	//	notif.runInsert();
		System.exit(0);
	}

	/**
	 * reads the review tab of the site for the given id
	 * @param document the UI4J.Document item for the given id
	 * @return a list of String with the data 
	 */
	public static List<String> readOverview(Document document){
		List<String> review = new ArrayList<String>();
		int counter=0;		
		int ATTEMPTS=5;
		int sleepTime=SLEEP;
		
		while(true){
			try{
				Thread.sleep(sleepTime);
				document.query("#dnn_ctr521_View_txtTitlos").getText();
				
				break;
			}catch (InterruptedException e1) {
				e1.printStackTrace();
			}catch(Exception e){
				sleepTime*=2;
				if(++counter>=ATTEMPTS){
					log(sleepTime+"connection time out. Could not connect to website after "+sleepTime/1000+" seconds.\n Terminating");
					 notif.addError(sleepTime+"connection time out. Could not connect to website after "+sleepTime/1000+" seconds.\n Terminating");
					 System.exit(1);
					
				}
			}	
		}//while
		sleepTime=SLEEP;
		try {
			Thread.sleep(sleepTime);
			
	
		
		review.add(document.query("#dnn_ctr521_View_txtTitlos").getText());
		review.add(document.query("#dnn_ctr521_View_txtForeas").getText());
		
		//wait for the page to load
			Thread.sleep(sleepTime);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//if there is a "read more" link, click it
		//try this for ATTEMPTS times before throwing an exception
		sleepTime=SLEEP;
		Element element = null;
		while(true){
			try{Thread.sleep(sleepTime);
				element= document.queryAll("#dnn_ctr521_View_perigrafiMoreBtn").get(0).click();  
				
				break;
			}catch(Exception e){
				
				if(++counter>=ATTEMPTS){
					break;
				}
			}	
		}//while		
		sleepTime=SLEEP;

	
		 counter=0;
		try{
			Thread.sleep(sleepTime);
		//read the description
		element = document.query("#dnn_ctr521_View_perigrafi");
		
		}
		catch(Exception e){
			if(++counter>=ATTEMPTS){
				log("can not load page. Terminating");
				notif.addError(	"can not load page. Terminating");
				System.exit(1);
			}
		}
      review.add(element.getText());
      
      //read the general information table
     
      while (true){
      	try{ 
      		element = document.query("#dnn_ctr521_View_normalEpiskopisi");
      		List<Element> rows = element.queryAll("tr"); 
      		for(Element row:rows){
      			List<Element> cells = row.queryAll("td");
      			
      			String value=cells.get(1).getText();

      			review.add(value.trim());
      			
      		}  
      		break;
      	}catch(Exception e){
      		if(++counter>=ATTEMPTS){
      			throw e;
      		}
      	}
      }
      

      counter=0;
    //read the extra status table
      while (true){
      	try{ 
      		element = document.query("#dnn_ctr521_View_Tr7_1"); 
		
			List<Element> cells = element.queryAll("td").get(1).queryAll("span");
			String row="";
			for(Element cell:cells){
				row+=cell.getText()+" ";
			}
			review.add(row.trim());
			break;
      	}catch(Exception e){
      		System.out.println("exception2");
      		if(++counter>=ATTEMPTS){
      			throw e;
      		}
      	}
      }
      
      String pageBody=document.getBody().toString();	     
      String mapCoordinates=pageBody.substring(pageBody.indexOf("<coordinates>")+13,pageBody.indexOf("</coordinates>"));
      review.add(mapCoordinates);
      return review;
	}
	/**
	 * reads the sub works tab of the site for the given id
	 * @param document the UI4J.Document item for the given id
	 * @return a list of String Array with the data 
	 */
	public static List<String[]> readSubWorks(Document document,String ops){
		Element subworks=document.query("#dnn_ctr521_View_pageIpoerga");
		List<Element> rows=new ArrayList<Element>();
		try{
			
			//if the project has more than 50 sub -projects, these are split into pages.
			//try to read the footer with the pages.
			
			if(!document.query("#dnn_ctr521_View_episkopisiGrid_ctl00").query("tfoot").query("tbody").query("tr").queryAll("td").get(1).queryAll("div").get(1).toString().isEmpty()){
				System.out.println("many pages"+ops);
//				there are more than one pages
				try{
					rows=readMultipleSubs(document);
				}catch(Exception e){
					notif.addError("Error while processing multiple pages of sub-projects for project:"+ops);
				}
			}
			else{//There are no other pages, just 1
				rows=subworks.query("#dnn_ctr521_View_episkopisiGrid_ctl00").queryAll("tbody").get(1).queryAll("tr");
			}
			 
		}catch (Exception e){	
//			The project only has one page of sub project
			rows=subworks.query("#dnn_ctr521_View_episkopisiGrid_ctl00").queryAll("tbody").get(1).queryAll("tr");
	
		}
		
		
		List<String[]> table=new ArrayList<String[]>();
		for(Element row:rows){
			
			List<Element> cols=row.queryAll("td");
			String[] tableRow=new String[cols.size()-1];
			for (int i=1;i<cols.size();i++){
				tableRow[i-1]=cols.get(i).getText();
			}  	
			table.add(tableRow);
		}
	
		return table;
	}

	
	
	/**
	 * reads the sellers part of the tab of the site for the given id
	 * @param document the UI4J.Document item for the given id
	 * @return a list of list of String with the data 
	 */
	public static List<List<String>> getSellersTable(Document document){
		List<List<String>> table=new ArrayList<List<String>>();
		List<String> tableRow; 
		Element sellers=document.query("#dnn_ctr521_View_treeAnadoxoi").query("tbody");
		
		List<Element> rows=sellers.queryAll("tr");
		for(int row=1;row<rows.size();row++){
			List<Element> cols=rows.get(row).queryAll("td");
			tableRow=new ArrayList<String>();
			tableRow.add(cols.get(3).getText());
			tableRow.add(cols.get(4).getText());
			table.add(tableRow);
		}
		return table;
	}

	
	
	/**
	 * reads the foreis part of the tab of the site for the given id
	 * @param element the UI4J.Document item for the given id
	 * @return a list of list of String with the data 
	 */
	public static List<String> getForeisSubTable(Element element){
	try{
		List<String> data=new ArrayList<String>();
		List<Element> table=element.query("tbody").queryAll("tr").get(1).queryAll("td");
		
		for (int c=3;c<table.size();c++){
			data.add(table.get(c).getText().trim());
		
		}	
		return data;
	}
	catch(Exception e){
		return null;
	}
}
	/**
	 * will update the table Overview of the database
	 * @param review a List of String with the data to be inserted
	 * @param ops The given id
	 * @return true if insertion is successful, false otherwise
	 */
	public static boolean updateOverview(List<String> review,String ops){
		Connection conn = null;
		
	
		Statement st = null;
		PreparedStatement pstmt =null;
		
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			st = conn.createStatement();
			ResultSet rs=null;
		try{	
			 
			rs= st.executeQuery("select * from Overview where" +
				" ops='"+ops+"' and" +
				" date= \""+new SimpleDateFormat("yyyy-MM-dd").format(new Date()) +"\" and" +
				" title=\""+ deleteChars(review.get(0))+"\" and" +
				" beneficiary_Institution=\""+ deleteChars(review.get(1)) +"\" and" +
				" description =\""+deleteChars(review.get(2))+"\" and " +
				" public_Expenditure_Budget = "+deleteChars(review.get(3))+" and " +
				" payments ="+deleteChars(review.get(4))+" and " +
				" completition_percent="+deleteChars(review.get(5))+" and " +
				" start=\""+deleteChars(review.get(6))+"\" and " +
				" finish=\""+deleteChars(review.get(7))+"\" and " +
				" sub_projects="+deleteChars(review.get(8))+" and "+
				" public_aid="+deleteChars(review.get(9))+" and " +
				" operational_programme_number="+deleteChars(review.get(10))+" and "+
				" operational_programme=\""+deleteChars(review.get(11))+"\" and " +
				" title_ops=\""+deleteChars(review.get(12))+"\" and " +
				" map_coordinates=\""+deleteChars(review.get(13))+"\" ");
		}catch(Exception e){
			log("Exception on select * from Operation_Body where\n error="+e.getMessage()+"\n"+e.toString()+"\n"+e.getStackTrace().toString());
			notif.addWarning("Exception on select * from Operation_Body where\n error="+e.getMessage()+"\n"+e.toString()+"\n"+e.getStackTrace().toString());
		}
		
	 if (!rs.isBeforeFirst()|| rs==null ) {    
    	  //there is No record found maching the parameters. must be created

			 pstmt =
		    		    conn.prepareStatement("INSERT INTO Overview(ops,date,title,beneficiary_Institution,description,public_Expenditure_Budget,payments,completition_percent,start,finish,sub_projects,public_aid,operational_programme_number,operational_programme,title_ops,map_coordinates)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			 pstmt.setString(1,ops);
			 pstmt.setString(2,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			 for(int i=0;i<=review.size()-1;i++){
				 pstmt.setString(i+3,deleteChars(review.get(i)));
			 }
			 pstmt.execute();
	 }
			 pstmt.close();
			 st.close();
			 conn.close();
			 return true;
		} catch (SQLException e) {
			log("Error inside updateOverview "+e.getStackTrace());
			notif.addWarning("Error inside updateOverview "+e.getStackTrace());
			e.printStackTrace();
			return false;
		}
	}
//	/**
//	 * will update the table subWorks of the database
//	 * @param subWorks a List of String Array with the data to be inserted
//	 * @param ops The given id
//	 * @return true if insertion is successful, false otherwise
//	 */
//	public static boolean updateSubWorks(List<String[]> subWorks,String ops){
//		Connection conn = null;
//		Statement stmt = null;
//		Statement st = null;
//		PreparedStatement pstmt =null;
//		if (subWorks==null)return true;
//			try {
//				conn = DriverManager.getConnection(DB_URL,USER,PASS);
//				st = conn.createStatement();
//				
//				 pstmt =
//			    		    conn.prepareStatement("INSERT INTO Sub_Projects(AnadoxosID,ops,title,constructors,budget,start,finish)  VALUES(?,?,?,?,?,?,?)");
//				
//				 pstmt.setString(2,ops);
//				 for(int i=0;i<=subWorks.size()-1;i++){
//					 String[] row=subWorks.get(i); 
//					 int id = 0;
//					 stmt = conn.createStatement();
//					 	ResultSet rs= stmt.executeQuery("select id from Sellers where title='"+row[2]+"'" );
//					 if (rs.isBeforeFirst() ) {
//						 rs.next();
//						 id=rs.getInt("id");
//					 }else{
//						 
//						  rs= stmt.executeQuery("select id from Sellers where title=\" \"");
//						 rs.next();
//							 id=rs.getInt("id");
////				
//						
//					 }
//					 
//					 	 
//					 pstmt.setLong(1,id);				
//						 for (int r=1;r<row.length;r++){
//							 pstmt.setString(r+2,deleteChars(row[r]));	}
//						 pstmt.execute();
//					 }
//				 pstmt.close();
//				 st.close();
//				 conn.close();
//				 return true;
//			} catch (SQLException e) {
//				e.printStackTrace();
//																																																																																																																																																																																																																																																													
//				return false;
//			}		
//	}
	
	/**
	 * will update the table subWorks of the database
	 * @param subWorks a List of String Array with the data to be inserted
	 * @param ops The given id
	 * @return true if insertion is successful, false otherwise
	 */
	public static boolean V2_updateSubWorks(List<String[]> subWorks,String ops){
		Connection conn = null;
		Statement stmt = null;
		Statement st = null;
		PreparedStatement pstmt =null;
		if (subWorks==null)return true;
			try {
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
				st = conn.createStatement();
				
				
				 pstmt =
			    		    conn.prepareStatement("INSERT INTO Sub_Projects(ops,title,budget,start,finish)  VALUES(?,?,?,?,?)");
				
				 pstmt.setString(1,ops);
				 for(int i=0;i<=subWorks.size()-1;i++){
					 String[] row=subWorks.get(i); 
					 pstmt.setString(2,row[1]);//title
					 pstmt.setString(3,deleteChars(row[3]));//budget
					 pstmt.setString(4,row[4]);//start
					 pstmt.setString(5,row[5]);//finish
					 pstmt.execute();
					 stmt = conn.createStatement();
					 	ResultSet rs= stmt.executeQuery("select id from Sub_Projects where ops="+ops+" AND title=\""+deleteChars(row[1])+"\"" );
					 if (rs.isBeforeFirst() ) {
						 rs.next();
//						 subProjectId=rs.getInt("id");
						 updateProjectConstrucotrs(row[2],ops,rs.getInt("id"));
					 }
					 
					 }
				
				 pstmt.close();
				 st.close();
				 conn.close();
				 return true;
			} catch (SQLException e) {
				e.printStackTrace();
																																																																																																																																																																																																																																																													
				return false;
			}		
	}
	
	public static boolean updateProjectConstrucotrs(String fullConstructors, String ops,int subProjectId){
		
		Connection conn = null;
		Statement stmt = null;
		Statement st = null;
	//	PreparedStatement insertProjectConstructor =null;
		String[] constructors=fullConstructors.split(",");
	
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			st = conn.createStatement();
			
			
//			 insertProjectConstructor =
//		    		    conn.prepareStatement("INSERT INTO Projects_Constructors(subProjectID,constructorID,ops) VALUES(?,?,?");
//			 insertProjectConstructor.setLong(1, subProjectId);
//			 insertProjectConstructor.setString(3, ops);
			 
			 
			 int sellersId=0;
			 for(String constructorName:constructors){
				 stmt = conn.createStatement();
				 	ResultSet rs= stmt.executeQuery("select id from Sellers where title=\""+constructorName+"\"" );
				 		if (rs.isBeforeFirst() ) {
					 rs.next();
					 sellersId=rs.getInt("id");
				 }else{
					 rs= stmt.executeQuery("select id from Sellers where title=\" \"");
					 rs.next();
						 sellersId=rs.getInt("id");
				 }
				// 	System.out.println("select id from Anadoxoi where title=\" \"");
//				 insertProjectConstructor.setLong(2,anadoxoiId );
				 String sqlInsert="INSERT INTO Projects_Sellers(subProjectID,constructorID,ops) VALUES("+subProjectId+","+sellersId+",\""+ops+"\")";
				
//				 stmt = conn.createStatement();
					
stmt.execute(sqlInsert);

// execute insert SQL stetement
//					statement.executeUpdate(insertTableSQL)
				 
				 
				// insertProjectConstructor.executeUpdate();
				 
				 
			 }
//			 insertProjectConstructor.close();
			 
			 st.close();
			 conn.close();
return true;
		} catch (SQLException e) {
			e.printStackTrace();
			
			return false;
		}		
		
		 
	}
	/**
	 * will update the table Protash of the database
	 * @param table a List of String with the data to be inserted
	 * @param ops The given id
	 * @return true if insertion is successful, false otherwise
	 */
	 public static boolean updateProposalBody(List<String> table,String ops){
		
		Connection conn = null;
	//Statement stmt = null;
		Statement st = null;
		PreparedStatement pstmt =null;
		
		if (table==null)return true;
		
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			st = conn.createStatement();
			
			pstmt =
					conn.prepareStatement("INSERT INTO Proposal_Body (ops,title,representative,address,email)  VALUES(?,?,?,?,?)");
			pstmt.setString(1,ops);
			for(int i=0;i<=table.size()-1;i++){
				pstmt.setString(i+2,deleteChars(table.get(i)));
			
			}
		
			pstmt.execute();
		
			
			pstmt.close();
			st.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	} 
	 
		/**
		 * will update the table Funding_Body of the database
		 * @param table a List of String with the data to be inserted
		 * @param ops The given id
		 * @return true if insertion is successful, false otherwise
		 */
	public static boolean updateFundingBody(List<String> table,String ops){
		Connection conn = null;
//		Statement stmt = null;
		Statement st = null;
		PreparedStatement pstmt =null;
		if (table==null)return true;
		int id=getOverviewId(ops);
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			st = conn.createStatement();
			ResultSet rs= st.executeQuery("select * from Funding_Body where" +
					" OverviewID = "+id+" and" +
					" ops='"+ops+"' and" +
							" title= \""+deleteChars(table.get(0)) +"\" and" +
							" representative=\""+ deleteChars(table.get(1))+"\" and" +
							" address=\""+ deleteChars(table.get(2)) +"\" and" +
							" email=\""+deleteChars(table.get(3))+"\"");
			
			 if (!rs.isBeforeFirst() ) {    
		    	  //there is No record found maching the parameters. must be created
		
			pstmt =
					conn.prepareStatement("INSERT INTO Funding_Body(OverviewID,ops,title,representative,address,email)  VALUES(?,?,?,?,?,?)");
			pstmt.setLong(1,id);
			pstmt.setString(2,ops);

			for(int i=0;i<=table.size()-1;i++){
				pstmt.setString(i+3,deleteChars(table.get(i))); }
			pstmt.execute();
			 }
			pstmt.close();
			 
			st.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\n\nERROR\n\n"+ops);
			return false;
		}
	} 
	
	/**
	 * will update the table Administration_Body of the database
	 * @param table a List of String with the data to be inserted
	 * @param ops The given id
	 * @return true if insertion is successful, false otherwise
	 */
	public static boolean updateOperationBody(List<String> table,String ops){
		Connection conn = null;
//		Statement stmt = null;
		Statement st = null;
		PreparedStatement pstmt =null;
		
		if (table==null)return true;
		int id=getOverviewId(ops);		
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			st = conn.createStatement();
			ResultSet rs= st.executeQuery("select * from Operation_Body where " +
					"OverviewID = "+id +
					" and ops='"+ops+"' and" +
							" title= \""+deleteChars(table.get(0)) +"\" and" +
							" representative=\""+ deleteChars(table.get(1))+"\" and" +
							" address=\""+ deleteChars(table.get(2)) +"\" and" +
							" email=\""+deleteChars(table.get(3))+"\"");
			
			 if (!rs.isBeforeFirst() ) {    
		    	  //there is No record found maching the parameters. must be created
		
			pstmt =
					conn.prepareStatement("INSERT INTO Operation_Body(OverviewID,ops,title,representative,address,email)  VALUES(?,?,?,?,?,?)");
			pstmt.setLong(1,id);
			pstmt.setString(2,ops);
			
			for(int i=0;i<=table.size()-1;i++){
				pstmt.setString(i+3,deleteChars(table.get(i)));
			}
			pstmt.execute();
			 }
			pstmt.close();
			st.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	} 
	
	/**
	 * will update the table Ilopoiish of the database
	 * @param table a List of String with the data to be inserted
	 * @param ops The given id
	 * @return true if insertion is successful, false otherwise
	 */
	public static boolean updateImplementationBody(List<String> table,String ops){
		Connection conn = null;
//		Statement stmt = null;
		Statement st = null;
		PreparedStatement pstmt =null;

		if (table==null)return true;
		int id=getOverviewId(ops);
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			st = conn.createStatement();
			ResultSet rs= st.executeQuery("select * from Administration_Body where " +
					"OverviewID = "+id +
					" and ops='"+ops+"' and" +
							" title= \""+deleteChars(table.get(0)) +"\" and" +
							" representative=\""+ deleteChars(table.get(1))+"\" and" +
							" address=\""+ deleteChars(table.get(2)) +"\" and" +
							" email=\""+deleteChars(table.get(3))+"\"");
			
			 if (!rs.isBeforeFirst() ) {    
		    	  //there is No record found maching the parameters. must be created
		
			pstmt =
					conn.prepareStatement("INSERT INTO Implementation_Body(OverviewID,id,ops,title,representative,address,email)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1,id); 
			pstmt.setString(2,ops);
			for(int i=0;i<=table.size()-1;i++){
				pstmt.setString(i+3,deleteChars(table.get(i)));       }
			pstmt.execute();
			 }
			pstmt.close();
			st.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	} 
	
	/**
	 * will update the table Operation_Body of the database
	 * @param table a List of String with the data to be inserted
	 * @param ops The given id
	 * @return true if insertion is successful, false otherwise
	 */
	public static boolean updateAdministrationBody(List<String> table,String ops){
		Connection conn = null;
//		Statement stmt = null;
		Statement st = null;
		PreparedStatement pstmt =null;
		int id = 0;
		if (table==null)return true;
		id=getOverviewId(ops);		
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			st = conn.createStatement();
			
			ResultSet rs= st.executeQuery("select * from Administration_Body where" +
					" ops='"+ops+"' and" +
							" title= \""+deleteChars(table.get(0)) +"\" and" +
							" representative=\""+ deleteChars(table.get(1))+"\" and" +
							" address=\""+ deleteChars(table.get(2)) +"\" and" +
							" email=\""+deleteChars(table.get(3))+"\"");
			
			 if (!rs.isBeforeFirst() ) {    
		    	  //there is No record found maching the parameters. must be created
		
			pstmt =
					conn.prepareStatement("INSERT INTO Administration_Body (OverviewID,ops,title,representative,address,phone,email)  VALUES(?,?,?,?,?,?,?)");
			pstmt.setLong(1,id);
			pstmt.setString(2,ops);
			for(int i=0;i<=table.size()-1;i++){
				pstmt.setString(i+3,deleteChars(table.get(i)));
			}
			pstmt.execute();
			 }
			pstmt.close();
			st.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * will update the table Sellers of the database
	 * @param table a List of List of String with the data to be inserted
	 * @param ops The given id
	 * @return true if insertion is successful, false otherwise
	 */
	public static boolean updateSellers(List<List<String>> table,String ops){
		Connection conn = null;
		Statement stmt = null;
//		Statement st = null;
		PreparedStatement pstmt =null;
		if (table==null)return true;
		try {
			
			
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			//st = conn.createStatement();
			//select * from Sellers where title=.... AND diethinsh=....
			
			 //get each row of the sellers data
				//every row has one costructor information
			
			for(int i=0;i<=table.size()-1;i++){
				List<String> row=table.get(i); 		
				stmt = conn.createStatement();
				ResultSet rs= stmt.executeQuery("select * from Sellers where title='"+row.get(0)+"'");
				 if (!rs.isBeforeFirst() ) {    
			    	  //there is No record found maching the parameters. must be created
				pstmt =
						conn.prepareStatement("INSERT INTO Sellers(title,address)  VALUES(?,?)");
				for (int r=0;r<row.size();r++){
					pstmt.setString(r+1,deleteChars(row.get(r)));	
				}
				pstmt.execute(); 
				rs.close();
				}// if (!rs.isBeforeFirst() )	
				 else{
					 //TODO
				 }
				
				
				 
				
			}
			pstmt.close();
//			st.close();
			
			
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	/**
	 * deletes or replaces the wrong characters from the supplied string and return it clean to be used on the database
	 * Characters to be deleted the percentage symbol (%) and the Euro symbol (€) 
	 * Characters to be replaces The greek locale of number has dot (.) for thousands and comma (,) for decimal
	 * These will be replaced in a db friendly way. Dot for decimal and no thousand seperators
	 * @param value the String to check if needs replacements
	 * @return The String with the replaced chars
	 */
	public static String deleteChars(String value){

		if(value.equals("")||value==null)
		{	
			return "0";
		}
		if (value.contains("%")){
			value=	value.replace("%","");
			
		}
		if(value.contains("€")){
			value=value.replace("€","");
			value=value.replace(".", "");
			
			value= value.replace(",", ".");
		}
		if(value.contains("\"")){
		
			value= value.replaceAll("\"","'");
		}
	
		return value;
			
	}
	
	
	/**
	 * saves the log message to the log file and sends it over via email to the addresses supplied
	 * @param msg The message to be saved and sent via email
	 * @throws IOException
	 */
	public static void log(String msg) {
		//copy the msg to the log
	
		Date now=new Date();
		String logFile="EspaPerProject_log."+new SimpleDateFormat("yyyy.MM.dd").format(now)+".log";
		FileWriter writer;
		try {
			String file=logDir;
			if(File.separator.equals(logDir.charAt(logDir.length()-1))){
				file=logDir;
			}else
				file=logDir+File.separator;
			writer = new FileWriter(file+logFile,true);
			writer.append(now+"\t"+msg+"\n");
			writer.close();
		} catch (IOException e) {
			msg+="\n AND there was a problem with the Log file at "+logDir+"\nTerminating";
			notif.addError("there was a problem with the Log file at "+logDir+"\nTerminating");
			e.printStackTrace();
			System.exit(1);
		} 
		
			
		if(SEND_EMAIL)
			sendFromGMail(USER_NAME, PASSWORD, RECIPIENT, "Espa per Project", msg);
		
    	
	}
	/**
	 * send an email to the desired recipients
	 * @param from the email address of the sender
	 * @param pass the password of the sender
	 * @param to A String[] containing the recipients
	 * @param subject a String containing the subject of the email
	 * @param body a String containing the message to be sent
	 */
	private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        Session session =Session.getInstance(props); //Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
      
            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
            	
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
            }
            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            System.out.println("error"+ae.getLocalizedMessage());
        	ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
	public static int getOverviewId(String ops){
		try {
			Connection conn = null;
			Statement stmt = null;
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			
			stmt = conn.createStatement();
			ResultSet rs= stmt.executeQuery("select id from Overview where ops="+ops);
			
		 if (rs.isBeforeFirst() ) {
			 rs.next();
			 return rs.getInt("id");
		 }else{
			 //there is no record found. throw exception, log and exit
			 log("error while retriving id of project "+ops+"terminating");
			 notif.addError("error while retriving id of project "+ops+"terminating");
			System.exit(1);
			
		 }
		 rs.close();
		 stmt.close();
		 conn.close();
		 
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}
	
}
