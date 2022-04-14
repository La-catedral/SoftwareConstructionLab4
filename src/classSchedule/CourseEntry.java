package classSchedule;

import exception.otherClientException.IllegalInputException;
import location.ClassRoom;
import location.Location;
import locationChange.LocationCouldChange;
import locationChange.LocationCouldChangeImple;
import locationNumType.SingleLocationEntry;
import planningEntry.CommonPlanningEntry;
import resource.Resource;
import resource.Teacher;
import resourceType.SingleResourceEntry;
import timeslot.SingleSlot;
import timeslot.TimeSlot;

public class CourseEntry extends CommonPlanningEntry<Teacher> implements CoursePlanningEntry{
	//mutable 

	private final LocationCouldChange changeObject;
	private final SingleLocationEntry	singleLoc;
	private final SingleResourceEntry singleRes;
	private final SingleSlot  singleSlo;
	
	
//	 Abstraction function:
//    	AF(changeObject,singleLoc,singleRes，singleSlo) = the object to change the location ,the manager of the locations of the object,the manager of the resources of the object, the manager of the timeSlots of the object,  
//    Representation invariant:
//   	the field must be non null ,上课时间应该早于下课时间
//    Safety from rep exposure:
//     all fields are private and final 
//		all the Observer return an immutable object
	
	//constructor，创建一个计划项实例；例如：周三上午10:00-11:45 在正心楼 23 教室上“软件构造”课程。
	
public CourseEntry(LocationCouldChange changeObject, SingleLocationEntry singleLoc,
		SingleResourceEntry singleRes, SingleSlot singleSlo,String name) throws IllegalInputException
{
	super(name);
	this.changeObject = changeObject;
	this.singleLoc = singleLoc;
	this.singleRes = singleRes;
	this.singleSlo = singleSlo;
	((LocationCouldChangeImple)changeObject).setsingleLoc(singleLoc);
	checkRep();
}
		
	//checkRep
	private void checkRep() //防御式编程
	{
		assert changeObject != null;
		assert singleRes != null;
		assert singleLoc != null;
		assert singleSlo != null;
		if(singleSlo.getSlot()!=null)
			assert singleSlo.getSlot().getBeginTime().before(singleSlo.getSlot().getEndTime()):"时间对中时间顺序不符合规范";
		if(singleRes.getResource()!=null)
			assert singleRes.getResource().getClass().equals(Teacher.class):"资源类型应为Teacher";
		if(singleLoc.getLocation()!=null)
			assert singleLoc.getLocation().getClass().equals(ClassRoom.class);
	}

		


	//需要特定的资源（教师）
	@Override
	public boolean setResource(Resource thisresource) 
	{
		assert thisresource.getClass().equals(Teacher.class) :"错误的参数类型"+thisresource.getClass().getName();//断言检查前置条件
		
		if(super.allocateResource() )
		{
			singleRes.setResource(thisresource);
			System.out.println("已分配教师");
			checkRep();//防御式编程 检查不变量
			return true;
		}
		System.out.println("已经分配！！");
		checkRep();//防御式编程 检查不变量
		return false;
	}
	
	@Override
	public Resource getResource() {
		return singleRes.getResource();
	}
	
	
	//上课需要特定的物理位置（教室）
	@Override 
	public boolean setLocation(Location thisLocation)
	{
		if(thisLocation.getClass().equals(ClassRoom.class))
		{
			singleLoc.setLocation(thisLocation);
			checkRep();
			return true;
		}
		System.out.println("位置类型不匹配！");
		checkRep();
		return false;
	}
	
	@Override
	public Location getLocation() {
		return singleLoc.getLocation();
	}
	
	//确定时间。
	@Override
	public void setSlot(TimeSlot timeSlot) throws IllegalInputException{
		assert timeSlot !=null:"时间对为null";//防御式编程 检查前置条件
		if(!timeSlot.getBeginTime().before(timeSlot.getEndTime()))
			throw new IllegalInputException("时间对内的时间顺序不符合规范");
		singleSlo.setSlot(timeSlot);
		checkRep();
	}
	//获取时间
	@Override
	public TimeSlot getSlot() {
		return singleSlo.getSlot();
	}
	
	//教师可以上课、
	@Override
	public boolean run() {
		if(super.run())
		{
			System.out.println("上课");
			checkRep();
			return true;
		}
		else
			checkRep();
			return false;
	}
	
	
	//下课
	@Override
	public boolean end() {
		if(super.end())
		{
			System.out.println("下课了");
			checkRep();
			return true;
		}
		else
			checkRep();
			return false;
	}
	
	//更换教室
	@Override
	public void changeSingleLocation(Location thisLocation) throws IllegalInputException{
	if(thisLocation.equals(singleLoc.getLocation())) //防御式编程 检查非法输入
		throw new IllegalInputException("变更的位置不能委员位置");
	changeObject.changeSingleLocation(thisLocation);
	checkRep();
	}
	
	//但在未启动之前，教师可以因为临时有事而取消该次课程
	@Override
	public boolean cancel() {
		if(super.cancel())
		{
			System.out.println("课程已取消");
			checkRep();
			return true;
		}
		else
			checkRep();
			return false;
	}
//	public static void main(String[] args)
//	   {
//		CourseEntry newEntry = new CourseEntryFactory().getEntry("testname");
//		ClassRoom thisone;
//		try {
//			thisone = new ClassRoom("123",1,1);
//			newEntry.setLocation(thisone);
//		} catch (IllegalInputException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	   }
	
}
