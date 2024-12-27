package com.chandan.testapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.RenderEffect;
import android.graphics.RenderEffect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chandan.testapplication.databinding.ActivityImagePaletteBinding;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityImagePalette extends AppCompatActivity {

    private ActivityImagePaletteBinding binding;
    private MediaMetadataRetriever retriever;
    private Timer timer;

    private static final long FRAME_INTERVAL_MS = 500;

    private String url1 = "https://images.pexels.com/photos/807598/pexels-photo-807598.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
    private String url2 = "https://images.pexels.com/photos/33109/fall-autumn-red-season.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";

    private String videoPath = "https://videos.pexels.com/video-files/8941332/8941332-sd_640_360_25fps.mp4";
    private int currentPaletteIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityImagePaletteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Button Listeners
        binding.urlButton1.setOnClickListener(v -> loadImageAndSetBackground(url1));
        binding.urlButton2.setOnClickListener(v -> loadImageAndSetBackground(url2));

        setupVideoPlayer(videoPath);

        //
    }

    private void loadImageAndSetBackground(String imageUrl) {
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        binding.imageView.setImageBitmap(resource);

                        Palette.from(resource).generate(palette -> {
                            if (palette != null) {
                                // Default fallback color
                                int colorToSet = ContextCompat.getColor(ActivityImagePalette.this, android.R.color.black);

                                // Palette profiles
                                int[] paletteColors = {
                                        palette.getDominantColor(colorToSet),
                                        palette.getVibrantColor(colorToSet),
                                        palette.getLightVibrantColor(colorToSet),
                                        palette.getDarkVibrantColor(colorToSet),
                                        palette.getMutedColor(colorToSet),
                                        palette.getLightMutedColor(colorToSet),
                                        palette.getDarkMutedColor(colorToSet)
                                };

                                // Apply the current profile color
                                colorToSet = paletteColors[currentPaletteIndex];

                                binding.mainRl.setBackgroundColor(colorToSet);

                                // Display Toast with palette type
                                String[] paletteNames = {
                                        "Dominant", "Vibrant", "Light Vibrant", "Dark Vibrant",
                                        "Muted", "Light Muted", "Dark Muted"
                                };
                                Toast.makeText(ActivityImagePalette.this,
                                        "Palette: " + paletteNames[currentPaletteIndex],
                                        Toast.LENGTH_SHORT).show();

                                // Cycle to the next palette profile
                                currentPaletteIndex = (currentPaletteIndex + 1) % paletteNames.length;
                            }
                        });
                    }
                });
    }

    private void setupVideoPlayer(String videoPath) {
        binding.videoView.setVideoPath(videoPath);

        binding.videoView.setOnPreparedListener(mp -> {
            binding.videoView.start();
            initializeBackgroundEffect(videoPath);
        });

        binding.videoView.setOnCompletionListener(mp -> binding.videoView.start());
    }

    private void initializeBackgroundEffect(String videoPath) {
        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (binding.videoView.isPlaying()) {
                    updateBackgroundEffect();
                }
            }
        }, 0, FRAME_INTERVAL_MS);  // Repeat every 500ms
    }

    private void updateBackgroundEffect() {
        try {
            long currentPosition = binding.videoView.getCurrentPosition() * 1000;
            Bitmap currentFrame = retriever.getFrameAtTime(currentPosition);

            if (currentFrame != null) {
                // Generate blurred effect
                Bitmap blurredBitmap = applyBlur(currentFrame);

                // Extract colors and update UI
                Palette.from(currentFrame).generate(palette -> {
                    if (palette != null) {
                        int vibrantColor = palette.getVibrantColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                        runOnUiThread(() -> {
                            // Update background with blurred bitmap and vibrant color
                            binding.getRoot().setBackground(new BitmapDrawable(getResources(), blurredBitmap));
                        });
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap applyBlur(Bitmap inputBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Use RenderEffect for Android 12 and above (SDK 31+)
//            RenderEffect renderEffect = RenderEffect.createBlurEffect(20f, 20f);
            Bitmap blurredBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
            Canvas canvas = new Canvas(blurredBitmap);
            canvas.drawBitmap(inputBitmap, 0, 0, null);
//            canvas.drawBitmap(inputBitmap, 0, 0, new Paint(renderEffect));
            return blurredBitmap;
        } else {
            // Use RenderScript fallback for versions below Android 12
            return applyRenderScriptBlur(inputBitmap, 10f);
        }
    }

    private Bitmap applyRenderScriptBlur(Bitmap inputBitmap, float radius) {
        RenderScript rs = RenderScript.create(this);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation inputAlloc = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation outputAlloc = Allocation.createTyped(rs, inputAlloc.getType());
        blurScript.setRadius(radius);
        blurScript.setInput(inputAlloc);
        blurScript.forEach(outputAlloc);
        outputAlloc.copyTo(inputBitmap);
        rs.destroy();
        return inputBitmap;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (retriever != null) {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            retriever = null;
        }
    }
}

