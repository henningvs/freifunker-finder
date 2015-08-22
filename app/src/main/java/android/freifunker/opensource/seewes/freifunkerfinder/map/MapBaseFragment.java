package android.freifunker.opensource.seewes.freifunkerfinder.map;

import android.app.Activity;
import android.freifunker.opensource.seewes.freifunkerfinder.components.NodeCollection;
import android.location.Location;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by Henning von See on 22.08.2015.
 */
public abstract class MapBaseFragment extends Fragment {

    public abstract void refreshMap(NodeCollection collection);
    public abstract void refreshMap(NodeCollection collection, Activity context);

    public abstract void zoomToLocation(Location location);

    public abstract void zoomIntoBounds() ;

    public abstract void setNewLocation(Location loc);

    public abstract LatLngBounds getBounds();


}