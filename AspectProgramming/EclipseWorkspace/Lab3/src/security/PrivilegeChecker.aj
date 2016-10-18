package security;


public aspect PrivilegeChecker {
	
	private Ticket currTick = null;
	
	pointcut authPrivCall() :
		@annotation(Privilege) && call( PrivClient *.*(..) );
	
	pointcut ticket_level(Privilege privilege) :
		call(@Privilege * *(..)) && @annotation(privilege);
	
	Object around(Privilege privilege): ticket_level(privilege) {
		System.out.println("level: " + privilege.value());
		Object o = null;
		
		
		return o;
	}
	
	Object around(): authPrivCall() {
		return null;
	}
	
}
