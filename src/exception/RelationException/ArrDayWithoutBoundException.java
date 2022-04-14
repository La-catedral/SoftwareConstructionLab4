package exception.RelationException;

public class ArrDayWithoutBoundException extends Exception{

	//到达日期与出发日期之间超过一天而导致的异常
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ArrDayWithoutBoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}	
}
