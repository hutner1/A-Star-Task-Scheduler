package io;

/**
 * Contains a static method to print out an error message and quit the program
 */
public class ErrorMessenger {
	
	/**
	 * Method to print out error and exit the program immediately
	 * @param msg error message
	 */
	public static void reportError(String msg){
		System.out.println(msg);
		System.exit(0);
	}

}
