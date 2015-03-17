// MainActivity.java
// Sets MainActivity's layout
package com.deitel.doodlz;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;


public class MainActivity extends Activity
{
   private final int GET_USER_IMAGE_FROM_GALLERY = 10;
   
   

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      // determine screen size
      int screenSize = 
         getResources().getConfiguration().screenLayout & 
         Configuration.SCREENLAYOUT_SIZE_MASK;
      
      // use landscape for extra large tablets; otherwise, use portrait
      if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
         setRequestedOrientation(
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      else
         setRequestedOrientation(
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
   }
   
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {

   if (requestCode == GET_USER_IMAGE_FROM_GALLERY) {

           if (data != null) {

               Uri selectedImageUri = data.getData();
               String selectedImagePath = getPath(selectedImageUri);

               try {
                   File imageFile = new File(selectedImagePath);
                   Bitmap bitmap = BitmapFactory.decodeFile(imageFile
                           .getAbsolutePath());
                   DoodleView imageView = (DoodleView)findViewById(R.id.doodleView);
                   imageView.setBackgoundColor(Color.WHITE);
                   
                   imageView.setBitmap(bitmap);
                   imageView.invalidate();
               } catch (Exception e) {
            	   e.printStackTrace();
               }

           }
   }
   }
   /*
   private String getPath(Uri selectedImageUri) {

       String[] projection = { MediaStore.Images.Media.DATA };
       Cursor cursor = getContentResolver().query(selectedImageUri,
               projection, null, null, null);
       int column_index = cursor
               .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
       cursor.moveToFirst();
       return cursor.getString(column_index);
   }
   */
 //  @SuppressLint("NewApi")
   @SuppressLint("NewApi") private String getPath(Uri uri) {
       if( uri == null ) {
           return null;
       }

       String[] projection = { MediaStore.Images.Media.DATA };

       Cursor cursor;
       if(Build.VERSION.SDK_INT >19)
       {
           // Will return "image:x*"
           String wholeID = DocumentsContract.getDocumentId(uri);
           // Split at colon, use second item in the array
           String id = wholeID.split(":")[1];
           // where id is equal to             
           String sel = MediaStore.Images.Media._ID + "=?";

           cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
                                         projection, sel, new String[]{ id }, null);
       }
       else
       {
           cursor = getContentResolver().query(uri, projection, null, null, null);
       }
       String path = null;
       try
       {
           int column_index = cursor
           .getColumnIndex(MediaStore.Images.Media.DATA);
           cursor.moveToFirst();
           path = cursor.getString(column_index).toString();
           cursor.close();
       }
       catch(NullPointerException e) {

       }
       return path;
   }
   
   public void selectImageForBack()
   {
	   Intent intent = new Intent();
	   intent.setType("image/*");
	   intent.setAction(Intent.ACTION_GET_CONTENT);
	   startActivityForResult(Intent.createChooser(intent,
	           "Select Picture"),
	   GET_USER_IMAGE_FROM_GALLERY);
	   
	   
   }
   
} // end class MainActivity

/**************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 **************************************************************************/
