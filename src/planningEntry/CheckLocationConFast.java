package planningEntry;

import java.util.List;


import classSchedule.CourseEntry;
import flightSchedule.FlightEntry;
import timeslot.TimeSlot;
import trainSchedule.TrainEntry;

public class CheckLocationConFast implements CheckLocationInterface{

	//更加快速 减少内存调用的方法实现
	@Override
	public boolean checkLocationConflict(List<? extends PlanningEntry<?>>   entries)
	   {
			assert entries.size()>0:"被检查的计划项列表不能为空";//防御式编程 其他方法调用该方法时必须满足该前置条件 
		   Class<?> cla = entries.get(0).getClass();
		   if(cla.equals(TrainEntry.class) || cla.equals(FlightEntry.class))
		   {
			   return false;
		   }
		   else if(cla.equals(CourseEntry.class))
		   {
			   for(int i =0;i<entries.size();i++)
				   {
					   TimeSlot thisTime = ((CourseEntry)entries.get(i)).getSlot();
					   assert thisTime != null;			//防御式编程  检查必要的前置条件
					   assert ((CourseEntry)entries.get(i)).getLocation() != null;
					   for(int j =i+1;j<entries.size();j++)
					   {
						   CourseEntry compareEntry = (CourseEntry)entries.get(j);
						   assert compareEntry .getSlot()!=null; //防御式编程  检查必要的前置条件
						   assert compareEntry.getLocation() != null;
						   if(compareEntry.getSlot().checkCoinOrNot(thisTime))
						   {
							   if(compareEntry.getLocation().equals( ((CourseEntry)entries.get(i)).getLocation()))
									   return true;
						   }
					   }
				   }
		   }
		   return false;
	   }
}
