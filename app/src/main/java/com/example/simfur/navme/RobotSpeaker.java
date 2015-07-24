package com.example.simfur.navme;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

class RobotSpeaker implements TextToSpeech.OnInitListener {

    private final TextToSpeech tts;
    private boolean ready = false;
    private boolean allowed = false;

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            // TODO: Set correct locale
            tts.setLanguage(Locale.US);
            ready = true;
        }else{
            ready = false;
        }
    }

    public RobotSpeaker(Context context){
        tts = new TextToSpeech(context, this);
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed){
        this.allowed = allowed;
    }

    public void speak(String text){
        // Speak only if the TTS is ready
        // and the user has allowed speech
        if(ready && allowed) {
            //HashMap<String, String> hash = new HashMap<String,String>();
            //hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
            //        String.valueOf(AudioManager.STREAM_NOTIFICATION));
            //tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    public void pause(int duration){
        tts.playSilentUtterance(duration, TextToSpeech.QUEUE_ADD, null);
    }

    // Free up resources
//    public void destroy(){
//        tts.shutdown();
//    }
}
