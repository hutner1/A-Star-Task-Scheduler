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
		System.out.println("Type --help to see the input requirements");
		System.exit(0);
	}
	
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

}
