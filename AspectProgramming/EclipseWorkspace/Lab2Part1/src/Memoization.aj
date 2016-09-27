import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Aspect
public class Memoization {
	private HashMap<Signature, HashMap<List<Object>, Object>> functionMap = new HashMap<Signature, HashMap<List<Object>, Object>>();
	
	/**
	 * Stores the method output for any function with the given arguments, and returns the value
	 * if the function has had its output stored before. Otherwise, allows the method
	 * to proceed and stores the value for later use. Method must be annotated with
	 * Memoizable to trigger this around.
	 * @param joinPoint - Used to retrieve the method information for use in the advice
	 * @return Object - The cached or computed value returned by the called method
	 * @throws Throwable - Throws any errors that continuing method execution causes
	 */
	@Around("@annotation(Memoizable) && call(* *.*(..))")
	public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature mySig = joinPoint.getSignature();
		HashMap<List<Object>, Object> functionArgsMap = null;
		if( this.functionMap.containsKey(mySig) ){
			functionArgsMap = this.functionMap.get(mySig);
		}else{
			functionArgsMap = new HashMap<List<Object>, Object>();
			this.functionMap.put(mySig, functionArgsMap);
		}
		List<Object> myArgs = Arrays.asList(joinPoint.getArgs());
		
		Object returnObject = null;
		try{
			if( functionArgsMap.containsKey(myArgs) ){
				returnObject = functionArgsMap.get(myArgs);
			}else{
				returnObject = joinPoint.proceed();
				functionArgsMap.put(myArgs, returnObject);
			}
		}catch(Throwable throwable){
			throw throwable;
		}
		
		return returnObject;
	}
	
	/**
	 * Compile time error message for static methods annotated with Memoizable.
	 */
	@DeclareError("@annotation(Memoizable) && execution(static * *.*(..))")
	static final String staticError = "static methods may not be annotated with Memoizable";
	
	/**
	 * Compile time error message for void return type methods annotated with Memoizable.
	 */
	@DeclareError("@annotation(Memoizable) && execution(void *.*(..))")
	static final String voidError = "methods that return void may not be annotated with Memoizable";
	
	

}
