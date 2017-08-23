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

/**
 * Tests to make sure that the InputParser class functions as expected 
 */
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
		
		/**
		 * Test input with no option given
		 */
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
		
		/**
		 * Test input with zero processors
		 */
		@Test
		public void testInvalidProcessorsNoOption() {
			String[] input = new String[] {"input.dot", "0"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
				//fail();
			}
			
		}
		
		/**
		 * Test input with all options chosen
		 */
		@Test
		public void testAllOptionsValid() {
			String[] input = new String[] {"input.dot", "1", "-v", "-p", "2", "-o", "Yuan Wei.dot"};
			
			InputParser parser = null;
			try {
			parser = new InputParser(input);
			parser.parse();
			
			} catch (InputParserException e) {
				fail();
			}
			
			assertEquals(1, parser.getProcessors());
			assertTrue(parser.isVisualise());
			assertTrue(parser.isParallelise());
			assertEquals(2, parser.getCores());
			assertEquals("Yuan Wei.dot", parser.getOutputFileName());
		}
		
		/**
		 * Test input with all options chosen in another order
		 */
		@Test
		public void testAllOptionsValidOtherOrder() {
			String[] input = new String[] {"input.dot", "1", "-o", "Yuan Wei.dot", "-p", "1", "-v" };
			
			InputParser parser = null;
			try {
			parser = new InputParser(input);
			parser.parse();
			
			} catch (InputParserException e) {
				fail();
			}
			
			assertEquals(1, parser.getProcessors());
			assertTrue(parser.isVisualise());
			assertTrue(parser.isParallelise());
			assertEquals(1, parser.getCores());
			assertEquals("Yuan Wei.dot", parser.getOutputFileName());
		}
		
		/**
		 * Test input with file and processes added last
		 */
		@Test
		public void testFileAndProcessesLast() {
			String[] input = new String[] {"-o", "Yuan Wei.dot", "-p", "1", "-v", "input.dot", "1" };
			
			InputParser parser = null;
			try {
			parser = new InputParser(input);
			parser.parse();
			
			} catch (InputParserException e) {
			}
		}
		
		/**
		 * Test input with zero cores/threads
		 */
		@Test
		public void testInvalidCores() {
			String[] input = new String[] {"input.dot", "1", "-p", "0"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
			}
			
		}
		
		/**
		 * Test input with invalid output file name
		 */
		@Test
		public void testInvalidOutputName() {
			String[] input = new String[] {"input.dot", "3", "-o", "asdf"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
			}
			
		}
		
		/**
		 * Test input with invalid option (capitalised)
		 */
		@Test
		public void testInvalidCapitalOption() {
			String[] input = new String[] {"input.dot", "3", "-O", "asdf.dot"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
			}
			
		}
		
		/**
		 * Test input with invalid input file without an extension
		 */
		@Test
		public void testInvalidInputFile() {
			String[] input = new String[] {"input", "3"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
			}
		}
		
		/**
		 * Test input with no options and in wrong order
		 */
		@Test
		public void testWrongOrderNoOptions() {
			String[] input = new String[] {"3", "input.dot", };
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
			}
		}
		
		/**
		 * Test input with wrong order for all options
		 */
		@Test
		public void testWrongOrderAllOptions() {
			String[] input = new String[] {"input.dot", "1", "-v", "-p", "1", "Yuan Wei.dot", "-o"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
			}
		}
		
		/**
		 * Test input with an option but no argument
		 */
		@Test
		public void testOptionNoArgument() {
			String[] input = new String[] {"input.dot", "1", "-p"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
			}
		}
		
		/**
		 * Test input with multiple parameters after an option
		 */
		@Test
		public void testMultipleParametersAfterOption() {
			String[] input = new String[] {"input.dot", "1", "-p", "1", "2"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
			}
		}
		
		/**
		 * Test input with an option without the preceding dash
		 */
		@Test
		public void testNoDash() {
			String[] input = new String[] {"input.dot", "1", "p", "1"};
			InputParser parser;
			try {
			parser = new InputParser(input);
			parser.parse();
			} catch (InputParserException e) {
			}
		}
		
		
		@After
		public void deinitialise() {
			_file.delete();
		}
}
