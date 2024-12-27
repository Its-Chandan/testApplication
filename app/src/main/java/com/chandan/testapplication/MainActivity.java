package com.chandan.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import com.chandan.testapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private View overlayView;
    private ScrollView nestedScrollView;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        nestedScrollView = findViewById(R.id.nestedScrollView);
        overlayView = findViewById(R.id.overlayView);
        image = findViewById(R.id.img);
        overlayView.setVisibility(View.INVISIBLE); // Initially hidden
        image.setVisibility(View.VISIBLE);      // PNG always visible

        setupScrollBehavior();

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ActivityImagePalette.class));
            }
        });

    }

    private void setupScrollBehavior() {
        // Initially hide the overlayView and set the image alpha to full (fully visible)
        overlayView.setVisibility(View.INVISIBLE);
        image.setAlpha(1f);

        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Calculate progress: a value between 0 and 1 based on the scroll position
                float progress = Math.min(1f, (float) scrollY / 300f);

                if (scrollY > 0) {
                    // Show the overlayView as scrolling begins
                    overlayView.setVisibility(View.VISIBLE);
                    overlayView.setAlpha(progress);

                    // Simultaneously reduce image alpha for fade-out effect
                    image.setAlpha(1 - progress);
                } else {
                    // Hide the overlayView when scroll is at the top
                    overlayView.setVisibility(View.INVISIBLE);

                    // Reset image to fully visible at the top
                    image.setAlpha(1f);
                }
            }
        });
    }
    /*   private void setupScrollBehavior() {
       // Initially hide the overlayView
       overlayView.setAlpha(0f); // Fully transparent initially
       overlayView.setVisibility(View.INVISIBLE);

       nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
           @Override
           public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
               // Calculate scroll progress (0 -> 1 based on 300px of scroll)
               float progress = Math.min(1, (float) scrollY / 300f);

               if (scrollY > 0) {
                   overlayView.setVisibility(View.VISIBLE);
                   overlayView.setAlpha(1); // Ensure full visibility

                   // Create gradient mask for bottom-to-top reveal effect
                   ShapeDrawable gradient = new ShapeDrawable(new RectShape());

                   // A linear gradient that fades from transparent (bottom) to solid color (top)
                   LinearGradient shader = new LinearGradient(
                           0, overlayView.getHeight(),  // Start at the bottom
                           0, 0,  // End at the top
                           new int[]{0x00FF0000, 0x80FF0000},  // Transparent to Red with alpha
                           new float[]{1f - progress, 1f},  // Fraction stops: Fades in from the bottom as scroll progresses
                           Shader.TileMode.CLAMP
                   );
                   gradient.getPaint().setShader(shader);

                   overlayView.setBackground(gradient);  // Apply gradient to overlay
               } else {
                   overlayView.setVisibility(View.INVISIBLE);
                   overlayView.setAlpha(0f);
                   overlayView.setBackground(null); // Reset gradient
               }

               // Optionally fade out the image
               image.setAlpha(1 - progress);
           }
       });
   }*/

    /*private void setupScrollBehavior() {
        // Initially hide the overlayView
        overlayView.setAlpha(0f); // Fully transparent initially
        overlayView.setVisibility(View.INVISIBLE);

        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Calculate scroll progress (0 -> 1 based on 300px of scroll)
                float progress = Math.min(1, (float) scrollY / 300f);

                if (scrollY > 0) {
                    overlayView.setVisibility(View.VISIBLE);
                    overlayView.setAlpha(1); // Ensure full visibility

                    // Create gradient mask for top-to-bottom fade effect
                    ShapeDrawable gradient = new ShapeDrawable(new RectShape());

                    // A linear gradient that fades from transparent (top) to solid color (bottom)
                    LinearGradient shader = new LinearGradient(
                            0, 0, // Start at the top
                            0, overlayView.getHeight(),  // End at the bottom
                            new int[]{0x00FF0000, 0x80FF0000}, // Transparent to Semi-transparent red
                            new float[]{0f, progress},  // Fraction stops: Fade from top to bottom as progress increases
                            Shader.TileMode.CLAMP
                    );
                    gradient.getPaint().setShader(shader);

                    overlayView.setBackground(gradient);  // Apply gradient to overlay
                } else {
                    overlayView.setVisibility(View.INVISIBLE);
                    overlayView.setAlpha(0f);
                    overlayView.setBackground(null); // Reset gradient
                }

                // Optionally fade out the image
                image.setAlpha(1 - progress);
            }
        });
    }*/

    /*private void setupScrollBehavior() {
        // Initially hide the overlayView
        overlayView.setAlpha(0f); // Fully transparent initially
        overlayView.setVisibility(View.INVISIBLE);

        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Calculate scroll progress (0 -> 1 based on 300px of scroll)
                float progress = Math.min(1, (float) scrollY / 300f);

                if (scrollY > 0) {
                    overlayView.setVisibility(View.VISIBLE);
                    overlayView.setAlpha(1); // Ensure full visibility for the color

                    // Create gradient mask for top-to-bottom fade effect (starts light)
                    ShapeDrawable gradient = new ShapeDrawable(new RectShape());

                    // LinearGradient that starts with 100% transparency (fully clear) at the top,
                    // and gradually fades in with a light color (like semi-transparent red) from top to bottom
                    LinearGradient shader = new LinearGradient(
                            0, 0, // Start at the top
                            0, overlayView.getHeight(),  // End at the bottom
                            new int[]{0x00FF0000, 0x80FF0000}, // Transparent to light semi-transparent red
                            new float[]{0f, progress}, // Fraction stops (starts at 0 and ends at 'progress' as you scroll down)
                            Shader.TileMode.CLAMP
                    );
                    gradient.getPaint().setShader(shader);

                    overlayView.setBackground(gradient);  // Apply gradient to overlay
                } else {
                    overlayView.setVisibility(View.INVISIBLE);
                    overlayView.setAlpha(0f);
                    overlayView.setBackground(null); // Reset gradient
                }

                // Optionally fade out the image (keep image visible during scroll)
                image.setAlpha(1 - progress);
            }
        });
    }*/


}


