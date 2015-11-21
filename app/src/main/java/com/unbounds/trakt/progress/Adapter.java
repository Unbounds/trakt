package com.unbounds.trakt.progress;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.unbounds.trakt.R;
import com.unbounds.trakt.api.model.response.WatchedProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maclir on 2015-11-17.
 */
class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final List<WatchedProgress> mWatchedProgresses = new ArrayList<>();
    private final Context mContext;

    Adapter(final Context context) {
        mContext = context;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_item, parent, false);
        return new ViewHolder(view, new ViewHolder.OnWatched() {
            @Override
            public void onWatched(final int position) {
                Toast.makeText(parent.getContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WatchedProgress watchedProgress = mWatchedProgresses.get(position);
        holder.mShowTitle.setText(watchedProgress.getShow().getTitle());
        holder.mEpisodeTitle.setText(watchedProgress.getNextEpisode().getTitle());
        Picasso.with(mContext).load(watchedProgress.getShow().getImages().getPoster().getThumb()).into(holder.mShowPoster);
        holder.position = position;
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
        public interface OnWatched {
            void onWatched(final int position);
        }

        private final TextView mShowTitle;
        private final TextView mEpisodeTitle;
        private final ImageView mShowPoster;
        private int position;

        public ViewHolder(final View view, final OnWatched listener) {
            super(view);
            mShowPoster = (ImageView) view.findViewById(R.id.progress_item_show_poster);
            mShowTitle = (TextView) view.findViewById(R.id.progress_item_show_title);
            mEpisodeTitle = (TextView) view.findViewById(R.id.progress_item_episode_title);
            view.findViewById(R.id.progress_item_check).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    listener.onWatched(position);
                }
            });
        }
    }
}
