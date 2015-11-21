package com.unbounds.trakt.progress;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unbounds.trakt.ApiWrapper;
import com.unbounds.trakt.R;
import com.unbounds.trakt.api.model.Episode;
import com.unbounds.trakt.api.model.request.WatchedItems;
import com.unbounds.trakt.api.model.response.AddHistory;
import com.unbounds.trakt.api.model.response.WatchedProgress;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.functions.Action1;

/**
 * Created by maclir on 2015-11-17.
 */
class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final List<WatchedProgress> mWatchedProgresses = new ArrayList<>();
    private final Set<Integer> mSelectedPositions = new HashSet<>();
    private final Context mContext;

    Adapter(final Context context) {
        mContext = context;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_item, parent, false);
        return new ViewHolder(view, new ViewHolder.OnClicked() {
            @Override
            public void onCheckClicked(final int position) {
                mSelectedPositions.add(position);
                final WatchedProgress watchedProgress = mWatchedProgresses.get(position);
                final Episode episode = watchedProgress.getNextEpisode();
                ApiWrapper.postWatchedItems(new WatchedItems.Builder().addEpisode(episode).create()).subscribe(new Action1<AddHistory>() {
                    @Override
                    public void call(final AddHistory addHistory) {
                        //TODO check if added episode exists in not_added
                        ApiWrapper.getWatchedProgress(watchedProgress.getShow().getIds().getTrakt()).subscribe(new Action1<WatchedProgress>() {
                            @Override
                            public void call(final WatchedProgress newWatchedProgress) {
                                mSelectedPositions.remove(position);
                                if (newWatchedProgress.isCompleted()) {
                                    mWatchedProgresses.remove(position);
                                    notifyItemRemoved(position);
                                } else {
                                    newWatchedProgress.setShow(watchedProgress.getShow());
                                    mWatchedProgresses.set(position, newWatchedProgress);
                                    notifyItemChanged(position);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WatchedProgress watchedProgress = mWatchedProgresses.get(position);
        holder.mShowTitle.setText(watchedProgress.getShow().getTitle());
        holder.mEpisodeTitle.setText(watchedProgress.getNextEpisode().getTitle());
        Picasso.with(mContext).load(watchedProgress.getShow().getImages().getPoster().getThumb()).into(holder.mShowPoster);
        holder.mCheck.setSelected(mSelectedPositions.contains(position));
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
        public interface OnClicked {
            void onCheckClicked(final int position);
        }

        private final TextView mShowTitle;
        private final TextView mEpisodeTitle;
        private final ImageView mShowPoster;
        private final View mCheck;
        private int position;

        public ViewHolder(final View view, final OnClicked listener) {
            super(view);
            mShowPoster = (ImageView) view.findViewById(R.id.progress_item_show_poster);
            mShowTitle = (TextView) view.findViewById(R.id.progress_item_show_title);
            mEpisodeTitle = (TextView) view.findViewById(R.id.progress_item_episode_title);
            mCheck = view.findViewById(R.id.progress_item_check);
            mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (mCheck.isSelected()) return;
                    mCheck.setSelected(true);
                    listener.onCheckClicked(position);
                }
            });
        }
    }
}
