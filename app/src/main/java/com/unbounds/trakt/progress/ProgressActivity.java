package com.unbounds.trakt.progress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

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

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final Adapter adapter = new Adapter(this);
        recyclerView.setAdapter(adapter);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.progress_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.moonlight_blue,
                R.color.boogie_green,
                R.color.space_gray,
                R.color.cool_gray
        );
        final Action1<WatchedShow[]> loadShowsAction = new Action1<WatchedShow[]>() {
            @Override
            public void call(final WatchedShow[] watchedShows) {
                adapter.clear();
                final List<Observable<WatchedProgressWrapper>> observables = new ArrayList<>(watchedShows.length);
                for (final WatchedShow watchedShow : watchedShows) {
                    observables.add(ApiWrapper.getWatchedProgress(watchedShow.getShow().getIds().getTrakt()).map(new Func1<WatchedProgress, WatchedProgressWrapper>() {
                        @Override
                        public WatchedProgressWrapper call(final WatchedProgress watchedProgress) {
                            return new WatchedProgressWrapper(watchedProgress, watchedShow.getShow());
                        }
                    }));
                }

                Observable.concatEager(observables).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }).subscribe(new Action1<WatchedProgressWrapper>() {
                    @Override
                    public void call(final WatchedProgressWrapper watchedProgressWrapper) {
                        if (!watchedProgressWrapper.getWatchedProgress().isCompleted()) {
                            adapter.add(watchedProgressWrapper);
                        }
                    }
                });
            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiWrapper.getWatchedShows().subscribe(loadShowsAction);
            }
        });
        ApiWrapper.getWatchedShows().subscribe(loadShowsAction);
    }
}
