package com.unbounds.trakt.progress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.unbounds.trakt.R;

public class ProgressActivity extends AppCompatActivity {

    public static Intent createIntent(final Activity activity) {
        return new Intent(activity, ProgressActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
    }
}
