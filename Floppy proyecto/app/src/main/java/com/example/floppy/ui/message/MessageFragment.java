package com.example.floppy.ui.message;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.data.Models.User;
import com.example.floppy.ui.Adapters.AdapterSticker;
import com.example.floppy.data.Models.Estado_User;
import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Models.Message;
import com.example.floppy.data.Entitys.StickersEntity;
import com.example.floppy.ui.aboutfriend.AboutFriendFragment;
import com.example.floppy.ui.factory.DialogFactory;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.R;
import com.example.floppy.ui.mastercontrol.MasterControl;
import com.example.floppy.utils.Animations;
import com.example.floppy.utils.Extensions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class MessageFragment extends Fragment implements MessageView {
    public static MessagePresenter presenter;
    public static FriendEntity     friendEntity;
    public static User             user;

    private String           idChat="";
    private GlobalPresenter  presenterMaster;
    private RoundedImageView imgUser;
    private TextView         txtUser, txtOnline,txtOffline;
    private ImageView        btnSend;
    private ImageView        btnOptions;
    private Activity         activity;
    private RecyclerView     recyclerMessages, recyclerStickers;
    private RelativeLayout   keyboardSticker, btnSalir;
    private LinearLayout     layoutInfoFriend;
    private ImageView        btnSticker;
    private EditText         edtMessage;
    private ImageButton      btnAddSticker;
    private Message          message = new Message();//Create just one message and change it its properties
    private View             options;
    private LinearLayout     layoutAnimationOptions;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenterMaster = MasterControl.presenter;
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    public  interface CloseListeners { void closeListeners();}
    private static    CloseListeners closeListeners;
    public  static    CloseListeners getCloseListeners() { return closeListeners; }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenterMaster.showTollbar(false);
        activity  = MasterControl.activity;
        presenter = new MessagePresenterImpl(view.getContext(), activity, this, presenterMaster);

        closeListeners = () -> presenter.cancelListener();

        keyboardSticker  = view.findViewById(R.id.includeSticker);
        layoutInfoFriend = view.findViewById(R.id.layoutInfoFriend);

        imgUser       = view.findViewById(R.id.imgUserChat);
        txtUser       = view.findViewById(R.id.txtNombreUser);
        txtOnline     = view.findViewById(R.id.txtStateOnline);
        txtOffline    = view.findViewById(R.id.txtStateOffline);
        btnSend       = view.findViewById(R.id.btnLogin);
        btnOptions    = view.findViewById(R.id.btnOptions);
        btnSticker    = view.findViewById(R.id.btnSticker);
        edtMessage    = view.findViewById(R.id.edtMensaje);
        btnAddSticker = view.findViewById(R.id.btnShowDialogAddSticker);
        options       = view.findViewById(R.id.containerOptions);

        recyclerStickers       = view.findViewById(R.id.recyclerStickers);
        layoutAnimationOptions = options.findViewById(R.id.layoutOptions);
        recyclerStickers . setHasFixedSize(true);
        recyclerStickers . setLayoutManager(new GridLayoutManager(view.getContext(), 4));

        Extensions.Companion.listenerFocusChange(edtMessage, (view1,showStickers) -> {
            if(showStickers){
                Extensions.Companion.hideKeyboard(view1);
                keyboardSticker.setVisibility(View.VISIBLE);
            }else{ keyboardSticker.setVisibility(View.GONE); }
            return null;
        });

        layoutInfoFriend.setOnClickListener(v->{
            AboutFriendFragment.friend = user;
            if(friendEntity!=null){ AboutFriendFragment.friendEntity = friendEntity; }
            NavHostFragment.findNavController(this).navigate(R.id.action_messageFragment_to_aboutFriendFragment);

        });
        btnOptions.setOnClickListener(v-> showOptions(options,layoutAnimationOptions));

        btnSticker.setOnClickListener(view12 -> {
            if( keyboardSticker.getVisibility() == (View.GONE)){
                edtMessage.clearFocus();
                presenter .getStickers();
            }else{ keyboardSticker.setVisibility(View.GONE); }
        });
        presenter.getStateUser(user.getIdUser());

        btnSend.setOnClickListener(view1 -> {
            message.setMessage(edtMessage.getText().toString().trim());
            message.setTypeMessage(Message.TypesMessages.TEXT);
            if(!idChat.equals("")){
                if(presenter.sendMessages(idChat, message)){ edtMessage.setText(""); }
            }else{ presenter.createChat(user, message); }
        });

        btnAddSticker.setOnClickListener(view13 -> { presenter.showDialogAddSticker(); });

        recyclerMessages = view.findViewById(R.id.reciclerChat);
        recyclerMessages . setHasFixedSize(false);
        recyclerMessages . setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, true));

        if (friendEntity != null){
            idChat = friendEntity.idChat;
            presenter.showMessagesChat(friendEntity);
        }else{
            presenter.searchChat(user);
        }
        presenter.showDataFriend(user.getName(), user.getPhoto());
    }

    Boolean translate = true;
    private void showOptions(View view,LinearLayout layoutAnimation) {
        if(translate){
            layoutAnimation.scheduleLayoutAnimation(); }
        Animations.Companion.animationTranslate(view,translate);
        translate = !translate;
    }


    public void showDataFriend(String nick, String photo) {
        if (!photo.equals(""))
            Glide.with(activity.getApplicationContext())
                    .load(photo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgUser);
        txtUser.setText(nick);
    }


    public void showMessages(ArrayList<Message> messages, String myId, String idChat) {
        activity.runOnUiThread(() -> {
            this.idChat = idChat;
            AdapterMessage adapterMessage = new AdapterMessage(messages, myId);
            adapterMessage.setClick((view, position, message) -> {
               presenter.showDialogAddOrDeleteSticker(message);
            });
//            adapterMensage.setHasStableIds(true);
            recyclerMessages.setAdapter(adapterMessage);
            recyclerMessages.scheduleLayoutAnimation();
        });

    }
    @Override
    public void showDialogAddOrDeleteSticker(Context context, Message message,Boolean delete) {
        if(message.getTypeMessage() == Message.TypesMessages.STICKER) {
            DialogFactory.TypeDialog typeDialog = DialogFactory.TypeDialog.valueOf(message.getTypeMessage().toString());
            Dialog dialog = DialogFactory.getInstance().getDialog(context, typeDialog);
            ImageView imgSticker = dialog.findViewById(R.id.imgStickerDeleteOrADd);
            Button btnAddOrDelete = dialog.findViewById(R.id.btnAddOrDelete);

            if(delete){ btnAddOrDelete.setText(context.getResources().getString(R.string.delete)); }

            btnAddOrDelete.setOnClickListener(v->{
                if(delete){
                    presenter.deleteSticker(message.getMessage());
                }else{
                    presenter.addSticker   (message.getMessage());
                }
            });

            Glide.with(context)
                    .load(message.getMessage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgSticker);
            dialog.show();
        }
    }


    public void showStateUser(String response) {
        activity.runOnUiThread(() -> {
            if (response.equals(Estado_User.ONLINE.toString())) {
                Animations.Companion.animVanish(txtOffline);
                Animations.Companion.animAppear(txtOnline);
            }else{
                Animations.Companion.animAppear(txtOffline);
                Animations.Companion.animVanish(txtOnline);
            }
        });
    }


    public void showStickers(ArrayList<StickersEntity> stickers) {
        activity.runOnUiThread(() -> {
            recyclerStickers.scheduleLayoutAnimation();
            Animations.Companion.animAppear(keyboardSticker);

            AdapterSticker adapterSticker = new AdapterSticker(stickers);
            adapterSticker.setClick((view, stickersEntity) -> {
               message.setTypeMessage(Message.TypesMessages.STICKER);
               message.setMessage(stickersEntity.urlImage);
               presenter.sendMessages(idChat, message);
            });
            recyclerStickers.setAdapter(adapterSticker);
        });
    }

    @Override
    public void showDialogAddSticker() {
        Dialog dialog       = DialogFactory.getInstance().getDialog(getContext(), DialogFactory.TypeDialog.ADD_STICKER);
        Button btnAdd       = dialog.findViewById(R.id.btnAddSticker);
        Button btnDownload  = dialog.findViewById(R.id.btnDownload);
        EditText edtUrl     = dialog.findViewById(R.id.txtUrlSticker);
        ImageView imgSticker= dialog.findViewById(R.id.imgStickerAdd);

        Extensions.Companion.listenerEditText(edtUrl,url ->{
            if(Extensions.Companion.validateUrl(url)){ imgSticker.setImageResource(R.drawable.no_image); }else{
            Glide.with(getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgSticker);
            }
            return null;
        });
        String openApk    = getContext().getResources().getString(R.string.openApk);
        String installApk = getContext().getResources().getString(R.string.installApk);

        if(presenter.validateInstalledApk()){
            btnDownload.setText(openApk);
        }else{
            if(presenter.validateDownloadedApk()){
                btnDownload.setText(installApk);
            }
        }

        btnAdd     .setOnClickListener(view -> presenter.addSticker(edtUrl.getText().toString().trim()));
        btnDownload.setOnClickListener(view -> {
            switch (btnDownload.getText().toString()){
                case "Abrir":
                    presenter.openApk();
                    break;
                case "Instalar":
                    presenter.installApk();
                    break;
                default:
                    presenter.downloadApp();
                    break;
            }

        });

        dialog.show();
    }

}