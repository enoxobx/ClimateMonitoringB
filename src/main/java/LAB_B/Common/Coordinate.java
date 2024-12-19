package LAB_B.Common;

import java.io.Serializable;

import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

public class Coordinate implements ICoordinate,Serializable{
    private static final long serialVersionUID = 1L;
    private final String name;
    private double latitude;
    private double longitude;

    // Costruttore
    public Coordinate(double latitude, double longitude) {
        this.name = "None";
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate(String name,double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public double getLat() {
        return latitude;
    }

    @Override
    public void setLat(double lat) {
        this.latitude = lat;
    }

    @Override
    public double getLon() {
        return this.longitude;
    }

    @Override
    public void setLon(double lon) {
        this.longitude=lon;
    }
    public String getName(){
        return this.name;
    }
}
