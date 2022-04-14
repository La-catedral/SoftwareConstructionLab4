package state;

import planningEntry.CommonPlanningEntry;

public class RUNNING implements State{
	//immutable
	public static RUNNING instance = new RUNNING();//防御式编程 将成员变量设置为final
	private RUNNING() {};//防御式编程  隐藏构造器从而使程序中只有一个RUNNING状态实例
	

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
			assert instance.getClass().equals(RUNNING.class):"类成员变量的状态应该始终为为RUNNING";
		}
	
	@Override
	public boolean allocate(CommonPlanningEntry<?> entry) {
		System.out.println("当前状态无法进行此操作！");
		checkRep();//防御式编程 防止不变量被破坏
		return false;
	}
	
	@Override
	public boolean block(CommonPlanningEntry<?> entry) {
		entry.setState(BLOCKED.instance);
		checkRep();//防御式编程 防止不变量被破坏
		return true;
	}
	
	@Override
	public boolean cancel(CommonPlanningEntry<?> entry) {
		System.out.println("当前状态无法进行此操作！");
		checkRep();//防御式编程 防止不变量被破坏
		return false;
	}
	
	@Override
	public boolean end(CommonPlanningEntry<?> entry) {
		entry.setState(ENDED.instance);
		checkRep();//防御式编程 防止不变量被破坏
		return true;
	}
	
	@Override
	public boolean run(CommonPlanningEntry<?> entry) {
		System.out.println("当前状态无法进行此操作！");
		checkRep();//防御式编程 防止不变量被破坏
		return false;
	}
	@Override
	public String toString() {
		checkRep();//防御式编程 防止不变量被破坏
		return "已启动";
	}
	
}
