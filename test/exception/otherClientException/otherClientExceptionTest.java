package exception.otherClientException;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import classSchedule.CourseEntry;
import factory.CourseEntryFactory;
import factory.FlightEntryFactory;
import factory.TrainEntryFactory;
import flightSchedule.FlightEntry;
import location.ClassRoom;
import location.TrainStation;
import resource.Teacher;
import state.ALLOCATED;
import state.BLOCKED;
import state.CANCELED;
import state.ENDED;
import state.RUNNING;
import state.WAITING;
import timeslot.TimeSlot;
import trainSchedule.TrainEntry;

public class otherClientExceptionTest {//测试在otherClientException包中的所有异常

	//test strategy:
	
	//CannotCancelException:
	//调用时状态可分为：ALLOCATED、WAITING等可以合法cancel的状态，以及RUNNING等不可以cancel的状态
	//合法的操作返回true，非法的操作返回false，因此用if语句判断
	//若检测出非法抛出异常，并观察异常的信息
	
	//IllegalInputException：
	//调用方法很多,选取其中一个会抛出该异常的进行检测
	
	//LocationBeingUsedException
	//该异常主要用于阻止在app中删除正在被使用的位置
	//类比app，在当前所有的计划项列表中寻找占用该位置且未结束或未取消的
	//若有 抛出异常 观察异常信息
	
	//ResourceBeingUsedException
	//该异常主要用于阻止在app中删除正在被使用的资源
	//类比app，在当前所有的计划项列表中寻找占用该资源且未结束或未取消的
	//若有 抛出异常 观察异常信息
	
	//LocationExclusiveConflictException
	//异常检查在为某计划项变更位置的时候，如果变更后会导致与已有的其他计划项产生“位置独占冲突”
	//将计划项更改位置到一个正在被占用的位置 
	//观察信息
	
	//ResourceExclusiveConflictException
	//异常检查在为某计划项分配某资源的时候，如果分配后会导致与已有的其他计项产生“资源独占冲突”
	//为计划项分配一个正在被占用的资源 
	//观察信息
	
	@Test
	public void testCanNotCancelException() {
		try {
			
			FlightEntry testEntry = new FlightEntryFactory().getEntry("test");
			testEntry.setState(ALLOCATED.instance);
			assertTrue(testEntry.cancel());
			
			testEntry.setState(WAITING.instance);
			assertTrue(testEntry.cancel());
			
			testEntry.setState(BLOCKED.instance);
			assertTrue(testEntry.cancel());
			
			testEntry.setState(RUNNING.instance);
			if(!testEntry.cancel())
				throw new CannotCancelException(testEntry.getState().toString());
			
			assertTrue(false);//前面应出发异常而非到达该行
		}catch(Exception e )
		{
			assertEquals("已启动",e.getMessage());
		}
	}
	
	@Test
	public void testIllegalInputException()
	{
		try {
			Teacher secondTeacher = new Teacher("34567", "ang", "ma", "TA");
			assertTrue(!secondTeacher.getname().equals("ang"));//应检测到异常 不可到达改行 如果到了会报错  因为名字就是ang
		} catch (IllegalInputException e) {
			// TODO Auto-generated catch block
			assertEquals("gender must be \"male\" or \"female\" ",e.getMessage());
		}

	}
	
	@Test
	public void testLocationBeingUsedException()
	{
		CourseEntry newEntry;
		try {
			newEntry = new CourseEntryFactory().getEntry("testname");
			String nameOfLocation = "testname";
			double latitude = 0;
			double longitude = 1;
			ClassRoom testOne = new ClassRoom(nameOfLocation, latitude, longitude);
			newEntry.setLocation(testOne);
			List<ClassRoom> rooms = new ArrayList<ClassRoom>();
			rooms.add(testOne);
			List<CourseEntry> entries = new ArrayList<>();
			entries.add(newEntry);
			
			for (CourseEntry thisEntry : entries) {
				if (thisEntry.getLocation().equals(testOne)) {
					if (!(thisEntry.getState().equals(CANCELED.instance)
							|| thisEntry.getState().equals(ENDED.instance))) {
						System.out.println("有未结束的计划项正在占用该位置");
						throw new LocationBeingUsedException(thisEntry.getName());
					}
				}
			}
			rooms.remove(testOne);
			assertTrue(false);//不可到达该行
		} catch (Exception e) {
			assertEquals("testname",e.getMessage());
		}
		
	}
	
	
	@Test
	public void testResourceBeingUsedException()
	{
		
		try {
			String ID = "testID";
			String name = "testName";
			String gender = "male";
			String title = "professor";
			Teacher newTeacher = new Teacher(ID, name, gender, title);
			
			CourseEntry newEntry = new CourseEntryFactory().getEntry("testname");
			newEntry.setResource(newTeacher);
			
			List<Teacher> teachers = new ArrayList<Teacher>();
			teachers.add(newTeacher);
			List<CourseEntry> entries = new ArrayList<>();
			entries.add(newEntry);
			
			for (CourseEntry thisEntry : entries) {
				if(thisEntry.getResource()!= null) {
				if (thisEntry.getResource().equals(newTeacher)) {
					if (!(thisEntry.getState().equals(CANCELED.instance)
							|| thisEntry.getState().equals(ENDED.instance))) {
						System.out.println("有未结束的计划项正在占用该资源");
						throw new ResourceBeingUsedException(thisEntry.getName());
					}
				}}
			}
			teachers.remove(newTeacher);
			assertTrue(false);//不可到达该行
		} catch (Exception e) {
			assertEquals("testname",e.getMessage());
		}
		
		
	}
	
