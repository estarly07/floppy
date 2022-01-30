package com.example.floppy.ui.about;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.R;
import com.example.floppy.databinding.FragmentAboutMeBinding;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.aboutfriend.AboutFriendFragment;

public class AboutMeFragment extends Fragment {
    private FragmentAboutMeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_about_me, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(view.getContext())
                .load(User.getInstance().getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgUser);
        binding.setUser(User.getInstance());
        Triangle triangle = new Triangle(view.getContext());
        binding.triangleContainer.addView(triangle);
    }

    private static class Triangle extends View {
        public Triangle(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.fondo));
            paint.setStyle(Paint.Style.FILL);

            @SuppressLint("DrawAllocation") Path path = new Path();
            path.moveTo(getWidth(), getHeight());
            path.lineTo(getWidth() * 0.62f,    getHeight());
            path.lineTo(getWidth() * 0.62f, getHeight()*0.95f);
            path.lineTo(getWidth() * 0.67f, getHeight()*0.95f);
            path.lineTo(getWidth() * 0.67f, getHeight()*0.9f );
            path.lineTo(getWidth() * 0.72f, getHeight()*0.9f );
            path.lineTo(getWidth() * 0.72f, getHeight()*0.85f );
            path.lineTo(getWidth() * 0.77f, getHeight()*0.85f );
            path.lineTo(getWidth() * 0.77f, getHeight()*0.8f);
            path.lineTo(getWidth() * 0.82f, getHeight()*0.8f);
            path.lineTo(getWidth() * 0.82f, getHeight()*0.75f );
            path.lineTo(getWidth() * 0.87f, getHeight()*0.75f );
            path.lineTo(getWidth() * 0.87f, getHeight()*0.7f);
            path.lineTo(getWidth() * 0.93f, getHeight()*0.7f);
            path.lineTo(getWidth() * 0.93f, getHeight()*0.65f );
            path.lineTo(   getWidth()        , getHeight()*0.65f );

            canvas.drawPath(path, paint);
        }
    }
}