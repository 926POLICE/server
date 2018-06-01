package ro.ubb.catalog.core.utils;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class Utilities
{
    public static Float distance(Float latitude, Float longitude, Float destination_latitude, Float destination_longitude)
    {
        return (float)sqrt(pow(latitude-destination_latitude,2)-pow(longitude-destination_longitude,2));
    }
}
