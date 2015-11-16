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

import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.Utils;


/**
 * Created by eq on 11/27/14.
 */
public class FragmentMap extends Fragment implements GoogleMap.OnMarkerClickListener{

    ArrayList<Marker> markerArray;
    ArrayList<String> imageURLArray;
    boolean[] array;
    MapView mapView;
    GoogleMap map;
    LatLngBounds.Builder builder;
    int wardID;
    View view;
    ArrayList<LatLng> locations = new ArrayList<LatLng>();
    Bundle bundle;
    LocationListener locationListener;
    public FragmentMap(){
    }

    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);
        bundle = savedInstanceState;
        builder = new LatLngBounds.Builder();
        SharedPreferences preferences = this.getActivity().getSharedPreferences("MY_DEFAULTS", Context.MODE_PRIVATE);
        wardID = preferences.getInt("WARD",0);
        MapsInitializer.initialize(getActivity());
        boolean isFirstTime = true;
//        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) )
//        {
//            case ConnectionResult.SUCCESS:
//                mapView = (MapView) view.findViewById(R.id.map);
//                mapView.onCreate(bundle);
//                if(mapView!=null)
//                {
//                    map = mapView.getMap();
//                    map.getUiSettings().setMyLocationButtonEnabled(false);
//                    map.setMyLocationEnabled(true);
//                    GoogleMap.InfoWindowAdapter adapter = new GoogleMap.InfoWindowAdapter() {
//                        @Override
//                        public View getInfoWindow(Marker marker) {
//                            return null;
//                        }
//
//                        @Override
//                        public View getInfoContents(final Marker marker) {
//                            View v = getActivity().getLayoutInflater().inflate(R.layout.custom_infowindow,null);
//
//                            TextView tView = (TextView)v.findViewById(R.id.markerTextView);
//                            tView.setText(marker.getTitle());
//
//                            final ImageView imageView = (ImageView)v.findViewById(R.id.customImageView);
//                            //Picasso.with(getActivity()).load(imageURLArray.get(markerArray.indexOf(marker))).into(imageView);
//
//                            Picasso.with(getActivity()).load(imageURLArray.get(markerArray.indexOf(marker))).into(imageView);
//
////                            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
////
////                            imageLoader.get(imageURLArray.get(markerArray.indexOf(marker)), new ImageLoader.ImageListener() {
////
////                                @Override
////                                public void onErrorResponse(VolleyError error) {
////                                }
////
////                                @Override
////                                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
////                                    if (response.getBitmap() != null) {
////                                        // load image into imageview
////                                        imageView.setImageBitmap(response.getBitmap());
////                                    }
////                                }
////                            });
//
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (!array[markerArray.indexOf(marker)]){
//                                        array[markerArray.indexOf(marker)] = !array[markerArray.indexOf(marker)];
//                                        marker.showInfoWindow();
//                                    }
//                                }
//                            }, 200);
//                            return v;
//                        }
//                    };
//
//                    map.setInfoWindowAdapter(adapter);
//                }
//                break;
//            case ConnectionResult.SERVICE_MISSING:
//                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
//                break;
//            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
//                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
//                break;
            //default: Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
//        }
        request();
        return view;
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

        for (int i = 0; i < MembersFragment.arrayList.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();

            UserItem userItem = MembersFragment.arrayList.get(i);

            if (userItem.getUserCoordinateX().length() > 0 && userItem.getUserCooordinateY().length() > 0){
                markerOptions.position(new LatLng(Double.parseDouble(userItem.getUserCoordinateX()), Double.parseDouble(userItem.getUserCooordinateY())));

                System.out.println("X : " + userItem.getUserCoordinateX());
                System.out.println("Y : "+userItem.getUserCooordinateY());

            }

            locations.add(markerOptions.getPosition());
        }
        //createMap();

    }

    public void requestCoordinate(String jsonSTR) {
        System.out.println("MAP RESPONSE : "+jsonSTR);
        try {
            if (jsonSTR != null){
                JSONObject jsonResponse = new JSONObject(jsonSTR);
                JSONArray mainArray = jsonResponse.optJSONArray("array");

                for (int i = 0; i < mainArray.length(); i ++){
                    JSONObject obj = mainArray.optJSONObject(i);
                    if (obj.optString("locationX").length() > 0 && obj.optString("locationY").length() > 0){
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(Double.parseDouble(obj.optString("locationX").replaceAll(" ","")), Double.parseDouble(obj.optString("locationY").replaceAll(" ",""))));
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_apartment));
                        Marker marker = map.addMarker(markerOptions);
                        marker.setTitle(obj.optString("apartmentName")+"\n"+obj.optString("locationDescription"));
                        markerArray.add(marker);
                        imageURLArray.add(obj.optString("image_url"));
                        builder.include(markerOptions.getPosition());
                    }
                }
                if (markerArray != null)array = new boolean[markerArray.size()];
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createMap(){
        markerArray = new ArrayList<Marker>();
        imageURLArray = new ArrayList<String>();
        if (locations.size() > 0){
            PolygonOptions border = new PolygonOptions();
            for (LatLng item : locations){

                if (item != null)
                if (item.latitude > 0 && item.longitude > 0){
                    System.out.println("LAT : "+item.latitude);
                    System.out.println("LONG : "+item.longitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(item.latitude, item.longitude));
                    border.add(markerOptions.getPosition());
                    builder.include(markerOptions.getPosition());
                }
            }

            map.setMyLocationEnabled(true);
            border.fillColor(0x7F00FF00);
            border.strokeWidth(1.0f);
            border.strokeColor(Color.TRANSPARENT);
            final Polygon polygon = map.addPolygon(border);

            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 3));
                    map.setOnCameraChangeListener(null);
                }
            });
        }
    }

    public void setImageFromURL(final ImageView imageView, final String url) {
//        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//
//        imageLoader.get(url, new ImageLoader.ImageListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("ERROR IMAGE TAVIHAD");
//            }
//
//            @Override
//            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
//                // load image into imageview
//                if (response.getBitmap() != null) {
//                    System.out.println("URL : "+url);
//                    imageView.setImageBitmap(response.getBitmap());
//                    imageView.invalidate();
//                    imageView.postInvalidate();
//                    imageView.requestLayout();
//                    imageView.forceLayout();
//                }
//            }
//        });
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




