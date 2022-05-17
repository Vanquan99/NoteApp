package com.vanquan.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vanquan.noteapp.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {
EditText editText_titles, editText_notes;
ImageView imageView_save;
Notes notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);
        getSupportActionBar().hide();

        editText_titles=findViewById(R.id.edit_titles);
        editText_notes=findViewById(R.id.edit_notes);

        imageView_save=findViewById(R.id.img_save);
        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText_titles.getText().toString();
                String descreption = editText_notes.getText().toString();

                if (descreption.isEmpty()){
                    Toast.makeText(NotesTakerActivity.this, "Please add some notes", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MM yyy HH:mm a");
                Date date = new Date();

                notes = new Notes();
                notes.setTitle(title);
                notes.setNotes(descreption);
                notes.setDate(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("Note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}