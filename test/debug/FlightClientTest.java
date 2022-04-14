package debug;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

public class FlightClientTest {

	//test strategy
	//将输入分为：
	//飞机的数量大于等于航班数量、 飞机数量小于航班数量且航班时间重复 、 飞机数量小于航班数量但时间不重复
	@Test
	public void test1() {
		FlightClient client = new FlightClient();
		
		Plane plane1 = new Plane();
		plane1.setPlaneNo("N5375");
		plane1.setPlaneAge(5);
		plane1.setPlaneType("B757");
		plane1.setSeatsNum(261);
		
		Calendar dep1 = Calendar.getInstance();
		Calendar arr1 = Calendar.getInstance();
		Calendar date1 = Calendar.getInstance();
		Flight flight1 = new Flight();
		flight1.setDepartAirport("Beijing");
		flight1.setArrivalAirport("harbin");
		dep1.set(2020, 1, 3, 0, 4);
		flight1.setDepartTime(dep1);
		arr1.set(2020, 1, 3, 3, 51);
		flight1.setArrivalTime(arr1);
		flight1.setFlightNo("AA0644");
		date1.set(2020, 1, 3);
		flight1.setFlightDate(date1);
		
		Calendar dtime2 = Calendar.getInstance();
		Calendar atime2 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		Flight flight2 = new Flight();
		flight2.setDepartAirport("Taiyuan");
		flight2.setArrivalAirport("Wuhan");
		dtime2.set(2020, 1, 3, 0, 4);
		flight2.setDepartTime(dtime2);
		atime2.set(2020, 1, 3, 3, 51);
		flight2.setArrivalTime(atime2);
		flight2.setFlightNo("AA0644");
		date2.set(2020, 1, 3);
		flight2.setFlightDate(date2);
		
		List<Plane> planes = new ArrayList<>();
		List<Flight> flights = new ArrayList<>();
		planes.add(plane1);
		flights.add(flight1);
		flights.add(flight2);
		assertFalse(client.planeAllocation(planes, flights));
		
		
		Plane plane2 = new Plane();
		plane2.setPlaneNo("N3133");
		plane2.setPlaneAge(3);
		plane2.setPlaneType("B531");
		plane2.setSeatsNum(300);
		planes.add(plane2);
		
		assertTrue(client.planeAllocation(planes, flights));
	}
	
	@Test
	public void test2() {
FlightClient client = new FlightClient();
		
		Plane plane1 = new Plane();
		plane1.setPlaneNo("N5375");
		plane1.setPlaneAge(5);
		plane1.setPlaneType("B757");
		plane1.setSeatsNum(261);
		
		
		Calendar dep1 = Calendar.getInstance();
		Calendar arr1 = Calendar.getInstance();
		Calendar date1 = Calendar.getInstance();
		Flight flight1 = new Flight();
		flight1.setDepartAirport("Beijing");
		flight1.setArrivalAirport("harbin");
		dep1.set(2020, 1, 3, 0, 4);
		flight1.setDepartTime(dep1);
		arr1.set(2020, 1, 3, 3, 51);
		flight1.setArrivalTime(arr1);
		flight1.setFlightNo("AA0644");
		date1.set(2020, 1, 3);
		flight1.setFlightDate(date1);
		
		Calendar dtime2 = Calendar.getInstance();
		Calendar atime2 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		Flight flight2 = new Flight();
		flight2.setDepartAirport("Taiyuan");
		flight2.setArrivalAirport("Wuhan");
		dtime2.set(2020, 1, 5, 0, 4);
		flight2.setDepartTime(dtime2);
		atime2.set(2020, 1, 6, 3, 51);
		flight2.setArrivalTime(atime2);
		flight2.setFlightNo("AA0644");
		date2.set(2020, 1, 5);
		flight2.setFlightDate(date2);
		
		List<Plane> planes = new ArrayList<>();
		List<Flight> flights = new ArrayList<>();
		planes.add(plane1);
		flights.add(flight1);
		flights.add(flight2);
	
		assertTrue(client.planeAllocation(planes, flights));
	}

}
