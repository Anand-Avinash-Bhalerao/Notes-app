package com.example.notes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDAO noteDAO();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(instance).execute();
        }
    };
    private static class PopulateAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDAO noteDAO;
        private PopulateAsyncTask(NoteDatabase db){
            noteDAO = db.noteDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.insert(new Note("Anand","Me",1));
            noteDAO.insert(new Note("Akshay","Brother",2));
            noteDAO.insert(new Note("Amrapali","Mummy",3));
            noteDAO.insert(new Note("Avinash","Papa",4));
            return null;
        }
    }
}
