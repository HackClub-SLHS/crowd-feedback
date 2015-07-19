package piratecrew.hacksv.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by jiahao on 7/18/2015.
 */
public class Server {

    static WebResponderI wr;
    public static int n;

    static private final String WEB_ROOT = "https://hacksv-server-xeonjake.c9.io/";

    public static void createPoll(String text1, String text2,String question,String email, String pic1, String pic2,WebResponderI act){ //method to create a poll

        wr = act;
        String[] host = {WEB_ROOT+"create_poll.php"};
        String[] qus = {"qus", question};
        String[] opt1  = {"opt1", text1};
        String[] opt2 = {"opt2", text2};
        String[] bit1 = {"pic1", pic1};
        String[] bit2 = {"pic2", pic2};
        String[] mail = {"email", email};
        n = 0;

       new SendPostRequest().execute(host, opt1, opt2, qus, mail, bit1, bit2);

    }
    public static void readPoll(WebResponderI act){
        wr = act;
        new SendGetRequest().execute();
    }

    public static void vote(WebResponderI act, String ident,String choice){
        wr = act;
        String[] id = {"id",ident};
        Log.i("",ident);
        String[]vote = {"vote",choice};
        Log.i("",choice);
        String[] host = {WEB_ROOT+"vote.php"};
        n = 1;

        new SendPostRequest().execute(host,id,vote);

    }

    public static String BitMapToString(Bitmap bitmap){ // method to convert bitmap to string
        ByteArrayOutputStream byteOutRiver = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOutRiver);
        byte [] b=byteOutRiver.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString) { // method t convert string to bitmap
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    static private class SendPostRequest extends AsyncTask<String[], Void, String> {

        protected String doInBackground(String[]... params) {
            try {
                //Create request
                HttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost();

                httpPost.setURI(new URI(params[0][0]));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.length - 1);
                    for (int i = 1; i < params.length; i++) {
                        nameValuePairs.add(new BasicNameValuePair(params[i][0], params[i][1]));
                    }

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                //Get the response
                HttpResponse response = client.execute(httpPost);
                //Get content of response
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();

                ByteArrayOutputStream content = new ByteArrayOutputStream();
                // Read response into a buffered stream
                int readBytes = 0;
                byte[] sBuffer = new byte[512];
                while ((readBytes = inputStream.read(sBuffer)) != -1) {
                    content.write(sBuffer, 0, readBytes);
                }

                //Return result from buffered stream
                return new String(content.toByteArray());


            } catch (URISyntaxException e) {
                // TODO Generic catch
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null; //If program gets this far, something didn't work.
        }
        protected void onPostExecute(String result){
            String message;
            try {
                JSONObject data = new JSONObject(result);
                if(data.has("error")){
                    Log.i("server_error",result);
                    if (n==0)message = "Poll Create Failed: Server Error";
                    else message="Voting Failed: Server Error";
                }
                else{
                    if (n==0) message = "Poll Created";
                    else message = "Voting Successful";
                }
                String[] s = {message,""};
                wr.onWebResponse(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private static class SendGetRequest extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            try {
                //Get the response
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(URI.create(WEB_ROOT + "read_poll.php"));
                HttpResponse response = client.execute(request);

                //Get content of response
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();

                ByteArrayOutputStream content = new ByteArrayOutputStream();

                // Read response into a buffered stream
                int readBytes = 0;
                byte[] sBuffer = new byte[512];
                while ((readBytes = inputStream.read(sBuffer)) != -1) {
                    content.write(sBuffer, 0, readBytes);
                }

                //Return result from buffered stream

                return new String(content.toByteArray());


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null; //If program gets this far, something didn't work.
        }
        protected void onPostExecute(String result){
            String message="";
            try {
                JSONObject data = new JSONObject(result);
                String[]s={"","","","","","",""};
                if(data.has("error")) s[0] = "Poll Load Failed: Server Error";
                else{
                    s[0]="Success";
                    s[1]=data.getJSONObject("poll").getString("question");
                    s[2]=data.getJSONObject("poll").getJSONObject("opt1").getString("text");
                    s[3]=data.getJSONObject("poll").getJSONObject("opt2").getString("text");
                    s[4]=data.getJSONObject("poll").getJSONObject("opt1").getString("img");
                    s[5]=data.getJSONObject("poll").getJSONObject("opt2").getString("img");
                    s[6]=String.valueOf(data.getJSONObject("poll").getInt("id"));
                }
                wr.onWebResponse(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }


    }
}

