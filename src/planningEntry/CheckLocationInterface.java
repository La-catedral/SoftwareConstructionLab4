package planningEntry;

import java.util.List;


public interface CheckLocationInterface {

	/**
	 * 检测一组计划项之间是否存在位置独占冲突:
	 * 如果两个计划项在同一时间点上占用了不可共享的位置，那么就存在了位置冲突。
	 * @param entries，要检查的计划项列表 不能为空
	 * @return 如果存在返回true 否则返回false
	 */
	public boolean checkLocationConflict(List<? extends PlanningEntry<?>>   entries);
}
