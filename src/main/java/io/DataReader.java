package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scheduler.graphstructures.DefaultDirectedWeightedGraph;
import scheduler.graphstructures.Vertex;

/**
 * Reads an input file for a task digraph and stores the digraph and order information
 */
public class DataReader {

	private String _digraphName;

	/**
	 * Records the vertices with their names and edges with ">" as they are being read.
	 * 
	 * It is used alongside _verticesAndEdgesInfo, so that the lines recorded can be
	 * identified as lines associated with a vertex/edge and additional information
	 * will be added if it was a vertex. 
	 */
	private ArrayList<String> _verticesAndEdgesRead; 
	/**
	 * Records the exact lines of vertices and edges' information as they are being read
	 */
	private ArrayList<String> _verticesAndEdgesInfo;
	// Stores the currently read graph
	private DefaultDirectedWeightedGraph _digraph;
	private HashMap<String,Vertex> _mapping;
	private BufferedReader _reader;

	private static Logger _logger = LoggerFactory
			.getLogger(DataReader.class);

	/**
	 * Constructs DataReader
	 * @param file input file to read digraph from
	 */
	public DataReader(File file) {
		_logger.debug("Hello World");
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

			// move to next line to check if the line is not needed
			text = _reader.readLine();

			// Gracefully takes care of unneeded lines
			while(!text.trim().substring(text.trim().length() - 1).equals(";")||!text.contains("[Weight=")){
				text = _reader.readLine();
			}

			// Read the input file until last line reached 
			while (text != null && !(text.equals("}")) ) {
				//add input file's entries in order so that output file is just input file with extra attributes
				_verticesAndEdgesInfo.add(text);

				String[] lineArray=text.split("\\[");

				//Find the weights for all vertex/edge entries in input file
				int weight = getWeight(lineArray[1]);

				//Record vertex/edge info
				//if EDGE 
				if (lineArray[0].contains("->")) { //check if there's an arrow
					String[] vertexArray = lineArray[0].split("->");


					String vertexStringA = vertexArray[0].trim(); //get first character
					String vertexStringB = vertexArray[1].trim(); //get second character
					Vertex vertexA;
					Vertex vertexB;

					if (!_mapping.containsKey(vertexStringA)) {
						vertexA = new Vertex(vertexStringA);
						_digraph.addVertex(vertexA);
						_mapping.put(vertexStringA, vertexA);

					} else {
						vertexA = _mapping.get(vertexStringA);
					}

					if (!_mapping.containsKey(vertexStringB)) {
						vertexB = new Vertex(vertexStringB);
						_digraph.addVertex(vertexB);
						_mapping.put(vertexStringB, vertexB);
					} else {
						vertexB = _mapping.get(vertexStringB);
					}

					_digraph.addEdge(vertexA, vertexB, weight);

					_verticesAndEdgesRead.add(">"); // indicates that the current entry is an edge

					//if VERTEX
				} else {
					String vertexString = lineArray[0].trim();

					if (!_mapping.containsKey(vertexString)) {
						Vertex vertex = new Vertex(vertexString);
						vertex.setWeight(weight);
						_digraph.addVertex(vertex);
						_mapping.put(vertexString, vertex);

					} else {
						Vertex vertex = _mapping.get(vertexString);
						vertex.setWeight(weight);

					}
					_verticesAndEdgesRead.add(vertexString);
				}
				text = _reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Resets the data in the stored ArrayLists and Graphs
	 */
	public void resetData() {

		_verticesAndEdgesRead = new ArrayList<String>();
		_verticesAndEdgesInfo = new ArrayList<String>();
		//Creates digraph with weighted vertices and weighted edges
		_digraph = new DefaultDirectedWeightedGraph();
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
	 * Method to parse and get the weight of the vertex/edge
	 * @param weightString string containing the vertex/edge weight
	 * @return Weight of the vertex/edge
	 */
	private int getWeight(String weightString) {
		weightString = weightString.replaceAll("[^0-9]+", " "); //get only the integers
		String[] weightArray = weightString.trim().split(" "); //get array of integers
		int weight = Integer.parseInt(weightArray[0]); //the weight is the first element in array

		return weight;
	}

	// Getter Methods

	/**
	 * Returns the name of the input digraph
	 * @return name of input digraph
	 */
	public String getGraphName() {
		return _digraphName;
	}

	/**
	 * Get the list of vertices and edges(resprsented as ">") in the order they are read
	 * @return list of vertices and edges(resprsented as ">") in the order they are read
	 */
	public ArrayList<String> getVerticesAndEdgesRead() {
		return _verticesAndEdgesRead;
	}

	/**
	 * Get the lines on vertices and edges as they are read from the input file
	 * @return lines on vertices and edges as they are read from the input file
	 */
	public ArrayList<String> getVerticesAndEdgesInfo() {
		return _verticesAndEdgesInfo;
	}

	/**
	 * Get the graph that was constructed from the information in the input file
	 * @return graph that was just constructed from the information in the input file
	 */
	public DefaultDirectedWeightedGraph getGraph() {
		return _digraph;
	}

	/**
	 * Get the map for the mapping from the vertex name to its Vertex instance
	 * @return map for the mapping from the vertex name to its Vertex instance
	 */
	public HashMap<String, Vertex> getMapping() {
		return _mapping;
	}

}