package me.chenfuduo.requestfilesharing_contentsharingdemo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private Intent mRequestFileIntent;
    //The FileDescriptor returned by Parcel.readFileDescriptor(), allowing you to close it when done with it.
    private ParcelFileDescriptor mInputPFD;

    private ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);


    }

    public void fileSharing(View view){
        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/*");
        startActivityForResult(mRequestFileIntent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the selection didn't work
        if (resultCode != RESULT_OK) {
            // Exit without doing anything else
            return;
        } else {
            // Get the file's content URI from the incoming Intent
            Uri returnUri = data.getData();
            try {
                InputStream stream = getContentResolver().openInputStream(returnUri);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                iv.setImageBitmap(bitmap);
                //========================================================================


                ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(returnUri,"r");

                FileDescriptor fd = pfd.getFileDescriptor();

                String mimeType = getContentResolver().getType(returnUri);

                Cursor returnCursor = getContentResolver().query(returnUri,null,null,null,null);

                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);

                returnCursor.moveToFirst();



                Log.e("Test","MIMEType: " + mimeType + " nameIndex: " + nameIndex + " sizeIndex: " + sizeIndex);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("MainActivity","出错了:" + e.getMessage());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
