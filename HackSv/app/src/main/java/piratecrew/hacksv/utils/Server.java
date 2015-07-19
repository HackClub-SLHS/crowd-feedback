package piratecrew.hacksv.utils;

import java.io.IOException;
import 	java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jiahao on 7/18/2015.
 */
public class Server {

    static private final String WEB_ROOT = "https://hacksv-server-xeonjake.c9.io/";

    public String CreatePoll(String caption1, String caption2, String question, String email, String img1uri, String img2uri) throws IOException {
        URL url = new URL(WEB_ROOT);
        compileObject(caption1,caption2,question);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        return null;
    }

    public void compileObject(String caption1, String caption2, String question,) { //method to create a poll
        String[] host = {WEB_ROOT + "create_poll.php"};
        String[] cap1 = {"opt1", caption1};
        String[] cap2 = {"opt2", caption2};
        new sendPostRequest().execute(host, cap1, cap2, question);
    }

}

