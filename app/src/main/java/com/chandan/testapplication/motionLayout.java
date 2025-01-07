package com.chandan.testapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chandan.testapplication.databinding.ActivityMotionLayoutBinding;

public class motionLayout extends AppCompatActivity {
    private ActivityMotionLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMotionLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}