package security;


public aspect PrivilegeChecker {
	
	private Ticket currTick = null;
	
	before(Ticket newTicket) :
		set(private Ticket *) && args(newTicket){
		//System.out.println("new ticket: " + newTicket);
		//this.currTick = newTicket; 
	}
	
	pointcut ticket_level(Privilege privilege) :
		call(@Privilege * *(..)) && @annotation(privilege);
	
	Object around(Privilege privilege): ticket_level(privilege) {
		System.out.println("level: " + privilege.value());
		Object o = null;
		
		
		return o;
	}
	
}
