/**
 * Memoizes previous function calls so they can easily be recalled and speed
 * execution time.
 * 
 * @author Kevin Dombrosky <kfd6490@rit.edu>
 */
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Memoization aspect stores the output of previous method calls for any method
 * annotated with Memoization. It will generate compile time errors for any
 * static typed or void return typed methods.
 * @author Kevin Dombrosky
 */
@Aspect
public class Memoization {
	private HashMap<Object, HashMap<Signature, HashMap<List<Object>, Object>>>
		targetSigMap = 
			new HashMap<Object,
				HashMap<Signature, HashMap<List<Object>, Object>>>();
	
	/**
	 * Stores the method output for any function with the given arguments, and 
	 * returns the value if the function has had its output stored before.
	 * Otherwise, allows the method to proceed and stores the value for later
	 * use. Method must be annotated with Memoizable to trigger this around.
	 * @param joinPoint - Used to retrieve the method information for use in
	 * the advice
	 * @return Object - The cached or computed value returned by the called
	 * method
	 * @throws Throwable - Throws any errors that continuing method execution
	 * causes
	 */
	@Around( "@annotation( Memoizable ) && call( * *.*(..) )" )
	public Object advice( ProceedingJoinPoint joinPoint ) throws Throwable {
		// Map target to signature
		Object target = joinPoint.getTarget();
		HashMap<Signature, HashMap<List<Object>, Object>> sigArgsMap = null;
		if( this.targetSigMap.containsKey( target ) ){
			sigArgsMap = this.targetSigMap.get( target );
		} else {
			sigArgsMap = 
					new HashMap<Signature, HashMap<List<Object>, Object>>();
			this.targetSigMap.put( target, sigArgsMap );
		}
		
		// Map signature to arguments
		Signature signature = joinPoint.getSignature();
		HashMap<List<Object>, Object> argsValMap = null;
		if( sigArgsMap.containsKey(signature) ){
			argsValMap = sigArgsMap.get( signature );
		} else {
			argsValMap = new HashMap<List<Object>, Object>();
			sigArgsMap.put( signature, argsValMap );
		}
		
		// Map arguments to output
		List<Object> fnArgs = Arrays.asList( joinPoint.getArgs() );
		Object returnObject = null;
		try{
			if( argsValMap.containsKey( fnArgs ) ){
				returnObject = argsValMap.get( fnArgs );
			} else {
				// Proceed with function call since we don't have the value yet
				returnObject = joinPoint.proceed();
				argsValMap.put( fnArgs, returnObject );
			}
		} catch( Throwable throwable ) {
			throw throwable;
		}
		
		return returnObject;
	}
	
	/**
	 * Compile time error message for static methods annotated with 
	 * Memoizable.
	 */
	@DeclareError(
			"@annotation( Memoizable ) && execution( static * *.*(..) )" )
	static final String staticError =
		"static methods may not be annotated with Memoizable";
	
	/**
	 * Compile time error message for void return type methods annotated 
	 * with Memoizable.
	 */
	@DeclareError(
			"@annotation( Memoizable ) && execution( void *.*(..) )" )
	static final String voidError =
		"methods that return void may not be annotated with Memoizable";
	
	

}
