package com.example.todoapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList items;
    ArrayAdapter<String> itemsAdapter;
    ListView LvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        LvItems=(ListView) findViewById(R.id.LvItems);
        LvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void onAddItem(View v)
    {
        EditText NewItemTodo =(EditText) findViewById(R.id.NewItemTodo);
        String itemText = NewItemTodo.getText().toString();
        if(itemText.isEmpty())
        {
           Toast toast= Toast.makeText(getApplicationContext(), "Can't be blank", Toast.LENGTH_SHORT);
           View view = toast.getView();
           view.setBackgroundColor(Color.RED);
           toast.show();
        }
        else
        {
            itemsAdapter.add(itemText);
            NewItemTodo.setText("");
            writeItems();
            Toast toast= Toast.makeText(getApplicationContext(), "Item added to the list", Toast.LENGTH_SHORT);
            View view = toast.getView();
            view.setBackgroundColor(Color.BLUE);
            toast.show();
        }


    }

    private void setupListViewListener()
    {
        Log.i("MainActivity", "Setting up listener on list view");
        LvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.i("MainActivity", "Item removed from list" + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    private File getDataFile()
    {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems()
    {
        try {
            items=new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error reading the file", e);
            items = new ArrayList<>();
        }
    }

    public void writeItems()
    {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing the file", e);
        }
    }
}
