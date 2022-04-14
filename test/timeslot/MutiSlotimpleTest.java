package timeslot;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

public class MutiSlotimpleTest {

	
	//test strategy
		//setSlot(List<TimeSlot> slot) together with getSlot():
		//		调用setSlot（）设置一个时间对序列，随后调用getSlot()
		//		观察返回的序列
	
	
	@Test
	public void testSetAndGet() {
		Calendar testCalOne= Calendar.getInstance();
		testCalOne.set(2020, 4, 1,12,12);
		Calendar testCalTwo= Calendar.getInstance();
		testCalTwo.set(2021, 3, 2,13,13);
		Calendar testCalThr= Calendar.getInstance();
		testCalThr.set(2021, 4, 1,12,12);
		Calendar testCalFou= Calendar.getInstance();
		testCalFou.set(2022, 5, 2,13,13);
		TimeSlot testSlot= new TimeSlot(testCalOne, testCalTwo);
		TimeSlot testSlot2 = new TimeSlot(testCalThr,testCalFou);
		List<TimeSlot> testList = new ArrayList<>();
		testList.add(testSlot);
		testList.add(testSlot2);
		
		MutiSlotimple newOne = new MutiSlotimple();
		newOne.setSlot(testList);
		assertTrue(newOne.getSlotList().contains(testSlot));
		assertTrue(newOne.getSlotList().contains(testSlot2));
	}

}
