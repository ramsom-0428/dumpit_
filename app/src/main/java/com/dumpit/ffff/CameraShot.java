package com.dumpit.ffff;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
public class CameraShot extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private static final int REQUEST_IMAGE_CAPTURE= 672;
    private String imageFilePath;
    private Uri photoUri;
   int points = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        int permissionCheck = ContextCompat.checkSelfPermission(CameraShot.this, Manifest.permission.CAMERA);
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


            findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException e) {

                        }

                        if (photoFile != null) {

                            photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

                        }

                    }
                }
            });


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View view) {
                            File photoFile = null;
                            String email = user.getEmail();
                            int index = email.indexOf("@");
                            String id = email.substring(0, index);
                            String web = email.substring(index + 1);
                            int webidx = web.indexOf(".");
                            String website = web.substring(0, webidx);

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time = simpleDateFormat.format(System.currentTimeMillis());

                            points = snapshot.child("users").child(id + "_" + website).child("Totalpoint").getValue(Integer.class) + 50;

                                    databaseReference.child("users").child(id + "_" + website).child("point").child(time).setValue("폐의약품 1p");
                                    databaseReference.child("users").child(id + "_" + website).child("Totalpoint").setValue(points);
                                    Toast.makeText(getApplicationContext(), "50p 적립!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                        }
                    });
                }

                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegress(exifOrientation);

            } else {
                exifDegree = 0;
            }

            ((ImageView) findViewById(R.id.iv_result)).setImageBitmap(rotate(bitmap, exifDegree));
        }
    }

    private int exifOrientationToDegress(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(),bitmap.getHeight(), matrix, true);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };

}