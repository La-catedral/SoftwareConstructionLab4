package exception.signDuplicateException;

public class SignDuplicateException extends Exception{
	
	//用于处理多个航班的航班号和日期完全重复的情况

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SignDuplicateException() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SignDuplicateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	
}
