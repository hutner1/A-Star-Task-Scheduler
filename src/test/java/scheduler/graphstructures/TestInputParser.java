package scheduler.graphstructures;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import io.InputParser;

public class TestInputParser {
		File _file;
		@Before 
		public void initialise() 
		{
			_file = new File("input.dot"); 
			try {
				_file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Test
		public void testValidNoOption() {
			
			String[] input = new String[] {"input.dot", "1"};
			
			InputParser parser = new InputParser(input);
			parser.parse();
			assertEquals("input-output",parser.getOutputFileName());
			assertEquals(1, parser.getProcessors());
		}
		
		@After
		public void deinitialise() {
			_file.delete();
		}
}
