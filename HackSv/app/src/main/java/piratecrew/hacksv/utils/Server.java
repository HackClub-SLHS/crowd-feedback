package piratecrew.hacksv.utils;

import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import 	java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jiahao on 7/18/2015.
 */
public class Server {

    static private final String WEB_ROOT = "https://hacksv-server-xeonjake.c9.io/";

    public String sendPoll( data) throws IOException {
        URL url = new URL(WEB_ROOT);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(10000 /*milliseconds*/);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        //make some HTTP header nicety
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        //open connection
        urlConnection.connect();

        //setup send
        OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
        os.write(data.getBytes());

        //clean up
        os.flush();
        os.close();
        urlConnection.disconnect();

        return null;
    }
}

