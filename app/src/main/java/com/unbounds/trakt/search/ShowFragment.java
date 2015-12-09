package com.unbounds.trakt.search;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unbounds.trakt.ApiWrapper;
import com.unbounds.trakt.R;
import com.unbounds.trakt.api.model.Season;

import rx.functions.Action1;

/**
 * Created by Evelina Vorobyeva on 11/01/16.
 */
public class ShowFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_show, container, false);

        ShowWrapper show = (ShowWrapper) getArguments().getSerializable("show");
        Picasso.with(getActivity()).load(show.getShow().getImages().getBanner().getFull()).into((ImageView) view.findViewById(R.id.fragment_show_poster));
        ((TextView) view.findViewById(R.id.fragment_show_description)).setText(show.getShow().getOverview());
        ApiWrapper.getSeasonsEpisodes(show.getShow().getIds().getSlug()).subscribe(new Action1<Season[]>() {
            @Override
            public void call(Season[] seasons) {

            }
        });
        return view;
    }

}

