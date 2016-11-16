package com.example.boris.watch_list;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class showMovieInfo extends AppCompatActivity {
    private String title;
    private String showInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie_info);

        Bundle extras = getIntent().getExtras();
        showInfo = extras.getString("movie_info");
        setMovieInfo(showInfo);
    }

    private void setMovieInfo(String stringInfo) {

        try {

            // convert string into json object
            JSONObject jsonObj = new JSONObject(stringInfo);

            // obtain necessary information from the json object
            title = jsonObj.getString("Title");
            String year = jsonObj.getString("Year");
            String dateOfRelease = jsonObj.getString("Released");
            String runtime = jsonObj.getString("Runtime");
            String genre = jsonObj.getString("Genre");
            String director = jsonObj.getString("Director");
            String writer =jsonObj.getString("Writer");
            String actors = jsonObj.getString("Actors");
            String plot = jsonObj.getString("Plot");
            String posterimage = jsonObj.getString("Poster");
            String metascore = jsonObj.getString("Metascore");
            String imdbrating = jsonObj.getString("imdbRating");
            String imdbVotes = jsonObj.getString("imdbVotes");
            String imdbId = jsonObj.getString("imdbID");
            String type = jsonObj.getString("Type");

            // obtain the textview Id's
            TextView titleTextView = (TextView) findViewById(R.id.textViewTitle);
            TextView yeargenretime = (TextView)findViewById(R.id.textViewyeargenretime);
            TextView plotView = (TextView)findViewById(R.id.textViewPlot);
            TextView relDateView = (TextView)findViewById(R.id.textViewReldate);
            TextView actorsView = (TextView)findViewById(R.id.textViewActors);
            TextView directorsView = (TextView)findViewById(R.id.textViewDirectors);
            TextView writersView = (TextView)findViewById(R.id.textViewWriters);
            TextView imdbScoreView = (TextView)findViewById(R.id.textViewImdbscore);
            TextView imdbVotesView = (TextView)findViewById(R.id.textViewImdbvotes);
            TextView imdbIdView = (TextView)findViewById(R.id.textViewImdbsId);
            TextView metaScoreView = (TextView)findViewById(R.id.textViewMetascore);
            TextView typeView = (TextView)findViewById(R.id.textViewtype);

            // set textview's
            titleTextView.setText(title);
            yeargenretime.setText(year + " \u2022 " + genre + " \u2022 " + runtime);
            plotView.setText(plot);
            relDateView.setText(Html.fromHtml("<b> Release date: </b> <br>" + dateOfRelease));
            actorsView.setText(Html.fromHtml("<b> Actors: </b> <br>" + actors));
            directorsView.setText(Html.fromHtml("<b> Directors: </b> <br>" + director));
            writersView.setText(Html.fromHtml( "<b> Writers: </b> <br>" + writer));
            imdbScoreView.setText(Html.fromHtml("<b> Imdb rating: </b> <br>" + imdbrating));
            imdbVotesView.setText(Html.fromHtml("<b> Imdb votes: </b> <br>" + imdbVotes));
            imdbIdView.setText(Html.fromHtml("<b> Imdb ID: </b> <br>" + imdbId));
            metaScoreView.setText(Html.fromHtml("<b> MetaScore: </b> <br>" + metascore));
            typeView.setText(Html.fromHtml("<b> Type: </b> <br>" + type));

            // obtain the image url using asynctask again (getposterImage is a class beneath this class)
            new getPosterImage((ImageView) findViewById(R.id.imageViewMovie)).execute(posterimage);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // onclick event if user wants to add this movie
    public void addList(View view) {

        Intent addToList = new Intent(this, watchList.class);

        // title of movie will be viewed in the listview
        addToList.putExtra("title_for_list", title);

        // adds another string to determine what the previous activity was for next activity (watchList-activity)
        addToList.putExtra("activity", "add_movie");
        startActivity(addToList);
        finish();

        Toast toast = Toast.makeText(this, "Movie added!", Toast.LENGTH_SHORT);
        toast.show();
    }
}

class getPosterImage extends AsyncTask<String, Void, Bitmap> {
    ImageView posterImage;

    public getPosterImage(ImageView bmImage) {
        this.posterImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {

        // the passed url
        String stringurlOfImage = urls[0];
        URL urlOfImage = null;

        try {
            // making new url using the passed string url
            urlOfImage = new URL(stringurlOfImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Bitmap bmpOfPoster = null;
        try {
            // saving image url into end result
            bmpOfPoster = BitmapFactory.decodeStream(urlOfImage.openConnection().getInputStream());
            return bmpOfPoster;

        } catch (IOException e) {
            e.printStackTrace();
        }
        // if image could not be obtained
        return null;
    }

    protected void onPostExecute(Bitmap result) {
        // setting the image to an imageview where getPosterImage() is called
        posterImage.setImageBitmap(result);
    }
}
