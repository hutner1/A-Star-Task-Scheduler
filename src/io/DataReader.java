package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import scheduler.basicmilestone.Vertex;

public class DataReader {

	private String _digraphName;
	private ArrayList<String> _nodesAndEdgesRead; //Records nodes and edges as they are being read
	private ArrayList<String> _nodesAndEdgesInfo; //Records nodes and edges' info
	private DefaultDirectedWeightedGraph<Vertex,DefaultWeightedEdge> _digraph;
	private HashMap<String,Vertex> _mapping;
	private BufferedReader _reader;
	
	public DataReader(File file) {
		try {
			_reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			//File should already exist
			e.printStackTrace();
		}
	}

	/**
	 * Reads the next graph from the input file and stores the data. The digraph can
	 * be obtained by calling the getGraph() method.
	 */
	public void readNextGraph() {
		
		//Resets the stored data from possible previous graphs within the same file
		resetData();
		
		//Start reading the input file
		try {
			//Read first line of input file to get name of digraph and store it
			String text = _reader.readLine();
			String[] headerArray = text.split("\"");
			_digraphName = headerArray[1];

			//Read the input file until last line reached 
			while ((text = _reader.readLine()) != null && !(text.equals("}")) ) {
				//add input file's entries in order so that output file is just input file with extra attributes
				_nodesAndEdgesInfo.add(text);
				
				//System.out.println(text); 

				String[] lineArray=text.split("\\[");

				//Find the weights for all node/edge entries in input file
				int weight = getWeight(lineArray[1]);

				//Record node/edge info
				//if EDGE 
				if (lineArray[0].contains("->")) { //check if there's an arrow
					String[] nodeArray = lineArray[0].split("->");


					String nodeA = nodeArray[0].trim(); //get first character
					String nodeB = nodeArray[1].trim(); //get second character
					Vertex vertexA;
					Vertex vertexB;

					if (!_mapping.containsKey(nodeA)) {
						vertexA = new Vertex(nodeA);
						_digraph.addVertex(vertexA);
						_mapping.put(nodeA, vertexA);

					} else {
						vertexA = _mapping.get(nodeA);
					}

					if (!_mapping.containsKey(nodeB)) {
						vertexB = new Vertex(nodeB);
						_digraph.addVertex(vertexB);
						_mapping.put(nodeB, vertexB);
					} else {
						vertexB = _mapping.get(nodeB);
					}

					DefaultWeightedEdge edge = _digraph.addEdge(vertexA, vertexB);
					_digraph.setEdgeWeight(edge, weight);		

					_nodesAndEdgesRead.add(">"); // indicates that the current entry is an edge

					//if NODE
				} else {
					String node = lineArray[0].trim();

					if (!_mapping.containsKey(node)) {
						Vertex vertex = new Vertex(node);
						vertex.setWeight(weight);
						_digraph.addVertex(vertex);
						_mapping.put(node, vertex);

					} else {
						Vertex vertex = _mapping.get(node);
						vertex.setWeight(weight);

					}
					_nodesAndEdgesRead.add(node);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Resets the data in the stored ArrayLists and Graphs
	 */
	private void resetData() {
	
		_nodesAndEdgesRead = new ArrayList<String>();
		_nodesAndEdgesInfo = new ArrayList<String>();
		//Creates digraph with weighted vertices and weighted edges
		_digraph = new DefaultDirectedWeightedGraph<Vertex,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//Creates a HashMap mapping a string to the vertex with the same name
		_mapping = new HashMap<String,Vertex>();
	}

	/**
	 * Check if the file has more graphs to be read
	 * source : https://stackoverflow.com/questions/3714090/how-to-see-if-a-reader-is-at-eof
	 * @return boolean representing if the file has ended
	 */
	public boolean hasMoreGraphs() {
		int eof = 1;
		try {
			_reader.mark(2);
			eof = _reader.read();
			_reader.reset();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return eof > 0;
	}
	
	/**
	 * Method to parse and get the weight of the node/edge
	 * @param weightString string containing the node/edge weight
	 * @return Weight of the node/edge
	 */
	private int getWeight(String weightString) {
		weightString = weightString.replaceAll("[^0-9]+", " "); //get only the integers
		String[] weightArray = weightString.trim().split(" "); //get array of integers
		int weight = Integer.parseInt(weightArray[0]); //the weight is the first element in array

		return weight;
	}
	
	// Getter Methods
	
	public String getGraphName() {
		return _digraphName;
	}

	public ArrayList<String> getRead() {
		return _nodesAndEdgesRead;
	}

	public ArrayList<String> getInfo() {
		return _nodesAndEdgesInfo;
	}

	public DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> getGraph() {
		return _digraph;
	}
	
	public HashMap<String, Vertex> getMapping() {
		return _mapping;
	}

}