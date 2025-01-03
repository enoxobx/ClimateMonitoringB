package LAB_B.Common.Interface;

public class Citta {
    private final String geoname_id;
    private final String name;
    private final String ascii_name;
    private final String country_code;
    private final String country_name;
    private final double longitude;
    private final double latitude;

    public Citta(String geoname, String name, String ascii_name,String country_code,String country_name, double longitude,double latitude ){
        this.geoname_id=geoname;
        this.name=name;
        this.ascii_name=ascii_name;
        this.country_code = country_code;
        this.country_name = country_name;
        this.longitude=longitude;
        this.latitude=latitude;
    }
    public String getGeoname(){
        return geoname_id;
    }
    public String getName(){
        return name;
    }
    public String getAscii_name(){
        return ascii_name;
    }
    public String getCountry_code(){
        return country_code;
    }
    public String getCountry_name(){
        return country_name;
    }
    public double getLongitude(){
        return longitude;
    }
    public double getLatitide(){
        return latitude;
    }

    @Override
    public String toString(){
        return ascii_name+", "+country_code+", "+country_name;
    }
    
}
