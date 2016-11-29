import aop.cruisectl.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test harness for cruise control system
 *
 * @author James Heliotis
 * @version 1
 */
public class Test1 {

    private final static String ENABLE = "enable";
    private final static String SET = "set";
    private final static String RESUME = "resume";
    private final static String CANCEL = "cancel";
    private final static String DISABLE = "disable";

    /**
     * Command sequences are set up within the main function.
     * @param args not used
     */
    public static void main( String[] args ) {
        final ThrottleControl throttleControl = new ThrottleControl();
        final Wrapper< UserControls > sysBox = new Wrapper<>();

        // A mapping of control names to system commands
        //
        final Map< String, Command > ops =
                new HashMap< String, Command >() {{
                    put( ENABLE, () -> sysBox.object.enable() );
                    put( SET, () -> sysBox.object.set() );
                    put( RESUME, () -> sysBox.object.resume() );
                    put( CANCEL, () -> sysBox.object.cancel() );
                    put( DISABLE, () -> sysBox.object.disable() );
                }};

        // A sequence of tests. Each test has a name and sequence of actions.
        //
        final Map< String, String[] > tests =
                new LinkedHashMap< String, String[] >() {{
                    put( "Normal Operation",
                         new String[]{ ENABLE, SET, CANCEL, RESUME, DISABLE } );
                    put( "Set Without Enable",
                         new String[]{ SET } );
                    put( "Disable Before Enabling",
                            new String[]{ DISABLE }
                    );
        }};

        tests.forEach(
                ( testName, actions ) -> {
                    System.out.println( "\n" + testName + " Test" );
                    sysBox.object = new UserControls( throttleControl );
                    Arrays.stream( actions )
                          .forEach( action -> {
                                        System.out.printf( "*%s*\n", action );
                                        ops.get( action ).execute();
                                    }
                          );
                }
        );
    }
}
