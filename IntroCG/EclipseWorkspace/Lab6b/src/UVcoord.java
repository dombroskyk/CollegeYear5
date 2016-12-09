//
//  UVcoord.java
//
//  Simple representation of a texture coordinate pair.
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

public class UVcoord {

    ///
    // coordinates
    ///
    public float u;
    public float v;

    ///
    // Constructor
    //
    // @param uc u component of this normal
    // @param vc v component of this normal
    ///
    public UVcoord( float uc, float vc )
    {
        this.u = uc;
        this.v = vc;
    }
}
