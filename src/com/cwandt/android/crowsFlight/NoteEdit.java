

package com.cwandt.android.crowsFlight;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteEdit extends Activity {
	NotesDbAdapter mDbHelper;
	private EditText mTitleText;
    private EditText mLatText;
    private EditText mLonText;

    public Long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
    
    mDbHelper= new NotesDbAdapter(this);
    mDbHelper.open();
     
    setContentView(R.layout.note_edit);
     
    mTitleText = (EditText) findViewById(R.id.title);
    mLatText = (EditText) findViewById(R.id.lat);
    mLonText = (EditText) findViewById(R.id.lon);

    Button confirmButton = (Button) findViewById(R.id.confirm);
     
    mRowId = savedInstanceState != null ? savedInstanceState.getLong(NotesDbAdapter.KEY_ROWID) 
                                        : null;
    if (mRowId == null) {
        Bundle extras = getIntent().getExtras();
        mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID) 
                                : null;
    }
     
    populateFields();
     
    confirmButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View view) {
            setResult(RESULT_OK);
            finish();
        }
         
    });
    }
    
    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
    	            note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            mLatText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_LAT)));
            mLonText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_LON)));
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    
    private void saveState() {
        String title = mTitleText.getText().toString();
        String lat = mLatText.getText().toString();
        String lon = mLonText.getText().toString();

        if (mRowId == null) {
            long id = mDbHelper.createNote(title, lat, lon);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, title, lat, lon);
        }
    }
    
}
