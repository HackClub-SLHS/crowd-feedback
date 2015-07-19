package piratecrew.hacksv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Runnable;
import piratecrew.hacksv.utils.Server;

public class CreateActivity extends AppCompatActivity implements Runnable{

    int requestCodeRun, resultCodeRun;
    Button create;
    EditText textTop, textCenter, textBottom, emailText;
    Bitmap bitmap;
    String bit1;
    String bit2;
    String file;
    Bundle extras;
    ImageButton imageToSet;
    DisplayMetrics metrics;
    int width;
    int height;
    Uri selectedImage;
    ImageButton top;
    ImageButton bottom;

    @Override
    public void run() {
        bitmap = null;
        if (resultCodeRun == RESULT_OK) {
            if (requestCodeRun ==1) {   //took a photo
                if (imageToSet == top){
                    bit1 = null;
                }
                else{
                    bit2 = null;
                }
                bitmap = Bitmap.createScaledBitmap((Bitmap) extras.get("data"), (int) (width * ((((Bitmap) extras.get("data")).getWidth() * height * .29) / (((Bitmap) extras.get("data")).getHeight() * width))), (int) (height * .29), false);
                    imageToSet.setImageBitmap(bitmap);
                 String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "createAct" + File.separator + "default"; //create the file name
                    file = path;

            }
            else if (requestCodeRun == 2) {    //choose photo

                if (imageToSet == top){
                    bit1 = null;
                }
                else{
                    bit2 = null;
                }

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                file = picturePath;
                cursor.close();
                bitmap = (Bitmap.createScaledBitmap((BitmapFactory.decodeFile(picturePath)), (int) (width * (((BitmapFactory.decodeFile(picturePath)).getWidth() * height * .29) / ((BitmapFactory.decodeFile(picturePath)).getHeight() * width))), (int) (height * .29), false));
                imageToSet.setImageBitmap(bitmap);

            }
        }
        if (imageToSet == top){
            bit1 = file;
        }
        else{
            bit2 = file;
        }
         Log.i("Running", "Thread is running");
    }

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;


        textTop = (EditText) findViewById(R.id.editTextTop);
        textBottom = (EditText) findViewById(R.id.editTextBottom);
        textCenter = (EditText) findViewById(R.id.editTextCenter);
        emailText = (EditText) findViewById(R.id.editTextemail);
        create = (Button) findViewById(R.id.create);

        top = (ImageButton) findViewById(R.id.imageViewTop);
        bottom = (ImageButton) findViewById(R.id.imageViewBottom);

        Drawable upload = (ResourcesCompat.getDrawable(getResources(), R.drawable.uploadphoto, null));


        if (bit1 == null)(top).setImageBitmap((Bitmap.createScaledBitmap(((BitmapDrawable) (upload)).getBitmap(), (int) (width * ((578) * height * .29) / (321 * width)), (int) (height * .29), false)));
        if (bit2 == null)(bottom).setImageBitmap((Bitmap.createScaledBitmap(((BitmapDrawable) (upload)).getBitmap(), (int) (width * ((578) * height * .29) / (321 * width)), (int) (height * .29), false)));

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(top);
            }
        });

        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(bottom);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Turn the time into milliseconds
                //long time = 60_000 * (10 + 60 * (0 + 24 * 0));

                //TODO add additional verification for text boxes
                if (textTop.getText().toString().equals("") || textBottom.getText().toString().equals("") || textCenter.getText().toString().equals("")) {
                    showToast("Must fill in all fields.");
                } else {
                    Server.createPoll(textTop.getText().toString(), textBottom.getText().toString(), textCenter.getText().toString(), emailText.getText().toString(), bit1, bit2);
                }
            }
        });
    }


    private void selectImage(ImageButton im) { // select image button
        //im is the right image id when the right button is pressed.
        //im is the left  image id when the left  button is pressed.
        imageToSet = im;

        //Build prompt to take a photo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Photo:");
        final CharSequence[] photoOptions = {"Take A Photo", "Choose From Gallery", "Cancel"};
        builder.setItems(photoOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Intent intent;
                switch (item) {
                    //Take a photo
                    case 0:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                        resultCodeRun = RESULT_OK;
                        requestCodeRun = 1;
                        break;
                    //Chose a photo from gallery
                    case 1:
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        resultCodeRun = RESULT_OK;
                        requestCodeRun = 2;
                        startActivityForResult(i, 2);
                        break;
                    case 2:
                        dialog.cancel();
                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: startActivity(new Intent(CreateActivity.this, MainActivity.class));
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK&&data!=null) {
            if (requestCode == 1) {
                extras = data.getExtras();
                run();
            } else if (requestCode == 2) {
                selectedImage = data.getData();
                run();
            }
        }
    }


}
