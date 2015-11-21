package com.unbounds.trakt.progress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.unbounds.trakt.ApiWrapper;
import com.unbounds.trakt.R;
import com.unbounds.trakt.api.model.response.WatchedProgress;
import com.unbounds.trakt.api.model.response.WatchedShow;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
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
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        ApiWrapper.getWatchedShows().subscribe(new Action1<WatchedShow[]>() {
            @Override
            public void call(final WatchedShow[] watchedShows) {
                adapter.clear();
                final List<Observable<WatchedProgress>> observables = new ArrayList<>(watchedShows.length);
                for (final WatchedShow watchedShow : watchedShows) {
                    observables.add(ApiWrapper.getWatchedProgress(watchedShow.getShow().getIds().getTrakt()));
                }
                Observable.merge(observables).subscribe(new Action1<WatchedProgress>() {
                    @Override
                    public void call(final WatchedProgress watchedProgress) {
                        if (!watchedProgress.isCompleted()) {
                            adapter.add(watchedProgress);
                        }
                    }
                });
            }
        });
    }
}
