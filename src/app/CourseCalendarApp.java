package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;




import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import board.ClassBoard;
import classSchedule.CourseEntry;
import exception.otherClientException.CannotCancelException;
import exception.otherClientException.IllegalInputException;
import exception.otherClientException.LocationBeingUsedException;
import exception.otherClientException.LocationExclusiveConflictException;
import exception.otherClientException.ResourceBeingUsedException;
import exception.otherClientException.ResourceExclusiveConflictException;
import factory.CourseEntryFactory;
import location.ClassRoom;
import location.Location;
import planningEntry.CheckLocationConForceImp;
import planningEntry.PlanningEntry;
import planningEntry.PlanningEntryAPIs;
import resource.Teacher;
import state.CANCELED;
import state.ENDED;
import state.WAITING;
import timeslot.TimeSlot;

public class CourseCalendarApp {

	private static Logger logger = Logger.getLogger(Logger.class); 
	/**
	 * 在当前所有资源中查找特定的ID的资源
	 * @param ID 要查找的资源的ID
	 * @param resourceList 当前所有资源构成的列表
	 * @return 具有该特定的ID的资源
	 */
	public static Teacher findResource(String ID,Set<Teacher>resourceList)
	{
		for(Teacher thisTeacher:resourceList)
		{
			if(thisTeacher.getID().equals(ID))
			return thisTeacher;
		}
		
		return null;
	}
	
	/**
	 * 在当前所有位置中查找特定的名称的位置
	 * @param name 位置的名称
	 * @param locationList 所有的现有位置
	 * @return 所要找的位置
	 */
	public static ClassRoom findClassRoom(String name,Set<ClassRoom>locationList)
	{
		for(ClassRoom thisRoom : locationList)
		{
			if(thisRoom.getName().equals(name))
			return thisRoom;
		}
		return null;
	}
	
	/**
	 * 在当前所有的已有计划项中查找名称为name的计划项
	 * @param name 要找的计划项的名称
	 * @param entryList 所有已有计划项构成的list
	 * @return 要找的计划项
	 */
	public static CourseEntry findEntry(String name,List<CourseEntry>entryList)
	{
		for(PlanningEntry<?> thisEntry:entryList)
		{
			if(thisEntry.getName().equals(name))
				return (CourseEntry)thisEntry;
		}
		return null;
	}
	public static void printHelp()
	{
		System.out.println("********help********");
		System.out.println("输入下列英文字母或单词：");
		System.out.println("a :加入一位教师");
		System.out.println("b :减少一位教师");
		System.out.println("c :增加一个教室");
		System.out.println("d :删除一个教室");
		System.out.println("e :增加一个课程计划项");
		System.out.println("f :取消一个计划项");
		System.out.println("g :为某一课程分配教师");
		System.out.println("h :开始上课");
		System.out.println("i :变更某个已存在的计划项的位置:");
		System.out.println("j :下课");
		System.out.println("k :查看某课程状态");
		System.out.println("l :检测资源独占冲突");
		System.out.println("m :检测位置独占冲突");
		System.out.println("n :对于一位老师，查看与该老师有关的所有计划项");
		System.out.println("o :对于一位老师，查看某个计划项的前序计划项");
		System.out.println("p :显示一个教室的信息版");
		System.out.println("checklog :打印输出有关错误异常的日志");
		System.out.println("help :提供帮助信息");
		System.out.println("end :结束");
		System.out.println();
	}
	
