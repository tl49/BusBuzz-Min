package com.team6.busbuzz;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker gilmanMarker, regentMarker, arribaMarker, lebonMarker, nobelMarker, stop;

    //Parse Objects
    private ParseObject stopObj;

    //Buttons
    private Button emptyButton, spaciousButton, fullButton, busLeftButton;

    private ImageView dImage = (ImageView)findViewById(R.id.imageView2);
    private ImageView fImage = (ImageView)findViewById(R.id.imageView2);
    private ImageView sImage = (ImageView)findViewById(R.id.imageView2);
    private ImageView eImage = (ImageView)findViewById(R.id.imageView2);


    public void setImage(String status)
    {
        if(status == "full")
        {
            fImage.setVisibility(View.VISIBLE);
        }
        else if(status == "spacious")
        {
            sImage.setVisibility(View.VISIBLE);
        }
        else if (status == "empty")
        {
            eImage.setVisibility(View.VISIBLE);
        }
        else
        {
            dImage.setVisibility(View.VISIBLE);
        }
    }
    //This function calculate median value and return "string"
    public String median(List<ParseObject> data, String stopName) {
        double sum = 0;
        String result = "";
        double count = 0;

        for(int i = 0; i < data.size(); i++) {
           if(data.get(i).getString("Stops").equals(stopName)) {
               count++;
               if (data.get(i).getString("Status").equals("spacious")) {
                   sum += 1;
               } else if (data.get(i).getString("Status").equals("full")) {
                   sum += 2;
               }
           }
        }
        sum = sum/count;

        if(sum < 0.5) {
            result = "empty";
        }
        else if (sum <= 1.5 && sum >= 0.5) {
            result = "spacious";
        }
        else if(sum > 1.5) {
            result = "full";
        }
        else {
            //result = Double.toString(sum);
            result = "Not Available";
        }
        return result;
    }

    //This function return last time as a "string"
    public String lastTime(List<ParseObject> data, String stopName) {
        for(int i = 0; i < data.size(); i++) {
            if(data.get(i).getString("Stops").equals(stopName)) {
                return data.get(i).getString("LastTime");
            }
        }
        return "Not Available";
    }

    //This function return number of people who voted at that time.
    public int numberofPeople(List<ParseObject> data, String stopName) {
        int numPeople = 0;

        for(int i = 0; i < data.size(); i++) {
            if(data.get(i).getString("Stops").equals(stopName)) {
                numPeople++;
            }
        }
        return numPeople;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "jEzU7yAVpJXiqxwXLg3WSLTT274HJM8hoWIQxNfc", "5eAjXOb630iaYXcOpmDqLBqrh0q7i6WrLhgck0LJ");

        stopObj = new ParseObject("Bus");
        emptyButton =  (Button)findViewById(R.id.button);
        spaciousButton =  (Button)findViewById(R.id.button2);
        fullButton =  (Button)findViewById(R.id.button3);
        busLeftButton = (Button)findViewById(R.id.button4);

        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emptyButton.setBackgroundColor(Color.BLACK);
                emptyButton.setTextColor(Color.WHITE);
                spaciousButton.setBackgroundColor(Color.WHITE);
                spaciousButton.setTextColor(Color.BLACK);
                fullButton.setBackgroundColor(Color.WHITE);
                fullButton.setTextColor(Color.BLACK);

                if (stop.equals(gilmanMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Gilman");
                    stopObj.put("Status", "empty");
                } else if (stop.equals(regentMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Regent");
                    stopObj.put("Status", "empty");
                } else if (stop.equals(arribaMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Arriba");
                    stopObj.put("Status", "empty");
                } else if (stop.equals(lebonMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Lebon");
                    stopObj.put("Status", "empty");
                } else if (stop.equals(nobelMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Nobel");
                    stopObj.put("Status", "empty");
                }

                stopObj.saveInBackground();
            }
        });

        spaciousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emptyButton.setBackgroundColor(Color.WHITE);
                emptyButton.setTextColor(Color.BLACK);
                spaciousButton.setBackgroundColor(Color.BLACK);
                spaciousButton.setTextColor(Color.WHITE);
                fullButton.setBackgroundColor(Color.WHITE);
                fullButton.setTextColor(Color.BLACK);

                if (stop.equals(gilmanMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Gilman");
                    stopObj.put("Status", "spacious");
                } else if (stop.equals(regentMarker)){
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Regent");
                    stopObj.put("Status", "spacious");
                } else if (stop.equals(arribaMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Arriba");
                    stopObj.put("Status", "spacious");
                } else if (stop.equals(lebonMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Lebon");
                    stopObj.put("Status", "spacious");
                } else if (stop.equals(nobelMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Nobel");
                    stopObj.put("Status", "spacious");
                }

                stopObj.saveInBackground();
            }
        });

        fullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emptyButton.setBackgroundColor(Color.WHITE);
                emptyButton.setTextColor(Color.BLACK);
                spaciousButton.setBackgroundColor(Color.WHITE);
                spaciousButton.setTextColor(Color.BLACK);
                fullButton.setBackgroundColor(Color.BLACK);
                fullButton.setTextColor(Color.WHITE);

                if (stop.equals(gilmanMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Gilman");
                    stopObj.put("Status", "full");
                } else if (stop.equals(regentMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Regent");
                    stopObj.put("Status", "full");
                } else if (stop.equals(arribaMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Arriba");
                    stopObj.put("Status", "full");
                } else if (stop.equals(lebonMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Lebon");
                    stopObj.put("Status", "full");
                } else if (stop.equals(nobelMarker)) {
                    stopObj.put("Routes", "UCSD_Shuttle");
                    stopObj.put("Stops", "Nobel");
                    stopObj.put("Status", "full");
                }

                stopObj.saveInBackground();
            }
        });

        busLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                String lastBus = df.format(c.getTime());
                if (stop.equals(gilmanMarker)) {
                    stopObj.put("Stops", "Gilman");
                    stopObj.put("LastTime", lastBus);
                } else if (stop.equals(regentMarker)) {
                    stopObj.put("Stops", "Regent");
                    stopObj.put("LastTime", lastBus);
                } else if (stop.equals(arribaMarker)) {
                    stopObj.put("Stops", "Arriba");
                    stopObj.put("LastTime", lastBus);
                } else if (stop.equals(lebonMarker)) {
                    stopObj.put("Stops", "Lebon");
                    stopObj.put("LastTime", lastBus);
                } else if (stop.equals(nobelMarker)) {
                    stopObj.put("Stops", "Nobel");
                    stopObj.put("LastTime", lastBus);
                }
            }
        });

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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        //User permission check for accessing last known positon
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        Location location = locationManager.getLastKnownLocation(provider);

        //Initial Map Camera Location
        LatLng UCSD = new LatLng(32.879228, -117.228738);

        //Bus Stops
        LatLng Gilman = new LatLng(32.877196, -117.235784);
        LatLng Nobel_Regent = new LatLng(32.867459, -117.219267);
        LatLng Arriba_Regent = new LatLng(32.861505, -117.223343);
        LatLng Lebon_Palmila = new LatLng(32.865053, -117.225050);
        LatLng Nobel_Lebon = new LatLng(32.868551, -117.225306);

        //Add bus pin
        gilmanMarker = mMap.addMarker(new MarkerOptions().position(Gilman).title("Gilman Dr & Myer's")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
        regentMarker = mMap.addMarker(new MarkerOptions().position(Nobel_Regent).title("Regent Rd & Nobel Dr")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
        arribaMarker = mMap.addMarker(new MarkerOptions().position(Arriba_Regent).title("Arriba St & Regent Rd")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
        lebonMarker  = mMap.addMarker(new MarkerOptions().position(Lebon_Palmila).title("Lebon Dr & Palmila Dr")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
        nobelMarker  = mMap.addMarker(new MarkerOptions().position(Nobel_Lebon).title("Nobel Dr & Lebon Dr")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));

        //Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UCSD));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //set textView invisible
                findViewById(R.id.textView3).setVisibility(View.INVISIBLE);
                findViewById(R.id.textView4).setVisibility(View.INVISIBLE);
                findViewById(R.id.textView5).setVisibility(View.INVISIBLE);
                findViewById(R.id.textView6).setVisibility(View.INVISIBLE);
                findViewById(R.id.textView7).setVisibility(View.INVISIBLE);
                findViewById(R.id.textView8).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageView2).setVisibility(View.INVISIBLE);
                //set buttons invisible
                emptyButton.setVisibility(View.INVISIBLE);
                spaciousButton.setVisibility(View.INVISIBLE);
                fullButton.setVisibility(View.INVISIBLE);
                busLeftButton.setVisibility(View.INVISIBLE);
                //set image invisible
                eImage.setVisibility(View.INVISIBLE);
                sImage.setVisibility(View.INVISIBLE);
                fImage.setVisibility(View.INVISIBLE);
                dImage.setVisibility(View.INVISIBLE);



            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                findViewById(R.id.imageView2).setVisibility(View.VISIBLE);
                findViewById(R.id.textView3).setVisibility(View.VISIBLE);
                findViewById(R.id.textView5).setVisibility(View.VISIBLE);
                findViewById(R.id.textView6).setVisibility(View.VISIBLE);
                findViewById(R.id.textView7).setVisibility(View.VISIBLE);
                findViewById(R.id.textView8).setVisibility(View.VISIBLE);
                emptyButton.setVisibility(View.VISIBLE);
                spaciousButton.setVisibility(View.VISIBLE);
                fullButton.setVisibility(View.VISIBLE);
                busLeftButton.setVisibility(View.VISIBLE);

                //reset button highlight
                emptyButton.setBackgroundColor(Color.WHITE);
                emptyButton.setTextColor(Color.BLACK);
                spaciousButton.setBackgroundColor(Color.WHITE);
                spaciousButton.setTextColor(Color.BLACK);
                fullButton.setBackgroundColor(Color.WHITE);
                fullButton.setTextColor(Color.BLACK);



                if (marker.equals(gilmanMarker)) {
                    findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                    TextView text = (TextView) findViewById(R.id.textView4);
                    text.setText("Gilman Dr & Myer's");
                    stop = gilmanMarker;

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Bus");
                    query.whereExists("Status");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> statusList, com.parse.ParseException e) {
                            if (e == null) {
                                String status = median(statusList, "Gilman");
                                gilmanMarker.setSnippet("Status: " + status );
                                setImage(status);
                                //need to change image
                            } else {
                            }
                        }
                    });

                } else if (marker.equals(regentMarker)) {
                    findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                    TextView text = (TextView) findViewById(R.id.textView4);
                    text.setText("Regents Rd & Nobel Dr");
                    stop = regentMarker;

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Bus");
                    query.whereExists("Status");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> statusList, com.parse.ParseException e) {
                            if (e == null) {
                                regentMarker.setSnippet("Status: " + median(statusList, "Regent")
                                        + "Number of People: " + numberofPeople(statusList, "Regent")
                                + "Last bus time: " + lastTime(statusList, "Regent"));
                                //need to change image
                                String status = median(statusList, "Regent");
                                gilmanMarker.setSnippet("Status: " + status );
                                setImage(status);
                            } else {
                            }
                        }
                    });

                } else if (marker.equals(arribaMarker)) {
                    findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                    TextView text = (TextView) findViewById(R.id.textView4);
                    text.setText("Arriba St & Regent Rd");
                    stop = arribaMarker;

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Bus");
                    query.whereExists("Status");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> statusList, com.parse.ParseException e) {
                            if (e == null) {
                                arribaMarker.setSnippet("Status: " + median(statusList, "Arriba"));
                                //need to change image
                                String status = median(statusList, "Arriba");
                                gilmanMarker.setSnippet("Status: " + status );
                                setImage(status);
                            } else {
                            }
                        }
                    });

                } else if (marker.equals(lebonMarker)) {
                    findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                    TextView text = (TextView) findViewById(R.id.textView4);
                    text.setText("Lebon Dr & Palmila Dr");
                    stop = lebonMarker;

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Bus");
                    query.whereExists("Status");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> statusList, com.parse.ParseException e) {
                            if (e == null) {
                                lebonMarker.setSnippet("Status: " + median(statusList, "Lebon"));
                                //need to change image
                                String status = median(statusList, "Lebon");
                                gilmanMarker.setSnippet("Status: " + status );
                                setImage(status);
                            } else {
                            }
                        }
                    });

                } else if (marker.equals(nobelMarker)) {
                    findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                    TextView text = (TextView) findViewById(R.id.textView4);
                    text.setText("Nobel Dr & Lebon Dr");
                    stop = nobelMarker;

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Bus");
                    query.whereExists("Status");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> statusList, com.parse.ParseException e) {
                            if (e == null) {
                                nobelMarker.setSnippet("Status: " + median(statusList, "Nobel"));
                                //need to change image
                                String status = median(statusList, "Nobel");
                                gilmanMarker.setSnippet("Status: " + status );
                                setImage(status);
                            } else {
                            }
                        }
                    });
                }


                return false;
            }
        });

    }


}

