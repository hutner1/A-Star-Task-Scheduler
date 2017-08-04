package io;

public class InputError {
	
	public InputError() {
	}
	/**
	 * Method to print out error and exit the program immediately
	 * @param msg error message
	 */
	public static void inputError(String msg){
		System.out.println(msg);
		System.exit(0);
	}

}
