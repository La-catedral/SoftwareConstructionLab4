package debug;
import java.util.Calendar;
import java.util.Comparator;

public class Flight {

	private String flightNo; // �����
	private Calendar flightDate; // �������ڣ�����2020-01-01
	private String departAirport; // �����������
	private String arrivalAirport;// ���ཱུ�����
	private Calendar departTime;// �������ʱ��
	private Calendar arrivalTime;// ���ཱུ��ʱ��
	private Plane plane;// ִ�ɵķɻ�

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public Calendar getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(Calendar flightDate) {
		this.flightDate = flightDate;
	}

	public String getDepartAirport() {
		return departAirport;
	}

	public void setDepartAirport(String departAirport) {
		this.departAirport = departAirport;
	}

	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public Calendar getDepartTime() {
		return departTime;
	}

	public void setDepartTime(Calendar departTime) {
		this.departTime = departTime;
	}

	public Calendar getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Calendar arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getPlaneNo() {
		return this.plane.getPlaneNo();
	}

	public String getPlaneType() {
		return this.plane.getPlaneType();
	}

	public int getSeatsNum() {
		return this.plane.getSeatsNum();
	}

	public double getPlaneAge() {
		return this.plane.getPlaneAge();
	}

	@Override
	public boolean equals(Object another) {
		if (another == null)
			return false;
		if (!(another instanceof Flight))
			return false;
		Flight anotherFlight = (Flight) another;
		if (anotherFlight.getFlightNo().equals(this.getFlightNo())
				&& anotherFlight.getFlightDate().equals(this.getFlightDate()))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return this.flightNo.hashCode();
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}
	
	  class FlightComparatorOne implements Comparator<Flight>
	 {
		 @Override
		public int compare(Flight o1, Flight o2) {
			 assert o1!= null; //防御式编程
			 assert o2!= null;
			 assert o1.getDepartTime()!= null;
			 assert o2.getDepartTime()!= null;
			if(o1.getDepartTime().after(o2.getDepartTime()))
				return 1;
			else if(o1.getDepartTime().equals(o2.getDepartTime()))
				return 0;
			else
				return -1;
				
		}
	 }
	
}
class FlightComparatorOne implements Comparator<Flight>
{
	 @Override
	public int compare(Flight o1, Flight o2) {
		 assert o1!= null; //防御式编程
		 assert o2!= null;
		 assert o1.getDepartTime()!= null;
		 assert o2.getDepartTime()!= null;
		if(o1.getDepartTime().after(o2.getDepartTime()))
			return 1;
		else if(o1.getDepartTime().equals(o2.getDepartTime()))
			return 0;
		else
			return -1;
			
	}
}