package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import scheduler.astar.Solution;
import scheduler.dfsbranchandbound.SolutionGenerator;
import scheduler.graphstructures.Vertex;

/**
 * Writes optimal solution to a file.
 * 
 * The ordering of the information on nodes and edges will be the
 * same as the input file.
 */
public class OutputWriter {

	private File _outputFile;

	/**
	 * Initialise OutputWriter with the output file name with ".dot" appended to it
	 * @param outputName name of output file before the ".dot" is appended to it
	 */
	public OutputWriter(String outputName) {
		_outputFile = new File(outputName + ".dot");
	}

	/**
	 * Checks and creates the necessary output file before writing to it
	 */
	public void initialise() {
		//create output file to write to if it doesn't exists  
		if(!_outputFile.exists()){
			try {
				_outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//clear the output file if it exists to start anew with new outputs
		} else {
			clearFile(_outputFile);
		}
	}

	/**
	 * Outputs the optimal schedule to a file, while considering the order demonstrated in the input file.
	 * @param digraphName name of input task digraph
	 * @param verticesAndEdgesInfo lines on vertices and edges as they are read from the input file
	 * @param verticesAndEdgesRead list of vertices and edges(resprsented as ">") in the order they are read
	 * @param solution optimal solution
	 * @param vertexMapping map for mapping from vertex name to Vertex instance
	 */
	public void createScheduleAStar(String digraphName, ArrayList<String> verticesAndEdgesInfo, ArrayList<String> verticesAndEdgesRead, Solution solution, HashMap<String, Vertex> vertexMapping){

		//record first line of output file which contains the title
		record(_outputFile, "digraph \"" + digraphName +"\" {");

		//record the weight info together with the start time and processor, in order according to input file 
		for(String info : verticesAndEdgesInfo){
			int currentPos = verticesAndEdgesInfo.indexOf(info);
			// edge would be ">"
			String vertexOrEdge = verticesAndEdgesRead.get(currentPos);

			// record initially recorded edge info directly back to file as no extra info is needed
			if(vertexOrEdge.equals(">")){
				record(_outputFile, info);

			} else {
				// add the start and processor info to the end before closing bracket
				StringBuilder augmentedInfo = new StringBuilder(info).insert(info.length()-2, solution.getVertexString(vertexMapping.get(vertexOrEdge)));
				record(_outputFile, augmentedInfo.toString());
			}
		}

		//end the output file with closing bracket
		record(_outputFile, "}");
	}
	
  	
	public void createScheduleDFS(String digraphName, ArrayList<String> weightInfos, ArrayList<String> verticesAndEdges, SolutionGenerator solutionGenerator, HashMap<String, Vertex> vertexMapping){

		//record first line of output file which contains the title
		record(_outputFile, "digraph \"" + digraphName +"\" {");

		//record the weight info together with the start time and processor, in order according to input file 
		for(String info : weightInfos){
			int currentPos = weightInfos.indexOf(info);
			// edge would be ">"
			String vertexOrEdge = verticesAndEdges.get(currentPos);

			// record initially recorded edge info directly back to file as no extra info is needed
			if(vertexOrEdge.equals(">")){
				record(_outputFile, info);

			} else {
				// add the start and processor info to the end before closing bracket
				//StringBuilder augmentedInfo = new StringBuilder(info).insert(info.length()-2, solution.getVertexInfo(vertexMapping.get(vertexOrEdge)).outputString());
				StringBuilder augmentedInfo = new StringBuilder(info).insert(info.length()-2, solutionGenerator.outputString(vertexMapping.get(vertexOrEdge)));

				record(_outputFile, augmentedInfo.toString());
			}
		}

		//end the output file with closing bracket
		record(_outputFile, "}");
	}
  
	/**
	 * Convenient method to record a single line to a file and then print new line
	 * @param file file to write to
	 * @param line line of words to record to a file
	 */
	private void record(File file,String line){
		try{
      
      
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			writer.println(line);
			writer.close();
		} catch(IOException e){
			//InputParserException("An I/O Error Occurred when trying to write to " + file.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Convenient method to clear a single file
	 * @param file file to clear
	 */
	private void clearFile(File file){
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
