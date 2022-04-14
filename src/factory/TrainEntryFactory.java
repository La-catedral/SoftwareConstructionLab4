package factory;

import exception.otherClientException.IllegalInputException;
import locationNumType.MultipleLocationEntry;
import locationNumType.MultipleLocationEntryImplement;
import resourceType.MutipleSortedResourceEntry;
import resourceType.MutipleSortedResourceEntryImplement;
import timeslot.MutiSlot;
import timeslot.MutiSlotimple;
import trainSchedule.TrainEntry;

public class TrainEntryFactory {

	public TrainEntry getEntry(String name) throws IllegalInputException{	//防御式编程 在创建计划项出现异常时直接将异常抛到调用工厂类方法处
		assert name != null;//防御式编程 检查必要的前置条件
		MutipleSortedResourceEntry mutiSorResource = new MutipleSortedResourceEntryImplement();
		MultipleLocationEntry multiLocation = new MultipleLocationEntryImplement();
		MutiSlot mutiSlo = new MutiSlotimple();
		return new TrainEntry(mutiSorResource, multiLocation, mutiSlo, name);
	}
}
