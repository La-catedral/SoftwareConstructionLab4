package planningEntry;

import java.util.List;

import classSchedule.CourseEntry;
import flightSchedule.FlightEntry;
import location.Location;
import timeslot.TimeSlot;
import trainSchedule.TrainEntry;

public class CheckLocationConForceImp implements CheckLocationInterface{

	

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
					   Location thisLocation = ((CourseEntry)entries.get(i)).getLocation();
					   TimeSlot thisTime = ((CourseEntry)entries.get(i)).getSlot();
					   assert thisLocation != null;//防御式编程 判断是否存在计划项没分配位置
					   assert thisTime != null;
					   for(int j =0;j<entries.size();j++)
					   {
						   if(j != i)
						   {
							   CourseEntry compareEntry = (CourseEntry)entries.get(j);
							   assert compareEntry.getLocation()!=null;
							   assert compareEntry.getSlot()!=null;
							  if(compareEntry.getLocation().equals(thisLocation) && compareEntry.getSlot().checkCoinOrNot(thisTime))
								  return true;
						   }
					   }
				   }
		   }
		   return false;
	   }
}
