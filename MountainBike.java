public class MountainBike extends Bicycle {
	private double seatHeight;
	
	public MountainBike(int gear, int speed, double startHeight) { 
        super(gear, speed);
        this.seatHeight = startHeight;
    } 
	
	public void setHeight(double newValue) {
		this.seatHeight = newValue;
	}
	
	public String toString() {
		return (super.toString() + "\nSeat height is " + seatHeight);
	}
}
