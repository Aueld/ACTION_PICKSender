package com.example.action_picksenderdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 카메라 사용 허가 획득
        requestPermission();
    }

    public void onCapture(View view) {
        try {
            //사진을 저장할 파일 이름 만들기
            File file = createImageFile();
            takePhoto(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String prefix = "photo";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //파일 이름 만들기
        File file = File.createTempFile(
                prefix, /*파일 이름 앞부분*/
                ".png", /*파일 이름 뒷부분*/
                dir /*디렉토리*/
        );
        return file;
    }

    private static final int REQUEST_TAKE_PHOTO = 1;
    private Uri currentUri;

    private void takePhoto(File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //카메라앱이 있는지 확인한다
        if (intent.resolveActivity(getPackageManager()) != null) {
            currentUri = FileProvider.getUriForFile(
                    this, /*context*/
                    "com.example.android.fileprovider", /*권한*/
                    file /*파일*/
            );
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentUri);
        //캡쳐하고 저장
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    //이 메서드는 사진을 화면에 출력하기 위하여 작성되었다
    //단순히 저장만 할거면 이 메서드는 없어도 된다
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            ImageView iv = findViewById(R.id.image_view);
            iv.setImageURI(currentUri);
        }
    }

    private static final int REQUEST_PERMISSION = 1;

    public void requestPermission() {
        //이미 허가 되었는지 알아본다.
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
        //허가 요청 항목
            String[] permissions = new String[]{
                    Manifest.permission.CAMERA
            };
        //허가를 요청한다.
            ActivityCompat.requestPermissions(this, permissions,
                    REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] result) {
        super.onRequestPermissionsResult(requestCode, permissions, result);
        if (requestCode == REQUEST_PERMISSION) {
            if (result.length == 0 ||
                    result[0] != PackageManager.PERMISSION_GRANTED) {
        //허가 되지 않음
                finish();
            }
        }
    }
}