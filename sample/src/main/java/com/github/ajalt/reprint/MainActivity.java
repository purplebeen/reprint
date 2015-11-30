package com.github.ajalt.reprint;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.ajalt.library.AuthenticationFailureReason;
import com.github.ajalt.library.AuthenticationListener;
import com.github.ajalt.library.Reprint;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements AuthenticationListener {
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final TextView hardwarePresent = (TextView) findViewById(R.id.hardware_present);
        final TextView fingerprintsRegistered = (TextView) findViewById(R.id.fingerprints_registered);
        result = (TextView) findViewById(R.id.result);

        hardwarePresent.setText(String.valueOf(Reprint.instance().isHardwarePresent()));
        fingerprintsRegistered.setText(String.valueOf(Reprint.instance().hasFingerprintRegistered()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("listening");
                Reprint.instance().authenticate(MainActivity.this);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Reprint.instance().cancelAuthentication();
        result.setText("cancelled");
    }

    @Override
    public void onSuccess() {
        result.setText("success");
    }

    @Override
    public void onFailure(int fromModule, AuthenticationFailureReason failureReason, int errorCode, @Nullable CharSequence errorMessage) {
        if (errorMessage != null) {
            result.setText(errorMessage);
        } else {
            result.setText("failed: " + failureReason + " (" + errorCode + ')');
        }
    }
}
