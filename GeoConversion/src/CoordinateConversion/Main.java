package CoordinateConversion;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GPSConverter gpsConverter = new GPSConverter();
		
		double lat = 70;
		double lon = 30;
		double height = 0;
		
		ECEF ecef = gpsConverter.geodeticToECEF(lat, lon, height);
		ENU enu = gpsConverter.ecefToENU(ecef.x, ecef.y, ecef.z, lat, lon, height);
		ecef.printECEFInKM();
		//System.out.println("ECEFx = "+ecef.x/1000+" KM" + ", ECEFy = "+ecef.y/1000 +" KM , ECEFz = "+ecef.z/1000 +" KM");
	}

}
