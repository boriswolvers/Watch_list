package com.example.boris.watch_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // user enters a movie title, this title will be passed to obtainmovieInformation-activity
    public void getdata(View view) {
        EditText filledMovie = (EditText)findViewById(R.id.editTextMovie);
        String filledMovieString = filledMovie.getText().toString();
        filledMovie.setText("");

        // make sure user enters a movie
        if (!(filledMovieString.length() == 0)) {
            filledMovieString = filledMovieString.replaceAll(" ", "+");

            Intent obtainmovieinfo= new Intent(this, obtainmovieInformation.class);
            obtainmovieinfo.putExtra("movie_title", filledMovieString);
            startActivity(obtainmovieinfo);
        }
        else {
            Toast toast = Toast.makeText(this, "You did not enter a movie!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // onclick event if user wants to see his/her watch list from the main-activity
    public void gowatchlist(View view) {
        Intent watchlist = new Intent(MainActivity.this, watchList.class);
        watchlist.putExtra("activity", "main");
        startActivity(watchlist);
    }
}
