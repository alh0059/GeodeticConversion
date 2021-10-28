package CoordinateConversion;

public class ECEF {

	double x;
	double y;
	double z;
	
	public ECEF(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void printECEF() {
		System.out.println("ecef-X = "+this.x+" M" + ": ecef-y = "+this.y +" M : ecef-Z = "+this.z +" M");
	}
	
	public void printECEFInKM() {
		System.out.println("ecef-X = "+this.x/1000+" KM" + ": ecef-y = "+this.y/1000 +" KM : ecef-Z = "+this.z/1000 +" KM");
	}
}
