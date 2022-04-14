package exception.grammarException;

public class DepOrArrAirportMissingException extends Exception{

	//出发或离开的机场名缺失引发的异常
	
	private static final long serialVersionUID = 1L;


	public DepOrArrAirportMissingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}


}
