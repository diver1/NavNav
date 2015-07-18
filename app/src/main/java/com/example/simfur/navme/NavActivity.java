package com.example.simfur.navme;

import java.util.List;
import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;

public class NavActivity extends ActionBarActivity {
    /* Private variables */
    private TextView routeText;
    private List<POI> pois;
    private RobotSpeaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        /* Check TTS status and create a speaker object */
        checkTTS();

        /* Get text view for coordinate information */
        routeText = (TextView)findViewById(R.id.textView);

        /* Acquire a reference to the system Location Manager */
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

	    /* Define a listener that responds to location updates */
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("This is tha latitude: ", Double.toString(location.getLatitude()));
                onLocationFound(location);
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
            pois = parser.parse(getAssets().open("example.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("This is the name of the first in list ", pois.get(0).getName());
        Log.d("This is the name of the second in list ", pois.get(1).getName());

        //speaker.speak("HEJHEJ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkTTS(){
        // Check if TTS is available
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, 0x1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If no TTS is available, tell the user to install one
        if(requestCode == 0x1){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new RobotSpeaker(this);
                speaker.allow(true);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    public void startRoute(View v) {
        Log.d("Start the route!", "");
        speaker.speak("Start the route!");
    }

    public void stopRoute(View v) {
        Log.d("Stop the route!", "");
        speaker.speak("Stop the route!");
    }

    public void onLocationFound(Location location) {
        /* Method for action when a matching coordinate is found */
        routeText.setText("Sundbyberg Municipality (Sundbybergs kommun or Sundbybergs stad) is a municipality in Stockholm County in east central Sweden, just north of the capital Stockholm. Sundbyberg is wholly within the city of Stockholm and has a 100% urban population.");
        speaker.speak("Sundbyberg Municipality (Sundbybergs kommun or Sundbybergs stad) is a municipality in Stockholm County in east central Sweden, just north of the capital Stockholm. Sundbyberg is wholly within the city of Stockholm and has a 100% urban population.");
    }
}
