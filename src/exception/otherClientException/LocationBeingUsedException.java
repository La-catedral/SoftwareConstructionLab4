package exception.otherClientException;

public class LocationBeingUsedException extends Exception{

	//当前位置正在被占用而抛出的异常
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public LocationBeingUsedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}


	
}
