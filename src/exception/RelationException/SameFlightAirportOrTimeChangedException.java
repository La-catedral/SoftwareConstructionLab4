package exception.RelationException;

public class SameFlightAirportOrTimeChangedException extends Exception{

	//同一航班在不同计划项中出现了不同的起飞到达机场或者不同的起飞到达时间
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SameFlightAirportOrTimeChangedException() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SameFlightAirportOrTimeChangedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}


	
}
