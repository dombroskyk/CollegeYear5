package security;

/**
 * Performs role checking based on current Ticket values. Does not allow calls
 * to Privilege annotated methods if it is outside of a PrivClient subclass.
 * 
 * @author Kevin Dombrosky <kfd6490@rit.edu>
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * PrivilegeChecker aspect checks that the proper privileges are followed
 * for various method calls inside of a PrivClient class or subclass. Does
 * not allow for other methods to be annotated with Privilege.
 * @author Kevin Dombrosky
 */
public aspect PrivilegeChecker {
	
	private Properties myProps = null;
	private PrintWriter logWriter = null;
	private static boolean securityOverride = false;
	
	/**
	 * Pointcut to catch the execution of the main method of the program
	 */
	pointcut progMain() :
		execution(static void main(..));
	
	/**
	 * Pointcut to check if a sufficient ticket level is passed to a
	 * Privilege annotated method.
	 * @param privilege - The privilege level to check against
	 * @param privClient - The PrivClient instance containing the ticket
	 * to check against
	 */
	pointcut ticket_level(Privilege privilege, PrivClient privClient) :
		within(security.PrivClient+) && call(@Privilege * *(..)) &&
		@annotation(privilege) && this(privClient) && this(PrivClient) &&
		if(!securityOverride);
	
	/**
	 * Pointcut to catch when a Privilege annotated method is called
	 * outside a PrivClient class that would have a ticket.
	 */
	pointcut no_ticket() :
		!this(PrivClient+) && call(@Privilege * *(..)) &&
		if(!securityOverride);
	
	/**
	 * Advice to execute when the program's main method is beginning.
	 * 
	 * Loads in the protection.xml file and parses it. Opens the logWriter,
	 * and sets the security override flag.
	 */
	before() : progMain() {
		myProps = new Properties();
		File propertiesFile = new File("./protection.xml");
		
		try{
			FileInputStream propertiesStream =
					new FileInputStream(propertiesFile);
			myProps.loadFromXML(propertiesStream);
			String logFileName = myProps.getProperty("logFile",
													 "protection.log");
			logWriter = new PrintWriter(logFileName);
		} catch(IOException e){
			e.printStackTrace();
		}
		
		String tempBool = myProps.getProperty("override_security", "false");
		securityOverride = Boolean.valueOf(tempBool);
		logWriter.write("Log file started " + new Date().toString() + "\n");
	}
	
	/**
	 * Advice to execute when the program's main method is exiting.
	 * 
	 * Closes the logWriter
	 */
	after() : progMain() {
		logWriter.write("Log file closed " + new Date().toString());
		logWriter.close();
	}
	
	/**
	 * Advice for when a method annotated with Privilege is called properly.
	 * Throws a SecurityBreach and logs the violation if the call does not
	 * include a ticket or the supplied ticket does not have the appropriate
	 * privilege level. Otherwise, it will allow execution to continue.
	 * @return value returned by the called method
	 * @throws SecurityBreach - Throws a SecurityBreach exception to note
	 * that a security breach occurred in the code
	 */
	Object around(Privilege privilege, PrivClient privClient):
		ticket_level(privilege, privClient) {
		
		String caller = thisEnclosingJoinPointStaticPart.getSignature()
				.getDeclaringTypeName() + "." +
				thisEnclosingJoinPointStaticPart.getSignature().getName() +
				"(" + thisEnclosingJoinPointStaticPart.getSourceLocation() +
				")";
		String callee = thisJoinPoint.getSignature().getDeclaringTypeName() +
				"." + thisJoinPoint.getSignature().getName() + "(" +
				thisJoinPoint.getSourceLocation() + ")";
		
		if(privClient.getClientTicket() == null){
			logWriter.write(caller + " --> " + callee + "\n");
			throw new SecurityBreach(caller, callee);
		}
		
		if(privClient.getClientTicket().compareTo(privilege.value()) >= 0){
			return proceed(privilege, privClient);
		}
		
		logWriter.write(caller + "/" + privClient.getClientTicket() + " --> " +
				callee + "!" + privilege.value() + "\n");
		throw new SecurityBreach(privilege.value(),
				privClient.getClientTicket(), caller, callee);
	}
	
	/**
	 * Advice for when a method annotated with Privilege is called properly,
	 * but has no ticket associated with it. Throws a SecurityBreach and logs
	 * the violation.
	 * @return null
	 * @throws SecurityBreach - Throws a SecurityBreach exception to note
	 * that a security breach occurred in the code
	 */
	void around(): no_ticket() {
		
		String caller = thisEnclosingJoinPointStaticPart.getSignature()
				.getDeclaringTypeName() + "." +
				thisEnclosingJoinPointStaticPart.getSignature().getName() +
				"(" + thisEnclosingJoinPointStaticPart.getSourceLocation() +
				")";
		String callee = thisJoinPoint.getSignature().getDeclaringTypeName() +
				"." + thisJoinPoint.getSignature().getName() + "(" +
				thisJoinPoint.getSourceLocation() + ")";
		logWriter.write(caller + " --> " + callee + "\n");
		throw new SecurityBreach(caller, callee);
	}
	
	/**
	 * Compile time error message for calls to methods annotated with
	 * Privilege and are not inside of a class of  type PrivClient.
	 */
	declare error: !within(PrivClient+) && call(@Privilege * *(..)):
		"[error] Only PrivClient implementers may call this method.";
	
}
