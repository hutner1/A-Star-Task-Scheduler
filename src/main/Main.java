package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		
		String inputFileName = args[0];
		File file = new File(inputFileName);
		if(!file.exists()){
			System.out.println("File not found!");
			System.exit(0);
		}
		
		String NumberOfProcessors = args[1];
		
		
		if(args[2] != null){
			
			
			
		}
		
		
		BufferedReader reader = null;
		
		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    while ((text = reader.readLine()) != null && (text != "}") ) {
		    	
		    	System.out.println(reader.readLine());
		    	
		    }
		    
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
		    e.printStackTrace();
		}
		

		

	}

}
