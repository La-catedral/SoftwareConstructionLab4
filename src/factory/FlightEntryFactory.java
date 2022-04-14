package factory;

import exception.otherClientException.IllegalInputException;
import flightSchedule.FlightEntry;

import locationNumType.DoubleLocationEntry;
import locationNumType.DoubleLocationEntryImplement;
import resourceType.SingleResourceEntry;
import resourceType.SingleSourceEntryImplement;
import timeslot.SingleSlot;
import timeslot.SingleSlotimple;

public class FlightEntryFactory{


	public FlightEntry getEntry(String name) throws IllegalInputException{	//防御式编程 在创建计划项出现异常时直接将异常抛到调用工厂类方法处
		
		assert name != null;//防御式编程 检查必要的前置条件
		SingleResourceEntry singleRes = new SingleSourceEntryImplement();
		DoubleLocationEntry doubleLoc = new DoubleLocationEntryImplement();
		SingleSlot singleSlot = new SingleSlotimple();
		return  new FlightEntry(singleRes, doubleLoc, singleSlot, name);
	}
	
}
