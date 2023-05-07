package com.basetechz.showbox.I_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basetechz.showbox.F_Adapter.Recycler_Movie_Parent_Adapter;
import com.basetechz.showbox.H_MVVMPaterns.ViewModel.FirebaseViewModel;
import com.basetechz.showbox.R;
import com.basetechz.showbox.databinding.ActivityVideoBinding;
import com.basetechz.showbox.G_models.parent_model;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class VideoActivity extends AppCompatActivity {
    ActivityVideoBinding binding;
    FirebaseFirestore db;

    ImageView exoLock;
    ImageView exoReplay;
    ImageView exoPlay;
    ImageView exoPause;
    ImageView exoForward;
    TextView exoPosition;
    TextView exoDuration;
    ImageView fullScreen;
    ProgressBar progressBar;
    SimpleExoPlayer player ;
    int currentPosition;
    Boolean isFullScreen = false;
    Boolean isLock = false;
    PlayerView playerView;
    ImageView back;

    float fingerSpacing;
    PlayerView playerView1;


    private FirebaseViewModel firebaseViewModel;
    Recycler_Movie_Parent_Adapter parent_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String itemId =  getIntent().getStringExtra("itemId");
        String itemTxt = getIntent().getStringExtra("itemTxt");
        String videoUrl = getIntent().getStringExtra("videoUrl");
        binding.videoTxt.setText(itemTxt);

        exoLock = findViewById(R.id.exo_lock);
        back = findViewById(R.id.back);
        exoReplay = findViewById(R.id.exo_replay);
        exoPlay = findViewById(R.id.exo_play);
        exoPause = findViewById(R.id.exo_pause);
        exoForward = findViewById(R.id.exo_forward);
        exoPosition = findViewById(R.id.exo_position);
        exoDuration = findViewById(R.id.exo_duration);
        fullScreen = findViewById(R.id.bt_fullScreen);
        progressBar = findViewById(R.id.progressBar);
        player= new SimpleExoPlayer.Builder(VideoActivity.this).build();
        playerView1 = findViewById(R.id.players);

        // create object of database
        db = FirebaseFirestore.getInstance();


        // Create a MediaSource object from the video URL
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        if (mediaItem != null) {
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(
                    new DefaultDataSourceFactory(VideoActivity.this)).createMediaSource(mediaItem);

            // Prepare the player with the media source and start playing
            player.setMediaSource(mediaSource);
            player.prepare();
            player.play();

            // Find the PlayerView element in the XML layout
            playerView = findViewById(R.id.players);

            // Set the player to the PlayerView
            playerView.setPlayer(player);
        } else {
            Toast.makeText(this, "Unable to play video", Toast.LENGTH_SHORT).show();
            finish(); // close the activity
        }



        // set progress bar
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState == Player.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                }else if(playbackState == Player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // Forward 5 sec by click exo_forward button
        exoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition += 10000; // 5000 milliseconds = 5 seconds
               player.seekTo(currentPosition);
            }
        });

        // Backward 5 Sec by click exo_backward button
        exoReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition -= 10000; // 5000 milliseconds = 5 seconds
                if(currentPosition<0){
                    currentPosition =0;
                }
                player.seekTo(currentPosition);
            }
        });

        // Full Screen Controller
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFullscreen(!isFullScreen);
                isFullScreen = !isFullScreen;
            }
        });

//        lockButton Click and Screen Lock
        exoLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLock){
                    exoLock.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_lock_24));

                }else{
                    exoLock.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_lock_open_24));

                }
                isLock = !isLock;
                lockScreen(isLock);
            }
        });

        // back from current activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // MVVM Architecture
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(VideoActivity.this));
        parent_adapter = new Recycler_Movie_Parent_Adapter(VideoActivity.this);
        binding.recyclerView.setAdapter(parent_adapter);
        firebaseViewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        firebaseViewModel.setReference("ForYou");
        firebaseViewModel.getAllData();

        firebaseViewModel.getParentModelMutableLiveData().observe(this, new Observer<List<parent_model>>() {
            @Override
            public void onChanged(List<parent_model> parentModelList) {
                parent_adapter.setParentItemList(parentModelList);
                parent_adapter.notifyDataSetChanged();
            }
        });

        firebaseViewModel.getDatabaseErrorMutableLiveData().observe(this, new Observer<DatabaseError>() {
            @Override
            public void onChanged(DatabaseError error) {
                Toast.makeText(VideoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void setFullscreen(boolean isFullscreen) {
        if (isFullscreen) {
            // Set activity to landscape mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            fullScreen.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_fullscreen_exit_24));

            // Calculate screen dimensions in landscape mode
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenHeight = displayMetrics.widthPixels;
            int screenWidth = displayMetrics.heightPixels;

            // Set height and width of player view to match screen dimensions
            ViewGroup.LayoutParams params = playerView.getLayoutParams();
            params.height = screenHeight;
            params.width = screenWidth;
            playerView.setLayoutParams(params);
        } else {
            // Set activity to portrait mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            fullScreen.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_fullscreen_24));

            // Reset height and width of player view to original values
            ViewGroup.LayoutParams params = playerView.getLayoutParams();
            params.height = (int) getResources().getDimension(com.intuit.sdp.R.dimen._180sdp);
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(params);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release player resources when activity is destroyed
        player.release();
    }

    private void lockScreen(boolean isLock) {
        LinearLayout mid = findViewById(R.id.sec_controlVid1);
        LinearLayout button = findViewById(R.id.sec_controlVid2);
        if (isLock) {
            mid.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
        } else {
            mid.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null) {
            player.stop();
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // stop video when ready
        player.setPlayWhenReady(false);
        // Get playback state
        player.getPlaybackState();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        // Play video when ready
        player.setPlayWhenReady(true);
        // Get playback state
        player.getPlaybackState();

    }
}