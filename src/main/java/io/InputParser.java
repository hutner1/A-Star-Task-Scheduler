package io;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses the command line input from the user and then determine
 * the output file name, number of processors to schedule for, number of cores to use
 * and whether or not to provide a visual representation of the scheduling process.
 *
 * Uses Apache Commons Cli library to parse the optional commands that starts with a 
 * dash "-", followed by the option's alphabet
 */
public class InputParser {

	private String[] _input;
	private int _numberOfProcessors;
	private boolean _visualise = false;
	private int _cores;
	private boolean _parallelise = false;
	private String _outputFileName;
	private File _file;
	private Logger _logger = LoggerFactory.getLogger(InputParser.class);

	/**
	 * InputParser constructor
	 * @param input the command line input stored in a string array.
	 */
	public InputParser(String[] input) {
		_input = input;
	}

	/**
	 * This method calls the helper methods from within the class in order to
	 * fully parse the required information from the command line input
	 * @throws InputParserException 
	 */
	public void parse() throws InputParserException {

		checkInputLengthAndHelp();
		parseFileName();
		parseProcessors();
		parseOptions();


	}

	/**
	 * This method checks to see whether or not the command line input has the required arguments,
	 * and terminates execution if it is not. If the input argument is "--help" instead, print
	 * out the help message.
	 * @throws InputParserException 
	 */
	private void checkInputLengthAndHelp() throws InputParserException {

		if((_input.length == 1) && (_input[0].equals("--help"))) {
			showHelpMessage();			
		} else if(_input.length < 2){
			//Error for when no arguments are supplied
			throw new InputParserException("Error! Please follow the correct input format!");
		}

	}

	/**
	 * This method parses the file name and terminates execution if the file is not
	 * in the specified ".dot" format.
	 * @throws InputParserException 
	 */
	private void parseFileName() throws InputParserException {
		//Read the file name from the input arguments - by David Qi
		String inputFileName = _input[0];

		//Verify that the file exists with correct extension. type - David Qi
		_file = new File(inputFileName);
		if(!(_file.exists() && inputFileName.substring(inputFileName.lastIndexOf(".") + 1, inputFileName.length()).equals("dot"))){
			throw new InputParserException("Error! The file is either not found or is of wrong type!");

		}
		//Set the default output file name
		_outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".")) + "-output";
	}

	/**
	 * This method parses the number of processors that the schedule is to be run on,
	 * and terminates execution if a number less than 1 is given.
	 * @throws InputParserException 
	 */
	private void parseProcessors() throws InputParserException {

		//Read the number of processors from the input argument - David Qi
		String Processors = _input[1];

		//Verify the validity of the argument for the number of processors and store it - David Qi

		_numberOfProcessors = Integer.parseInt(Processors);

		//Check that the number of processors is of valid value
		if(_numberOfProcessors < 1){
			throw new InputParserException("Invalid input for the number of processors!");
		}

	}

	/**
	 * This method parses the optional command line parameters
	 * @throws InputParserException 
	 */
	private void parseOptions() throws InputParserException {
		//Create the Common CLI command line options for each optional command
		CommandLine commandLine;
		Option option_V = Option.builder("v")
				.required(false)
				.desc("visualise")
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

		//Add the created options to the option object to be parsed
		options.addOption(option_V);
		options.addOption(option_P);
		options.addOption(option_O);

		try
		{
			//Get the array that may or may not contain optional commands
			String[] optionalCommands = Arrays.copyOfRange(_input, 2, _input.length);

			//Initialize the command line parser
			commandLine = parser.parse(options, optionalCommands);

			if (commandLine.hasOption("v"))
			{
				_visualise = true; //Set the visualization flag to true
				_logger.debug("Option v is present.  This is a flag option.");
			}

			if (commandLine.hasOption("p"))
			{
				_parallelise = true; //Parallelisation opted for

				//Check whether the p option is repeated, if yes output error
				if(commandLine.getOptionValues("p").length > 1){
					throw new InputParserException("Parse error: This option cannot be repeated!");
				}

				//Stored the number of cores to be used, output error if the 
				//entered argument cannot be parsed into an integer (invalid input)
				_cores = Integer.parseInt(commandLine.getOptionValue("p"));

				//Double check that the core count is valid
				if(_cores < 1) {
					throw new InputParserException("Parse error: Invalid input for the number of cores!");
				} else if(_cores > 8){
					_cores = 8;
				}

				_logger.debug("Option p is present.  The number of cores is: " + _cores);


			}

			if (commandLine.hasOption("o"))
			{

				//Check whether the o option is repeated, if yes output error
				if(commandLine.getOptionValues("o").length > 1){
					throw new InputParserException("Parse error: This option cannot be repeated!");
				}

				//Check if the output file name is empty/ invalid
				if(commandLine.getOptionValue("o") != null && commandLine.getOptionValue("o") != "") {

					//If the name is valid, change the output file name to the entered one
					_outputFileName = commandLine.getOptionValue("o"); 
				} else {
					throw new InputParserException("Parse error: Invalid input for the output name!");
				}

				_logger.debug("Option o is present.  The output name is: " + _outputFileName);

			}


			{
				//If there are arguments that does not belong to option o or p, output error
				String[] remainderArgs = commandLine.getArgs();

				if(remainderArgs.length > 0) {
					throw new InputParserException("Parse error: Invalid argument found!");
				}

			}

		} catch (ParseException exception) {
			throw new InputParserException("Parse error: " + exception.getMessage());
		}
	}

	/**
	 * Shows the Help message from the "--help" option, 
	 * displaying all the possible options
	 */
	public static void showHelpMessage(){

		System.out.println("java -jar scheduler.jar INPUT.dot P [OPTION]");
		System.out.println("");
		System.out.println("INPUT.dot	a task graph with integer weights in dot format");
		System.out.println("P		number of processors to schedule the INPUT graph on");
		System.out.println("");
		System.out.println("Optional:");
		System.out.println("-p N		use N cores for execution in parallel (default is sequential)");
		System.out.println("-v		visualise the search");
		System.out.println("-o OUTPUT	output file is named OUTPUT (default is INPUT-output.dot)");
		System.exit(0);
	}

	// Getter Methods

	/**
	 * Get number of cores to use when calculating the optimum schedule
	 * @return number of cores to use when calculating the optimum schedule
	 */
	public int getCores() {
		return _cores;
	}

	/**
	 * Get number of processors available for the schedule 
	 * @return number of processors available for the schedule
	 */
	public int getProcessors() {
		return _numberOfProcessors;
	}

	/**
	 * Determine whether or not to provide visualisation
	 * @return boolean representing whether or not to provide visual representation
	 */
	public boolean isVisualise() {
		return _visualise;
	}


	/**
	 * Determine whether parallelisation is opted for 
	 * @return whether parallelisation is opted for
	 */
	public boolean isParallelise(){
		return _parallelise;
	}

	/**
	 * Get the output file name
	 * @return name of output file
	 */
	public String getOutputFileName() {
		return _outputFileName;
	}

	/**
	 * Get the input file
	 * @return input file
	 */
	public File getFile() {
		return _file;
	}
}
