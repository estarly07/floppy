package com.example.floppy.ui.aboutfriend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.CircularFlow;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.R;
import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Models.User;


public class AboutFriendFragment extends Fragment {
    private RelativeLayout     triangleContainer;
    private ImageView          imgFriend;
    private TextView           nick;
    private TextView           name;
    private TextView           info;
    public static User         friend;
    public static FriendEntity friendEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        triangleContainer = view.findViewById(R.id.triangleContainer);
        imgFriend         = view.findViewById(R.id.imgFriend);
        nick              = view.findViewById(R.id.txtNick);
        name              = view.findViewById(R.id.txtName);
        info              = view.findViewById(R.id.txtInfo);

        setInfo(view.getContext());
        Triangle triangle = new Triangle(view.getContext());
        triangleContainer.addView(triangle);
    }

    private void setInfo(Context context) {
        Glide.with(context)
                .load(friend.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgFriend);

        if (friendEntity!=null){ nick.setText(friendEntity.nick); }
        name.setText(friend.getName());
        info.setText(friend.getMessageUser());
    }

    private class Triangle extends View {
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
            path.moveTo(0, getHeight());
            path.lineTo(getWidth(), getHeight());
            path.lineTo(getWidth(), getHeight() * 0.85f);
            canvas.drawPath(path, paint);
        }
    }
}
