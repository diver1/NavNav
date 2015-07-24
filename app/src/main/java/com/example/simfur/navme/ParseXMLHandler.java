package com.example.simfur.navme;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException;

class ParseXMLHandler {
    private final List<POI> pois;
    private POI poi;
    private String text;

    public ParseXMLHandler() {
        pois = new ArrayList<>();
    }

    public List<POI> parse(InputStream is) {
        XmlPullParserFactory factory;
        XmlPullParser parser;
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
                        } else if (tagname.equalsIgnoreCase("text")) {
                            poi.setText(text);
                        } else if (tagname.equalsIgnoreCase("tts")) {
                            poi.setTts(text);
                        } else if (tagname.equalsIgnoreCase("coord")) {
                            poi.setLon(Double.parseDouble(parser.getAttributeValue(null, "lon")));
                            poi.setLat(Double.parseDouble(parser.getAttributeValue(null, "lat")));
                            poi.setRadius(Integer.parseInt(parser.getAttributeValue(null, "radius")));
                        } else if (tagname.equalsIgnoreCase("id")) {
                            poi.setId(Integer.parseInt(text));
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e){
            e.printStackTrace();
        }

        return pois;
    }
}
