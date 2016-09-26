public class Tester {
    public static void main( String[] args ) {
        Pluribus p1 = new Pluribus();
        Pluribus p2 = new Pluribus();
        Pluribus p3 = new Pluribus();
        Unum u1 = new Unum();
        Unum u2 = new Unum();
        Unum u3 = new Unum();
        if ( p1 != p2 && p2 != p3 && p1 != p3 ) {
            System.out.println( "All my Pluribi are different." );
        }
        else if ( p1 == p2 && p2 == p3 ) {
            System.out.println("OMG! All my Pluribi are the same!" );
        }
        else {
            System.out.println("Eeew. Some of my Pluribi are the same." );
        }
        if ( u1 != u2 && u2 != u3 && u1 != u3 ) {
            System.out.println( "All my Una are different." );
        }
        else if ( u1 == u2 && u2 == u3 ) {
            System.out.println("OMG! All my Una are the same!" );
        }
        else {
            System.out.println("Eeew. Some of my Una are the same." );
        }
    }
}
