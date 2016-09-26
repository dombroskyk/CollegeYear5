/**
 * Compute Fibonacci numbers inefficiently.
 * Do not modify this code!
 *
 * @author James Heliotis
 */
public class Fibo {

    /**
     * Compute the nth Fibonacci number
     * @param n the ordinal value of the number to produce, starting at 0
     * @return fibonacci number n
     */
    @Memoizable
    public long fibo( int n ) {
        switch ( n ) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return fibo( n - 2 ) + fibo( n - 1 );
        }
    }

    public static void main( String[] args ) {
        Fibo f = new Fibo();
        for ( int n : new int[]{ 0, 1, 2, 3 } ) { //, 5, 10, 15, 20, 25, 30, 35, 50
            System.out.println( "Fibo(" + n + ") = " + f.fibo( n ) );
        }
    }
}
