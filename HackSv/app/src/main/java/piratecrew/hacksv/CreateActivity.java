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

import java.lang.Runnable;
import piratecrew.hacksv.utils.Server;

public class CreateActivity extends AppCompatActivity implements Runnable{

    int requestCodeRun, resultCodeRun;
    Button create;
    EditText textTop, textCenter, textBottom;
    Bitmap bitmap, bit1, bit2;
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
                bitmap = (Bitmap) extras.get("data");
                    imageToSet.setImageBitmap(Bitmap.createScaledBitmap(bitmap, (int)(width*((bitmap.getWidth()*height*.29)/(bitmap.getHeight()*width))), (int) (height*.29), false));

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
                imageToSet.setImageBitmap(Bitmap.createScaledBitmap(bitmap, (int) (width * ((bitmap.getWidth() * height * .29) / (bitmap.getHeight() * width))), (int) (height * .29), false));

            }
        }
        if (imageToSet == top){
            if (bit1!=null)bit1.recycle();
            bit1 = bitmap;
        }
        else{
            if(bit2!=null)bit2.recycle();
            bit2 = bitmap;
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

                //TODO add additional verification for text boxes
                if (textTop.getText().toString().equals("") || textBottom.getText().toString().equals("") || textCenter.getText().toString().equals("")) {
                    showToast("Must describe both options and add question.");
                } else {
                    Server.createPoll(textCenter.getText().toString(),textTop.getText().toString(),textBottom.getText().toString(),bit1,bit2);
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
