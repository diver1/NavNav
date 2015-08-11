package com.example.simfur.navme;

import java.util.List;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class NavFragment extends Fragment {
    /* Private variables */
    private TextView routeTextName;
    private TextView routeTextInfo;
    private List<POI> pois;
    private RobotSpeaker speaker;
    private boolean active = false;

    private RouteListFragment.OnFragmentInteractionListener mListener;

    Button buttonToggle;
    PopupWindow popupPoi;

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
                Log.d("This is the latitude: ", Double.toString(location.getLatitude()));
                Log.d("This is the longitude: ", Double.toString(location.getLongitude()));

                /* Check if we are close to a coordinate */
                try {
                    for (POI poi : pois) {
                        float [] dist = new float[1];
                        Location.distanceBetween(location.getLatitude(), location.getLongitude(), poi.getLat(), poi.getLon(), dist);
                        Log.d("Check of coord: ", poi.getName());
                        Log.d("Distance between: ", Float.toString(dist[0]));

                        /* Calculate distance */
                        if (dist[0] < poi.getRadius()) {
                            Log.d("Match of: ", poi.getName());
                            onLocationFound(poi);
                        }
                    }
                } catch (NullPointerException e) {
                    Log.d("No route is loaded", "");
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        /* Get text views for coordinate information */
        View v = inflater.inflate(R.layout.fragment_nav, container, false);

        /* Create and add a onclicklistener programatically since this button only shall
         * be used in the fragment and not in the entire activity */
        buttonToggle = (Button) v.findViewById(R.id.button);
        buttonToggle.setOnClickListener(startClickListener);

        /* Create a popupwindow to be used when a matching poi is found */
        popupPoi = new PopupWindow(inflater.inflate(R.layout.popup_poi, null),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupPoi.setBackgroundDrawable(new BitmapDrawable());
        popupPoi.setOutsideTouchable(true);
        routeTextName = ((TextView)popupPoi.getContentView().findViewById(R.id.textViewName));
        routeTextInfo = ((TextView)popupPoi.getContentView().findViewById(R.id.textViewInfo));

        return v;
    }

    private final View.OnClickListener startClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startRoute(v);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (RouteListFragment.OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    private void startRoute(View v) {
        /* Toggle active */
        if (this.active) {
            this.active = false;
            buttonToggle.setText("Start");
        }
        else {
            this.active = true;
            buttonToggle.setText("Stop");
        }
        Log.d("Start/Stop the route!", "");
    }

    private void onLocationFound(POI poi) {
        /* Method for action when a matching coordinate is found */
        routeTextInfo.setText(poi.getText());
        routeTextName.setText(poi.getName());
        speaker.speak(poi.getTts());

        popupPoi.showAtLocation(getActivity().findViewById(R.id.textViewNameOld), Gravity.CENTER , 0, 0);
        popupPoi.update();
    }

    public void routeSelected(Activity a, String file) {
        /* Parse the XML */
        if (file != null) {
            try {
                ParseXMLHandler parser = new ParseXMLHandler();
                this.pois = parser.parse(a.getAssets().open("routes/" + file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
