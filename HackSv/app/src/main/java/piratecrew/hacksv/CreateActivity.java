package piratecrew.hacksv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.Runnable;
import piratecrew.hacksv.utils.Server;

public class CreateActivity extends AppCompatActivity implements Runnable{

    int requestCodeRun, resultCodeRun;
    Intent dataRun;
    Button create;
    EditText textTop, textCenter, textBottom, emailText;
    Bitmap bit1, bit2;
    Bundle extras;
    int imageToSet;
    DisplayMetrics metrics;
    int width;
    int height;
    Uri selectedImage;


    @Override
    public void run() {
        Bitmap bitmap = null;
        if (resultCodeRun == RESULT_OK) {
            if (requestCodeRun ==1) {   //took a photo
                bitmap = (Bitmap) extras.get("data");
                    ((ImageView) findViewById(imageToSet)).setImageBitmap(Bitmap.createScaledBitmap(bitmap, (int)(width*((bitmap.getWidth()*height*.3)/(bitmap.getHeight()*width))), (int) (height*.3), false));

                    //TODO: find why to use this code
                   /* OutputStream outFile = null;
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            +File.separator
                            + "createAct" + File.separator + "default"; //create the file name
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outFile); //compress captured image
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) { //idk what these are it just had to be there
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

            }
            else if (requestCodeRun == 2) {    //choose photo

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                bitmap = (BitmapFactory.decodeFile(picturePath));

                //TODO: find why to use this code
                /*OutputStream compressThumbnail = null;
                try {
                    compressThumbnail = new FileOutputStream(picturePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                thumbnail.compress(Bitmap.CompressFormat.JPEG,85,compressThumbnail); //use to compress the display image
                Log.w("image path:", picturePath + "");*/
                ((ImageView) findViewById(imageToSet)).setImageBitmap(Bitmap.createScaledBitmap(bitmap, (int)(width*((bitmap.getWidth()*height*.3)/(bitmap.getHeight()*width))), (int) (height*.3), false));

            }
        }
        if (imageToSet == R.id.imageViewTop){
            bit1 = bitmap;
            //bitmap.recycle();
            selectImage(R.id.imageViewBottom, false);
        }
        else{
            bit2 = bitmap;
            //bitmap.recycle();
            Server.createPoll(textTop.getText().toString(), textBottom.getText().toString(),textCenter.getText().toString(),emailText.getText().toString(), bit1, bit2);
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
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Turn the time into milliseconds
                //long time = 60_000 * (10 + 60 * (0 + 24 * 0));

                //TODO add additional verification for text boxes
                if (textTop.getText().toString().equals("") || textBottom.getText().toString().equals("") || textCenter.getText().toString().equals("")) {
                    showToast("Must describe both options and add question.");
                } else {
                    selectImage(R.id.imageViewTop, true);
                    Server.createPoll(textTop.getText().toString(), textBottom.getText().toString(), textCenter.getText().toString(), emailText.getText().toString(), bit1, bit2);
                }
            }
        });
    }


    private void selectImage(int im, boolean first) { // select image button
        //im is the right image id when the right button is pressed.
        //im is the left  image id when the left  button is pressed.
        imageToSet = im;

        //Build prompt to take a photo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (first) builder.setTitle("Choose your first photo:");
        else builder.setTitle("Choose your second photo:");
        final CharSequence[] photoOptions = {"Take a photo", "Choose from gallery", "Cancel"};
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
                        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            extras =data.getExtras();
            run();
        }
        else if (requestCode == 2 && resultCode == RESULT_OK){
            selectedImage = data.getData();
            run();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
