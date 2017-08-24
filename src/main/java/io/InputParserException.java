package io;

/**
 * Exception class that can be instantiated to print an error message and quit the program
 */
public class InputParserException extends Exception {
	
	/**
	 * Constructor for InputParserException
	 */
	public InputParserException() {
		super();
	}
	
	/**
	 * Method to print out error and exit the program immediately
	 * @param msg error message
	 */
	public InputParserException(String msg) {
		super(msg + "\nType --help to see the input requirements");
	}

}
