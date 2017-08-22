package scheduler.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import io.InputParser;
import io.InputParserException;

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
			
			try {
			InputParser parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
				fail();
			}
		}
		
		@Test
		public void testNonValidNoOption() {
			String[] input = new String[] {"input.dot", "0"};
			
			try {
			InputParser parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
				//fail();
			}
			
		}
		
		@Test
		public void testVisualise() {
			String[] input = new String[] {"input.dot", "1", "-v"};
			
			try {
			InputParser parser = new InputParser(input);
			parser.parse();
			assertTrue(parser.isVisualise());
			
			} catch (InputParserException e) {
				fail();
			}
		}
		
		@Test
		public void testValidProcessors() {
			String[] input = new String[] {"input.dot", "1", "-v"};
		}
		
		@After
		public void deinitialise() {
			_file.delete();
		}
}
