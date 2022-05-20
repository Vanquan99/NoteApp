package com.vanquan.noteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vanquan.noteapp.Adapter.NotesListAdapter;
import com.vanquan.noteapp.Database.RoomDB;
import com.vanquan.noteapp.Models.Notes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    SearchView searchView_home;
    Notes selectedNote;

    List<Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);
        searchView_home=findViewById(R.id.search_home);
        
        database =RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();
        
        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent,101);


            }
        });


        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String newText) {
        List<Notes> filterList = new ArrayList<>();
        for (Notes singNote : notes){
            if (singNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                || singNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(singNote);
            }

        }

        notesListAdapter.filterList(filterList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){
            if (requestCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("Note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode==102){
            if (requestCode==Activity.RESULT_OK){
                Notes new_note = (Notes) data.getSerializableExtra("Note"); // putextra
                database.mainDAO().update(new_note.getID(),new_note.getTitle(),new_note.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {

        recyclerView.setHasFixedSize(true);
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes,noteClickListener );

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(notesListAdapter);

    }

    private final NoteClickListener noteClickListener = new NoteClickListener() {
        @Override
        public void onClick(Notes notes) {

            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent,102);

        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {

            selectedNote = new Notes();
            selectedNote = notes;

            showPopup(cardView);


        }
    };

    private void showPopup(CardView cardView) {

        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();


    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.pin:
                if (selectedNote.isPinned()){

                    database.mainDAO().pin(selectedNote.getID(),false);
                    Toast.makeText(MainActivity.this, "Unpined", Toast.LENGTH_SHORT).show();
                }
                else {
                    database.mainDAO().pin(selectedNote.getID(),true);
                    Toast.makeText(MainActivity.this, "Pin", Toast.LENGTH_SHORT).show();

                }

                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                database.mainDAO().delete(selectedNote);
                notes.remove(selectedNote);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return false;
        }
    }
}