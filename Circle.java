public class Circle {
	private double radius;
	
	Circle(double radius) {
		this.radius = radius;
	}
	
	double getRadius() {
		return radius;
	}
	
	void setRadius(double radius) {
		if(radius < 0)
			this.radius = 0;
		else
			this.radius = radius;
	}
	
	double area() {
		return Math.PI * this.radius * this.radius;
	}
	
	double circumference() {
		return 2 * Math.PI * this.radius;
	}
	
	public static void main(String[] args) {
		Circle circle = new Circle(2.0);
		
		System.out.println("The circle's area is " + circle.area());
		System.out.println("The circle's circumference is " + circle.circumference());
	}
}
