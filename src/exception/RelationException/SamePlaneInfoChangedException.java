package exception.RelationException;

public class SamePlaneInfoChangedException extends Exception{

	//同一架飞机的信息产生了变化而导致的异常
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SamePlaneInfoChangedException() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SamePlaneInfoChangedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}


	
}
