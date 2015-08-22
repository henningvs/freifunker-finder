package android.freifunker.opensource.seewes.freifunkerfinder.map.google;

/**
 * Created by Henning von See on 22.08.2015.
 */

import android.freifunker.opensource.seewes.freifunkerfinder.R;
import android.freifunker.opensource.seewes.freifunkerfinder.components.FFNode;
import android.freifunker.opensource.seewes.freifunkerfinder.components.NodeCollection;
import android.freifunker.opensource.seewes.freifunkerfinder.map.GoogleMapFragment;
import android.location.Location;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;


public class MapContent {

    private FragmentActivity act;
    private GoogleMap mapView;
    private Boolean init = false;
    private Map<String, Map<FFNode, Marker>> markerList;
    private int maxNumber = 200;
    private GoogleMapFragment googleMapFragment;
    private Boolean initialzoom = false;
    private float onLocationZoomLevel = 15.0f;


    /**
     * Initialisiert die Karte neu falls noetig, setzt anschliessend Marker und GPS Position.
     * @param act Activity in der die Karte dargestellt wird.
     * @param googleMapFragment
     * @param mapView Zu initialisierende GoogleMap.
     * @param sammlung Darzustellende NodeCollection
     */
    public void fillMap(final FragmentActivity act, GoogleMapFragment googleMapFragment, GoogleMap mapView, NodeCollection sammlung){
        if(this.googleMapFragment==null)this.googleMapFragment = googleMapFragment;
        if(!this.init)initMap(mapView, act);

        this.markerList = new HashMap<String, Map<FFNode, Marker>>();

        setMarker(sammlung);
    }

    /**
     * Initialisiert die Karte der uebergebenen GoogleMAp.
     * @param mapView Zu initialisierende GoogleMap.
     * @param act Activity in der die Karte dargestellt wird.
     */
    public void initMap(GoogleMap mapView, FragmentActivity act){
        this.act = act;
        this.mapView = mapView;

        if (this.mapView == null) {
            this.mapView = ((SupportMapFragment) googleMapFragment.getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();//((SupportMapFragment) act.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }

        mapView.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                googleMapFragment.markerSelected(null, false);
            }
        });

        init = true;
    }



    /**
     * Setzt die Marker fuer die uebergebene NodeCollection.
     * @param collection Darzustellende NodeCollection.
     */
    private void setMarker(NodeCollection collection) {

        if(collection!=null){
            if(this.markerList==null)this.markerList = new HashMap<String, Map<FFNode, Marker>>();

			/*if(this.markerList.size()>this.maxNumber){
				this.mapView.clear();
				this.markerList = new HashMap<String, Map<Box, Marker>>();
			}*/

            for(FFNode node : collection.getNodeList()) {
                initMarker(node, collection.getArea());
            }
        }
    }

    /**
     * Initilisiert den Marker fuer die einzelne Maschine inklusive Clicklistener.
     * @param node Anzuzeigende Node.
     * @param areaCode Code of the area.
     */
    private void initMarker(FFNode node, Long areaCode){

        createSingleMarker(node, areaCode);
    }

    private void createSingleMarker(FFNode node, Long AreaCode){
        if(this.markerList.containsKey(node.getUniqueId())==false){

            MarkerOptions options = new MarkerOptions();

            options.position(new LatLng(node.getLat(), node.getLon()));

            //options.icon(BitmapDescriptorFactory.fromResource(icon.getCustomItemResource()));

            options.title("DUMMY");
            options.snippet(node.getUniqueId()+"%"+AreaCode);


            mapView.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                public boolean onMarkerClick(Marker marker) {
                    if(marker.getSnippet()!=null){
                        googleMapFragment.markerSelected(marker, true);
                        return true;
                    }
                    return false;
                }
            });

            Marker marker = mapView.addMarker(options);

            Map<FFNode, Marker> boxmap = new HashMap<FFNode, Marker>();
            boxmap.put(node, marker);
            this.markerList.put(node.getUniqueId(), boxmap);
        }
    }



    /**
     * Zentriert die Darstellung des uebergebenen Standorts und animiert einen Anflug.
     * @param location Zu zentrierender Standort.
     * @param level Zoom Level.
     * @param autoZoom Activate auto zoom.
     */
    public void zoomToLocation(Location location, Float level, Boolean autoZoom) {
        if(this.initialzoom==false || autoZoom==false){

            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            if(level!=null){
                this.mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15.0f));
            }else{
                float zoom = this.mapView.getCameraPosition().zoom;
                if(zoom<onLocationZoomLevel)zoom = onLocationZoomLevel;
                this.mapView.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(ll)
                        .zoom(mapView.getCameraPosition().zoom)
                        .zoom(zoom)
                        .bearing(0)
                        .build()));
            }
        }
        this.initialzoom = true;
        //int color = this.act.getResources().getColor(R.color.farbe_grau);
        //int colortr = this.act.getResources().getColor(R.color.farbe_grau_transparent);
        //Circle circle = this.mapView.addCircle(new CircleOptions().center(ll).radius(10).strokeColor(color).fillColor(colortr));
    }


}

