import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import model.DrawingApp;
import observing.Observer;

@Aspect
public class MVConnector {

	@Around( "execution( DrawingApp.new(..) ) && this(o)" )
	public Object subscribePointcut( ProceedingJoinPoint myJoinPoint, Object o ) {
		System.out.println(o);
	
		DrawingApp model;
		try {
			model = (DrawingApp) myJoinPoint.proceed();
			model.subscribe( (Observer) o );
			
			return model;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
