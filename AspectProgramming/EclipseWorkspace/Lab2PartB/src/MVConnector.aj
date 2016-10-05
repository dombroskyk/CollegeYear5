import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MVConnector {

	@After( "execution( DrawingApp.new(..) )" )
	public void subscribePointcut(){
		
	}
	
	
}
