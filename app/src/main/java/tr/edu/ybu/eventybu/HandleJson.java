package tr.edu.ybu.eventybu;

/**
 * Created by YILDIZ on 12.04.2016.
 */

import org.json.JSONException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HandleJson {
    private final ParseMethod parseMethod;
    public volatile boolean parsingComplete = true;
    private String urlString = null;

    public HandleJson(String url, ParseMethod parseMethod){
        this.urlString = url;
        this.parseMethod = parseMethod;
    }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void fetchJSON(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    String data = convertStreamToString(stream);
                    parseMethod.readAndParseJSON(data);
                    stream.close();
                    parsingComplete = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public interface ParseMethod {
        void readAndParseJSON(String in) throws JSONException;
    }
}
