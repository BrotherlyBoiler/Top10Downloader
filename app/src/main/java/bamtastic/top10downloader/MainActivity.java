package bamtastic.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String mFileContents;
    private ListView listview_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SyncData syncData = new SyncData();
        syncData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices" +
              ".woa/ws/RSS/topfreeapplications/limit=10/xml");

        listview_data = (ListView) findViewById(R.id.listView_data);

        ParseApplications appsParser = new ParseApplications(mFileContents);
        appsParser.process();

//        listview_data.setAdapter(new ArrayAdapter<>(
//              MainActivity.this,
//              R.layout.list_item,
//              appsParser.getListApps()
//        ));
    }

    private class SyncData extends AsyncTask<String, Void, String> {

        private final String TAG = SyncData.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            mFileContents = downloadXMLFile(params[0]);
            if (mFileContents == null) {
                Log.d(TAG, "doInBackground: Error downloading");
            }
            return mFileContents;
        }

        private String downloadXMLFile(String urlPath) {
            StringBuilder builder = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.d(TAG, "downloadXMLFile: The response code was " + connection.getResponseCode());

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

                int readCount;
                char[] readBuffer = new char[500];
                while (true) {
                    readCount = streamReader.read(readBuffer);
                    if (readCount <= 0)
                        break;
                    builder.append(String.copyValueOf(readBuffer), 0, readCount);
                }

                return builder.toString();
            } catch (IOException e) {
                Log.d(TAG, "downloadXMLFile: IOException reading data: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: " + s);
        }
    }
}
