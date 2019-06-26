package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {


    EditText etItemText;

    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItemText = (EditText) findViewById(R.id.etItemText);

        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT) );

        position = getIntent().getIntExtra(ITEM_POSITION, 0);

        getSupportActionBar().setTitle("Edit Item");




    }

    public void onSaveItem(View v){

        //Prepares the Intent that we will pass back to MainActivity
        Intent data = new Intent();

        //Pass UPDATED item text and ORIGINAL item position (modify the correct object in our list)
        data.putExtra(ITEM_TEXT, etItemText.getText().toString() );
        data.putExtra(ITEM_POSITION, position);

        //Set result code and bundle data for response
        setResult(RESULT_OK, data);

        //Close EditActivity and pass Intent back to MainActivity
        finish();

    } // end onSaveItem


}
