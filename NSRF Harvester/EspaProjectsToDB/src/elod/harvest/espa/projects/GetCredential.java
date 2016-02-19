package elod.harvest.espa.projects;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetCredential {
private String fileName;

	public GetCredential(String file){
		this.fileName=file;
	}

	
	public String getProp(String title){		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(fileName));
			return prop.getProperty(title);
		} catch (IOException e) {
			e.printStackTrace();
					return null;
		}
	}
	
	
	
}
