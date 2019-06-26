package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //Keeps track of the Strings to display in the ListView
    ArrayList<String> items;


    ArrayAdapter<String> itemsAdapter;

    //This resembles the ListView that we have on the Activity
    ListView lvItems;

    //Numeric code used to identify the edit activity.
    public static final int EDIT_REQUEST_CODE = 20;

    //Keys used for passing data between activities.
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";



    //Function that runs when the Activity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        readItems();

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                items);

        lvItems = (ListView) findViewById(R.id.listViewItems); //R class here DIFFERENT from Android.R

        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();

    } // end onCreate

    //Function that gets called when the Button is pressed (Connected in the XML File)
    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.editText);

        String itemText = etNewItem.getText().toString();

        itemsAdapter.add(itemText);

        etNewItem.setText(R.string.generic_textField_text);

        writeItems();

        Toast.makeText(getApplicationContext(), "Item added to list!", Toast.LENGTH_SHORT).show();


    } // end onAddItem

    //Private since it'll only be called internally
    private void setupListViewListener(){

        Log.i("MainActivityJose","Setting up Listener on ListView");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("MainActivityJose", "Item Removed from List at Position: " + i );
                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;

            } // end onItemLongClick

        }); // end ItemLongClickListener

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //First parameter is context, second is class of activity to launch
                Intent clickIntent = new Intent(MainActivity.this, EditItemActivity.class);

                clickIntent.putExtra(ITEM_TEXT, items.get(i) );
                clickIntent.putExtra(ITEM_POSITION, i);

                startActivityForResult(clickIntent, EDIT_REQUEST_CODE);

            }
        }); // end onItemClickListener

    }


    //The following functions are used for persistence
        //Allows the user to save the list through multiple uses of the application


    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset() ) );
        } catch (IOException e) {
            Log.e("MainActivityJose","Error Reading File :((");
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivityJose","Error Writing File :((");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {

            String updatedItem = data.getExtras().getString(ITEM_TEXT);

            int position = data.getExtras().getInt(ITEM_POSITION, 0);

            items.set(position, updatedItem);

            itemsAdapter.notifyDataSetChanged();

            writeItems();

            Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show();

        } // end if


    } // end onActivityResult



}
