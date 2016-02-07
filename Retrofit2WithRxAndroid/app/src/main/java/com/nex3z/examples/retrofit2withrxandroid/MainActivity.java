package com.nex3z.examples.retrofit2withrxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nex3z.examples.retrofit2withrxandroid.app.App;
import com.nex3z.examples.retrofit2withrxandroid.model.Movie;
import com.nex3z.examples.retrofit2withrxandroid.rest.service.MovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ListView mListView;
    private ArrayAdapter<String> mMovieAdapter;
    private List<Movie> mMovies;
    private Subscription mGetMovieSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.list);

        MovieService movieService = App.getRestClient().getMovieService();

        mGetMovieSub = movieService.getMovies(MovieService.SORT_BY_POPULARITY_DESC)
                .timeout(5, TimeUnit.SECONDS)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            mMovies = response.getMovies();
                            Log.v(LOG_TAG, "mMovies size = " + mMovies.size());
                            updateListView();
                        },
                        throwable -> {
                            Toast.makeText(MainActivity.this,
                                    throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGetMovieSub != null) {
            mGetMovieSub.unsubscribe();
        }
    }

    private void updateListView() {
        List<String> listContent = new ArrayList<>();
        for (Movie movie : mMovies) {
            listContent.add(movie.toString());
        }

        mMovieAdapter = new ArrayAdapter<>(
                MainActivity.this,
                R.layout.list_item,
                R.id.list_item_textview,
                listContent);
        mListView.setAdapter(mMovieAdapter);
    }
}
