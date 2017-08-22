package scheduler.io;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;
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
			InputParser parser = null;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
				fail();
			}
			assertEquals("input-output", parser.getOutputFileName());
			assertEquals(1, parser.getProcessors());
		}
		
		@Test
		public void testNonValidNoOption() {
			String[] input = new String[] {"input.dot", "0"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
				//fail();
			}
			
		}
		
		@Test
		public void testVisualise() {
			String[] input = new String[] {"input.dot", "1", "-v"};
			InputParser parser = null;
			try {
			parser = new InputParser(input);
			parser.parse();
			
			} catch (InputParserException e) {
				fail();
			}
			assertTrue(parser.isVisualise());
		}
		
		@Test
		public void testValidProcessors() {
			String[] input = new String[] {"input.dot", "1", "-v", "-p", "1"};
			InputParser parser = null;
			try {
			parser = new InputParser(input);
			parser.parse();
			
			} catch (InputParserException e) {
				fail();
			}
			assertEquals(1, parser.toString());
		}
		
		@Test
		public void testAllOptionsValid() {
			String[] input = new String[] {"input.dot", "1", "-v", "-p", "1", "-o", "Yuan Wei.dot"};
			
			InputParser parser = null;
			try {
			parser = new InputParser(input);
			parser.parse();
			
			} catch (InputParserException e) {
				fail();
			}
			
			assertEquals(1, parser.getProcessors());
			assertTrue(parser.isVisualise());
			assertEquals(1, parser.getCores());
			
			
			
		}
		@After
		public void deinitialise() {
			_file.delete();
		}
}
