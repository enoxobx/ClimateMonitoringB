package LAB_B.Common.Interface;

import java.io.Serializable;

import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

public class Coordinate implements ICoordinate, Serializable {
    private static final long serialVersionUID = 1L;
    private final Citta citta;
    private double latitude;
    private double longitude;

    // Costruttore
    public Coordinate(Citta citta) {
        this.citta = citta;
        this.latitude = citta.getLatitide();
        this.longitude = citta.getLongitude();
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
        this.longitude = lon;
    }

    public Citta getCitta() {
        return citta;
    }

    @Override
    public String toString() {
        return citta.toString();
    }

}
