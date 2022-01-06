package com.example.floppy.ui.aboutfriend;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.EditText;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.R;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.User;
import com.example.floppy.databinding.FragmentAboutFriendBinding;
import com.example.floppy.ui.factory.DialogFactory;


public class AboutFriendFragment extends Fragment implements AboutFriendView{
    public  static User                friend;
    public  static FriendEntity        friendEntity;
    private AboutFriendPresenter       aboutFriendPresenter;
    private FragmentAboutFriendBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_about_friend, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aboutFriendPresenter = new AboutFriendPresenterImpl(getContext(),this);
        binding.btnUpdateFriend.setOnClickListener(v->{ aboutFriendPresenter.updateFriend(friendEntity.idFriend); });

        setInfo(view.getContext());
        Triangle triangle = new Triangle(view.getContext());
        binding.triangleContainer.addView(triangle);
    }

    private void setInfo(Context context) {
        if(!friend.getPhoto().equals("")){
            Glide.with(context)
                    .load(friend.getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imgFriend);
        }
        if (friendEntity!=null){ binding.setFriendEntity(friendEntity);}
        binding.setFriend(friend);
    }

    @Override
    public void showDialog(String idFriend) {
        Dialog dialog = DialogFactory.getInstance().getDialog(getContext(), DialogFactory.TypeDialog.SET_NICK);
        EditText nick = dialog.findViewById(R.id.txtNickFriend);
        dialog.findViewById(R.id.btnAccept).setOnClickListener(v->{
            if(aboutFriendPresenter.updateNick(nick.getText().toString().trim(), idFriend)){
                dialog.dismiss();
            }
        });
        dialog.show();
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
            path.moveTo(0, getHeight());
            path.lineTo(getWidth(), getHeight());
            path.lineTo(getWidth(), getHeight() * 0.85f);
            canvas.drawPath(path, paint);
        }
    }
}
