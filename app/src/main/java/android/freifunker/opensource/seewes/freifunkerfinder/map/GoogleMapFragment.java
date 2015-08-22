package android.freifunker.opensource.seewes.freifunkerfinder.map;

import android.app.Activity;
import android.content.Context;
import android.freifunker.opensource.seewes.freifunkerfinder.MainActivity;
import android.freifunker.opensource.seewes.freifunkerfinder.R;
import android.freifunker.opensource.seewes.freifunkerfinder.components.NodeCollection;
import android.freifunker.opensource.seewes.freifunkerfinder.map.google.MapContent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Henning von See on 22.08.2015.
 */
public class GoogleMapFragment extends MapBaseFragment{

    private MainActivity act;
    private GoogleMap map;
    private GoogleMapFragment frag;
    private int maxzoom = 13;

    private OnMarkerSelectedListener mCallback;
    private Context mContext;
    private boolean init = false;
    private Location currentlocation;
    private Boolean locationButtonActivated;
    private boolean mapObjectInitializer=false;
    private static View view;
    private NodeCollection nodes;
    private MapContent mapcontent;

    public GoogleMapFragment(){

    }

    public GoogleMapFragment(Context context){
        this.mContext = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if(init==false)initMap();
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51, 11), 15.0f));

        //BoxCollection collection = Singleton_boxColl.getInstance().getCollection();
        if(this.act==null)this.act = (MainActivity)getActivity();

        if(mapObjectInitializer==false)setObjectActualizer(map);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_map_google, container, false);
        } catch (InflateException e) {
	        /* map is already there, just return view as it is */
        }

        RelativeLayout relLay = (RelativeLayout) inflater.inflate(R.layout.fragment_map_google_container, null);
        LinearLayout mapCont = (LinearLayout) relLay.findViewById(R.id.main_gui_layout_map_container);
        mapCont.addView(view);

        return relLay;
    }


    private void setObjectActualizer(final GoogleMap map) {

        if(map!=null){
            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition position) {

                }
            });

            mapObjectInitializer = true;
        }

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.act = (MainActivity) activity;

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMarkerSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMarkerSelectedListener");
        }

        //super.onActivityCreated(getArguments());
    }

    // Container Activity must implement this interface
    public interface OnMarkerSelectedListener {
        public void onMarkerSelected(Marker marker, Boolean selected);
    }

    public void markerSelected(Marker marker, Boolean selected){
        mCallback.onMarkerSelected(marker, selected);
    }

    /**
     * Uebergibt die Daten an die das Fragment
     * @param collection Collection der Nodes
     * @param context Kontext des Fragments
     * @return Neues GoogleMapFragment.
     */
    public GoogleMapFragment newInstance(NodeCollection collection, Context context){
        this.frag = new GoogleMapFragment(context);

        Bundle args = frag.getArguments();
        if(args==null){args = new Bundle();}
        SingletonNodeCollection.getInstance().setCollection(collection);

        return frag;
    }


    /**
     * Aktualisiert die Kartenansicht.
     * @param collection Darzustellende NodeCollection.
     */
    @Override
    public void refreshMap(NodeCollection collection, Activity context){
        //long time = System.currentTimeMillis();

        if(frag==null) newInstance(collection, context);
        Bundle args = frag.getArguments();
        if(args==null){args = new Bundle();}
        SingletonNodeCollection.getInstance().setCollection(collection);

        drawMap(collection, this.map);

        //long duration = System.currentTimeMillis()-time;
        //System.out.println("Time for Map Drawing: " + duration + " ms");
    }

    @Override
    public void refreshMap(NodeCollection collection) {

        if(init==false)initMap();
        if(mapObjectInitializer==false)setObjectActualizer(map);

        this.nodes = nodes;
        drawMap(SingletonNodeCollection.getInstance().getCollection(), this.map);
    }

    /**
     * Zeichnet die Karte fuer die Uebergebene BoxCollection, auf der angegebenen GoogleMap, im angegebenen Zeitraum.
     * @param collection Collection to be shown.
     * @param map Zur verwendende Karte.
     */
    private void drawMap(NodeCollection collection, GoogleMap map) {
        SingletonNodeCollection.getInstance().setCollection(collection);
        if(this.mapcontent==null)this.mapcontent = new MapContent();
        this.mapcontent.fillMap((FragmentActivity)act, this, map, collection);
        if(currentlocation!=null){
            this.mapcontent.zoomToLocation(currentlocation, 15.0f, true);
        }
    }

    private void initMap(){
        if(this.map!=null){
            UiSettings settings = this.map.getUiSettings();
            settings.setAllGesturesEnabled(true);
            settings.setMyLocationButtonEnabled(true);
            settings.setZoomControlsEnabled(false);
            settings.setMyLocationButtonEnabled(false);
            settings.setCompassEnabled(false);

            this.map.setIndoorEnabled(true);
            this.map.setMyLocationEnabled(true);
            this.map.setBuildingsEnabled(true);
            //DeviceDisplay.getPixelsFromDp(this.act, 65)
            this.map.setPadding(0, 0, 0, 0);

            init = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(map!=null)map.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        /* Disable the my-location layer (this causes our LocationSource to be automatically deactivated.) */
        map.setMyLocationEnabled(false);
        super.onPause();
    }

    @Override
    public void setNewLocation(Location location) {
        if(this.mapcontent!=null){
            this.mapcontent.zoomToLocation(location, 15.0f, true);
        }
        else{
            this.currentlocation = location;
        }
    }


    @Override
    public void zoomToLocation(Location location){
        mapcontent.zoomToLocation(location, null, false);
    }

    public void zoomIntoBounds(){
        map.animateCamera( CameraUpdateFactory.zoomTo(maxzoom) );
    }

    @Override
    public LatLngBounds getBounds() {
        if(map!=null && map.getCameraPosition().zoom>maxzoom)return map.getProjection().getVisibleRegion().latLngBounds;
        return null;
    }

}
