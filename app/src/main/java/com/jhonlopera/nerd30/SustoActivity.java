package com.jhonlopera.nerd30;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SustoActivity extends AppCompatActivity {

    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_susto);
        player = MediaPlayer.create(this, R.raw.grito);
        //player.setLooping(true);
        player.start();


    }
}
