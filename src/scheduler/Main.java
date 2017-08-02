package scheduler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

import scheduler.basicmilestone.Vertex;

/**
 * This is the main class for the task scheduler program.
 */
public class Main {

	public static void main(String[] args) {

		int numberOfProcessors;
		boolean visualize = false;
		int cores = 1;
		String outputFileName;
		String[] optionalCommands;
		
		//Records nodes and edges as they are being read
		ArrayList<String> nodesAndEdgesRead =new ArrayList<>();
		//Records nodes and edges' info
		ArrayList<String> nodesAndEdgesInfo = new ArrayList<>();

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
		
		//Set the default output file name
		outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".")) + "-output";

		//Read the number of processors from the input argument - David Qi
		String Processors = args[1];

		//Verify the validity of the argument for the number of processors and store it - David Qi
		try{
			numberOfProcessors = Integer.parseInt(Processors);
		} catch (NumberFormatException e) {
			InputError("Invalid input for the number of processors!");
		}
		
		//Create the Common CLI command line options for each optional command
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
			optionalCommands = Arrays.copyOfRange(args, 2, args.length);
			
			commandLine = parser.parse(options, optionalCommands);

			if (commandLine.hasOption("v"))
			{
				visualize = true; //Set the visualization flag to true
				System.out.println("Option v is present.  This is a flag option.");
			}

			if (commandLine.hasOption("p"))
			{
				
				//Check whether the p option is repeated, if yes output error
				if(commandLine.getOptionValues("p").length > 1){
					InputError("Parse error: This option cannot be repeated!");
				}

				//Stored the number of cores to be used, output error if the 
				//entered argument cannot be parsed into an integer (invalid input)
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
				
				//Check whether the o option is repeated, if yes output error
				if(commandLine.getOptionValues("o").length > 1){
					InputError("Parse error: This option cannot be repeated!");
				}
				
				//Check if the output file name is empty/ invalid
				if(commandLine.getOptionValue("o") != null && commandLine.getOptionValue("o") != "") {
					
					//If the name is valid, change the output file name to the entered one
					outputFileName = commandLine.getOptionValue("o"); 
				} else {
					InputError("Parse error: Invalid input for the output name!");
				}
				
				System.out.print("Option o is present.  The output name is: ");
				System.out.println(outputFileName);
				
			}


			{
				String[] remainderArgs = commandLine.getArgs();
				
				if(remainderArgs.length > 0) {
					InputError("Parse error: Invalid argument found!");
				}
				
			}

		} catch (ParseException exception) {
			InputError("Parse error: " + exception.getMessage());
		}


		//Start reading the input file
		BufferedReader reader = null;
		String digraphName = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			
			//Read first line of input file to get name of digraph and store it
			String text = reader.readLine();
			String[] headerArray = text.split("\"");
			digraphName = headerArray[1];

			//Creates digraph with weighted vertices and weighted edges
			SimpleDirectedWeightedGraph<String,DefaultWeightedEdge> digraph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
			HashMap<String,Integer> nodeWeights = new HashMap<String,Integer>(); //Create hash map to store node weights as SimpleDirectedWeightedGraph doesn't do this 

			//Read the input file until last line reached 
			while ((text = reader.readLine()) != null && !(text.equals("}")) ) {
				nodesAndEdgesInfo.add(text);
				System.out.println(text); 

				String[] lineArray=text.split("\\[");

				//Find the weights for all node/edge entries in input file
				int weight = getWeight(lineArray[1]);

				//Record node/edge info
				//if EDGE 
				if (lineArray[0].contains("->")) { //check if there's an arrow
					String[] nodeArray = lineArray[0].split("->"); 
					String nodeA = nodeArray[0].trim(); //get first character
					String nodeB = nodeArray[1].trim(); //get second character
					DefaultWeightedEdge edge = digraph.addEdge(nodeA, nodeB);
					digraph.setEdgeWeight(edge, weight);		

					nodesAndEdgesRead.add(">"); // indicates that the current entry is an edge
					
				//if NODE
				} else {
					String node = lineArray[0].trim();
					//char node = nodeString.charAt(0); //get first character of string
					digraph.addVertex(node);
					nodeWeights.put(node, weight);
					
					nodesAndEdgesRead.add(node);
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
		
		//Create the optimal schedule
		createSchedule(outputFileName,digraphName,nodesAndEdgesInfo,nodesAndEdgesRead);
	}
	
	/**
	 * Method to parse and get the weight of the node/edge
	 * @param weightString string containing the node/edge weight
	 * @return Weight of the node/edge
	 */
	private static int getWeight(String weightString) {
		weightString = weightString.replaceAll("[^0-9]+", " "); //get only the integers
		String[] weightArray = weightString.trim().split(" "); //get array of integers
		int weight =Integer.parseInt(weightArray[0]); //the weight is the first element in array
		
		return weight;
	}
	
	/**
	 * Method to print out error and exit the program immediately
	 * @param msg error message
	 */
	public static void InputError(String msg){
		System.out.println(msg);
		System.exit(0);
	}

	private static void createSchedule(String outputName, String digraphName, ArrayList<String> weightInfos, ArrayList<String> nodesAndEdges){
		File outputFile = new File(outputName + ".dot");
		
		//create output file to write to if it doesn't exists  
		if(! outputFile.exists()){
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		//clear the output file if it exists to start anew with new outputs
		} else {
			General.clearFile(outputFile);
		}
		
		//record first line of output file which contains the title
		General.record(outputFile, "digraph \"" + digraphName +"\" {");
		
		//record the weight info together with the start time and processor, in order according to input file 
		for(String info : weightInfos){
			int currentPos = weightInfos.indexOf(info);
			// edge would be ">"
			String nodeOrEdge = nodesAndEdges.get(currentPos);
			
			// record initially recorded edge info directly back to file as no extra info is needed
			if(nodeOrEdge.equals(">")){
				General.record(outputFile, info);
			} else {
				// add the start and processor info to the end before closing bracket
				String augmentedInfo = new StringBuilder(info).insert(info.length()-2, ",Start=" + getTaskStart(nodeOrEdge)).toString();
				augmentedInfo = new StringBuilder(augmentedInfo).insert(augmentedInfo.length()-2, ",Processor=" + getTaskProcessor(nodeOrEdge)).toString();
				General.record(outputFile, augmentedInfo);
			}
		}
		
		//end the output file with closing bracket
		General.record(outputFile, "}");

		
	}
	
	/**
	 * Returns the Start time for a task
	 * @param task task to obtain Start time for
	 * @return Start time for the task
	 */
	private static int getTaskStart(String task){
		// TODO because it currently returns dummy value
		return 0;
	}

	/**
	 * Returns the Processor for a task to run on
	 * @param task task to obtain Processor to run on for
	 * @return Processor for the task to run on
	 */
	private static int getTaskProcessor(String task){
		// TODO beacause it currently returns dummy value
		return 1;
	
	}
}
