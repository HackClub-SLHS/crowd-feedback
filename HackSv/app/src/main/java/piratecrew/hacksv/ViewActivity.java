package piratecrew.hacksv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import piratecrew.hacksv.utils.Server;
import piratecrew.hacksv.utils.WebResponderI;


public class ViewActivity extends AppCompatActivity implements WebResponderI {

    ImageButton top = (ImageButton)findViewById(R.id.firstImage);
    ImageButton bottom=(ImageButton)findViewById(R.id.secondImage);
    TextView topText = (TextView) findViewById(R.id.firstOption);
    TextView bottomText = (TextView) findViewById(R.id.secondOption);
    TextView question = (TextView)findViewById(R.id.question);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Server.readPoll(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: startActivity(new Intent(ViewActivity.this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onWebResponse(String[] result) {
        if (result[0].equals("Poll Load Failed: Server Error")) {
            Toast.makeText(getApplicationContext(), result[0], Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ViewActivity.this, MainActivity.class));
        }
        else {
            top.setImageBitmap(StringToBitMap(result[4]));
            bottom.setImageBitmap(StringToBitMap(result[5]));
            topText.setText(result[2]);
            bottomText.setText(result[3]);
            question.setText(result[1]);

            top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }
}
