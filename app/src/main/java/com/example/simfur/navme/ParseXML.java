package com.example.simfur.navme;

import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException;

public class ParseXML {
    List<POI> pois;
    private POI poi;
    private String text;

    public ParseXML() {
        pois = new ArrayList<POI>();
    }

    public List<POI> getPOIs() {
        return pois;
    }

    public List<POI> parse(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("poi")) {
                            // create a new instance of poi
                            poi = new POI();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("poi")) {
                            // add poi object to list
                            pois.add(poi);
                        } else if (tagname.equalsIgnoreCase("name")) {
                            poi.setName(text);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            poi.setId(Integer.parseInt(text));
                            break;
                        }

                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pois;
    }
}
