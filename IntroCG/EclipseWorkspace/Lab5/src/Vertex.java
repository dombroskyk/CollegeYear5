//
//  Vertex.java
//
//  Simple representation of a "Vertex".
//
//  Author:  Warren R. Carithers
//  Date:    2016/10/07 12:34:54
//
//  This class is an example of an anomaly in Java, as it
//  has data members but no member functions (other than a
//  constructor for ease of use).  It is, in essence, a
//  record/struct.
//
//  Java purists who insist that this is a heinous crime
//  should refer to the Wikipedia description of "PODS",
//  <https://en.wikipedia.org/wiki/Plain_old_data_structure>.
//
//  Not everything needs to be "smart".
//

public class Vertex {

    ///
    // canvas size information
    ///
    public float x;
    public float y;
    public float z;

    ///
    // Constructor
    //
    // @param xc x coordinate of this point
    // @param yc y coordinate of this point
    // @param zc z coordinate of this point
    ///
    public Vertex( float xc, float yc, float zc )
    {
        this.x = xc;
        this.y = yc;
        this.z = zc;
    }
}
