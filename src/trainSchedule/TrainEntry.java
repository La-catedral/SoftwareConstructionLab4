package trainSchedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import exception.otherClientException.IllegalInputException;
import exception.otherClientException.TimeSlotNotEnoughException;
import location.Location;
import location.TrainStation;
import locationNumType.MultipleLocationEntry;
import planningEntry.CommonPlanningEntry;
import resource.Coach;
import resource.Resource;
//import planningEntry.CommonPlanningEntry;
import resourceType.MutipleSortedResourceEntry;
import timeslot.MutiSlot;
import timeslot.TimeSlot;

//高铁车次管理 记得把新增功能设为接口
public class TrainEntry extends CommonPlanningEntry<Coach> implements TrainPlanningEntry {
//mutable
	
	private final MutipleSortedResourceEntry mutiSorResource ;
	private final MultipleLocationEntry multiLocation;
	private final MutiSlot mutiSlo;
	

	// Abstraction function:
    // 	AF(mutiSorresource,multiLocation,mutiSlo) = the manager of the resources of the object, the manager of the locations of the object,the manager of the timeSlots of the object,  
    // Representation invariant:
    //	the field must be non null,总的发车时间应该早于总的到站时间和所有停靠、半途发车时间，中途途经车站不得重读，车厢不能有重复
    // Safety from rep exposure:
    //  all fields are private 
	//	all the Observer return an immutable object
	
	//铁路局可以增加一个新车次、 （创建）
	//constructor
	public TrainEntry(MutipleSortedResourceEntry mutiSorResource, MultipleLocationEntry multiLocation,
			 MutiSlot mutiSlo,String name) throws IllegalInputException{
		super(name);
		this.mutiSorResource = mutiSorResource;
		this.multiLocation = multiLocation;
		this.mutiSlo = mutiSlo;
		checkRep();
	}
	
	private void checkRep()
	{
		assert mutiSorResource != null;
		assert multiLocation != null;
		assert mutiSlo !=  null;
		if(mutiSlo.getSlot()!=null)
		{
			assert mutiSlo.getSlot().getBeginTime().before(mutiSlo.getSlot().getEndTime()):"起止时间的先后顺序不符合规范";
			if(mutiSlo.getSlotList()!=null)
			{
				for(TimeSlot thisSlot:mutiSlo.getSlotList())
				{
					assert thisSlot.getBeginTime().after(mutiSlo.getSlot().getBeginTime()):"所有途经站的到达时间都应该比始发车时间晚！";
					assert thisSlot.getEndTime().after(mutiSlo.getSlot().getBeginTime()):"所有途经站的发车时间都应该比始发车时间晚！"; 
					assert thisSlot.getBeginTime().before(thisSlot.getEndTime()):"途经站的到达时间应该早于途经站的发车时间！";
					assert thisSlot.getBeginTime().before(mutiSlo.getSlot().getEndTime()):"所有途经站的到达时间都应该比终点站到达时间早！";
					assert thisSlot.getEndTime().before(mutiSlo.getSlot().getEndTime()):"所有途经站的发车时间都应该比终点站到达时间早！"; 
				}
			}
		}
		
		if(multiLocation.getLocationList()!=null)
		{
			for(int i = 0;i<multiLocation.getLocationList().size();i++)
			{
				Location thisLoc = multiLocation.getLocationList().get(i);
				for(int j = 0;j<multiLocation.getLocationList().size();j++)
				{
					if(i!= j)
					{
						assert !thisLoc.equals(multiLocation.getLocationList().get(j)):"不能有重复的途经站";
					}
				}
			}
		}
		
		if(mutiSorResource.getResource()!= null)
		{
			for(int i = 0;i<mutiSorResource.getResource().size();i++)
			{
				Coach thisRes = (Coach)mutiSorResource.getResource().get(i);
				for(int j = 0;j<mutiSorResource.getResource().size();j++)
				{
					if(i!= j)
					{
						assert !thisRes.equals(mutiSorResource.getResource().get(j)):"不能有重复的车厢";
					}
				}
			}
		}
	}
		//为新车次分配一组特定的车厢（有序）
	@Override
	public boolean setResource(List<? extends Resource> thisresource) throws IllegalInputException
	{
		if(thisresource.size() ==0)	//防御式编程 防止输入空的车厢组
			throw new IllegalInputException("空的车厢组！");
		assert thisresource.get(0).getClass().equals(Coach.class):"资源类型不匹配";//防御式编程 检查前置条件
		
		if(super.allocateResource() )
		{
			mutiSorResource.setResource(thisresource);
			System.out.println("分配车厢成功～");
			checkRep();//防御式编程 检查不变量 
			return true;
		}
		System.out.println("已经分配！！");
		checkRep();
		return false;
	}
	
