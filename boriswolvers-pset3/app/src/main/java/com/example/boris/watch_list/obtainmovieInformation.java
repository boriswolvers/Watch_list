package com.example.boris.watch_list;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class obtainmovieInformation extends AppCompatActivity {

    private String filledMovieTitle;
    private TextView filledMovieLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtainmovie_information);

        // obtain the movie title from user, pass it to asynctask
        Bundle extras = getIntent().getExtras();
        filledMovieTitle = extras.getString("movie_title");
        new obtainInformation().execute(filledMovieTitle);

    }

    private class obtainInformation extends AsyncTask<String, Void, String> {

        // declaring variable for urlconnection
        private HttpURLConnection urlConnection;

        // this function retrieves information of the url-link
        @Override
        protected String doInBackground(String... params) {
            try {

                // params[0] is the title user entered, pass this to the url
                URL urlOfMovie = new URL("http://www.omdbapi.com/?t=" + params[0] + "&plot=full&r=json");

                try {
                    // makes a connection with the provided url-link
                    urlConnection = (HttpURLConnection) urlOfMovie.openConnection();

                    // reads from the source -> json object in this case
                    BufferedReader readInfo = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder totalInformation = new StringBuilder();
                    String line;

                    // add line per line from json to a string
                    while ((line = readInfo.readLine()) != null) {
                        totalInformation.append(line + "\n"); //.append('\n');
                    }

                    readInfo.close();

                    // this string will be passed to onpostexecute
                    return totalInformation.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // if catch occures, return null -> pass to onpostexecute
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObj = null;

            try {

                // the passed result is a string -> convert it to json object
                jsonObj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String respons = null;

            try {
                // the json object has a key-value pair of -> response:true or response:false
                respons = jsonObj.getString("Response");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // if the value of response equals false then give following toast message to the user
            if (respons.equals("False")) {
                Toast toast = Toast.makeText(obtainmovieInformation.this, "Movie not found! Be specific!", Toast.LENGTH_SHORT);
                toast.show();

                // redirect to main-activity where user can enter another movie
                Intent redirectMain = new Intent(obtainmovieInformation.this, MainActivity.class);
                startActivity(redirectMain);
                finish();
            }
            else {
                // if the movie is found, pass entire string(containing info) to showmovieInfo-activity
                Intent showInfo = new Intent(obtainmovieInformation.this, showMovieInfo.class);
                showInfo.putExtra("movie_info", result);
                startActivity(showInfo);
                finish();
            }
        }

        @Override
        protected void onPreExecute() {
            // if internet connection is slow a toast message is pleasant
            Toast toast = Toast.makeText(obtainmovieInformation.this, "Wait untill information is obtained...", Toast.LENGTH_SHORT);
            toast.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
