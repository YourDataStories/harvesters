import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class OpenCorporatesHarvester {

	private static final int BUFFER_SIZE = 4096;
	 
	public static void main(String[] args) throws InterruptedException {
		try {
			File file = new File("companies.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String outputDir = "";
			String line;
			String apiToken = "?api_token=";
			int i = 1;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println("Company: " + line + apiToken);
				fetchResource(line+apiToken, outputDir + "response_" + i + ".rdf");
				i++;
				//TimeUnit.SECONDS.sleep(1);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
     * Fetches an OpenCorporates company description as an RDF file from a URL
     * @param requestURL HTTP URL of the company to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws IOException
     */
	public static void fetchResource(String requestURL, String saveDir)
            throws IOException {
        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/rdf+xml");
        connection.setInstanceFollowRedirects(true);
        HttpURLConnection.setFollowRedirects(true);
        
        int responseCode = connection.getResponseCode();
 
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String contentType = connection.getContentType();
            int contentLength = connection.getContentLength();
 
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Length = " + contentLength);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = connection.getInputStream();
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveDir);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            System.out.println("File downloaded");
        } else {
        	if (responseCode == HttpURLConnection.HTTP_MOVED_PERM){
        		System.out.println("REDIRECTED TO " + connection.getHeaderField("Location"));
        	}
        	else {
        		System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        	}
        }
        connection.disconnect();
    }

}
