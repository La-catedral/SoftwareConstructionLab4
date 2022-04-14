package timeslot;

import exception.otherClientException.IllegalInputException;

public interface SingleSlot {
//用于管理一个特定的时间对
	/**
	 * set the management timeslot of this object 
	 * @param timeSlot the slot to be set
	 */
	public void setSlot(TimeSlot timeSlot) throws IllegalInputException;
	
	/**
	 * 返回该对象管理的时间对
	 * @return the timeSlot which is being managed by this manager
	 */
	public TimeSlot getSlot();
}
