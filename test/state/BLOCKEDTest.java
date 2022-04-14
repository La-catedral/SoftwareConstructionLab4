package state;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import exception.otherClientException.IllegalInputException;
import factory.FlightEntryFactory;
import flightSchedule.FlightEntry;

public class BLOCKEDTest {

	//Test strategy
		//toString()
		// 		invoke it,compare it with the relevant String
		//allocate()
		//		no input
		//		invoke it,catch the system output and observe it
		//block()
		//		no input
		//		invoke it,catch the system output and observe it
		//cancel()
		//		no input
		//		invoke it,observe the next state 
		//end()
		//		no input
		//		invoke it,catch the system output and observe it
		//run()
		//		no input
		//		invoke it,observe the next state 

		
	@Rule
	public final SystemOutRule log= new SystemOutRule().enableLog();

		@Test
		public void testToString() {
			BLOCKED  testOne = BLOCKED.instance;
			assertEquals("已阻塞", testOne.toString());
		}

		@Test
		public void testallocate() throws IllegalInputException {
			FlightEntry testEntry = new FlightEntryFactory().getEntry("test");
			BLOCKED testOne = BLOCKED.instance;
			testEntry.setState(testOne);
			assertFalse(testOne.allocate(testEntry));
			assertEquals("当前状态无法进行此操作！\n", log.getLog());
		}

		@Test
		public void testBlock()throws IllegalInputException  {
			log.clearLog();
			FlightEntry testEntry = new FlightEntryFactory().getEntry("test");
			BLOCKED testOne = BLOCKED.instance;
			testEntry.setState(testOne);
			assertFalse(testOne.block(testEntry));
			assertEquals("当前状态无法进行此操作！\n", log.getLog());
		}

		@Test
		public void testCancel() throws IllegalInputException {
			FlightEntry testEntry = new FlightEntryFactory().getEntry("test");
			BLOCKED testOne = BLOCKED.instance;
			testEntry.setState(testOne);
			assertTrue(testOne.cancel(testEntry));
			assertEquals(CANCELED.instance, testEntry.getState());
		}

		@Test
		public void testEnd()throws IllegalInputException  {
			log.clearLog();
			FlightEntry testEntry = new FlightEntryFactory().getEntry("test");
			BLOCKED testOne = BLOCKED.instance;
			testEntry.setState(testOne);
			assertFalse(testOne.end(testEntry));
			assertEquals("当前状态无法进行此操作！\n", log.getLog());
		}

		@Test
		public void testRun() throws IllegalInputException {
			log.clearLog();
			FlightEntry testEntry = new FlightEntryFactory().getEntry("test");
			BLOCKED testOne = BLOCKED.instance;
			testEntry.setState(testOne);
			assertTrue(testOne.run(testEntry));
			assertEquals(RUNNING.instance, testEntry.getState());
		}
}
