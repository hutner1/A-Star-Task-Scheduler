package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import scheduler.basicmilestone.Schedule;
import scheduler.basicmilestone.Vertex;

public class OutputWriter {

	private File _outputFile;

	public OutputWriter(String outputName) {
		_outputFile = new File(outputName + ".dot");
	}

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
	 * Create a schedule and write it to the output dot file 
	 * @param digraphName name of the digraph
	 * @param weightInfos the entries in the input file which will have 2 extra properties appended to it in the end for the output file
	 * @param nodesAndEdges list that contains all the tasks and edges as ">", in the same order as the input digraph, to have the schedule be in the same order as the input digraph
	 * @param solution Schedule containing Vertices and NodeInfos
	 */
	public void createSchedule(String digraphName, ArrayList<String> weightInfos, ArrayList<String> nodesAndEdges, Schedule solution, HashMap<String, Vertex> nodeMapping){

		//record first line of output file which contains the title
		record(_outputFile, "digraph \"" + digraphName +"\" {");

		//record the weight info together with the start time and processor, in order according to input file 
		for(String info : weightInfos){
			int currentPos = weightInfos.indexOf(info);
			// edge would be ">"
			String nodeOrEdge = nodesAndEdges.get(currentPos);

			// record initially recorded edge info directly back to file as no extra info is needed
			if(nodeOrEdge.equals(">")){
				record(_outputFile, info);

			} else {
				// add the start and processor info to the end before closing bracket
				StringBuilder augmentedInfo = new StringBuilder(info).insert(info.length()-2, solution.getVertexInfo(nodeMapping.get(nodeOrEdge)).outputString());
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
			System.out.println("An I/O Error Occurred when trying to write to " + file.toString());
			System.exit(0);
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