	public static void main(String[] args) {
		

			Set<ClassRoom> locationList = new HashSet<>();//现有位置列表
			Set<Teacher> resourceList = new HashSet<>();//现有资源列表
			List<CourseEntry> entryList = new ArrayList<>();//创立的所有计划项 无论状态 都要加入这里
			Map<CourseEntry,Calendar> cancelTime = new HashMap<>();
			printHelp();
			Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("请输入选项：");
			String choice = in.nextLine();
			switch (choice) {
			case "a": {// 加入一位教师");
				System.out.println(
						"请输入要增加教师的身份证号、姓名、性别（male/female）、职称，参数间空格分开，如\"110100198001010001 ZhangSan male professor\"：");
				try {
					String[] param = in.nextLine().split(" ");
					Teacher newTeacher = new Teacher(param[0], param[1], param[2], param[3]);
					resourceList.add(newTeacher);
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+"a");
				} catch (ArrayIndexOutOfBoundsException e) { //防御式编程 处理不合法用户输入
					System.out.println("不符合要求的格式！");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				} catch (IllegalInputException e) {
					System.out.println("失败"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				}
				break;
			}
			case "b":// 减少一位教师
			{
				System.out.println("请输入要离开的教师的身份证号：");
				String ID = in.nextLine();
				Teacher TeacherToDel = findResource(ID, resourceList);
				try{if (TeacherToDel == null) {
					System.out.println(" 不存在此教师");
				} else {
					for (CourseEntry thisEntry : entryList) {
						if(thisEntry.getResource()!= null) {
						if (thisEntry.getResource().equals(TeacherToDel)) {
							if (!(thisEntry.getState().equals(CANCELED.instance)
									|| thisEntry.getState().equals(ENDED.instance))) {
								System.out.println("有未结束的计划项正在占用该资源");
								throw new ResourceBeingUsedException(ID);
							}
						}}
					}
					resourceList.remove(TeacherToDel);
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+"b");
				}}catch(ResourceBeingUsedException e )  //防御式编程 处理不合法用户输入
				{
					System.out.println("失败"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);


				}
				break;
			}
			case "c":// 增加一个教室
			{
				System.out.println("请输入要增加教室的名字、经度、纬度，参数间空格分开，如\"正心11 -30 155\"：");
				try {
					String[] paramOfPort = in.nextLine().split(" ");
					ClassRoom newLocation = new ClassRoom(paramOfPort[0], Double.valueOf(paramOfPort[1]),
							Double.valueOf(paramOfPort[2]));
					locationList.add(newLocation);
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+"c");
				} catch (ArrayIndexOutOfBoundsException e) {  //防御式编程 处理不合法用户输入
					System.out.println("不符合要求的格式！");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);

				} catch (NumberFormatException e) {
					System.out.println("失败"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);

				} catch (IllegalInputException e) {
					System.out.println("失败"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);

				}
				break;
			}
			case "d":// 删除一个教室
			{
				System.out.println("请输入要删除教室的名字");
				String nameOfPort = in.nextLine();
				ClassRoom portToDel = findClassRoom(nameOfPort, locationList);
				try{if (portToDel == null) {
					System.out.println(" 不存在此教室");
				} else {
					for (CourseEntry thisEntry : entryList) {
						if (thisEntry.getLocation().equals(portToDel)) {
							if (!(thisEntry.getState().equals(CANCELED.instance)
									|| thisEntry.getState().equals(ENDED.instance))) {
								System.out.println("有未结束的计划项正在占用该位置");
								throw new LocationBeingUsedException(nameOfPort);
							}
						}
					}
					locationList.remove(portToDel);
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+"d");
				}}
				catch (LocationBeingUsedException e) {  //防御式编程 处理不合法用户输入
					System.out.println("失败"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);

				}
				break;
			}
			case "e":// 增加一个课程计划项
			{
				System.out.println("请输入课程计划项的名称、上课教室名，参数间空格分开，如\"软件构造 正心21\"：");
				
					String[] firstParam = in.nextLine().split(" ");
					try {CourseEntry newEntry = new CourseEntryFactory().getEntry(firstParam[0]);
					Location fromLocation = findClassRoom(firstParam[1], locationList);
					if (fromLocation != null) {
						System.out.println("请输入课程开始时间，以yyyy MM dd HH mm的格式，例如\"2020 1 1 13 27\"代表2020年1月1日13点27分：");
						String[] secondParam = in.nextLine().split(" ");
						Calendar from = Calendar.getInstance();
						from.set(Integer.valueOf(secondParam[0]), Integer.valueOf(secondParam[1]) - 1,
								Integer.valueOf(secondParam[2]), Integer.valueOf(secondParam[3]),
								Integer.valueOf(secondParam[4]));
						System.out.println("请输入课程结束时间，以yyyy MM dd HH mm的格式，例如\"2020 1 1 13 27\"代表2020年1月1日13点27分：");
						String[] thirdParam = in.nextLine().split(" ");
						Calendar to = Calendar.getInstance();
						to.set(Integer.valueOf(thirdParam[0]), Integer.valueOf(thirdParam[1]) - 1,
								Integer.valueOf(thirdParam[2]), Integer.valueOf(thirdParam[3]),
								Integer.valueOf(thirdParam[4]));
						newEntry.setSlot(new TimeSlot(from, to));
						newEntry.setLocation(fromLocation);
						entryList.add(newEntry);
						logger.info("合法操作"+" 被捕获"+" 计划项ID:"+firstParam[0]+" 操作："+"e");
					} else {
						System.out.println("不存在的教室");
					}
				} catch (ArrayIndexOutOfBoundsException e) {  //防御式编程 处理不合法用户输入
					System.out.println("不符合要求的格式！");
					System.out.println(e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+firstParam[0]+" 操作："+choice,e);

				} catch (IllegalInputException e) {
					System.out.println("失败"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+firstParam[0]+" 操作："+choice,e);

				}
				break;
			}
			case "f":// 取消一个计划项
			{
				System.out.println("请输入课程计划项的名称:");
				String paramString = in.nextLine();
				CourseEntry entryToDel = findEntry(paramString, entryList);
				System.out.println("请输入当前时间，以yyyy MM dd HH mm的格式，例如\"2020 1 1 13 27\"代表2020年1月1日13点27分：");
				String[] timeParam = in.nextLine().split(" ");
				Calendar from = Calendar.getInstance();
				from.set(Integer.valueOf(timeParam[0]), Integer.valueOf(timeParam[1]) - 1,
						Integer.valueOf(timeParam[2]), Integer.valueOf(timeParam[3]),
						Integer.valueOf(timeParam[4]));
				try {
					if (entryToDel != null) {
						if (!entryToDel.cancel()) {
							throw new CannotCancelException(entryToDel.getState().toString());
						}else {
							cancelTime.put(entryToDel, from);    //记录取消时间
							logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+"f");
						}
					} else {
						System.out.println("没有该计划项！");
					}
				} catch (CannotCancelException e) { // 防御式编程 处理不合法用户输入
					System.out.println("失败" + e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice,e);

				}
				break;
			}
			case "g":// 为某一课程分配教师
			{
				System.out.println("请输入要分配资源的课程计划项的名称、教师的ID，参数间空格分开，如\"软件构造 100101198001010001\"：");
				String[] firstParam = in.nextLine().split(" ");
				CourseEntry entryToAllo = findEntry(firstParam[0], entryList);
				Teacher planeToAllo = findResource(firstParam[1], resourceList);
				try{if (entryToAllo != null && planeToAllo != null) {
					for (CourseEntry thisEntry : entryList) {
						if (thisEntry.getResource()!=null) {
						if (thisEntry.getResource().equals(planeToAllo)) {
							if (!(thisEntry.getState().equals(CANCELED.instance))
									&& thisEntry.getSlot().checkCoinOrNot(entryToAllo.getSlot())) {
								throw new ResourceExclusiveConflictException(thisEntry.getName());
							}
						}}
					}
					entryToAllo.setResource(planeToAllo);
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToAllo.getName()+" 操作："+choice);

				} else {
					System.out.println("请检查您的输入！");
				}}
				catch(ResourceExclusiveConflictException e) //防御式编程 处理不合法用户输入
				{ 
					System.out.println("失败"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+entryToAllo.getName()+" 操作："+choice,e);

				}
				break;
			}
			case "h":// 开始上课
			{
				System.out.println("请输入要上课的课程名称:");
				String paramString = in.nextLine();
				CourseEntry entryToDel = findEntry(paramString, entryList);
				if (entryToDel != null) {
					entryToDel.run();
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice);

				} else {
					System.out.println("没有该计划项！");
				}
				break;
			}
			case "i":// 变更某个已存在的计划项的位置
			{
				System.out.println("请输入要上课的课程名称、要变更到哪个教室，参数间空格分开，如\"软件构造 正心31\":");
				String[] paramString = in.nextLine().split(" ");
				CourseEntry entryToDel = findEntry(paramString[0], entryList);
				ClassRoom changeToRoom = findClassRoom(paramString[1], locationList);
				try{if (entryToDel != null && changeToRoom != null) {
					for (CourseEntry thisEntry : entryList) {
						if (thisEntry.getLocation().equals(changeToRoom)) {
							if (!(thisEntry.getState().equals(CANCELED.instance))
									&& thisEntry.getSlot().checkCoinOrNot(entryToDel.getSlot())) {
								throw new LocationExclusiveConflictException(changeToRoom.toString());
							}
						}
					}
					entryToDel.changeSingleLocation(changeToRoom);
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice);

				} else {
					System.out.println("没有该计划项或教室！");
				}}catch(LocationExclusiveConflictException e) //防御式编程 处理不合法用户输入
				{
					System.out.println(e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice,e);

				} catch (IllegalInputException e) {
					System.out.println(e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice,e);

				}
				break;
			}
			case "j":// 下课
			{
				System.out.println("请输入要下课的课程名称:");
				String paramString = in.nextLine();
				CourseEntry entryToDel = findEntry(paramString, entryList);
				if (entryToDel != null) {
					entryToDel.end();
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice);
				} else {
					System.out.println("没有该计划项！");
				}
				break;
			}
			case "k":// 查看某课程状态
			{
				System.out.println("请输入查询状态的课程计划项的名称:");
				String paramString = in.nextLine();
				CourseEntry entryToDel = findEntry(paramString, entryList);
				if (entryToDel != null) {
					System.out.println(entryToDel.getState());
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice);
				} else {
					System.out.println("没有该计划项！");
				}
				break;
			}
			case "l":// 检测资源独占冲突
			{
				System.out.println("请稍等");
				PlanningEntryAPIs<?> thisOne = new PlanningEntryAPIs<>();
				try {
					System.out.println("是否存在资源独占冲突：" + thisOne.checkResourceExclusiveConflict(entryList));
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

				} catch (IllegalInputException e) {  //防御式编程 处理不合法用户输入
					System.out.println("失败"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);

				}
				break;
			}
			case "m":// 检测位置独占冲突
			{
				
				try {
					System.out.println("请稍等");
					PlanningEntryAPIs<?> thisOne = new PlanningEntryAPIs<>();
					System.out.println(
							"是否存在位置独占冲突：" + thisOne.checkLocationConflict(new CheckLocationConForceImp(), entryList));
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);
				} catch (IllegalInputException e) {  //防御式编程 处理不合法用户输入
					System.out.println("失败"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);

				}
				break;
			}
			case "n":// 对于一位老师，查看与该老师有关的所有计划项
			{
				System.out.println("请输入需要查询的教师的ID:");
				String paramString = in.nextLine();
				Teacher teacherToSear = findResource(paramString, resourceList);
				if (teacherToSear != null) {
					for (CourseEntry thisEntry : entryList) {
						if(!thisEntry.getState().equals(WAITING.instance))
						{if (thisEntry.getResource().equals(teacherToSear))
							System.out.println(thisEntry.getName());
						}
					}
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);
				} else {
					System.out.println("不存在该教师");  
				}
				break;
			}
			case "o":// 对于一位老师，查看某个计划项的前序计划项
			{
				System.out.println("请输入课程计划项名称、以及有关教师的ID，参数间空格分开，如\"软件构造 100101198001010001\"：");
				PlanningEntryAPIs<Teacher> thisOne = new PlanningEntryAPIs<>();
				String[] firstParam = in.nextLine().split(" ");
				try{PlanningEntry<Teacher> checkedOne = findEntry(firstParam[0], entryList);
				Teacher thisResource = findResource(firstParam[1], resourceList);
				if (checkedOne != null && thisResource != null) {
					PlanningEntry<Teacher> formerOne = thisOne.findPreEntryPerResource(thisResource, checkedOne,
							entryList);
					if (formerOne != null)
						System.out.println(formerOne.getName());
					else
						System.out.println("没有更早的计划项");
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+firstParam[0]+" 操作："+choice);
				}
				}catch(IllegalInputException e)  //防御式编程 处理不合法用户输入
				{
					System.out.println(e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+firstParam[0]+" 操作："+choice,e);

				}
				break;
			}
			case "p":// 显示一个教室的信息版
			{
				System.out.println("请输入需要查询的教室的名字:");
				String paramString = in.nextLine();
				ClassRoom portToBoard = findClassRoom(paramString, locationList);
				System.out.println("请输入当前时间，以yyyy MM dd HH mm的格式，例如\"2020 1 1 13 27\"代表2020年1月1日13点27分：");
				String[] secondParam = in.nextLine().split(" ");
				Calendar timeNow = Calendar.getInstance();
				timeNow.set(Integer.valueOf(secondParam[0]), Integer.valueOf(secondParam[1]) - 1,
						Integer.valueOf(secondParam[2]), Integer.valueOf(secondParam[3]),
						Integer.valueOf(secondParam[4]));
				if (portToBoard != null) {
					ClassBoard newBoard = new ClassBoard(timeNow, portToBoard);
					newBoard.setClasses(entryList);
					newBoard.iterator();
					newBoard.visualize();
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

				} else {
					System.out.println("不存在该教室");
				}
				break;
			}
			case "checklog":
			{
				try
				{boolean checkTime = false;
				boolean checkID = false;
				boolean checkOpt = false;
				Calendar timefrom = Calendar.getInstance();
				Calendar timeto = Calendar.getInstance();
				String wantID= " ";
				String wantOpt = " ";
				
				System.out.println("请问您要查看的日志是否要求在时间段内，若是请输入字母y，否则随意输入：");
				if(in.nextLine().equals("y"))
				{
					checkTime = true;
				System.out.println("请输入时间段的起始时间，以yyyy MM dd HH mm ss的格式，例如\"2020 1 1 13 27 12\"代表2020年1月1日13点27分12秒：");
				String[] firstParam = in.nextLine().split(" ");
				timefrom.set(Integer.valueOf(firstParam[0]), Integer.valueOf(firstParam[1]) - 1,
						Integer.valueOf(firstParam[2]), Integer.valueOf(firstParam[3]),
						Integer.valueOf(firstParam[4]),Integer.valueOf(firstParam[5]));
				System.out.println("请输入时间段的结束时间，以yyyy MM dd HH mm ss的格式，例如\"2020 1 1 13 27 12\"代表2020年1月1日13点27分12秒：");
				String[] secondParam = in.nextLine().split(" ");
				timeto.set(Integer.valueOf(secondParam[0]), Integer.valueOf(secondParam[1]) - 1,
						Integer.valueOf(secondParam[2]), Integer.valueOf(secondParam[3]),
						Integer.valueOf(secondParam[4]),Integer.valueOf(secondParam[5]));
				}
				
				System.out.println("请问您要查看的日志是否要求为特定计划项，若是请输入字母y，否则随意输入：");
				if(in.nextLine().equals("y"))
				{
					 checkID = true;
					System.out.println("请输入该计划项的名称，例如\"软件构造\"：");
					wantID = in.nextLine();
				}
				
				System.out.println("请问您要查看的日志是否要求为，某一特定操作，若是请输入字母y，否则随意输入：");
				if(in.nextLine().equals("y"))
				{
					 checkOpt = true;
					System.out.println("请输入操作对应的选项，例如\"a\"对应加入资源操作，具体请参照上面help的提示：");
					wantOpt = in.nextLine();
				}
				Scanner fileScan = new Scanner(new File("src/app/debug.txt"));
				StringBuilder allString = new StringBuilder();
				while(fileScan.hasNext())
				{
					allString.append(fileScan.nextLine()+"\n");
				}
		
				String pattern = "\\[.*\\]\\s([0-9]{4}-\\d\\d-\\d\\d) (\\d\\d:\\d\\d:\\d\\d)\\s*(.*)\\n(.*) (被捕获) 计划项ID:(.*) 操作：(.*)\\s*";
				Pattern newPar = Pattern.compile(pattern);
				Matcher m = newPar.matcher(allString);
				while(m.find())
				{
					
					String day=m.group(1);
					String time=m.group(2);
					String method = m.group(3);
					String tell = m.group(4); 
					String manage = m.group(5);
					String ID = m.group(6);
					String option = m.group(7);
					
					String[] Day = day.split("-");
					String[] Time = time.split(":");
					Calendar timeCal = Calendar.getInstance();
					timeCal.set(Integer.valueOf(Day[0]), Integer.valueOf(Day[1]) - 1,
							Integer.valueOf(Day[2]), Integer.valueOf(Time[0]),
							Integer.valueOf(Time[1]),Integer.valueOf(Time[2]));
					if((checkTime&&timeCal.getTime().after(timefrom.getTime())&&timeCal.getTime().before(timeto.getTime())) ||!checkTime )
					{
						if((checkID&&wantID.equals(ID+"\n")) || !checkID)
						{
							if((checkOpt&& wantOpt.equals(option)) ||!checkOpt)
							{
								System.out.println("日志时间："+day+" "+time);//2020-04-30 12:31:31
								System.out.println("位置:"+method);//方法
								System.out.println("提示信息:"+tell);//提示信息
								System.out.println("处理结果:"+manage);//处理方式 被捕获
								System.out.println("有关计划项ID:"+ID);//计划项ID
								System.out.println("操作所对应的选项："+option);//操作字符
							}
						}
					}
				fileScan.close();
				logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

				}
				}catch(FileNotFoundException e)
				{
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);

				}
				break;
				
			}
			case "help":// 提供帮助信息
			{
				printHelp();
				logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);
				break;
			}
			case "end":
				in.close();
				logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);
				System.exit(0);
			default:
				System.out.println("非法选项");
			}
		}
	}
}
