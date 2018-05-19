package ro.ubb.catalog.core.utils;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class Utilities
{
    public static Double distance(Double latitude, Double longitude, Double destination_latitude, Double destination_longitude)
    {
        return sqrt(pow(latitude-destination_latitude,2)-pow(longitude-destination_longitude,2));
    }
}
