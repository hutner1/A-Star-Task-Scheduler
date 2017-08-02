package scheduler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class containing general static reusable methods 
 */
public class General {
	/**
	 * Convenient method to record a single line to a file and then print new line
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
	
	/**
	 * Convenient method to clear a single file
	 * @param file file to clear
	 */
	public static void clearFile(File file){
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


}
