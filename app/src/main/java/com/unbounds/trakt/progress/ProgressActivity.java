package com.unbounds.trakt.progress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.unbounds.trakt.ApiWrapper;
import com.unbounds.trakt.R;
import com.unbounds.trakt.api.model.response.WatchedShow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

public class ProgressActivity extends AppCompatActivity {

    public static Intent createIntent(final Activity activity) {
        return new Intent(activity, ProgressActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.progress_recycle_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final List<WatchedShow> watchedShows = new ArrayList<>();
        // specify an adapter (see also next example)
        final RecyclerView.Adapter adapter = new Adapter(watchedShows);
        recyclerView.setAdapter(adapter);
        ApiWrapper.getWatchedShows().subscribe(new Action1<WatchedShow[]>() {
            @Override
            public void call(final WatchedShow[] watchedShowsArray) {
                watchedShows.clear();
                Collections.addAll(watchedShows, watchedShowsArray);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
