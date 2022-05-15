package com.vanquan.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vanquan.noteapp.Adapter.NotesListAdapter;
import com.vanquan.noteapp.Database.RoomDB;
import com.vanquan.noteapp.Models.Notes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;

    List<Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);
        
        database =RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();
        
        updateRecycler(notes);
    }

    private void updateRecycler(List<Notes> notes) {
    }
}