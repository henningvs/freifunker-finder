package android.freifunker.opensource.seewes.freifunkerfinder.components;

/**
 * Created by Henning von See on 22.08.2015.
 */
public class FFNode {

    private String uniqueId;
    private double lat;
    private double lon;


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public double getLat() {
        return this.lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLon() {
        return this.lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLon(String lon) {
        this.setLon(Double.valueOf(lon));
    }

    public void setLat(String lat) {
        this.setLat(Double.valueOf(lat));
    }
}
