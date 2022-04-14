package exception.otherClientException;

public class CannotCancelException extends Exception{
	//因当前计划项无法删除而导致的异常
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	public CannotCancelException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	

}
