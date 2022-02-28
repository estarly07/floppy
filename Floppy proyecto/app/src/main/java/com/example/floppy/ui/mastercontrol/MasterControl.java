package com.example.floppy.ui.mastercontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.airbnb.lottie.LottieAnimationView;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.databinding.ActivityMasterControlBinding;
import com.example.floppy.domain.models.Estado;
import com.example.floppy.domain.models.Estado_User;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.Chat.ChatActivity;
import com.example.floppy.ui.factory.DialogFactory;
import com.example.floppy.ui.message.MessageFragment;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.ui.global_presenter.GlobalPresenterImpl;
import com.example.floppy.R;
import com.example.floppy.ui.GlobalView;
import com.example.floppy.ui.menu.MenuFragment;
import com.example.floppy.ui.message.MessagePresenter;
import com.example.floppy.utils.Animations;
import com.example.floppy.utils.global.GlobalUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class MasterControl extends AppCompatActivity implements GlobalView {

    public static GlobalPresenter presenter;
    public GlobalView             globalView = this;
    public static Activity        activity;
    public static User            user;
    public static String          stickersReceiver;
    public BroadcastReceiver      broadcastReceiver = null;
    private ActivityMasterControlBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new GlobalPresenterImpl(this, this, this);
        activity  = this;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_master_control);
        binding.setRoute(GlobalUtils.Routes.MENU);

        if(!stickersReceiver.equals("")){
            presenter.insertStickers(stickersReceiver);
        }
        presenter.updateState(Estado_User.ONLINE);
        binding.btnAddFriend.setOnClickListener(view -> {
            MenuFragment.getCallbackNavigationFragments().navigateTo();
        });
        binding.btnSettings.setOnClickListener(view -> binding.setRoute(GlobalUtils.Routes.ME));
        binding.btnHome.setOnClickListener(view -> binding.setRoute(GlobalUtils.Routes.MENU));

        binding.btnAddFriend.setOnClickListener(view -> MenuFragment.getCallbackNavigationFragments().navigateTo());
    }

    @Override
    public void showAlertDialog() {
        Dialog dialog = DialogFactory.getInstance().getDialog(this, DialogFactory.TypeDialog.ADD_ALL_STICKER);
        dialog.setCancelable(false);
        Button btnInsertStickers = dialog.findViewById(R.id.btnAccept);
        Button btnCancel         = dialog.findViewById(R.id.btnCancel);
        btnInsertStickers.setOnClickListener(view -> {
            Animations.Companion.animVanish(dialog.findViewById(R.id.btnAccept));
            Animations.Companion.animAppear(dialog.findViewById(R.id.progress));
            presenter.addAllStickers(stickersReceiver,dialog );
        });
        btnCancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showToast(String msg) {
        binding.messageCustom.setMsg(msg);
        Animations.Companion.animAppearAndVanish(binding.messageCustom.getRoot());
    }

    /**
     * ACCION PARA PASAR O DEVOLVER LOS ESTADOS
     */
    public interface CallBack {
        void nextImage();

        void backImage();
    }

    CallBack callBack;
    private int contador = 0;

    @Override
    public void showStateDialog(ArrayList<Estado> states) {
        contador = 0;

        animAparecer(binding.includeEstado.getRoot());
        binding.includeEstado.progressEstado.setProgress(0);
        View[] buttons = new View[]{
                binding.includeEstado.btnEstadoIzq,
                binding.includeEstado.btnEstadoCenter,
                binding.includeEstado.btnEstadoRight};

        callBack = new CallBack() {
            @Override
            public void nextImage() {
                contador++;
                showStates(states.get(contador),
                        binding.includeEstado.txtMensajeEstado,
                        binding.includeEstado.progressEstado,
                        buttons,
                        binding.includeEstado.getRoot(),
                        binding.includeEstado.imgEstado,
                        states.get(states.size() - 1) == states.get(contador), callBack,
                        binding.includeEstado.animLike);
            }

            @Override
            public void backImage() {
                contador--;
                showStates(states.get(contador),
                        binding.includeEstado.txtMensajeEstado,
                        binding.includeEstado.progressEstado,
                        buttons,
                        binding.includeEstado.getRoot(),
                        binding.includeEstado.imgEstado,
                        states.get(states.size() - 1) == states.get(contador), callBack,
                        binding.includeEstado.animLike);
            }
        };
        showStates(states.get(contador),
                binding.includeEstado.txtMensajeEstado,
                binding.includeEstado.progressEstado,
                buttons,
                binding.includeEstado.getRoot(),
                binding.includeEstado.imgEstado,
                states.get(states.size() - 1) == states.get(contador), callBack,
                binding.includeEstado.animLike);
    }

    @Override
    public void showUsesDialog(User friend) {
        Dialog dialog = DialogFactory.getInstance().getDialog(this, DialogFactory.TypeDialog.SHOW_PHOTO_USER);
        RoundedImageView imgFriend = dialog.findViewById(R.id.imgDialogoUser);
        Glide.with(this)
                .load(friend.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( imgFriend);
        dialog.show();
    }



    @Override
    public void showHandlingGeneral(boolean show, String title) {
        if (show) animAparecer(binding.progressGeneral);
        else animDesaparecer(binding.progressGeneral);
    }

    private  CountDownTimer countDownTimer;
    private void showStates(Estado estado, TextView txtMensaje, ProgressBar progressEstado, View[] botones, View dialogoEstado,
                            ImageView imagenEstado, boolean finish, CallBack callBack, LottieAnimationView like) {
        like.setVisibility(View.GONE);

        progressEstado.setProgress(0);
        Glide.with(this)
                .load(estado.getImagen())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagenEstado);

        txtMensaje.setText(estado.getMensaje());
        countDownTimer = new CountDownTimer(10_000, 1) {
            @Override
            public void onTick(long l) {
                progressEstado.setProgress((int) l);

            }

            @Override
            public void onFinish() {
                if (finish) {
                    animDesaparecer(dialogoEstado);
                } else {
                    callBack.nextImage();
                }
                cancelar(this);

            }
        }.start();

        botones[0].setOnClickListener(view -> {

            if (contador > 0) {

                callBack.backImage();
                cancelar(countDownTimer);
            }
        });
        botones[1].setOnClickListener(view -> {
            like.setVisibility(View.VISIBLE);
            like.playAnimation();

        });

        botones[2].setOnClickListener(view -> {
            if (finish) {
                animDesaparecer(dialogoEstado);
            } else {
                callBack.nextImage();
            }
            cancelar(countDownTimer);
        });
    }


    private void cancelar(CountDownTimer countDownTimer) {
        countDownTimer.cancel();
    }


    private void animAparecer(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_aparecer);
        view.setVisibility(View.VISIBLE);
        view.setAnimation(animation);
    }


    public void animToolbar(boolean show) {
        if (show) { Animations.Companion.animAppear(binding.btnAddFriend); }
        else { Animations.Companion.animVanish(binding.btnAddFriend); }

        Toolbar toolbar = findViewById(R.id.toolbar);
        float moveY = show ? 0 : toolbar.getHeight() * 2;
        toolbar.animate().translationY(moveY).setDuration(300).setStartDelay(100).start();
    }

    @Override
    public void showImage(boolean send, Uri image) {

    }

    @Override
    public void showContacts(ArrayList<User> users) {

    }

    @Override
    public void nextActivity() {
        Intent intent = new Intent(MasterControl.this, ChatActivity.class);
        startActivity(intent);
    }

    @Override
    public void beginDownload(BroadcastReceiver broadcastReceiver) {
        registerReceiver(broadcastReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void recordAudio(String name, String idChat, User friend, MessagePresenter messagePresenter) {

    }

    @Override
    public void stopAudio() {

    }

    @Override
    public void getMessage(String idChat, User friend, MessagePresenter messagePresenter) {

    }

    private void animDesaparecer(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_desaparecer);
        view.setVisibility(View.GONE);
        view.setAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter . updateState(Estado_User.ONLINE);
    }
    @Override
    protected void onPause() {
        super.onPause();
        presenter . updateState(Estado_User.OFFLINE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
        presenter.updateState(Estado_User.OFFLINE);
    }

    @Override
    public void onBackPressed() {
        if(MessageFragment.presenter!=null){ MessageFragment.presenter.cancelListener(); }

        if(binding.includeEstado.getRoot().getVisibility() == View.VISIBLE){
            Animations.Companion.animVanish(binding.includeEstado.getRoot());
            cancelar(countDownTimer);
        }else{ super.onBackPressed(); }
    }
}