package com.pet.att.pickapet.AppActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.pet.att.pickapet.AuxiliaryClasses.FileUtil;
import com.pet.att.pickapet.R;
import java.io.File;
import java.io.IOException;
import id.zelory.compressor.Compressor;

public class AddNewPetActivity extends AppCompatActivity {
    private static final int PICK_IMAGE=100;
    private static final String TAG ="AddNewPetActivity" ;
    private static final int QUALITY =40 ;
    private static final int MAX_WIDTH = 640;
    private static final int MAX_HEIGHT = 480;
    private ImageView actualImageView;

    private File actualImage;
    private File compressedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button imgButton = (Button) findViewById(R.id.add_image_button);
        actualImageView = (ImageView) findViewById(R.id.add_pet_image) ;

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CANCELED) return;
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            try {
                actualImage = FileUtil.from(this, data.getData());
                customCompressImage(MAX_WIDTH, MAX_HEIGHT,QUALITY);
            } catch (IOException e) {
                e.printStackTrace();
                showError("fail to read picture data");
            }

        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }


    public void customCompressImage(int maxWidth,int maxHeight,int quality) {
        if (actualImage == null) {
            showError("Please choose an image!");
        } else {
            // Compress image in main thread using custom Compressor
            try {
                compressedImage = new Compressor(this)
                        .setMaxWidth(maxWidth)
                        .setMaxHeight(maxHeight)
                        .setQuality(quality)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(actualImage);

                setCompressedImage();
            } catch (IOException e) {
                e.printStackTrace();
                showError(e.getMessage());
            }
        }
    }

    private void setCompressedImage() {
        Bitmap finalImage =BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
        finalImage = getResizedBitmap(finalImage,actualImageView.getHeight(),actualImageView.getWidth());
        actualImageView.setImageBitmap(finalImage);
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());
        Log.d("Compressor", "image size is " +finalImage.getByteCount());
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}


