package CoordinateConversion;

// Some helpers for converting GPS readings from the WGS84 geodetic system to a local North-East-Up cartesian axis.

// The implementation here is according to the paper: "Conversion of Geodetic coordinates to the Local Tangent Plane" Version 2.01.
// The basic reference for this paper is J.Farrell & M.Barth 'The Global Positioning System & Inertial Navigation'
// Also helpful is Wikipedia: http://en.wikipedia.org/wiki/Geodetic_datum
// Also helpful are the guidance notes here: http://www.epsg.org/Guidancenotes.aspx

public class GPSConverter {
   
	// WGS-84 Geodetic constants
    final static double a = 6378137.0;          // WGS-84 Earth semimajor axis (m)

    final static double b = 6356752.314245;     // Derived Earth semiminor axis (m)
    static double f = (a - b) / a;        		// Ellipsoid Flatness
    double f_inv = 1.0 / f;       				// Inverse flattening

    //const double f_inv = 298.257223563; // WGS-84 Flattening Factor of the Earth 
    //const double b = a - a / f_inv;
    //const double f = 1.0 / f_inv;

    double a_sq = a * a;
    double b_sq = b * b;
    final static double e_sq = f * (2 - f);    // Square of Eccentricity

    // Converts WGS-84 Geodetic point (lat, lon, h) to the 
    // Earth-Centered Earth-Fixed (ECEF) coordinates (x, y, z).
    public ECEF geodeticToECEF(double lat, double lon, double h) {
    	
    	double lambda = DegreesToRadians(lat);
    	double phi = DegreesToRadians(lon);
    	
    	double s = Math.sin(lambda);
    	double N = a / Math.sqrt(1 - e_sq * s * s);

    	double sin_lambda = Math.sin(lambda);
    	double cos_lambda = Math.cos(lambda);
    	double cos_phi = Math.cos(phi);
    	double sin_phi = Math.sin(phi);
    	
        double x = (h + N) * cos_lambda * cos_phi;
        double y = (h + N) * cos_lambda * sin_phi;
        double z = (h + (1 - e_sq) * N) * sin_lambda;
        
        ECEF ecef = new ECEF(x,y,z);
        return ecef;
    }
    
    

    // Converts the Earth-Centered Earth-Fixed (ECEF) coordinates (x, y, z) to 
    // (WGS-84) Geodetic point (lat, lon, h).
    public GPS ecefToGeodetic(double x, double y, double z) {
    	double eps = e_sq / (1.0 - e_sq);
    	double p = Math.sqrt(x * x + y * y);
    	double q = Math.atan2((z * a), (p * b));
    	double sin_q = Math.sin(q);
    	double cos_q = Math.cos(q);
    	double sin_q_3 = sin_q * sin_q * sin_q;
    	double cos_q_3 = cos_q * cos_q * cos_q;
    	double phi = Math.atan2((z + eps * b * sin_q_3), (p - e_sq * a * cos_q_3));
    	double lambda = Math.atan2(y, x);
    	double v = a / Math.sqrt(1.0 - e_sq * Math.sin(phi) * Math.sin(phi));
        
    	double h = (p / Math.cos(phi)) - v;
        double lat = RadiansToDegrees(phi);
        double lon = RadiansToDegrees(lambda);
        
        GPS gps = new GPS(lat,lon,h);
        return gps;
    }

    // Converts the Earth-Centered Earth-Fixed (ECEF) coordinates (x, y, z) to 
    // East-North-Up coordinates in a Local Tangent Plane that is centered at the 
    // (WGS-84) Geodetic point (lat0, lon0, h0).
    public ENU ecefToENU(double x, double y, double z, double lat0, double lon0, double h0) {
        // Convert to radians in notation consistent with the paper:
        double lambda = DegreesToRadians(lat0);
        double phi = DegreesToRadians(lon0);
        double s = Math.sin(lambda);
        double N = a / Math.sqrt(1 - e_sq * s * s);

        double sin_lambda = Math.sin(lambda);
        double cos_lambda = Math.cos(lambda);
        double cos_phi = Math.cos(phi);
        double sin_phi = Math.sin(phi);

        double x0 = (h0 + N) * cos_lambda * cos_phi;
        double y0 = (h0 + N) * cos_lambda * sin_phi;
        double z0 = (h0 + (1 - e_sq) * N) * sin_lambda;

        double xd, yd, zd;
        xd = x - x0;
        yd = y - y0;
        zd = z - z0;

        // This is the matrix multiplication
        double xEast = -sin_phi * xd + cos_phi * yd;
        double yNorth = -cos_phi * sin_lambda * xd - sin_lambda * sin_phi * yd + cos_lambda * zd;
        double zUp = cos_lambda * cos_phi * xd + cos_lambda * sin_phi * yd + sin_lambda * zd;
        
        ENU enu = new ENU(xEast, yNorth, zUp);
        return enu;
        
    }

    // Inverse of EcefToEnu. Converts East-North-Up coordinates (xEast, yNorth, zUp) in a
    // Local Tangent Plane that is centered at the (WGS-84) Geodetic point (lat0, lon0, h0)
    // to the Earth-Centered Earth-Fixed (ECEF) coordinates (x, y, z).
    public static ECEF enuToECEF(double xEast, double yNorth, double zUp, double lat0, double lon0, double h0) {
        // Convert to radians in notation consistent with the paper:
    	double lambda = DegreesToRadians(lat0);
        double phi = DegreesToRadians(lon0);
        double s = Math.sin(lambda);
        double N = a / Math.sqrt(1 - e_sq * s * s);

        double sin_lambda = Math.sin(lambda);
        double cos_lambda = Math.cos(lambda);
        double cos_phi = Math.cos(phi);
        double sin_phi = Math.sin(phi);

        double x0 = (h0 + N) * cos_lambda * cos_phi;
        double y0 = (h0 + N) * cos_lambda * sin_phi;
        double z0 = (h0 + (1 - e_sq) * N) * sin_lambda;

        double xd = -sin_phi * xEast - cos_phi * sin_lambda * yNorth + cos_lambda * cos_phi * zUp;
        double yd = cos_phi * xEast - sin_lambda * sin_phi * yNorth + cos_lambda * sin_phi * zUp;
        double zd = cos_lambda * yNorth + sin_lambda * zUp;

        double x = xd + x0;
        double y = yd + y0;
        double z = zd + z0;
        
        ECEF ecef = new ECEF(x, y, z);
        return ecef;
    }

    static boolean AreClose(double x0, double x1)
    {
        var d = x1 - x0;
        return (d * d) < 0.1;
    }


    static double DegreesToRadians(double degrees)
    {
        return (Math.PI / 180.0) * degrees;
    }

    static double RadiansToDegrees(double radians)
    {
        return (180.0 / Math.PI) * radians;
    }
}
