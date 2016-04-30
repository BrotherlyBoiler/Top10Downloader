package bamtastic.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications {

    private String data;
    private ArrayList<Application> listApps;

    public ParseApplications(String xmlData) {
        this.data = xmlData;
        this.listApps = new ArrayList<>();
    }

    public ArrayList<Application> getListApps() {
        return listApps;
    }

    public boolean process() {
        boolean status = true;
        Application currentRecord = null;
        boolean inEntry = false;
        String textValue = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(this.data));

            for (int eventType = parser.getEventType();
                 eventType != XmlPullParser.END_DOCUMENT;
                 eventType = parser.next()) {

                String eventName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (eventName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            currentRecord = new Application();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if (eventName.equalsIgnoreCase("entry")) {
                                listApps.add(currentRecord);
                                inEntry = false;
                            } else if (eventName.equalsIgnoreCase("name")) {
                                currentRecord.setName(textValue);
                            } else if (eventName.equalsIgnoreCase("artist")) {
                                currentRecord.setArtist(textValue);
                            } else if (eventName.equalsIgnoreCase("releaseDate")) {
                                currentRecord.setArtist(textValue);
                            }
                        }
                        break;
                    default:
                }
            }
        } catch (Exception e) {
            status = false;
            Log.d(ParseApplications.class.getSimpleName(), "process: " + e.getMessage());
        }

        return true;
    }
}
