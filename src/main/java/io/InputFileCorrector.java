package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Formats Oliver's example input files to the right format 
 */
public class InputFileCorrector {
	// COMMENTED OUT SO NOBODY ACCIDENTALLY CALLS THIS
/*
	public static void main(String[] args) {
		// name of files to format from which to which (names must be number)
		int fromFile = 4;
		int toFile = 207;
		// from which line to which line to remove
		int fromLine = 2;
		int toLine = 8;
		
		// remove lines causing format to be wrong
		for(int i=fromFile; i<toFile+1; i++){
			// give the file a proper name
			File inputFile = new File("test-examples/"+i+".dot");
			ArrayList<String> contents = new ArrayList<>();
			
			try {
				// read and store all the contents of the file
				BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				String line = null;
				while ((line = reader.readLine()) != null) {
					contents.add(line);
				}
				reader.close();

				contents.subList(fromLine-1,toLine).clear();
				
				// clear file and output the lines needed
				clearFile(inputFile);
				for(String s: contents){
					record(inputFile, s);
				}
				
			} catch (FileNotFoundException e) {
				System.out.println(inputFile.toString()+" not found.");
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
			
		}

	}
  
	/**
	 * Convenient method to record a single line to a file and then print new line
	 * @param file file to write to
	 * @param line line of words to record to a file
	 */
	private static void record(File file,String line){
		try{
      
      
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			writer.println(line);
			writer.close();
		} catch(IOException e){
			ErrorMessenger.reportError("An I/O Error Occurred when trying to write to " + file.toString());
		}
	}

	/**
	 * Convenient method to clear a single file
	 * @param file file to clear
	 */
	private static void clearFile(File file){
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
