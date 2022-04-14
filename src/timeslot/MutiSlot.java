package timeslot;

import java.util.List;

import exception.otherClientException.TimeSlotNotEnoughException;

public interface MutiSlot extends SingleSlot{
//管理一个或者多个时间对
	
	/**
	 * 设置该对象管理的时间对序列
	 * @param timeSlot,被管理的时间对
	 * @throws TimeSlotNotEnoughException if the number of the slots could not match the number of passing location
	 */
	public void setSlot(List<TimeSlot> timeSlot) throws TimeSlotNotEnoughException;//在设置多个时间对时进行防御式编程
	
	/**
	 * get the list of TimeSlot that this manager have 
	 * @return,the TimeSlot list
	 */
	public List<TimeSlot> getSlotList();
}
