package factory;

import classSchedule.CourseEntry;
import exception.otherClientException.IllegalInputException;
import locationChange.LocationCouldChange;
import locationChange.LocationCouldChangeImple;
import locationNumType.SingleLocationEntry;
import locationNumType.SingleLocationEntryImplement;
import resourceType.SingleResourceEntry;
import resourceType.SingleSourceEntryImplement;
import timeslot.SingleSlot;
import timeslot.SingleSlotimple;

public class CourseEntryFactory {


	public CourseEntry getEntry(String name) throws IllegalInputException{	//防御式编程 在创建计划项出现异常时直接将异常抛到调用工厂类方法处
		
		assert name != null;//防御式编程 检查必要的前置条件
		LocationCouldChange changeObject  = new LocationCouldChangeImple();
		SingleLocationEntry singleLoc = new SingleLocationEntryImplement();
		SingleResourceEntry singleRes = new SingleSourceEntryImplement();
		SingleSlot singleSlo = new SingleSlotimple();
		return new CourseEntry(changeObject, singleLoc, singleRes, singleSlo, name);
	}
	
}