	@Override
		public List<? extends Resource> getResource() {
			return mutiSorResource.getResource();
		}
	
		//分配始发终止时间对 （总出发时间，总预计到达时间）
	@Override
	public void setSlot(TimeSlot BegandEnd) throws IllegalInputException
	{
		assert BegandEnd != null:"时间对为null";
		if(!BegandEnd.getBeginTime().before(BegandEnd.getEndTime()))
			throw new IllegalInputException("时间对内时间顺序不合法");
		mutiSlo.setSlot(BegandEnd);
		checkRep();
	}
	
	//中途停靠时间对 (到站时间，离站时间) 设定后不可更改
	@Override
	public void setSlot(List<TimeSlot> slotList) throws TimeSlotNotEnoughException
	{
		if(multiLocation.getLocationList().size() != slotList.size()+2) //防御式编程 对客户端输入进行检查
			throw new TimeSlotNotEnoughException("设置的时间对数量应为: "+(multiLocation.getLocationList().size()-2));
		mutiSlo.setSlot(slotList);
		checkRep();
	}
	
	@Override
	public TimeSlot getSlot()
	{
		return mutiSlo.getSlot();
	}
	
	@Override
	public List<TimeSlot> getSlotList() 
	{
		return mutiSlo.getSlotList();
	}
	
	//分配高铁站的序列 设定后不可更改
	@Override
	public boolean setLocationList(List<? extends Location> locaitonList) throws IllegalInputException{
		assert locaitonList.get(0).getClass().equals(TrainStation.class):"位置类型不正确";//防御式编程  对前置条件进行处理
		
		if(locaitonList.size()<2)
			throw new IllegalInputException("总行程中车站的数量应大于2");
			multiLocation.setLocationList(locaitonList);
			checkRep();
			return true;
		
	}

	@Override
		public List<? extends Location> getLocationList() {
			return multiLocation.getLocationList();
		}
		 //始发 start 
	@Override
	public boolean run() {
		assert this.getState() != null:"状态为null";//防御式编程 检查任何情况下均应满足的前置条件

		if (super.run()) {
			checkRep();//防御式编程 保证不变量仍被满足
			return true;
		}
		else
			checkRep();//防御式编程 保证不变量仍被满足
			return false;
	}	
	
		  //在中间站停车
	@Override
	public boolean blockTheEntry(Calendar atTime)
	{
		assert this.getState() != null:"状态为null";//防御式编程 检查任何情况下均应满足的前置条件

		for(TimeSlot thisSlot:mutiSlo.getSlotList())
		{
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String thisStr = form.format(atTime.getTime());
			String legalString = form.format(thisSlot.getBeginTime().getTime());
			
			if(thisStr.equals(legalString))
			{	
				checkRep();	//防御式编程 保证不变量仍被满足
				return this.getState().block(this);
			}
		}
		System.out.println("非法的阻塞时间或状态");
		checkRep();
		return false;
	}
	
	
	//在中间站发车
	@Override
	public boolean restart(Calendar atThisTime)
	{
		assert this.getState() != null:"状态为null";//防御式编程 检查任何情况下均应满足的前置条件

		for(TimeSlot thisSlot:mutiSlo.getSlotList())
		{
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String thisStr = form.format(atThisTime.getTime());
			String legalString = form.format(thisSlot.getEndTime().getTime());
			if(legalString.equals(thisStr))
			{
				checkRep();//防御式编程 保证不变量仍被满足
				return run();
			}
		}
		System.out.println("非法的重新开启时间,应当和某一途径车站的出发时间相同");
		checkRep();//防御式编程 保证不变量仍被满足
		return false;
	}
		
	//抵达终点站。end 
	@Override
	public boolean end() {
		
		assert this.getState() != null:"状态为null";//防御式编程 检查任何情况下均应满足的前置条件
		if (super.end()) {
			checkRep();//防御式编程 保证不变量仍被满足
			return true;
		}
		else
			checkRep();//防御式编程 保证不变量仍被满足
			return false;
	}
	
		     //高铁在起点站未出发前或中间站停车时可以被取消。 
	@Override
	public boolean cancel()
	{
		assert this.getState() != null:"状态为null";//防御式编程 检查任何情况下均应满足的前置条件
		if(super.cancel())
		{
			System.out.println("该列车车次被取消");
			checkRep();
			return true;
		}
		else
			checkRep();
			return false;
	}

}
