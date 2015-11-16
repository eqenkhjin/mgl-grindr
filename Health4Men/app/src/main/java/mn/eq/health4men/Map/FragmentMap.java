package mn.eq.health4men.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.Utils;


/**
 * Created by eq on 11/27/14.
 */
public class FragmentMap extends Fragment implements GoogleMap.OnMarkerClickListener{

    ArrayList<Marker> markerArray = new ArrayList<>();
    ArrayList<String> imageURLArray = new ArrayList<>();
    MapView mapView;
    GoogleMap map;
    LatLngBounds.Builder builder;
    View view;
    private static String TAG = "MAP : ";

    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        setMapView();

        GoogleMap.InfoWindowAdapter adapter = new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.custom_infowindow, null);

                TextView tView = (TextView)v.findViewById(R.id.markerTextView);
                tView.setText(marker.getTitle());
                TextView textView = (TextView)v.findViewById(R.id.memberDistanceTextView);

                try {
                    Location myLcation = map.getMyLocation();

                    Location loc2 = new Location("");
                    loc2.setLatitude(marker.getPosition().latitude);
                    loc2.setLongitude(marker.getPosition().longitude);


                    float distanceInMeters = myLcation.distanceTo(loc2);


                    int distance = (int)distanceInMeters;

                    if (distance/1000 > 0){
                        textView.setText(distance / 1000 + " km " + distance % 1000 + " metr away");
                    }else {
                        textView.setText(distance%1000+" metr away");
                    }

                }catch (Exception e){
                        textView.setText("Can't find your location. Please turn on your location");
                }



                RoundedImageView imageView = (RoundedImageView)v.findViewById(R.id.customImageView);

                if (imageURLArray.get(markerArray.indexOf(marker)).length() > 3){
                    Picasso.with(getActivity()).load(imageURLArray.get(markerArray.indexOf(marker))).into(imageView);
                }else {
                    imageView.setImageResource(R.drawable.placholder_member);
                }

                return v;
            }
        };

        map.setInfoWindowAdapter(adapter);

        return view;
    }

    private void setMapView() {
        try {
            MapsInitializer.initialize(getActivity());

            switch (GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(getActivity())) {
                case ConnectionResult.SUCCESS:
                    if (mapView != null) {

                        map = mapView.getMap();
                        map.getUiSettings().setMyLocationButtonEnabled(true);
                        map.setMyLocationEnabled(true);

                        builder = new LatLngBounds.Builder();

                        request();

                    }
                    break;
                case ConnectionResult.SERVICE_MISSING:

                    break;
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:

                    break;
                default:

            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mapView != null)mapView.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)mapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    public void request(){

        System.out.println(TAG + "MEMBER COUNT : "+MembersFragment.arrayList.size());

        for (int i = 0; i < MembersFragment.arrayList.size(); i++){

            UserItem userItem = MembersFragment.arrayList.get(i);


            if (userItem.getUserCoordinateX().length() > 0 && userItem.getUserCooordinateY().length() > 0){

                System.out.println(TAG + " X - "+userItem.getUserCoordinateX());


                LatLng location = new LatLng(Double.parseDouble(userItem.getUserCoordinateX()), Double.parseDouble(userItem.getUserCooordinateY()));
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

                builder.include(marker.getPosition());
                marker.setTitle(userItem.getUserName());

                markerArray.add(marker);
                imageURLArray.add(userItem.getUserImageURL());

            }
        }

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 2));
                map.setOnCameraChangeListener(null);
            }
        });

    }


    private class InfoWindowRefresher implements Callback {
        private Marker markerToRefresh;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            markerToRefresh.showInfoWindow();
        }

        @Override
        public void onError() {}
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}




