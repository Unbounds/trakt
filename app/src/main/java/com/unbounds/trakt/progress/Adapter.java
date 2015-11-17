package com.unbounds.trakt.progress;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unbounds.trakt.R;
import com.unbounds.trakt.api.model.response.WatchedShow;

import java.util.List;

/**
 * Created by maclir on 2015-11-17.
 */
class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final List<WatchedShow> mWatchedShows;

    public Adapter(final List<WatchedShow> watchedShows) {
        mWatchedShows = watchedShows;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WatchedShow watchedShow = mWatchedShows.get(position);
        holder.mTextView.setText(watchedShow.getShow().getTitle());
    }

    @Override
    public int getItemCount() {
        return mWatchedShows.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextView;

        public ViewHolder(final View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.progress_item_title);
        }
    }
}
