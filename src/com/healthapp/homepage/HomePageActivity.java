package com.healthapp.homepage;

import com.healthapp.mainpage.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;
import android.widget.Button;
import android.net.Uri;
import android.media.MediaPlayer;
import android.content.Intent;
import android.util.Log;
import android.widget.RelativeLayout;

public class HomePageActivity extends BaseActivity {

   private static final String TAG = "HomePageActivity";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.homepage_activity);
      VideoView videoView = (VideoView) findViewById(R.id.video_view);
      videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.healthapp_background));
    //   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
    //     RelativeLayout.LayoutParams.MATCH_PARENT,
    //     RelativeLayout.LayoutParams.MATCH_PARENT);
    //   videoView.setLayoutParams(layoutParams);
      videoView.setScaleX(1);
      videoView.setScaleY(1);    
      videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
         @Override
         public void onPrepared(MediaPlayer mp) {
             mp.setLooping(true);
         }
      });     
      videoView.start();

      Button exitButton = findViewById(R.id.exit_button);

      exitButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v){
            finish();
         }
      });
      
      Button startButton = findViewById(R.id.start_button);

      startButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
             startActivity(intent);
         }
     });
   }
}