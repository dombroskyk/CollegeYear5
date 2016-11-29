import aop.cruisectl.*;

/**
 * Initial sunny day test for cruise control simulator.
 * No rules of the user controls are violated.
 *
 * @author James Heliotis
 * @version 1
 */
public class Test0 {

    /**
     * Play out a simple scenario of using cruise control.
     * @param args not used
     */
    public static void main( String[] args ) {
                                                       getLineNumber();
        UserControls sys = new UserControls( new ThrottleControl() );
                                                       getLineNumber();
        sys.enable();
                                                       getLineNumber();
        sys.set();
                                                       getLineNumber();
        sys.cancel();
                                                       getLineNumber();
        sys.resume();
                                                       getLineNumber();
        sys.disable();
                                                       getLineNumber();
    }

    /**
     * Print the line number where the statement is that called this
     * function. WARNING: this may change in other versions of the JDK.
     * It was tested with version 8.
     */
    private static void getLineNumber() {
        System.out.println( "Line " +
                Thread.currentThread().getStackTrace()[ 2 ].getLineNumber() );
    }
}
