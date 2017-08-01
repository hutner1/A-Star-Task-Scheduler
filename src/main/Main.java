package main;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		int NumberOfProcessors, cores;
		int pFlag = 0;
		int vFlag = 0;
		int oFlag = 0;
		String OutputFileName;
		ArrayList<String> optional_commands = new ArrayList<String>();

		//Read the file name from the input arguments - by David Qi
		String inputFileName = args[0];

		//Verify that the file exists with correct extension. type - David Qi
		File file = new File(inputFileName);
		if(file.exists() && inputFileName.substring(inputFileName.lastIndexOf(".") + 1, inputFileName.length()).equals(".dot"));{
			System.out.println("Error! The file is either not found or is of wrong type!");
			System.exit(0);
		}


		//Read the number of processors from the input argument - David Qi
		String Processors = args[1];


		//Verify the validity of the argument for the number of processors and store it - David Qi
		try{
			NumberOfProcessors = Integer.parseInt(Processors);

		} catch (NumberFormatException e) {

			System.out.println("Invalid input for the number of processors!");
			System.exit(0);

		}


		int argumentIndex = 2;
		//Read and store the optional commands - David Qi
		while(args[argumentIndex] != null){
			optional_commands.add(args[argumentIndex]);
			argumentIndex++;
		}

		//Verify the validity of the optional commands if it is not empty - David Qi
		if(optional_commands.size() != 0){

			for(int i = 0; i < optional_commands.size(); i++){

				if(optional_commands.get(i).equals("-v")){	
					
					vFlag = 1; //Set the v optional command flag if found
					
				} else if(optional_commands.get(i).equals("-p")){
					
					//Checking that the optional command for cores is not repeated
					if(pFlag == 1){
						System.out.println("Warning, invalid optional command found!");
						System.exit(0);
					}
					
					pFlag = 1; //Set the p optional command flag
					
					//Stored the number of cores to be used
					try{
						cores = Integer.parseInt(optional_commands.get(i+1));

					} catch (NumberFormatException e) {

						System.out.println("Invalid input for the number of cores!");
						System.exit(0);

					}
					
					i++;
					
				} else if(optional_commands.get(i).equals("-o")){
					
					//Checking that the optional command for output name is not repeated
					if(oFlag == 1){
						System.out.println("Warning, invalid optional command found!");
						System.exit(0);
					}
					
					oFlag = 1; //Set the o optional command flag
					
					String filename = optional_commands.get(i+1);
					
					//Store the output file name
						OutputFileName = filename;
					
					i++;
					
				} else {
					
					System.out.println("Warning, invalid optional command found!");
					System.exit(0);
				}


			}

		}


		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			while ((text = reader.readLine()) != null && (text != "}") ) {


			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			e.printStackTrace();
		}




	}

}
