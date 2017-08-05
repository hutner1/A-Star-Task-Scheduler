package io;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This class is used to parse the command line input from the user and then determine
 * the output file name, number of processors to schedule for, number of cores to use
 * and whether or not to provide a visual representation of the scheduling process.
 * @author Hunter
 *
 */
public class InputParser {

	private String[] _input;
	private int _numberOfProcessors;
	private boolean _visualise = false;
	private int _cores;
	private String _outputFileName;
	private File _file;

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
	 */
	public void parse() {

		checkInputLength();
		parseFileName();
		parseProcessors();
		parseOptions();

	}

	/**
	 * This method checks to see whether or not the command line input has the required arguments,
	 * and terminates execution if it is not.
	 */
	private void checkInputLength() {
		//Error for when no arguments are supplied - Hunter
		if(_input.length < 2) {
			ErrorMessenger.reportError("Error! Please follow the correct input format");
		}
	}

	/**
	 * This method parses the file name and terminates execution if the file is not
	 * in the specified ".dot" format.
	 */
	private void parseFileName() {
		//Read the file name from the input arguments - by David Qi
		String inputFileName = _input[0];

		//Verify that the file exists with correct extension. type - David Qi
		_file = new File(inputFileName);
		if(!(_file.exists() && inputFileName.substring(inputFileName.lastIndexOf(".") + 1, inputFileName.length()).equals("dot"))){
			ErrorMessenger.reportError("Error! The file is either not found or is of wrong type!");

		}
		//Set the default output file name
		_outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".")) + "-output";
	}

	/**
	 * This method parses the number of processors that the schedule is to be run on,
	 * and terminates execution if a number less than 1 is given.
	 */
	private void parseProcessors() {

		//Read the number of processors from the input argument - David Qi
		String Processors = _input[1];

		//Verify the validity of the argument for the number of processors and store it - David Qi
		try{
			_numberOfProcessors = Integer.parseInt(Processors);

			//Check that the number of processors is of valid value
			if(_numberOfProcessors < 1){
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {
			ErrorMessenger.reportError("Invalid input for the number of processors!");
		}
	}

	/**
	 * This method parses the optional command line parameters
	 */
	private void parseOptions() {
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
				System.out.println("Option v is present.  This is a flag option.");
			}

			if (commandLine.hasOption("p"))
			{

				//Check whether the p option is repeated, if yes output error
				if(commandLine.getOptionValues("p").length > 1){
					ErrorMessenger.reportError("Parse error: This option cannot be repeated!");
				}

				//Stored the number of cores to be used, output error if the 
				//entered argument cannot be parsed into an integer (invalid input)
				try{
					_cores = Integer.parseInt(commandLine.getOptionValue("p"));

					//Double check that the core count is valid
					if(_cores < 1) {
						throw new NumberFormatException();
					}

				} catch (NumberFormatException e) {

					ErrorMessenger.reportError("Parse error: Invalid input for the number of cores!");

				}

				System.out.print("Option p is present.  The number of cores is: ");
				System.out.println(_cores);


			}

			if (commandLine.hasOption("o"))
			{

				//Check whether the o option is repeated, if yes output error
				if(commandLine.getOptionValues("o").length > 1){
					ErrorMessenger.reportError("Parse error: This option cannot be repeated!");
				}

				//Check if the output file name is empty/ invalid
				if(commandLine.getOptionValue("o") != null && commandLine.getOptionValue("o") != "") {

					//If the name is valid, change the output file name to the entered one
					_outputFileName = commandLine.getOptionValue("o"); 
				} else {
					ErrorMessenger.reportError("Parse error: Invalid input for the output name!");
				}

				System.out.print("Option o is present.  The output name is: ");
				System.out.println(_outputFileName);

			}


			{
				//If there are arguments that does not belong to option o or p, output error
				String[] remainderArgs = commandLine.getArgs();

				if(remainderArgs.length > 0) {
					ErrorMessenger.reportError("Parse error: Invalid argument found!");
				}

			}

		} catch (ParseException exception) {
			ErrorMessenger.reportError("Parse error: " + exception.getMessage());
		}
	}

	// Getter Methods
	
	/**
	 * 
	 * @return number of cores to use when calculating the optimum schedule
	 */
	public int getCores() {
		return _cores;
	}

	/**
	 * 
	 * @return number of processors available for the schedule
	 */
	public int getProcessors() {
		return _numberOfProcessors;
	}
	
	/**
	 * 
	 * @return boolean representing whether or not to provide visual representation
	 */
	public boolean isVisualise() {
		return _visualise;
	}

	/**
	 * 
	 * @return name of output file
	 */
	public String getOutputFileName() {
		return _outputFileName;
	}

	/**
	 * 
	 * @return input file
	 */
	public File getFile() {
		return _file;
	}



}
