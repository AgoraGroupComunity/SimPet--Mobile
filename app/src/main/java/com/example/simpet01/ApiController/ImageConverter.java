package com.example.simpet01.ApiController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageConverter {
    private Context context;
    private TextView textView;
    private ImageView imageView;
    String base64Image;

    public String getSImage() {
        return base64Image;
    }

    public ImageConverter(Context context, TextView textView, ImageView imageView) {
        this.context = context;
        this.textView = textView;
        this.imageView = imageView;
    }

    public void selectImage() {
        // Clear previous data
        //textView.setText("");
        //imageView.setImageBitmap(null);
        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Set type
        intent.setType("image/*");
        // Start activity result
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check condition
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            // When the result is OK, initialize URI
            Uri uri = data.getData();
            // Initialize Bitmap
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                // Determine the size of the square that you want (1:1 ratio)
                int size = Math.min(width, height);
                // Calculate the coordinates for cropping (if necessary)
                int left = (width - size) / 2;
                int top = (height - size) / 2;

                // Create a square (1:1) bitmap from the original image
                Bitmap squareBitmap = Bitmap.createBitmap(bitmap, left, top, size, size);

                // Create a square (1:1) bitmap from the original image
                //Bitmap squareBitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);

                // Initialize byte stream
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress Bitmap
                squareBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                // Initialize byte array
                byte[] bytes = stream.toByteArray();
                // Get base64 encoded string
                base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                // Set encoded text on TextView
                //textView.setText(base64Image);
                // Set the image on ImageView
                imageView.setImageBitmap(squareBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String encodeImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = Math.min(width, height);
        // Initialize byte stream
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress Bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        // Initialize byte array
        byte[] bytes = stream.toByteArray();
        // Get base64 encoded string
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public Bitmap decodeImage(String sImage) {
        // Decode base64 string
        byte[] bytes = Base64.decode(sImage, Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public String checkUrlImage(String input, String[] substrings) {
        for (String substring : substrings) {
            if (input.contains(substring)) {
                input = input.replace(substring, "");
            }
        }
        return input;
    }
}

