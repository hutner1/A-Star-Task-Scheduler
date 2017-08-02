package scheduler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class Main {

	public static void main(String[] args) {

		int NumberOfProcessors, cores;
		int pFlag = 0;
		int vFlag = 0;
		int oFlag = 0;
		String OutputFileName;
		ArrayList<String> optional_commands = new ArrayList<String>();

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


		int argumentIndex = 2;
		//Read and store the optional commands - David Qi
		while(argumentIndex < args.length){
			optional_commands.add(args[argumentIndex]);
			argumentIndex++;
		}
		
		

		//Verify the validity of the optional commands if it is not empty - David Qi
		if(optional_commands.size() != 0){

			for(int i = 0; i < optional_commands.size(); i++){

				if(optional_commands.get(i).equals("-v")){	

					vFlag = 1; //Set the v optional command flag if found

				} else if(optional_commands.get(i).equals("-p")){

					//Checking that the optional command for cores is not repeated
					if(pFlag == 1){
						InputError("Warning, invalid optional command found!");
					}

					pFlag = 1; //Set the p optional command flag

					//Stored the number of cores to be used
					try{
						cores = Integer.parseInt(optional_commands.get(i+1));

					} catch (NumberFormatException e) {

						InputError("Invalid input for the number of cores!");

					}

					i++;

				} else if(optional_commands.get(i).equals("-o")){

					//Checking that the optional command for output name is not repeated
					if(oFlag == 1){
						InputError("Warning, invalid optional command found!");
					}

					oFlag = 1; //Set the o optional command flag

					String filename = optional_commands.get(i+1);

					//Store the output file name
					OutputFileName = filename;

					i++;

				} else {
					InputError("Warning, invalid optional command found!");
				}


			}

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
				int weight = getWeight(lineArray[1]);

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
	
	private static int getWeight(String weightString) {
		weightString = weightString.replaceAll("[^0-9]+", " "); //get only the integers
		String[] weightArray = weightString.trim().split(" "); //get array of integers
		int weight =Integer.parseInt(weightArray[0]); //the weight is the first element in array
		
		return weight;
	}
	public static void InputError(String msg){
		System.out.println(msg);
		System.exit(0);
	}

}
