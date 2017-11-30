package com.jhonlopera.nerd30;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class SustoActivity extends AppCompatActivity {

    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_susto);
        player = MediaPlayer.create(this, R.raw.grito);
        //player.setLooping(true);
        player.start();

    }

    @Override
    public void onBackPressed() {
        player.stop();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        player.stop();
        super.onPause();
    }
}
