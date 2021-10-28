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
		System.out.println("Lat = "+this.latitude + " : longitude = "+this.longitude +" : zUp = "+this.height);
	}
	
	
}
