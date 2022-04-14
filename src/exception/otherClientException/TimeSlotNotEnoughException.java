package exception.otherClientException;

public class TimeSlotNotEnoughException extends Exception {

	//列车时间对与途经车站数量不匹配而导致的异常
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public TimeSlotNotEnoughException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	
}
