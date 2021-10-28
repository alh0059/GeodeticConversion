package CoordinateConversion;

public class GPS {
	
	double latitude;
	double longitude;
	double height;
	
	public GPS(double x, double y, double z) {
		this.latitude = x;
		this.longitude = y;
		this.height = x;
	}
	
	public void printGPS() {
		System.out.println("Latitude = "+this.latitude + " : Longitude = "+this.longitude +" : Height = "+this.height);
	}
	
	
}
