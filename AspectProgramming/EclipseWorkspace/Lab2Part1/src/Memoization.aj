import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import java.util.Arrays;
import java.util.HashMap;

import org.aspectj.lang.JoinPoint;

@Aspect
public class Memoization {
	private HashMap<Signature, Object[]> functionMap = new HashMap<Signature, Object[]>();
	private HashMap<Object[], Object> argsMap = new HashMap<Object[], Object>();
	
	@Around("@annotation(Memoizable) && execution(* *(..))")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
		//System.out.println(joinPoint.toString());
		Signature mySig = joinPoint.getSignature();
		Object[] myArgs = joinPoint.getArgs();
		
		Object returnObject = null;
		
		try{
			
			if( this.argsMap.containsKey(myArgs) ){
				System.out.println("in");
				returnObject = this.argsMap.get(myArgs);
			}else{
				returnObject = joinPoint.proceed();
				this.argsMap.put(myArgs, returnObject);
				
				//System.out.println(Arrays.toString(myArgs));
				//System.out.println(this.argsMap.get(myArgs).toString());
				for( Object[] k: this.argsMap.keySet() ){
					System.out.println("Start out");
					System.out.println(Arrays.toString(k) + ": " + this.argsMap.get(k).toString());
				}
			}
			
		}catch(Throwable throwable){
			throw throwable;
		}
		
		return returnObject;
	}

}
