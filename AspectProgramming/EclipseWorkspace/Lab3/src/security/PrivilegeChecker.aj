package security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class PrivilegeChecker {

	
	@Around( "@annotation( Privilege )" )
	public Object advice( ProceedingJoinPoint joinPoint ) throws Throwable {
		
	}
}
