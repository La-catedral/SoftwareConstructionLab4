package exception.grammarException;


//航班日期，遵循 yyyy-MM-dd 格式 ------------航班日期格式错误   
//航班日期缺失
//航班号，由两位大写字母和 2-4 位数字构成 ---------航班号格式错误
//航班号缺失
//起飞和降落两个时间均为 yyyy-MM-dd HH:mm 的格式 ---------起飞降落格式错误
//出发或起飞机场缺失？
//Plane编号的格式错误
//机型格式错误
//座位格式数量错误
//机龄格式错误



public class FlightDateFormatException extends Exception{

	//航班日期格式错误的异常
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public FlightDateFormatException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}


	
}
