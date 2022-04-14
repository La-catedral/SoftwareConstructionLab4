package state;

import planningEntry.CommonPlanningEntry;

public class BLOCKED implements State{
	//immutable
	public static final BLOCKED instance = new BLOCKED(); //防御式编程 实例不可变 
	private BLOCKED() {};//防御式编程  隐藏构造器从而使程序中只有一个BLOCKED状态实例
	

	//Abstraction function:
	//	AF(instance) = the static instance of this state  
	//Representation invariant:
	//	the field must be non null，instance 必须为BLOCKED类实例
	//Safety from rep exposure:
	//	the field is final
	// 	没有改变成员变量的方法 仅有委托的方法

	//checkRep()
		private void checkRep()
		{
			assert instance != null;
			assert instance.getClass().equals(BLOCKED.class):"类成员变量的状态应该始终为为BLOCKED";
		}
	
	@Override
	public boolean allocate(CommonPlanningEntry<?> entry) {
		System.out.println("当前状态无法进行此操作！");
		checkRep(); //防御式编程 检查不变量
		return false;
	}
	
	@Override
	public boolean block(CommonPlanningEntry<?> entry) {
		System.out.println("当前状态无法进行此操作！");
		checkRep(); //防御式编程 检查不变量
		return false;
	}
	
	@Override
	public boolean cancel(CommonPlanningEntry<?> entry) {
		entry.setState(CANCELED.instance);
		checkRep(); //防御式编程 检查不变量
		return true;
	}
	
	@Override
	public boolean end(CommonPlanningEntry<?> entry) {
		System.out.println("当前状态无法进行此操作！");
		checkRep(); //防御式编程 检查不变量
		return false;
	}
	
	@Override
	public boolean run(CommonPlanningEntry<?> entry) {
		entry.setState(RUNNING.instance);
		checkRep(); //防御式编程 检查不变量
		return true;
	}
	@Override
	public String toString() {
		return "已阻塞";
	}
	
	
}
