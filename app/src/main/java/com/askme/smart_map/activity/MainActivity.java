package com.askme.smart_map.activity;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.askme.mapmyday.R;
import com.askme.smart_map.activity.AddToFavourite.AddToFavourite;
import com.askme.smart_map.activity.DatabaseBuilder.DBadapter;
import com.askme.smart_map.activity.HomePageBuilder.MyMenuFragment;
import com.askme.smart_map.activity.HomePageBuilder.Nearby_Place_Name_Adapter;
import com.askme.smart_map.activity.HomePageBuilder.Nearby_Place_Name_Info;
import com.askme.smart_map.activity.ShowingMarker.GetMyLocation;
import com.askme.smart_map.activity.ShowingMarker.GooglePlacesReadTask;
import com.askme.smart_map.activity.util.MapUtilHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements Nearby_Place_Name_Adapter.ClickListener,SensorEventListener {

    MapUtilHandler mapTool;
    private float currentDegree = 0f;
    private SensorManager sensorManager;

    com.mxn.soul.flowingdrawer_core.LeftDrawerLayout map_layout;
    private Nearby_Place_Name_Adapter ca;
    FloatingActionButton floatingActionButton;
    FloatingActionButton nearby;
    FloatingActionButton search_place;
    int height, width;
    RecyclerView recList;
    Point size;

    FlowingView mFlowingView;
    FragmentManager fm;
    MyMenuFragment mMenuFragment;
    private RecyclerView rvFeed;
    LeftDrawerLayout mLeftDrawerLayout;


    SupportMapFragment fragment;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 999;
    private static final long RIPPLE_DURATION = 250;
    //used googles Web api key
    private static final String GOOGLE_API_KEY = "AIzaSyAnujWrP43wSm5jF0G944g5oP3uavhxBVA";
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN};
    Animation slide_up, slide_down, move, move_in;
    RelativeLayout popUpLayout;
    RelativeLayout markerClickLayout;
    LatLng pos;
    Marker showingMarker;
    ImageButton showRoute;
    ImageButton addToFav;
    ImageButton openStreetView;
    Button dialog_done;

    Circle circle;

    com.askme.smart_map.widget.CanaroTextView markerTitle;
    GoogleMap googleMap;
    Polyline markerPolyline;
    double latitude = 0;
    double longitude = 0;
    Location lastKnownLocation;
    String[] place_name = {"Airport", "ATM", "Bank", "Bus", "Church", "Ferry", "Food", "Fuel", "Hospital", "Market",
            "Mosque", "Movie", "Park", "Police", "Mail", "School", "University"};
    Context context;


    Boolean isShowing = true;
    int x = 1;
    GetMyLocation myLocation;
    private int PROXIMITY_RADIUS = 10000;
    private ImageButton menuButton;
    private TextView textView;

    DBadapter dBadapter;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitializations();
        setValues();
        initMap();
        setClickListeners();
    }

    private void initMap() {
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(MAP_TYPES[1]);
        googleMap.setTrafficEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        LatLng location=new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
        mapTool.animateCamera(location,12,16);
        mapTool.circleAnimation(location,Color.BLUE,500,1500);
    }

    private void setValues() {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        height = map_layout.getHeight();
        width = map_layout.getWidth();
        context = MainActivity.this;


        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();
        }


        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment(mMenuFragment);
        mMenuFragment.leftDrawerLayout = mLeftDrawerLayout;

        googleMap = fragment.getMap();
        mapTool=new MapUtilHandler(context,googleMap);

        myLocation = new GetMyLocation(getApplicationContext());
        if (myLocation.canGetLocation()) {
            lastKnownLocation = myLocation.getLocationValue();
        } else {
            showSettingsAlert();

        }


        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recList.setLayoutManager(llm);


        ca = new Nearby_Place_Name_Adapter(this, createlist(17));
        ca.setClickListener(this);
        recList.setAdapter(ca);


        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        recList.animate().setDuration(1).y(height);

    }

    private List<Nearby_Place_Name_Info> createlist(int size) {
        List<Nearby_Place_Name_Info> result = new ArrayList<Nearby_Place_Name_Info>();

        int[] icon = {R.drawable.airport, R.drawable.atm, R.drawable.bank, R.drawable.bus, R.drawable.church,
                R.drawable.ferry, R.drawable.food, R.drawable.fuel, R.drawable.hospital, R.drawable.market,
                R.drawable.mosque, R.drawable.movie, R.drawable.park, R.drawable.police, R.drawable.post_office,
                R.drawable.school, R.drawable.university};

        String[] place_name = {"Airport", "ATM", "Bank", "Bus", "Church", "Ferry", "Food", "Fuel", "Hospital", "Market",
                "Mosque", "Movie", "Park", "Police", "Mail", "School", "University"};

        for (int i = 0; i < size; i++) {
            Nearby_Place_Name_Info ci = new Nearby_Place_Name_Info();
            ci.name = place_name[i];
            ci.image = icon[i];
            result.add(ci);
        }
        return result;
    }


    public void showSearchedLocation(Place searchedPlace) {


            /*used marker for show the location */
        googleMap.clear();


        googleMap.addMarker(new MarkerOptions()
                .position(searchedPlace.getLatLng())
                .title(searchedPlace.getName().toString())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.search_marker)));
        mapTool.animateCamera(searchedPlace.getLatLng(),10,16);

    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }





    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity_main requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity_main has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);

                showSearchedLocation(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity_main closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }
    @Override
    public void itemClicked(View clickedView, int position) {

        ResetMenu();

        String[] place_name = {"Airport", "ATM", "Bank", "Bus", "Church", "Ferry", "Food", "Fuel", "Hospital", "Market",
                "Mosque", "Movie", "Park", "Police", "Mail", "School", "University"};

        String result = "";
        switch (place_name[position].toLowerCase()) {
            case "bus":
                result = "bus_station";
                break;
            case "food":
                result = "restaurant";
                break;
            case "fuel":
                result = "gas_station";
                break;
            case "market":
                result = "shopping_mall";
                break;
            case "movie":
                result = "movie_theater";
                break;
            case "mail":
                result = "post_office";
                break;
            default:
                result = place_name[position].toLowerCase();
                break;
        }


        showNearbyPlaces(result);
    }

    public void showNearbyPlaces(String type) {
        Toast.makeText(getApplicationContext(), "Showing Nearby " + type + "'s", Toast.LENGTH_SHORT).show();
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask(getApplicationContext());


        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = googlePlacesUrl.toString();
            /*toPass[2]=pd;*/
        googlePlacesReadTask.execute(toPass);



    }




    public Boolean isAvailable() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1    www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            if (reachable) {
                System.out.println("Internet access");
                return reachable;
            } else {
                System.out.println("No Internet access");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    private String nameFromLatLng(LatLng latLng) {

        if(!isAvailable()) return latLng.toString();

        String[]info=new String[5];
        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {

                if(addresses.get(0).getFeatureName()!=null)
                {
                    return addresses.get(0).getFeatureName();
                }
                else if(addresses.get(0).getLocality()!=null)
                {
                    return addresses.get(0).getLocality();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng.toString();
    }

    private void check_theme_color() {




        SharedPreferences bgshared= getSharedPreferences("background", MODE_PRIVATE);
        String Bgfromshared = bgshared.getString("BKGRND", "Error");
        if(Bgfromshared.equals("bg1")) {
            markerClickLayout.setBackgroundColor(Color.parseColor("#9CCC65"));

            recList.setBackgroundColor(Color.parseColor("#9CCC65"));

            menuButton.setBackgroundResource(R.drawable.bg1);

        }
        else if(Bgfromshared.equals("bg2")) {
            markerClickLayout.setBackgroundColor(Color.parseColor("#2D9FC9"));

            recList.setBackgroundColor(Color.parseColor("#2D9FC9"));

            menuButton.setBackgroundResource(R.drawable.bg2);
        }
        else if(Bgfromshared.equals("bg3")) {
            markerClickLayout.setBackgroundColor(Color.parseColor("#FF9800"));

            recList.setBackgroundColor(Color.parseColor("#FF9800"));

            menuButton.setBackgroundResource(R.drawable.bg3);
        }
        else if(Bgfromshared.equals("bg4")) {
            markerClickLayout.setBackgroundColor(Color.parseColor("#9575CD"));

            recList.setBackgroundColor(Color.parseColor("#9575CD"));

            menuButton.setBackgroundResource(R.drawable.bg4);
        }
        else if(Bgfromshared.equals("bg5")) {
            markerClickLayout.setBackgroundColor(Color.parseColor("#e74c3c"));

            recList.setBackgroundColor(Color.parseColor("#e74c3c"));

            menuButton.setBackgroundResource(R.drawable.bg5);
        }
        else if(Bgfromshared.equals("bg6")) {
            markerClickLayout.setBackgroundColor(Color.parseColor("#009688"));

            recList.setBackgroundColor(Color.parseColor("#009688"));

            menuButton.setBackgroundResource(R.drawable.bg6);
        }
        else if(Bgfromshared.equals("bg7")) {
            markerClickLayout.setBackgroundColor(Color.parseColor("#F06292"));

            recList.setBackgroundColor(Color.parseColor("#F06292"));

            menuButton.setBackgroundResource(R.drawable.bg7);
        }
        else if(Bgfromshared.equals("bg8")) {
            markerClickLayout.setBackgroundColor(Color.parseColor("#FF6F00"));

            recList.setBackgroundColor(Color.parseColor("#FF6F00"));

            menuButton.setBackgroundResource(R.drawable.bg8);
        }


    }



    public void openFloatingMenu()
    {
        floatingActionButton.animate().setDuration(100).rotation(90.f);
        nearby.animate().setDuration(100).y(map_layout.getHeight() - (recList.getHeight()*1.5f));
        nearby.animate().setDuration(100).alpha((float) 1.0);


        search_place.animate().setDuration(100).y(map_layout.getHeight() - (recList.getHeight()*2.0f));
        search_place.animate().setDuration(100).alpha((float) 1.0);

    }
    public void closeFloatingMenu(int menuSelected)
    {
        if(menuSelected==1)
        {
            search_place.animate().setDuration(100).y(map_layout.getHeight());
            nearby.animate().setDuration(300).y(map_layout.getHeight()-recList.getHeight()/2);
            floatingActionButton.animate().setDuration(200).y(map_layout.getHeight());

            search_place.animate().setDuration(100).alpha((float) 0.0);
            nearby.animate().setDuration(300).alpha((float) 0.0);
            floatingActionButton.animate().setDuration(200).alpha((float) 0.0);
            openRecyclerMenu();

        }
        else if(menuSelected==2)
        {
            search_place.animate().setDuration(300).y(recList.getHeight());
            nearby.animate().setDuration(200).y(map_layout.getHeight());
            floatingActionButton.animate().setDuration(100).y(map_layout.getHeight());

            search_place.animate().setDuration(300).alpha((float) 0.0);
            nearby.animate().setDuration(200).alpha((float) 0.0);
            floatingActionButton.animate().setDuration(100).alpha((float) 0.0);
        }
        else
        {
            floatingActionButton.animate().setDuration(100).rotation(0);
            search_place.animate().setDuration(100).y(map_layout.getHeight()-recList.getHeight());
            nearby.animate().setDuration(200).y(map_layout.getHeight()-recList.getHeight());

            search_place.animate().setDuration(100).alpha((float) 0.0);
            nearby.animate().setDuration(200).alpha((float) 0.0);

        }

    }
    public void openRecyclerMenu()
    {
        ScaleAnimation expantionWidth=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,0f);
        expantionWidth.setDuration(300);
        recList.startAnimation(expantionWidth);
        recList.animate().setDuration(0).alpha((float) 1.0).y(map_layout.getHeight() - recList.getHeight());

    }
    public void ResetMenu()
    {
        floatingActionButton.animate().setDuration(0).rotation(0);
        floatingActionButton.animate().setDuration(100).y(map_layout.getHeight()-recList.getHeight());
        floatingActionButton.animate().setDuration(100).alpha((float) 1.0);

        nearby.animate().setDuration(100).y(map_layout.getHeight()-recList.getHeight());
        nearby.animate().setDuration(100).alpha((float) 0.0);

        search_place.animate().setDuration(100).y(map_layout.getHeight()-recList.getHeight());
        search_place.animate().setDuration(100).alpha((float) 0.0);

        recList.animate().setDuration(100).y(map_layout.getHeight());
        recList.animate().setDuration(100).alpha((float) 0.0);
    }
    public void setClickListeners()
    {
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeFloatingMenu(1);
            }
        });

        search_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFloatingMenu(2);
                openAutocompleteActivity();

            }
        });





        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (floatingActionButton.getAlpha()==0)
                {
                    ResetMenu();
                }
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nearby.getAlpha() == 0.0) {
                    openFloatingMenu();


                } else {
                    closeFloatingMenu(0);
                }
            }
        });

    }
    public void setInitializations()
    {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_button);
        nearby = (FloatingActionButton) findViewById(R.id.nearby_button);
        search_place = (FloatingActionButton) findViewById(R.id.searchPlace_button);
        map_layout = (com.mxn.soul.flowingdrawer_core.LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        recList = (RecyclerView) findViewById(R.id.cardList);

        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        fm = getSupportFragmentManager();
        mMenuFragment = (MyMenuFragment) fm.findFragmentById(R.id.id_container_menu);

        mFlowingView = (FlowingView) findViewById(R.id.sv);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);

        if (Math.abs(degree + currentDegree + 0f) > 5)
        {
            CameraPosition position = CameraPosition.builder()
                    .target(googleMap.getCameraPosition().target)
                    .zoom(googleMap.getCameraPosition().zoom)
                    .tilt(googleMap.getCameraPosition().tilt)
                    .bearing(googleMap.getCameraPosition().bearing-degree)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
