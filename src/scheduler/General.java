package scheduler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class General {
	/**
	 * Convenient method to record a single line to a file
	 * @param file file to write to
	 * @param line line of words to record to a file
	 */
	public static void record(File file,String line){
		try{
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			writer.println(line);
			writer.close();
		} catch(IOException e){
			System.out.println("An I/O Error Occurred when trying to write to " + file.toString());
			System.exit(0);
		}
	}
	
	


}