	@Test
	public void testLocationExclusiveConflictException()
	{
		try {
			CourseEntry newEntry = new CourseEntryFactory().getEntry("testname");
			Calendar testCalOne= Calendar.getInstance();
			testCalOne.set(2020, 4, 1,12,12);
			Calendar testCalTwo= Calendar.getInstance();
			testCalTwo.set(2021, 3, 2,13,13);
			TimeSlot testSlot= new TimeSlot(testCalOne, testCalTwo);
			newEntry.setSlot(testSlot);
			
			CourseEntry secondEntry = new CourseEntryFactory().getEntry("name");
			secondEntry.setSlot(testSlot);
			

			String nameOfLocation = "locname";
			double latitude = 0;
			double longitude = 1;
			ClassRoom testOne = new ClassRoom(nameOfLocation, latitude, longitude);
			newEntry.setLocation(testOne);
			

			String nameOfLocation2 = "locname2";
			double latitude2 = 1;
			double longitude2 = 1;
			ClassRoom testTwo = new ClassRoom(nameOfLocation2, latitude2, longitude2);
			secondEntry.setLocation(testTwo);
			
			List<CourseEntry> entries = new ArrayList<>();
			entries.add(newEntry);
			entries.add(secondEntry);
			
			for (CourseEntry thisEntry : entries) {
				if (thisEntry.getLocation().equals(testTwo)) {
					if (!(thisEntry.getState().equals(CANCELED.instance))
							&& thisEntry.getSlot().checkCoinOrNot(newEntry.getSlot())) {
						throw new LocationExclusiveConflictException(thisEntry.getName());
					}
				}
			}
			newEntry.changeSingleLocation(testTwo);
			assertTrue(false);//不可到达该行
		} catch (Exception e) {
			assertEquals("name",e.getMessage());
		}
		
	}
	
	@Test
	public void testResourceExclusiveConflictException()
	{
		CourseEntry newEntry;
		try {
			newEntry = new CourseEntryFactory().getEntry("testname");
			Calendar testCalOne= Calendar.getInstance();
			testCalOne.set(2020, 4, 1,12,12);
			Calendar testCalTwo= Calendar.getInstance();
			testCalTwo.set(2021, 3, 2,13,13);
			TimeSlot testSlot= new TimeSlot(testCalOne, testCalTwo);
			newEntry.setSlot(testSlot);
			
			CourseEntry secondEntry = new CourseEntryFactory().getEntry("name");
			secondEntry.setSlot(testSlot);
			
			List<CourseEntry> entries = new ArrayList<>();
			entries.add(newEntry);
			entries.add(secondEntry);
			
			String ID = "testID";
			String name = "testName";
			String gender = "male";
			String title = "professor";
			Teacher newTeacher = new Teacher(ID, name, gender, title);
			newEntry.setResource(newTeacher);
			
			for (CourseEntry thisEntry : entries) {
				if(thisEntry.getResource()!= null) {
				if (thisEntry.getResource().equals(newTeacher)) {
					if (!(thisEntry.getState().equals(CANCELED.instance))
							&& thisEntry.getSlot().checkCoinOrNot(secondEntry.getSlot())) {
						throw new ResourceExclusiveConflictException(thisEntry.getName());
					}
				}}
			}
			secondEntry.setResource(newTeacher);
			assertTrue(false);//不可到达该行
		} catch (Exception e) {
			assertEquals("testname",e.getMessage());
		}
		
	}
	
	@Test
	public void testTimeSlotNotEnoughException()
	{
	
		try {
			TrainEntry newEntry = new TrainEntryFactory().getEntry("testname");
		
		Calendar testCalOne= Calendar.getInstance();
		testCalOne.set(2020, 4, 1,12,12);
		Calendar testCalTwo= Calendar.getInstance();
		testCalTwo.set(2021, 3, 2,13,13);
		TimeSlot testSlot= new TimeSlot(testCalOne, testCalTwo);
		
		Calendar testCalThree= Calendar.getInstance();
		testCalThree.set(2022, 4, 1,12,12);
		Calendar testCalFour= Calendar.getInstance();
		testCalFour.set(2023, 3, 2,13,13);
		TimeSlot testSlottwo= new TimeSlot(testCalThree, testCalFour);

		List<TimeSlot> newList =new ArrayList<>();
		newList.add(testSlot);
		newList.add(testSlottwo);
		
		String nameOfLocation = "testname";
		double latitude = 0;
		double longitude = 1;
		TrainStation testOne = new TrainStation(nameOfLocation, latitude, longitude);
		
		String secondNameOfLocation = "secondname";
		double secondLatitude = 2;
		double secondLongitude = 3;
		TrainStation testTwo = new TrainStation(secondNameOfLocation, secondLatitude, secondLongitude);
		
		String nameOfLocation2 = "newn";
		double latitude2 = 2;
		double longitude2 = 3;
		TrainStation testThree = new TrainStation(nameOfLocation2, latitude2, longitude2);
		
		List<TrainStation> firstList = new ArrayList<>();
		firstList.add(testOne);
		firstList.add(testTwo);
		firstList.add(testThree);
		newEntry.setLocationList(firstList);
		
		
		newEntry.setSlot(newList);
		assertTrue(false);//不可到达该行
		} catch (Exception e) {
			assertEquals("设置的时间对数量应为: "+1,e.getMessage());
		}
	}

}
