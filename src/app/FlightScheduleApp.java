package app;
import java.io.File;



import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import board.FlightBoard;
import factory.FlightEntryFactory;
import flightSchedule.FlightEntry;
import location.Airport;
import location.Location;
import planningEntry.PlanningEntry;
import planningEntry.PlanningEntryAPIs;
import resource.Plane;
import state.CANCELED;
import state.ENDED;
import state.WAITING;
import timeslot.TimeSlot;
import exception.grammarException.* ;
import exception.otherClientException.CannotCancelException;
import exception.otherClientException.IllegalInputException;
import exception.otherClientException.LocationBeingUsedException;
import exception.otherClientException.ResourceBeingUsedException;
import exception.otherClientException.ResourceExclusiveConflictException;
import exception.signDuplicateException.SignDuplicateException;
import exception.RelationException.*;
public class FlightScheduleApp {

	private static Logger logger = Logger.getLogger(Logger.class); 

	/**
	 * 在当前所有资源中查找特定的ID的资源
	 * @param ID 要查找的资源的ID
	 * @param resourceList 当前所有资源构成的列表
	 * @return 具有该特定的ID的资源
	 */
	public static Plane findResource(String ID,Set<Plane>resourceList)
	{
		for(Plane thisPlane:resourceList)
		{
			if(thisPlane.getID().equals(ID))
			return thisPlane;
		}
		
		return null;
	}
	
	/**
	 * 在当前所有位置中查找特定的名称的位置
	 * @param name 位置的名称
	 * @param locationList 所有的现有位置
	 * @return 所要找的位置
	 */
	public static Airport findAirport(String name,Set<Airport>locationList)
	{
		for(Airport thisPort : locationList)
		{
			if(thisPort.getName().equals(name))
			return thisPort;
		}
		return null;
	}
	
