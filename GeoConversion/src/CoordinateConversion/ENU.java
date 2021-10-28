package CoordinateConversion;

public class ENU {
	
	double xEast; 
	double yNorth;
	double zUp;
	
	public ENU(double x, double y, double z) {
		this.xEast = x;
		this.yNorth = y;
		this.zUp = z;
	}
	
	public void printENU() {
		System.out.println("xEast = "+this.xEast+" M" + ", yNorth = "+this.yNorth +" M : zUp = "+this.zUp +" M");
	}
	
	public void printENUInKM() {
		System.out.println("xEast = "+this.xEast/1000+" KM" + ", yNorth = "+this.yNorth/1000 +" KM : zUp = "+this.zUp/1000 +" KM");
	}
}
