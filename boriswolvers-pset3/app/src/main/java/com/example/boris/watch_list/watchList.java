package com.example.boris.watch_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class watchList extends AppCompatActivity {
    private String title_for_list;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private ListView listViewMovies;
    private SharedPreferences prefs;
    private Bundle extras;
    public static final String PREFERENCES = "movies_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        // obtain movie title user wants to add to watch list
        extras = getIntent().getExtras();
        String from_activity = extras.getString("activity");
        setList(from_activity);
    }

    private void setList(String from_activity) {

        if (from_activity.equals("add_movie")) {
            title_for_list = extras.getString("title_for_list");

            // write to sharedpreferences
            prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            // save a movie title into sharedpreferences where key is same as value (value doesnt matter
            // as I am only interested in the key)
            editor.putString(title_for_list, title_for_list);
            editor.commit();
        }
        // need to be called again if the activity comes from the Main activity
        prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        // find the listview and create empty array which will contain movie titles
        listViewMovies = (ListView) findViewById(R.id.listView);
        String[] movies = {};

        // arraylist to pass the adapter
        arrayList = new ArrayList<>(Arrays.asList(movies));

        // adapter will fill the listview with movie titles
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textview, arrayList);

        // get all the keys in sharedpreferences and add them to arraylist
        Map<String, ?> keys = prefs.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            arrayList.add(entry.getKey());
        }

        // sorting the listview
        adapter.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });

        // setting the listview
        listViewMovies.setAdapter(adapter);

        // when a list item is clicked -> remove from listview and sharedpreference
        listViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // selected item string
                String movie = (String) adapterView.getItemAtPosition(i);

                // remove from listview
                adapter.remove(adapter.getItem(i));

                // remove from sharedpreference
                prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove(movie);
                editor.apply();

                Toast toast = Toast.makeText(watchList.this, "Movie deleted!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        if (adapter.getCount() > 0 ) {
            Toast toast = Toast.makeText(this, "Click on movie to delete from watch list!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // saving key value pair -> activity main, so whenever the screen is rotated it is just the
        // same as the previous activity was main
        outState.putString("activity", "main");
    }

    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);

        // obtaining string
        String from_activity = inState.getString("activity");

        // setting the listview
        setList(from_activity);
    }
}