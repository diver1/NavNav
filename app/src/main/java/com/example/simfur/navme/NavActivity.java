package com.example.simfur.navme;

import java.util.List;
import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NavActivity extends Fragment {
    /* Private variables */
    private TextView routeTextName;
    private TextView routeTextInfo;
    private List<POI> pois;
    private RobotSpeaker speaker;
    private boolean active = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Check TTS status and create a speaker object */
        checkTTS();

        /* Acquire a reference to the system Location Manager */
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

	    /* Define a listener that responds to location updates */
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (!active) {
                    /* Return if not active */
                    Log.d("Route not active!", "");
                    return;
                }

                // Called when a new location is found by the network location provider.
                Log.d("This is tha latitude: ", Double.toString(location.getLatitude()));

                /* Check if we are close to a coordinate */
                for (POI poi : pois) {
                    /* Calculate distance */
                    if (calcDistance(location.getLatitude(), location.getLongitude(), poi.getLat(), poi.getLon()) < poi.getRadius()) {
                        Log.d("Match of: ", poi.getName());
                        onLocationFound(poi);
                    }
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

	    /* Register the listener with the Location Manager
	     * to receive location updates */
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        /* Parse the XML */
        try {
            ParseXMLHandler parser = new ParseXMLHandler();
            pois = parser.parse(getActivity().getAssets().open("example.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        /* Get text views for coordinate information */
        View v = inflater.inflate(R.layout.activity_nav, container, false);
        routeTextName = (TextView)v.findViewById(R.id.textViewName);
        routeTextInfo = (TextView)v.findViewById(R.id.textViewInfo);

        return v;
    }

    private double calcDistance(double lat1, double lng1, double lat2, double lng2) {
        /* Calculate distance in meter */
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c;
    }

    private void checkTTS(){
        // Check if TTS is available
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, 0x1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If no TTS is available, tell the user to install one
        if(requestCode == 0x1){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new RobotSpeaker(this.getActivity());
                speaker.allow(true);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    public void startRoute(View v) {
        this.active = true;
        Log.d("Start the route!", "");
    }

    public void stopRoute(View v) {
        this.active = false;
        Log.d("Stop the route!", "");
    }

    public void onLocationFound(POI poi) {
        /* Method for action when a matching coordinate is found */
        routeTextInfo.setText(poi.getText());
        routeTextName.setText(poi.getName());
        speaker.speak(poi.getTts());
    }
}
