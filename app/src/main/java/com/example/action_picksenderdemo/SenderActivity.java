package com.example.action_picksenderdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class SenderActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sender);
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1);
                for (File f : dir.listFiles())
                        adapter.add(f.getName());
                ListView lv = findViewById(R.id.image_list);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(this);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int I,
                                long l) {
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                String filename = ((TextView) view).getText().toString();
                File file = new File(dir, filename);
                Uri uri = FileProvider.getUriForFile(
                        this,/*context*/
                        "com.example.android.fileprovider",/*권한*/
                        file/*파일*/
                );
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, getContentResolver().getType(uri));
                setResult(RESULT_OK, intent);
                finish();
        }

        public void onCancel(View view) {
                setResult(RESULT_CANCELED);
                finish();
        }
}