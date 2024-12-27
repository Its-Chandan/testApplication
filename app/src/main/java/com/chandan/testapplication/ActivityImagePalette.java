package com.chandan.testapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chandan.testapplication.databinding.ActivityImagePaletteBinding;

public class ActivityImagePalette extends AppCompatActivity {


    private ActivityImagePaletteBinding binding; // View binding variable

    // Image URLs
    String url1 = "https://images.pexels.com/photos/807598/pexels-photo-807598.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
    String url2 = "https://images.pexels.com/photos/33109/fall-autumn-red-season.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityImagePaletteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Button click listener to load Image 1 and set the background color
        binding.urlButton1.setOnClickListener(v -> loadImageAndSetBackground(url1));

        // Button click listener to load Image 2 and set the background color
        binding.urlButton2.setOnClickListener(v -> loadImageAndSetBackground(url2));
    }

    // Function to load an image using Glide and set the background color using Palette
    /*private void loadImageAndSetBackground(String imageUrl) {
        Glide.with(this)
                .asBitmap() // Load as Bitmap
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        binding.imageView.setImageBitmap(resource); // Set the loaded image into ImageView

                        // Generate palette colors from the image
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                if (palette != null) {
                                    // Get the dominant color
                                    int dominantColor = palette.getDominantColor(ContextCompat.getColor(ActivityImagePalette.this, android.R.color.black));

                                    // Set the background color of RelativeLayout
                                    binding.mainRl.setBackgroundColor(dominantColor);
                                }
                            }
                        });
                    }
                });
    }*/

    private int currentPaletteIndex = 0; // To track which palette to use next

    private void loadImageAndSetBackground(String imageUrl) {
        Glide.with(this)
                .asBitmap() // Load as Bitmap
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        binding.imageView.setImageBitmap(resource); // Set the loaded image into ImageView

                        // Generate palette colors from the image
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                if (palette != null) {
                                    // Default color if no palette is generated
                                    int colorToSet = ContextCompat.getColor(ActivityImagePalette.this, android.R.color.black);

                                    // Array of palette color extraction methods
                                    int[] paletteColors = {
                                            palette.getDominantColor(colorToSet),
                                            palette.getVibrantColor(colorToSet),
                                            palette.getLightVibrantColor(colorToSet),
                                            palette.getDarkVibrantColor(colorToSet),
                                            palette.getMutedColor(colorToSet),
                                            palette.getLightMutedColor(colorToSet),
                                            palette.getDarkMutedColor(colorToSet)
                                    };

                                    // Get the color for the current palette index
                                    colorToSet = paletteColors[currentPaletteIndex];

                                    // Set the background color of RelativeLayout to the chosen color
                                    binding.mainRl.setBackgroundColor(colorToSet);

                                    String[] paletteNames = {
                                            "Dominant", "Vibrant", "Light Vibrant", "Dark Vibrant",
                                            "Muted", "Light Muted", "Dark Muted"
                                    };
                                    Toast.makeText(ActivityImagePalette.this, "Palette: " + paletteNames[currentPaletteIndex], Toast.LENGTH_SHORT).show();

                                    // Update currentPaletteIndex to point to the next palette profile
                                    currentPaletteIndex = (currentPaletteIndex + 1) % paletteNames.length; // Cycle through 7 profiles now
                                }
                            }
                        });
                    }
                });
    }


}