// Author: Kevin Dombrosky
public aspect Singleton {
	private Unum my_unum;

	pointcut singleton_unum():
		call( public Unum.new(..) );
		
	Unum around(): singleton_unum() {
		if(my_unum == null){
			my_unum = proceed();
		}
		return my_unum;
	}
}
