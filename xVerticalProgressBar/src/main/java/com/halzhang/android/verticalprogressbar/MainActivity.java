package com.halzhang.android.verticalprogressbar;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements MyVerticalSeekBar.OnStateChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MyVerticalSeekBar mMyVerticalSeekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyVerticalSeekBar =  (MyVerticalSeekBar) findViewById(R.id.myVerticalBar);
        mMyVerticalSeekBar.setOnStateChangeListener(this);

    }



    @Override
    public void OnStateChangeListener(View view, float progress) {
        Log.d(TAG, "OnStateChangeListener: "+progress);


    }

    @Override
    public void onStopTrackingTouch(View view, float progress) {

    }
}
