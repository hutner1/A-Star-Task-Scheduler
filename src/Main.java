import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class Main {

	public static void main(String[] args) {

		int NumberOfProcessors;
		int vFlag= 0;
		int cores = 1;
		String OutputFileName = "INPUT-output";
		String[] optional_commands;

		//Error for when no arguments are supplied - Hunter
		if(args.length < 2) {
			InputError("Error! Please follow the correct input format");
		}

		//Read the file name from the input arguments - by David Qi
		String inputFileName = args[0];

		//Verify that the file exists with correct extension. type - David Qi
		File file = new File(inputFileName);
		if(!(file.exists() && inputFileName.substring(inputFileName.lastIndexOf(".") + 1, inputFileName.length()).equals("dot"))){
			InputError("Error! The file is either not found or is of wrong type!");

		}


		//Read the number of processors from the input argument - David Qi
		String Processors = args[1];

		//Verify the validity of the argument for the number of processors and store it - David Qi
		try{
			NumberOfProcessors = Integer.parseInt(Processors);

		} catch (NumberFormatException e) {

			InputError("Invalid input for the number of processors!");

		}
		
		CommandLine commandLine;
		Option option_V = Option.builder("v")
				.required(false)
				.desc("Visualize")
				.build();
		Option option_P = Option.builder("p")
				.required(false)
				.desc("The number of processors")
				.numberOfArgs(1)
				.build();
		Option option_O = Option.builder("o")
				.required(false)
				.desc("The output name")
				.numberOfArgs(1)
				.build();
		Options options = new Options();
		CommandLineParser parser = new DefaultParser();
		
		options.addOption(option_V);
		options.addOption(option_P);
		options.addOption(option_O);
		
		try
		{
			optional_commands = Arrays.copyOfRange(args, 2, args.length);
			
			commandLine = parser.parse(options, optional_commands);

			if (commandLine.hasOption("v"))
			{
				vFlag = 1;
				System.out.println("Option v is present.  This is a flag option.");
			}

			if (commandLine.hasOption("p"))
			{

				//Stored the number of cores to be used
				try{
					cores = Integer.parseInt(commandLine.getOptionValue("p"));

				} catch (NumberFormatException e) {

					InputError("Parse error: Invalid input for the number of cores!");

				}
				
				System.out.print("Option p is present.  The number of cores is: ");
				System.out.println(cores);
				
				
			}

			if (commandLine.hasOption("o"))
			{
				
				if(commandLine.getOptionValue("o") != null && commandLine.getOptionValue("o") != "") {
					OutputFileName = commandLine.getOptionValue("o");
				} else {
					InputError("Parse error: Invalid input for the output name!");
				}
				
				System.out.print("Option o is present.  The output name is: ");
				System.out.println(commandLine.getOptionValue("o"));
				
			}


			{
				String[] remainderArgs = commandLine.getArgs();
				
				if(remainderArgs.length > 0) {
					InputError("Warning, invalid argument found!");
				}
				
			}

		} catch (ParseException exception) {
			InputError("Parse error: " + exception.getMessage());
		}


		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new FileReader(file));
			String text = null;
			text = reader.readLine();
			String[] headerArray = text.split("\"");
			String digraphName = headerArray[1];

			//Creates digraph with weighted vertices and weighted edges
			SimpleDirectedWeightedGraph<Character,DefaultWeightedEdge> digraph = new SimpleDirectedWeightedGraph<Character, DefaultWeightedEdge>(DefaultWeightedEdge.class);
			HashMap<Character,Integer> nodeWeights = new HashMap<Character,Integer>();

			while ((text = reader.readLine()) != null && !(text.equals("}")) ) {

				System.out.println(text);

				String[] lineArray=text.split("\\[");

				//finds the weight

				String weightString = lineArray[1];      
				weightString = weightString.replaceAll("[^0-9]+", " "); //get only the integers
				String[] weightArray = weightString.trim().split(" "); //get array of integers
				int weight =Integer.parseInt(weightArray[0]); //the weight is the first element in array

				//Read node

				if (lineArray[0].contains("->")) { //check if there's an arrow
					String[] nodeArray = lineArray[0].split("->"); 
					char nodeA = nodeArray[0].trim().charAt(0); //get first character
					char nodeB = nodeArray[1].trim().charAt(0); //get second character
					DefaultWeightedEdge edge = digraph.addEdge(nodeA, nodeB);
					digraph.setEdgeWeight(edge, weight);

				} else {
					String nodeString = lineArray[0].trim();
					char node = nodeString.charAt(0); //get first character of string
					digraph.addVertex(node);
					nodeWeights.put(node, weight);
				}


			}
			
			System.out.println(digraph.vertexSet().toString());
			System.out.println(nodeWeights.values());
			System.out.println(digraph.edgeSet().toString());	

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			e.printStackTrace();
		}




	}
	
	
	public static void InputError(String msg){
		System.out.println(msg);
		System.exit(0);
	}

}
