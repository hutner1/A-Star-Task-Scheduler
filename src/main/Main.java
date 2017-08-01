package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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
		    text = reader.readLine();
		    String[] headerArray = text.split("\"");
		    String digraphName = headerArray[1];
		    
		    while ((text = reader.readLine()) != null && (text != "}") ) {
		    	
		    	System.out.println(text);
		    	
		    	String[] lineArray=text.split("[");
		    	
		    	//finds the weight
		
		    	String weightString = lineArray[1];      
		    	weightString = weightString.replaceAll("[^0-9]+", " "); //get only the integers
		    	String[] weightArray = weightString.trim().split(" "); //get array of integers
		    	int weight =Integer.parseInt(weightArray[0]); //the weight is the first element in array
		    	System.out.println(weight);
		    	
		    	//Read node
		    	
		    	if (lineArray[0].contains("->")) { //check if there's an arrow
		    		String[] nodeArray = lineArray[0].split("->"); 
		    		char nodeA = nodeArray[0].trim().charAt(0); //get first character
		    		System.out.println(nodeA);
		    		char nodeB = nodeArray[1].trim().charAt(1); //get second character
		    		System.out.println(nodeB);
		    	} else {
		    		String nodeString = lineArray[0].trim();
		    		char node = nodeString.charAt(0); //get first character of string
		    		System.out.println(node);
		    	}
		    	
		    			    	
		    }
		    
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
		    e.printStackTrace();
		}
		

		

	}

}
