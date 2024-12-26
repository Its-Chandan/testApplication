package com.chandan.testapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private View overlayView;
    private ScrollView nestedScrollView;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        nestedScrollView = findViewById(R.id.nestedScrollView);
        overlayView = findViewById(R.id.overlayView);
        image = findViewById(R.id.img);
        overlayView.setVisibility(View.INVISIBLE); // Initially hidden
        image.setVisibility(View.VISIBLE);      // PNG always visible

        setupScrollBehavior();

    }
    private void setupScrollBehavior() {
        // Initially hide the overlayView
        overlayView.setVisibility(View.INVISIBLE);

        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Calculate transparency based on scroll position
                float progress = Math.min(1, (float) scrollY / 300f);

                if (scrollY > 0) {
                    // Make overlayView visible as soon as scrolling starts
                    overlayView.setVisibility(View.VISIBLE);
                    overlayView.setAlpha(progress);
                } else {
                    // Keep overlayView hidden at the top
                    overlayView.setVisibility(View.INVISIBLE);
                }

                // Adjust Lottie animation visibility
                image.setAlpha(1 - progress);
            }
        });
    }

}