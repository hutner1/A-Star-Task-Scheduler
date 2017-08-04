package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import scheduler.General;
import scheduler.basicmilestone.Schedule;
import scheduler.basicmilestone.Vertex;

public class OutputWriter {
	/**
	 * Create a schedule and write it to the output dot file 
	 * @param outputName name of the output file which will have ".dot" appended to it
	 * @param digraphName name of the digraph
	 * @param weightInfos the entries in the input file which will have 2 extra properties appended to it in the end for the output file
	 * @param nodesAndEdges list that contains all the tasks and edges as ">", in the same order as the input digraph, to have the schedule be in the same order as the input digraph
	 * @param solution Schedule containing Vertices and NodeInfos
	 */
	public void createSchedule(String outputName, String digraphName, ArrayList<String> weightInfos, ArrayList<String> nodesAndEdges, Schedule solution, HashMap<String, Vertex> nodeMapping){
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
				StringBuilder augmentedInfo = new StringBuilder(info).insert(info.length()-2, solution.getVertexInfo(nodeMapping.get(nodeOrEdge)).outputString());
				General.record(outputFile, augmentedInfo.toString());
			}
		}

		//end the output file with closing bracket
		General.record(outputFile, "}");


	}
}
