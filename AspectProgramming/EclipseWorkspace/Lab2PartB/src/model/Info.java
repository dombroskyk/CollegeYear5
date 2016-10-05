package model;

public class Info {
	public double oldX;
	public double oldY;
	public double newX;
	public double newY;

	public Info( double newX, double newY ){
		this.newX = newX;
		this.newY = newY;
	}
	
	public Info( double oldX, double oldY, double newX, double newY ){
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
	}
}
