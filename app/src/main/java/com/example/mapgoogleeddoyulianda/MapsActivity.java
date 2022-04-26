package com.example.mapgoogleeddoyulianda;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity<stringReuqest> extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final float MAPS_ZOOM = 12f;
    private ArrayList<LatLng> latLngs = new ArrayList<>();
    private MarkerOptions markerOptions = new MarkerOptions();// untuk memanggil marker itu sendiri
    private JSONArray result;
    private static final String URL = "https://eddoyulianda.000webhostapp.com/addmarker.php";
    private static  final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /**
        latLngs.add(new LatLng(-0.9143092224647593, 100.46616172677408));// Politeknik Negeri Padang
        latLngs.add(new LatLng( -0.9015344247593674, 100.3633526303435)); // simpang Tinju
        latLngs.add(new LatLng(  -0.9230550295763085, 100.35055992089771));// Hotel Pangeran
        latLngs.add(new LatLng( -0.9124896773098203, 100.3573896399005)); //transmart
        latLngs.add(new LatLng( -0.900995072108605, 100.35083881755241)); //basko

        mMap.addMarker(new MarkerOptions().position(latLngs.get(0))
                .title("Politeknik Negeri Padang").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.addMarker(new MarkerOptions().position(latLngs.get(1))
                .title("Simpang Tinju").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        mMap.addMarker(new MarkerOptions().position(latLngs.get(2))
                .title("Hotel Pangeran Padang").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(latLngs.get(3))
                .title("Transmart").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.addMarker(new MarkerOptions().position(latLngs.get(4))
                .title("Basko Grand Mall").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));*/

        // Add a marker in Sydney and move the camera
        LatLng padang = new LatLng(-0.935430506009521, 100.3580347402425);
        mMap.addMarker(new MarkerOptions().position(padang).title("Kantor Gubernur SUMBAR")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(padang,MAPS_ZOOM));
        enableMapsStyle(mMap);
        enableDynamicMarker();
        enableMyLocation();
    }

    private void enableMyLocation() {
if(ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)==
        PackageManager.PERMISSION_GRANTED){
    mMap.setMyLocationEnabled(true);
        }else{
    ActivityCompat.requestPermissions(this,new
    String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            REQUEST_LOCATION_PERMISSION);
        }
    }

    private void enableDynamicMarker() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                (response) -> {
                    Log.d("JSONResult", response.toString());
                    JSONObject jobj = null;
                    try {
                        jobj = new JSONObject(response);
                        result = jobj.getJSONArray("LOCATION");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject1 = result.getJSONObject(i);
                            String lat_i = jsonObject1.getString("1");
                            String long_i = jsonObject1.getString("2");
                            String locationName = jsonObject1.getString("3");
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(lat_i),
                                            Double.parseDouble(long_i)))
                                    .title(Double.valueOf(lat_i).toString()
                                            + "," + Double.valueOf(long_i).toString())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .snippet(locationName));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.9143092224647593,100.46616172677408), 12.0f));
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MapsActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    private void enableMapsStyle(GoogleMap mMap) {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.maps_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException rne) {
            Log.e(TAG, "Style Not Found");
        }
    }
}