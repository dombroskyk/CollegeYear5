package model;

import java.util.HashSet;

import observing.AbstractSubject;
import model.Info;

public class DrawingApp extends AbstractSubject {

	public HashSet<String> coords = new HashSet<String>();
	
	public DrawingApp(){
		super.setSelf( this );
	}
	
	public void newRectangle( double x, double y ){
		String string_rep = Double.toString(x) + "," + Double.toString(y);
		coords.add(string_rep);
		
		Info passInfo = new Info( x, y );
		super.reportChange( passInfo );
	}
	
	public void moveRectangle( double sourceX, double sourceY, double x, double y ){
		String old_string = Double.toString( sourceX ) + "," + Double.toString( sourceY );
		String new_string = Double.toString( x ) + "," + Double.toString(y); 
		coords.remove( old_string );
		coords.add( new_string );
		
		Info passInfo = new Info( sourceX, sourceY, x, y );
		super.reportChange( passInfo );
	}
}
