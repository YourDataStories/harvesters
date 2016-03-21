package utils;

/**
 * @author G. Razis
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class Logger {
	
	private static Logger instance; //singleton pattern
	
	public static Logger getInstance() {
		
		if (instance == null) {
			instance = new Logger();
		}

		return instance;
	}
	
	public void write(String log) {
		
		try {
			BufferedWriter logWriter = new BufferedWriter(new FileWriter(
									   "log.txt", true));

			logWriter.write(DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").
							print(new DateTime()) + ": " + log + "\n");
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void error(Exception e) {
		
		String logMessage = "ERROR! : " + e.toString() + "\n";
		
		for (StackTraceElement elem : e.getStackTrace()) {
			logMessage += elem.toString() + "\n";
		}
		
		write(logMessage);
		
	}
	
}