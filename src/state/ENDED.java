package state;

import planningEntry.CommonPlanningEntry;

public class ENDED implements State{
	//immutable
	public static final ENDED instance = new ENDED();//防御式编程 类成员变量为final 无法改变
	private ENDED() {};//防御式编程  隐藏构造器从而使程序中只有一个ENDED状态实例
	

	//Abstraction function:
	//	AF(instance) = the static instance of this state  
	//Representation invariant:
	//	the field must be non null
	//Safety from rep exposure:
	//	the field is final
	// 	没有改变成员变量的方法 仅有委托的方法
	
	//checkRep()
		private void checkRep()
		{
			assert instance != null;
			assert instance.getClass().equals(ENDED.class):"类成员变量的状态应该始终为为ENDED";

		}
	
	@Override
	public boolean allocate(CommonPlanningEntry<?> entry) {
		System.out.println("该任务已结束！");
		checkRep();
		return false;
	}
	
	@Override
	public boolean block(CommonPlanningEntry<?> entry) {
		System.out.println("该任务已结束！");
		return false;
	}
	
	@Override
	public boolean cancel(CommonPlanningEntry<?> entry) {
		System.out.println("该任务已结束！");
		return false;
	}
	
	@Override
	public boolean end(CommonPlanningEntry<?> entry) {
		System.out.println("该任务已结束！");
		return false;
	}
	
	@Override
	public boolean run(CommonPlanningEntry<?> entry) {
		System.out.println("该任务已结束！");
		return false;
	}
	@Override
	public String toString() {
		return "已结束";
	}
	
	
	
}
