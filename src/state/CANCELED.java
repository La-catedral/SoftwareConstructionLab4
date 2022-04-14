package state;

import planningEntry.CommonPlanningEntry;

public class CANCELED implements State{
	//immutable
	public static final CANCELED instance = new CANCELED();//防御式编程 变量为final无法改变 
	private CANCELED() {};//防御式编程  隐藏构造器从而使程序中只有一个CANCELED状态实例
	

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
			assert instance.getClass().equals(CANCELED.class):"类成员变量的状态应该始终为为BLOCKED";
		}
	
	@Override
	public boolean allocate(CommonPlanningEntry<?> entry) {
		System.out.println("该任务项已取消！");
		checkRep();//防御式编程 检查不变量
		return false;
	}
	
	@Override
	public boolean block(CommonPlanningEntry<?> entry) {
		System.out.println("该任务项已取消！");
		checkRep();//防御式编程 检查不变量
		return false;
	}
	
	@Override
	public boolean cancel(CommonPlanningEntry<?> entry) {
		System.out.println("该任务项已取消！");
		checkRep();//防御式编程 检查不变量
		return false;
	}
	
	@Override
	public boolean end(CommonPlanningEntry<?> entry) {
		System.out.println("该任务项已取消！");
		checkRep();//防御式编程 检查不变量
		return false;
	}
	
	@Override
	public boolean run(CommonPlanningEntry<?> entry) {
		System.out.println("该任务项已取消！");
		checkRep();//防御式编程 检查不变量
		return false;
	}
	
	@Override
	public String toString() {
		checkRep();//防御式编程 检查不变量
		return "已取消";
	}
	
	
}
