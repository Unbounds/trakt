package com.unbounds.trakt.progress;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unbounds.trakt.R;
import com.unbounds.trakt.api.model.response.WatchedProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maclir on 2015-11-17.
 */
class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final List<WatchedProgress> mWatchedProgresses = new ArrayList<>();

    @Override
    public Adapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WatchedProgress watchedProgress = mWatchedProgresses.get(position);
        holder.mTextView.setText(watchedProgress.getNextEpisode().getTitle());
    }

    @Override
    public int getItemCount() {
        return mWatchedProgresses.size();
    }

    public void add(final WatchedProgress watchedProgress) {
        final int position = mWatchedProgresses.size();
        mWatchedProgresses.add(watchedProgress);
        notifyItemInserted(position);
    }

    public void clear() {
        mWatchedProgresses.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextView;

        public ViewHolder(final View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.progress_item_title);
        }
    }
}
