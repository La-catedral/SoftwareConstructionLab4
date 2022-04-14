package state;

import planningEntry.CommonPlanningEntry;

public class ALLOCATED implements State{
	//immutable
	public static final ALLOCATED instance = new ALLOCATED();//防御式编程 将成员变量设置为final
	private ALLOCATED() {};//防御式编程 隐藏构造器从而使程序中仅存在一个ALLOCATED状态实例
	

	//Abstraction function:
	//	AF(instance) = the static instance of this state  
	//Representation invariant:
	//	the field must be non null,instance 必须为ALLOCATED类的变量
	//Safety from rep exposure:
	//	the field is final
	// 	没有改变成员变量的方法 仅有委托的方法

	//checkRep()
		private void checkRep()
		{
			assert instance != null;
			assert instance.getClass().equals(ALLOCATED.class):"实例的类型必须为ALLOCATED";
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
		checkRep(); //防御式编程 检查不变量 
		return "已分配资源";
	}
	
	
}
