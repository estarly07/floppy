package com.example.floppy.ui.mastercontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.data.Models.Estado;
import com.example.floppy.data.Models.Estado_User;
import com.example.floppy.data.Models.User;
import com.example.floppy.ui.Chat.ChatActivity;
import com.example.floppy.ui.factory.DialogFactory;
import com.example.floppy.ui.message.MessageFragment;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.ui.global_presenter.GlobalPresenterImpl;
import com.example.floppy.R;
import com.example.floppy.ui.GlobalView;
import com.example.floppy.ui.menu.MenuFragment;
import com.example.floppy.utils.Animations;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class MasterControl extends AppCompatActivity implements GlobalView {

    public static GlobalPresenter presenter;
    public GlobalView             globalView = this;
    public static Activity        activity;
    public static User            user;
    public static String          stickersReceiver;
    public BroadcastReceiver      broadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new GlobalPresenterImpl(this, this, this);
        activity  = this;
        setContentView(R.layout.activity_master_control);
        if(!stickersReceiver.equals("")){
            presenter.insertStickers(stickersReceiver);
        }
        Button btnChats = findViewById(R.id.btnChats);
        presenter.updateState(Estado_User.ONLINE);
        findViewById(R.id.btnAddFriend).setOnClickListener(view -> {
            MenuFragment.getCallbackNavigationFragments().navigateTo();
        });

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

    }

    /**
     * ACCION PARA PASAR O DEVOLVER LOS ESTADOS
     */
    public interface CallBack {
        void pasarImagen();

        void volverImagen();
    }

    CallBack callBack;
    private int contador = 0;

    @Override
    public void showStateDialog(ArrayList<Estado> estados) {
        RelativeLayout dialogoEstado = findViewById(R.id.include_estado);
        ImageView imagenEstado = findViewById(R.id.imgEstado);
        TextView txtMensage = findViewById(R.id.txtMensajeEstado);
        LottieAnimationView animLike = findViewById(R.id.animLike);

        Button btnIzq = findViewById(R.id.btnEstadoIzq);
        Button btnCenter = findViewById(R.id.btnEstadoCenter);
        Button btnDer = findViewById(R.id.btnEstadoRight);

        ProgressBar progressEstado = findViewById(R.id.progressEstado);
        contador = 0;
        animAparecer(dialogoEstado);
        progressEstado.setProgress(0);
        View[] botones = new View[]{btnIzq, btnCenter, btnDer};

        callBack = new CallBack() {
            @Override
            public void pasarImagen() {
                contador++;
                mostrarEstados(estados.get(contador), txtMensage, progressEstado, botones, dialogoEstado, imagenEstado, estados.get(estados.size() - 1) == estados.get(contador), callBack, animLike);
            }

            @Override
            public void volverImagen() {
                contador--;
                mostrarEstados(estados.get(contador), txtMensage, progressEstado, botones, dialogoEstado, imagenEstado, estados.get(estados.size() - 1) == estados.get(contador), callBack, animLike);
            }
        };
        mostrarEstados(estados.get(contador), txtMensage, progressEstado, botones, dialogoEstado, imagenEstado, estados.get(estados.size() - 1) == estados.get(contador), callBack, animLike);
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
    public void showHandlingGeneral(boolean show) {
        ProgressBar progressBar = findViewById(R.id.progressGeneral);
        if (show)
            animAparecer(progressBar);
        else
            animDesaparecer(progressBar);
    }


    private void mostrarEstados(Estado estado, TextView txtMensaje, ProgressBar progressEstado, View[] botones, View dialogoEstado,
                                ImageView imagenEstado, boolean finish, CallBack callBack, LottieAnimationView like) {
        like.setVisibility(View.GONE);

        progressEstado.setProgress(0);
        Glide.with(this)
                .load(estado.getImagen())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagenEstado);

        txtMensaje.setText(estado.getMensaje());
        CountDownTimer countDownTimer = new CountDownTimer(10_000, 1) {
            @Override
            public void onTick(long l) {
                progressEstado.setProgress((int) l);

            }

            @Override
            public void onFinish() {
                if (finish) {
                    animDesaparecer(dialogoEstado);
                } else {
                    callBack.pasarImagen();
                }
                cancelar(this);

            }
        }.start();

        botones[0].setOnClickListener(view -> {

            if (contador > 0) {

                callBack.volverImagen();
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
                callBack.pasarImagen();
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
        if (show) { Animations.Companion.animAppear(findViewById(R.id.btnAddFriend)); }
        else { Animations.Companion.animVanish(findViewById(R.id.btnAddFriend)); }

        Toolbar toolbar = findViewById(R.id.toolbar);
        float moveY = show ? 0 : toolbar.getHeight() * 2;
        toolbar.animate().translationY(moveY).setDuration(300).setStartDelay(100).start();
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
//    @Override
//    protected void onPause() {
//        super.onPause();
//        presenter . updateState(Estado_User.OFFLINE);
//    }

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
        System.out.println(".onBackPressed");
        super.onBackPressed();
    }
}