	/**
	 * 在当前所有的已有计划项中查找名称为name的计划项
	 * @param name 要找的计划项的名称
	 * @param entryList 所有已有计划项构成的list
	 * @return 要找的计划项
	 */
	public static FlightEntry findEntry(String name,List<FlightEntry>entryList)
	{
		for(PlanningEntry<?> thisEntry:entryList)
		{
			if(thisEntry.getName().equals(name))
				return (FlightEntry)thisEntry;
		}
		return null;
	}
	public static void printHelp()
	{
		System.out.println("********help********");
		System.out.println("输入下列英文字母或单词：");
		System.out.println("a :增设一架飞机");
		System.out.println("b :删除一架飞机");
		System.out.println("c :增加一座机场");
		System.out.println("d :删除一座机场");
		System.out.println("e :增加一个航班计划项");
		System.out.println("f :取消一个计划项");
		System.out.println("g :为某一航班分配飞机");
		System.out.println("h :航班起飞");
		System.out.println("i :航班降落");
		System.out.println("j :查看某航班状态");
		System.out.println("k :检测资源独占冲突");
		System.out.println("l :对于一架飞机，查看与该飞机有关的所有计划项");
		System.out.println("m :对于一架飞机，查看某个计划项的前序计划项");
		System.out.println("n :显示一个机场的信息版");
		System.out.println("help :提供帮助信息");
		System.out.println("parser :从外部合法文件中读取计划项信息并生成计划项");
		System.out.println("checklog :打印输出有关错误异常的日志");
		System.out.println("end :结束");
		System.out.println();

	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException {

		Set<Airport> locationList = new HashSet<>();//现有位置列表
		Set<Plane> resourceList = new HashSet<>();//现有资源列表
		List<FlightEntry> entryList = new ArrayList<>();//创立的所有计划项 无论状态 都要加入这里
		Map<FlightEntry,Calendar> cancelTime = new HashMap<>();
		
		//运行app
		printHelp();
		Scanner in = new Scanner(System.in);
		while(true)
		{
			System.out.println("请输入选项：");
			String choice = in.nextLine();
			switch (choice) {

			case "a":
			{
				try{System.out.println("请输入要增加飞机的编号、即兴、座位数、机龄，参数间空格分开，如\"B9802 A350 300 2.5\"：");
				String[] param = in.nextLine().split(" ");
				Plane newPlane = new Plane(param[0], param[1], Integer.valueOf(param[2]), Double.valueOf(param[3]));
				resourceList.add(newPlane);
				logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

				}catch(ArrayIndexOutOfBoundsException e)
				{
					System.out.println("不符合要求的格式！");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);

				} catch (NumberFormatException e) {
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);

				} catch (IllegalInputException e) {
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				}
				break;
			}
			case "b":
			{
				System.out.println("请输入要删除飞机的编号：");
				String ID = in.nextLine();
				Plane planeToDel = findResource(ID, resourceList);
				try {
					if (planeToDel == null) {
						System.out.println(" 不存在此飞机");
					} else {
						for (FlightEntry thisEntry : entryList) {
							if(thisEntry.getResource()!= null) {
							if (thisEntry.getResource().equals(planeToDel)) {
								if (!(thisEntry.getState().equals(CANCELED.instance)
										|| thisEntry.getState().equals(ENDED.instance))) {
									System.out.println("有未结束的计划项正在占用该资源");
									throw new ResourceBeingUsedException(ID);
								}
							}}
						}
						resourceList.remove(planeToDel);
						logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

					}
				} catch (ResourceBeingUsedException e) {
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				}
				break;
			}
			case "c"://增加一座机场
			{
				try {
				System.out.println("请输入要增加机场的名字、经度、纬度，参数间空格分开，如\"Harbin -30 155\"：");
				String[] paramOfPort = in.nextLine().split(" ");
				Airport newLocation = new Airport(paramOfPort[0], Double.valueOf(paramOfPort[1]),
						Double.valueOf(paramOfPort[2]));
				locationList.add(newLocation);
				logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

				}catch(ArrayIndexOutOfBoundsException e)
				{
					System.out.println("不符合要求的格式！");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				} catch (NumberFormatException e) {
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				} catch (IllegalInputException e) {
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				}
				break;
			}
			case"d" ://删除一座机场
			{
				System.out.println("请输入要删除机场的名字");
				String nameOfPort = in.nextLine();
				Airport portToDel = findAirport(nameOfPort, locationList);
				try {
					if (portToDel == null) {
						System.out.println(" 不存在此机场");
					} else {
						for (FlightEntry thisEntry : entryList) {
							if (thisEntry.getfromLocation().equals(portToDel)
									|| thisEntry.getToLocation().equals(portToDel)) {
								if (!(thisEntry.getState().equals(CANCELED.instance)
										|| thisEntry.getState().equals(ENDED.instance))) {
									System.out.println("有未结束的计划项正在占用该位置");
									throw new LocationBeingUsedException(nameOfPort);

								}
							}
						}
						locationList.remove(portToDel);
						logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

					}
				} catch (LocationBeingUsedException e) {
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				}
				break;
			}
			case "e" ://增加一个航班计划项");
			{
				System.out.println("请输入航班计划项的编号、起始地点、终止地点，参数间空格分开，如\"CA1001 Harbin BeiJing\"：");
				
					String[] firstParam = in.nextLine().split(" ");
					try {FlightEntry newEntry = new FlightEntryFactory().getEntry(firstParam[0]);
					Location fromLocation = findAirport(firstParam[1], locationList);
					Location toLocation = findAirport(firstParam[2], locationList);
					if (fromLocation != null && toLocation != null) {
						newEntry.setLocation(fromLocation, toLocation);
						System.out.println("请输入起飞时间，以yyyy MM dd HH mm的格式，例如\"2020 1 1 13 27\"代表2020年1月1日13点27分：");
						String[] secondParam = in.nextLine().split(" ");
						Calendar from = Calendar.getInstance();
						from.set(Integer.valueOf(secondParam[0]), Integer.valueOf(secondParam[1]) - 1,
								Integer.valueOf(secondParam[2]), Integer.valueOf(secondParam[3]),
								Integer.valueOf(secondParam[4]));
						System.out.println("请输入降落时间，以yyyy MM dd HH mm的格式，例如\"2020 1 1 13 27\"代表2020年1月1日13点27分：");
						String[] thirdParam = in.nextLine().split(" ");
						Calendar to = Calendar.getInstance();
						to.set(Integer.valueOf(thirdParam[0]), Integer.valueOf(thirdParam[1]) - 1,
								Integer.valueOf(thirdParam[2]), Integer.valueOf(thirdParam[3]),
								Integer.valueOf(thirdParam[4]));
						newEntry.setSlot(new TimeSlot(from, to));
						entryList.add(newEntry);
						logger.info("合法操作"+" 被捕获"+" 计划项ID:"+newEntry.getName()+" 操作："+choice);

					} else {
						System.out.println("不存在的机场");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("不符合要求的格式！");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+firstParam[0]+" 操作："+choice,e);
				} catch (IllegalInputException e) {
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+firstParam[0]+" 操作："+choice,e);
				}
				break;
			}
			case "f" ://取消一个计划项")
			{
				System.out.println("请输入航班计划项的编号:");
				
					String paramString = in.nextLine();
					FlightEntry entryToDel = findEntry(paramString, entryList);
					try {System.out.println("请输入当前时间，以yyyy MM dd HH mm的格式，例如\"2020 1 1 13 27\"代表2020年1月1日13点27分：");
					String[] timeParam = in.nextLine().split(" ");
					Calendar from = Calendar.getInstance();
					from.set(Integer.valueOf(timeParam[0]), Integer.valueOf(timeParam[1]) - 1,
							Integer.valueOf(timeParam[2]), Integer.valueOf(timeParam[3]),
							Integer.valueOf(timeParam[4]));
					if (entryToDel != null) {
						if (!entryToDel.cancel()) {
							throw new CannotCancelException(entryToDel.getState().toString());
						}else {
							cancelTime.put(entryToDel, from);  //记录取消时间
							logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice);

						}
					} else {
						System.out.println("没有该计划项！");
					}
				} catch (CannotCancelException e) {
					System.out.println("失败:" + e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice,e);
				}
				break;
			}
			case"g" ://为某个计划项分配资源
			{
				System.out.println("请输入要分配资源的航班计划项的编号、飞机ID，参数间空格分开，如\"CA1001 B9802\"：");
				String[] firstParam = in.nextLine().split(" ");
				FlightEntry entryToAllo = findEntry(firstParam[0], entryList);
				Plane planeToAllo = findResource(firstParam[1], resourceList);
				try{if(entryToAllo !=null && planeToAllo!= null)
				{
					for (FlightEntry thisEntry : entryList) {
						if(thisEntry.getResource()!= null) {
						if (thisEntry.getResource().equals(planeToAllo)) {
							if (!(thisEntry.getState().equals(CANCELED.instance))
									&& thisEntry.getSlot().checkCoinOrNot(entryToAllo.getSlot())) {
								throw new ResourceExclusiveConflictException(thisEntry.getName());
							}
						}}
					}
					entryToAllo.setResource(planeToAllo);
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToAllo.getName()+" 操作："+choice);
				
				}
				else {
					System.out.println("请检查您的输入！");
				}}
				catch(ResourceExclusiveConflictException e)
				{
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+entryToAllo.getName()+" 操作："+choice,e);
				}
				break;
			}
			case "h" ://启动某一计划项
			{
				System.out.println("请输入要启动的航班计划项的编号:");
				String paramString = in.nextLine();
				FlightEntry entryToDel = findEntry(paramString, entryList);
				if (entryToDel != null) {
					entryToDel.run();
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice);

				} else {
					System.out.println("没有该计划项！");
				}
				break;
			}
			case "i" ://结束某个计划项
			{
				System.out.println("请输入要结束的航班计划项的编号:");
				String paramString = in.nextLine();
				FlightEntry entryToDel = findEntry(paramString, entryList);
				if (entryToDel != null) {
					entryToDel.end();
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice);

				} else {
					System.out.println("没有该计划项！");
				}
				break;
			}
			case "j" ://用户选定一个计划项，查看它的当前状态
			{
				System.out.println("请输入查询状态的航班计划项的编号:");
				String paramString = in.nextLine();
				FlightEntry entryToDel = findEntry(paramString, entryList);
				if (entryToDel != null) {
					System.out.println(entryToDel.getState());
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+entryToDel.getName()+" 操作："+choice);

				} else {
					System.out.println("没有该计划项！");
				}
				break;
			}
			case "k" ://检测当前的计划项集合中可能存在的资源独占冲突
			{	
				try {
					System.out.println("请稍等");
					PlanningEntryAPIs<?> thisOne = new PlanningEntryAPIs<>();
					System.out.println("是否存在资源独占冲突："+thisOne.checkResourceExclusiveConflict(entryList));
				} catch (IllegalInputException e) {
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				}
				break;
			}
				
			case "l" ://针对用户选定的某个资源，列出使用该资源的所有计划项
			{
				System.out.println("请输入需要查询的飞机的ID:");
				String paramString = in.nextLine();
				Plane planeToSear = findResource(paramString, resourceList);
				if(planeToSear != null)
				{
					for(FlightEntry thisEntry: entryList)
					{
						if (!thisEntry.getState().equals(WAITING.instance)) {
							if (thisEntry.getResource().equals(planeToSear))
								System.out.println(thisEntry.getName());
						}
					}
				}
				else {
					System.out.println("不存在该飞机");
				}
				break;
			}
			case "m"://对于一架飞机，查看某个计划项的前序计划项
			{
				System.out.println("请输入航班计划项的编号、以及有关飞机的ID，参数间空格分开，如\"CA1001 B9802\"：");
				
					PlanningEntryAPIs<Plane> thisOne = new PlanningEntryAPIs<>();
					String[] firstParam = in.nextLine().split(" ");
					try {PlanningEntry<Plane> checkedOne = findEntry(firstParam[0], entryList);
					Plane thisResource = findResource(firstParam[1], resourceList);
					if (checkedOne != null && thisResource != null) {
						PlanningEntry<Plane> formerOne = thisOne.findPreEntryPerResource(thisResource, checkedOne,
								entryList);
						if (formerOne != null)
							System.out.println(formerOne.getName());
						else
							System.out.println("没有更早的计划项");
						logger.info("合法操作"+" 被捕获"+" 计划项ID:"+firstParam[0]+" 操作："+choice);

					}
				} catch (IllegalInputException e) {
					System.out.println("失败:" + e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+firstParam[0]+" 操作："+choice,e);
				}
				break;
			}
			case "n"://选定特定位置，可视化展示当前时刻该位置的信息板
			{
				System.out.println("请输入需要查询的机场的名字:");
				String paramString = in.nextLine();
				Airport portToBoard = findAirport(paramString, locationList);
				System.out.println("请输入当前时间，以yyyy MM dd HH mm的格式，例如\"2020 1 1 13 27\"代表2020年1月1日13点27分：");
				String[] secondParam = in.nextLine().split(" ");
				Calendar timeNow = Calendar.getInstance();
				timeNow.set(Integer.valueOf(secondParam[0]), Integer.valueOf(secondParam[1]) - 1,
						Integer.valueOf(secondParam[2]), Integer.valueOf(secondParam[3]),
						Integer.valueOf(secondParam[4]));
				if(portToBoard != null)
				{
					FlightBoard newBoard = new FlightBoard(timeNow, portToBoard);
					newBoard.setFromList(entryList);
					newBoard.setToList(entryList);
					newBoard.setIterToArr();
					newBoard.iterator();
					newBoard.setIterToFro();
					newBoard.iterator();
					newBoard.visualize();
					logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

				}
				else {
					System.out.println("不存在该机场");
				}
				break;
			}
			case "help" ://提供帮助信息");	
			{
				printHelp();
				logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

				break;
			}
			case "parser":
			{
				System.out.println("请输入文件民，比如src/app/FlightSchedule_1.txt:");
				Scanner fileScan = new Scanner(new File(in.nextLine()));
				Set<Airport> temLocationList = new HashSet<>();//现有位置列表
				Set<Plane> temResourceList = new HashSet<>();//现有资源列表
				List<FlightEntry> temEntryList = new ArrayList<>();//创立的所有计划项 无论状态 都要加入这里
				
				try {
				while (fileScan.hasNext()) {
					String thisLine,pat1,pat2;
					String pattern1 = "Flight:(.*),(.*)\\s*";
					Pattern newPar = Pattern.compile(pattern1);
					thisLine = fileScan.nextLine();
					Matcher m = newPar.matcher(thisLine);
					if (m.matches()) {
						
						pat1 = m.group(1);
						pat2 = m.group(2);
						
						if(pat1.length() == 0)
						{
							throw new FlightDateMissingException(thisLine);
						}
						
						newPar = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d");
						m = newPar.matcher(pat1);
						if(!m.matches()) {
							throw new FlightDateFormatException(pat1);
						}
						
						if(pat2.length()==0 )
						{
							throw new FlightIdMissingException(thisLine);
						}
						
						newPar = Pattern.compile("[A-Z][A-Z]\\d{2,4}");
						m = newPar.matcher(pat2);
						if(!m.matches()) {
							throw new FlightIDFormatException(pat2);
						}
					}
					else {
						throw new FrameworkFormatException(thisLine);
					}
					String FlightName = pat2;
					String date = pat1;

					String pattern2 = "\\{";// 第二行
					newPar = Pattern.compile(pattern2);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					if (!m.matches()) {
						throw new FrameworkFormatException(thisLine);
						}

					String pattern3 = "\\s*DepartureAirport:(.*)";
					newPar = Pattern.compile(pattern3);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					if (m.matches()) {
						pat1 = m.group(1);
						if(pat1.length()==0)//缺失起飞机场
						{
							throw new DepOrArrAirportMissingException(thisLine);
						}
						newPar = Pattern.compile("\\w+");//起飞机场非单词 
						m = newPar.matcher(pat1);
						if(!m.matches()) {
							throw new DepOrArrAirportFormatException(pat1);
						}
					}
					else
					{
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}
					String departureAir = pat1;

					String pattern4 = "\\s*ArrivalAirport:(.*)";
					newPar = Pattern.compile(pattern4);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					if (m.matches()) {
						pat1 = m.group(1);
						if(pat1.length()==0)//缺失到达机场
						{
							throw new DepOrArrAirportMissingException(thisLine);
						}
						newPar = Pattern.compile("\\w+");//到达机场非单词 
						m = newPar.matcher(pat1);
						if(!m.matches()) {
							throw new DepOrArrAirportFormatException(pat1);
						}
					}
					else
					{
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}
					String ArrivalAir = pat1;

					String pattern5 = "\\s*DepatureTime:(.*) (.*)\\s*";
					newPar = Pattern.compile(pattern5);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					
					if (m.matches())// 若不匹配或者该日期和前面的日期不相同//|| !m.group(1).equals(date)
					{
						pat1 = m.group(1);
						pat2 = m.group(2);
						newPar = Pattern.compile("([0-9]{4}-\\d\\d-\\d\\d) (\\d\\d:\\d\\d)");
						m = newPar.matcher(pat1+" "+pat2);
						if(!m.matches()) {
							throw new DeparOrArriTimeFormatException(pat1+" "+pat2);
						}
						if(!pat1.equals(date))
						{
							throw new DateDifferentFromDepDayException(pat1);
						}
					}
					else {
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}
					String deparDate = pat1;
					String deparTime = pat2;

					String pattern6 = "\\s*ArrivalTime:(.*) (.*)\\s*";
					newPar = Pattern.compile(pattern6);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					Calendar firstCal = Calendar.getInstance();
					Calendar newCal = Calendar.getInstance();
					if (m.matches())// 若不匹配
					{
						pat1 = m.group(1);
						pat2 = m.group(2);
						newPar = Pattern.compile("([0-9]{4}-\\d\\d-\\d\\d) (\\d\\d:\\d\\d)"); 
						m = newPar.matcher(pat1+" "+pat2);
						if(!m.matches()) {
							throw new DeparOrArriTimeFormatException(pat1+" "+pat2);
						}
						
						String[] formerTimeString = date.split("-");
						firstCal.set(Integer.valueOf(formerTimeString[0]), Integer.valueOf(formerTimeString[1]) - 1,
								Integer.valueOf(formerTimeString[2]));
						if (!pat1.equals(date)) {// 该日期和前面的日期不相同
						
						String[] timeString = pat1.split("-");
						newCal.set(Integer.valueOf(timeString[0]), Integer.valueOf(timeString[1]) - 1,
								Integer.valueOf(timeString[2]));
						
						if (!(firstCal.get(Calendar.DAY_OF_YEAR) == newCal.get(Calendar.DAY_OF_YEAR) - 1)) {
							throw new ArrDayWithoutBoundException(pat1+" "+ pat2);
						}
						}
					}
					else {
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}
					String arriDate = pat1;
					String arriTime = pat2;

					String pattern7 = "\\s*Plane:(.*)\\s*";
					newPar = Pattern.compile(pattern7);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					
					if (m.matches()) {
						pat1 = m.group(1);
						newPar = Pattern.compile("(B|N)\\d{4}");
						m = newPar.matcher(pat1);
						if(!m.matches()) {
							throw new PlaneIdFormatException(pat1);
						}
						
					}else
					{
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}
					String PlaneID = pat1;

					String pattern8 = "\\{";
					newPar = Pattern.compile(pattern8);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					if (!m.matches()) {
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}

					String pattern9 = "\\s*Type:(.*)\\s*";
					newPar = Pattern.compile(pattern9);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					
					if (m.matches()) {
						pat1 = m.group(1);
						newPar = Pattern.compile("[\\da-zA-Z]+");//type 不符合规定 
						m = newPar.matcher(pat1);
						if(!m.matches()) {
							throw new PlaneTypeFormatException(pat1);
						}
					}else {
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}
					String type = pat1;

					String pattern10 = "\\s*Seats:(.*)\\s*";
					newPar = Pattern.compile(pattern10);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					
					if (m.matches()) {
						pat1 = m.group(1);
						newPar = Pattern.compile("\\d+");//起飞机场非单词 
						m = newPar.matcher(pat1);
						if(!m.matches() || Integer.valueOf(pat1) < 50 || Integer.valueOf(pat1) > 600) {
							throw new PlaneSeatsFormatException(pat1);
						}
					}else
					{
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}
					String seats = pat1;

					String pattern11 = "\\s*Age:(.*)\\s*";
					newPar = Pattern.compile(pattern11);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					
					if (m.matches())
					{
						pat1 = m.group(1);
						newPar = Pattern.compile("(([1-9]?\\d)|0)(\\.\\d)?");//起飞机场非单词 
						m = newPar.matcher(pat1);
						if(!m.matches() || Double.valueOf(pat1) < 0 || Double.valueOf(pat1) > 30)
						{	
							throw new PlaneAgeFormatException(pat1);
							
						}
					}else
					{
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}
					String age = pat1;

					String pattern12 = "\\s*\\}\\s*";
					newPar = Pattern.compile(pattern12);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					if (!m.matches()) {
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}

					String pattern13 = "\\s*\\}\\s*";
					newPar = Pattern.compile(pattern13);
					thisLine = fileScan.nextLine();
					m = newPar.matcher(thisLine);
					if (!m.matches()) {
						System.out.println(thisLine + "：该行不符合规范，停止读入");
						throw new FrameworkFormatException(thisLine);
					}
				

					
					{
						Plane thisOne =findResource(PlaneID, temResourceList);
						if( thisOne!=null)
						{
							if(!(thisOne.getType().equals(type) && thisOne.getSeats()==Integer.valueOf(seats) && thisOne.getAge()== Double.valueOf(age)))
							throw new SamePlaneInfoChangedException();
						}
						Plane newPlane = new Plane(PlaneID, type, Integer.valueOf(seats), Double.valueOf(age));
						temResourceList.add(newPlane);
						
						Airport newLocationOne = new Airport(departureAir, 0,0);
						temLocationList.add(newLocationOne);
						
						Airport newLocationTwo = new Airport(ArrivalAir, 0,0);
						temLocationList.add(newLocationTwo);
					}
					{
						FlightEntry newEn = findEntry(FlightName, temEntryList);
						if(newEn!=null)
						{
							if (newEn.getSlot().getBeginTime().get(Calendar.DAY_OF_YEAR) == firstCal
									.get(Calendar.DAY_OF_YEAR)) {//若航班编号相同 并且日期也相同 则为非法
								throw new SignDuplicateException();
							} else {//若是不同日期的同一编号的航班
								String[] depar = deparTime.split(":");
								String[] arri= arriTime.split(":");
								if (!(Integer.valueOf(depar[0]) == newEn.getSlot().getBeginTime()//起飞时间应相同
										.get(Calendar.HOUR_OF_DAY)
										&& Integer.valueOf(depar[1]) == newEn.getSlot().getBeginTime()
												.get(Calendar.MINUTE)
										&& Integer.valueOf(arri[0]) == newEn.getSlot().getEndTime()//抵达时间应相同
												.get(Calendar.HOUR_OF_DAY)
										&& Integer.valueOf(arri[1]) == newEn.getSlot().getEndTime().get(Calendar.MINUTE)
										&& departureAir.equals(newEn.getfromLocation().getName())//起飞机场应相同
										&& ArrivalAir.equals(newEn.getToLocation().getName()))) {//降落机场应相同
									throw new SameFlightAirportOrTimeChangedException();
								}
								else {//满足条件 就新建一个计划项
									FlightEntry newEntry = new FlightEntryFactory().getEntry(FlightName);
									Location fromLocation = findAirport(departureAir, temLocationList);
									Location toLocation = findAirport(ArrivalAir, temLocationList);
									if (fromLocation != null && toLocation != null) {
										newEntry.setLocation(fromLocation, toLocation);

										String[] deparDay = deparDate.split("-");
										String[] deparNew = deparTime.split(":");
										String[] arrDay = arriDate.split("-");
										String[] arr = arriTime.split(":");

										Calendar from = Calendar.getInstance();
										from.set(Integer.valueOf(deparDay[0]), Integer.valueOf(deparDay[1]) - 1,
												Integer.valueOf(deparDay[2]), Integer.valueOf(deparNew[0]),
												Integer.valueOf(deparNew[1]));
										Calendar to = Calendar.getInstance();
										to.set(Integer.valueOf(arrDay[0]), Integer.valueOf(arrDay[1]) - 1,
												Integer.valueOf(arrDay[2]), Integer.valueOf(arr[0]), Integer.valueOf(arr[1]));
										newEntry.setSlot(new TimeSlot(from, to));
										newEntry.setResource(findResource(PlaneID, temResourceList));
										temEntryList.add(newEntry);
									}
									
								}
							}
						}
						else {// 若没有此编号的航班 创建一个
							FlightEntry newEntry = new FlightEntryFactory().getEntry(FlightName);
							Location fromLocation = findAirport(departureAir, temLocationList);
							Location toLocation = findAirport(ArrivalAir, temLocationList);
							if (fromLocation != null && toLocation != null) {
								newEntry.setLocation(fromLocation, toLocation);

								String[] deparDay = deparDate.split("-");
								String[] depar = deparTime.split(":");
								String[] arrDay = arriDate.split("-");
								String[] arr = arriTime.split(":");

								Calendar from = Calendar.getInstance();
								from.set(Integer.valueOf(deparDay[0]), Integer.valueOf(deparDay[1]) - 1,
										Integer.valueOf(deparDay[2]), Integer.valueOf(depar[0]),
										Integer.valueOf(depar[1]));
								Calendar to = Calendar.getInstance();
								to.set(Integer.valueOf(arrDay[0]), Integer.valueOf(arrDay[1]) - 1,
										Integer.valueOf(arrDay[2]), Integer.valueOf(arr[0]), Integer.valueOf(arr[1]));
								newEntry.setSlot(new TimeSlot(from, to));
								newEntry.setResource(findResource(PlaneID, temResourceList));
								temEntryList.add(newEntry);
							}
						}
					}
					}
				} catch (FlightDateFormatException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (FlightDateMissingException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (FlightIDFormatException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (ArrDayWithoutBoundException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (DeparOrArriTimeFormatException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (DateDifferentFromDepDayException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (DepOrArrAirportFormatException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (DepOrArrAirportMissingException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (FlightIdMissingException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (FrameworkFormatException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (PlaneTypeFormatException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (PlaneIdFormatException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (PlaneSeatsFormatException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				}catch (PlaneAgeFormatException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				} catch (SignDuplicateException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				} catch (SameFlightAirportOrTimeChangedException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				} catch (SamePlaneInfoChangedException e) {
					System.out.println(e.toString());
					System.out.println("该文件内容存在错误，请选择其他操作或切换至其他文件～");
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
					break;
				} catch (IllegalInputException e) {
					System.out.println("失败:"+e);
					logger.error(e.getMessage()+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice,e);
				}
				
				//需要把假列表中的对象复制到真列表
				locationList.addAll(temLocationList);
				resourceList.addAll(temResourceList);
				entryList.addAll(temEntryList);
			
				fileScan.close();
				logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

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
			
			case "end":
				in.close();
				logger.info("合法操作"+" 被捕获"+" 计划项ID:"+"none"+" 操作："+choice);

				System.exit(0);
			default:
				System.out.println("非法选项！");
			}
			
		}
	}

}
