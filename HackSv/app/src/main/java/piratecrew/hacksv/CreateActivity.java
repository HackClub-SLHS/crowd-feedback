package piratecrew.hacksv;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.Runnable;


public class CreateActivity extends ActionBarActivity implements Runnable{

    int requestCodeRun, resultCodeRun;
    Intent dataRun;
    Button create;
    EditText textTop, textCenter, textBottom;
    Bitmap thumb, bit;

    @Override
    public void run() {
        super.onActivityResult(requestCodeRun, resultCodeRun, dataRun);
        if (resultCodeRun == RESULT_OK) {
            if (requestCodeRun == 1) {   //took a photo
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions);
                    bit = bitmap;
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            ((ImageView) findViewById(imageToSet)).setImageBitmap(bit);
                        }
                    });

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "createAct" + File.separator + "default"; //create the file name
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile); //compress captured image
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) { //idk what these are it just had to be there
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (requestCodeRun == 2) {    //choose photo

                Uri selectedImage = dataRun.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                OutputStream compressThumbnail = null;
                try {
                    compressThumbnail = new FileOutputStream(picturePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                thumbnail.compress(Bitmap.CompressFormat.JPEG,85,compressThumbnail); //use to compress the display image
                Log.w("image path:", picturePath + "");
                thumb = thumbnail;
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        ((ImageView) findViewById(imageToSet)).setImageBitmap(thumb);
                    }
                });

            }
        }
        Log.i("Running", "Thread is running");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        textTop = (EditText) findViewById(R.id.editTextTop);
        textBottom = (EditText) findViewById(R.id.editTextBottom);
        textCenter = (EditText) findViewById(R.id.editTextCenter);
        create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Turn the time into milliseconds
                long time = 60_000 * (10 + 60 * (0 + 24 * 0));

                for(byte i = 0;i<=1;i++) {
                    //TODO: add code to verify inputs and upload to server

                    //TODO add additional verification for text boxes
                    if (textTop.getText().toString().equals("") || textBottom.getText().toString().equals("")|| textCenter.getText().toString().equals("")) {
                        showToast("Must describe both options and add question.");
                        break;
                    }
                    else {
                        selectImage(R.id.imageViewTop);
                        selectImage(R.id.imageViewBottom);
                        //TODO: Database code for creating poll
                    }


                }
            }
        });
    }


    int imageToSet;
    private void selectImage(int im) { // select image button
        //im is the right image id when the right button is pressed.
        //im is the left  image id when the left  button is pressed.
        imageToSet = im;

        //Build prompt to take a photo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.photo_dialog_title);
        builder.setItems(R.array.photo_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Intent intent;
                switch (item) {
                    //Take a photo
                    case 0:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 1);
                        break;
                    //Chose a photo from gallery
                    case 1:
                        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
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
