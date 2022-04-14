package state;

import planningEntry.CommonPlanningEntry;

public class WAITING implements State{
	//immutable
	public static final WAITING instance = new WAITING();//防御式编程 类成员变量为final 无法改变
	private WAITING() {};//防御式编程  隐藏构造器从而使程序中只有一个WAITING状态实例
	
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
		assert instance.getClass().equals(WAITING.class):"类成员变量的状态应该始终为为WAITING";
	}
	
	@Override
	public boolean allocate(CommonPlanningEntry<?> entry) {
		entry.setState(ALLOCATED.instance);
		checkRep();//防御式编程 检查不变量
		return true;
	}
	
	
	@Override
	public boolean block(CommonPlanningEntry<?> entry) {
		System.out.println("当前状态无法进行此操作！");
		checkRep();//防御式编程 检查不变量
		return false;
	}
	
	
	@Override
	public boolean cancel(CommonPlanningEntry<?> entry) {
		entry.setState(CANCELED.instance);
		checkRep();//防御式编程 检查不变量
		return true;	
	}
	
	@Override
	public boolean end(CommonPlanningEntry<?> entry) {
		System.out.println("当前状态无法进行此操作！");
		checkRep();//防御式编程 检查不变量
		return false;
	}
	
	
	@Override
	public boolean run(CommonPlanningEntry<?> entry) {
		System.out.println("未分配资源！");
		checkRep();//防御式编程 检查不变量
		return false;
	}
	@Override
	public String toString() {
		checkRep();//防御式编程 检查不变量
		return "未分配资源";
	}
	

}
