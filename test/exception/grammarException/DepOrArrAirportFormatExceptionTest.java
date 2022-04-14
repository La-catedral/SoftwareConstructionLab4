package exception.grammarException;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import exception.RelationException.ArrDayWithoutBoundException;
import exception.RelationException.DateDifferentFromDepDayException;
import exception.RelationException.SameFlightAirportOrTimeChangedException;
import exception.RelationException.SamePlaneInfoChangedException;
import exception.signDuplicateException.SignDuplicateException;
import factory.FlightEntryFactory;
import flightSchedule.FlightEntry;
import location.Airport;
import location.Location;
import planningEntry.PlanningEntry;
import resource.Plane;
import timeslot.TimeSlot;

public class DepOrArrAirportFormatExceptionTest {

	//测试策略：
	//调用非法文本文件src/exception/testFiles/Flight6.txt 存在起飞或到达机场格式的错误
	//观察是否会抛出DepOrArrAirportFormatException异常 （捕捉异常 观察相关信息）
			
	
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
	@Test
	public void test() {
		
		Set<Airport> temLocationList = new HashSet<>();//现有位置列表
		Set<Plane> temResourceList = new HashSet<>();//现有资源列表
		List<FlightEntry> temEntryList = new ArrayList<>();//创立的所有计划项 无论状态 都要加入这里
		
		try (Scanner fileScan = new Scanner(new File("src/exception/testFiles/Flight6.txt"));){
			
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
		
		assertTrue(false); //不应执行到这里	
}
		catch(Exception e)
		{
			assertEquals("123*",e.getMessage());
		}
	}
	
}
