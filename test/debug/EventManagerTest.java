package debug;

import static org.junit.Assert.*;

import org.junit.Test;

import exception.otherClientException.IllegalInputException;

public class EventManagerTest {

	//test strategy
	//partition input as:
	//day :<1 , 1<day<=365 , day >365
	//start : <0 , start>=24 , 0<= start <24
	//end: <0 , end>=24 , 0<= end <24
	//start > end, start = end ,start < end
	//以及正确情况，观察返回值
	@Test
	public void test() {

	
		try {
			EventManager.book(0, 1, 2);	//day
		} catch (IllegalInputException e) {
			assertEquals("day 参数不合法: "+0,e.getMessage());
		}
		
		try {
			EventManager.book(366, 1, 2); //day
		} catch (IllegalInputException e) {
			assertEquals("day 参数不合法: "+366,e.getMessage());
		}
		
		try {
			EventManager.book(1, -1, 2);	//day
		} catch (IllegalInputException e) {
			assertEquals("start 参数不合法："+-1,e.getMessage());
		}
		
		try {
			EventManager.book(1, 1, 24);	//day
		} catch (IllegalInputException e) {
			assertEquals("end 参数不合法："+24,e.getMessage());
		}
		
		try {
			EventManager.book(1, 3, 2);	//day
		} catch (IllegalInputException e) {
			assertEquals("start 应不小于 end",e.getMessage());
		}
		
		try {
		assertEquals(1,EventManager.book(1, 2, 3));
		assertEquals(1,EventManager.book(1, 3, 4));
		assertEquals(2,EventManager.book(1, 2, 4));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		if(start <0 || start >=24)
//			throw new IllegalInputException("start 参数不合法："+start);
//		if(end <0 || end >=24)
//			throw new IllegalInputException("end 参数不合法："+start);
//		if(start > end)
//			throw new IllegalInputException("start 应不小于 end");
//		if(day < 1 || day > 365)
//			throw new IllegalInputException("day 参数不合法: "+day);
	
	
	}

}
