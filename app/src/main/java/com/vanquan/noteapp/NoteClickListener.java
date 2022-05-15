package com.vanquan.noteapp;

import androidx.cardview.widget.CardView;

import com.vanquan.noteapp.Models.Notes;

public interface NoteClickListener {

    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
