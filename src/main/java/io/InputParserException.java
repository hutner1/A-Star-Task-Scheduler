package io;

/**
 * Contains a static method to print out an error message and quit the program
 */
public class InputParserException extends Exception {
	
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
