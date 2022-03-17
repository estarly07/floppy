package com.example.floppy.ui.message;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.floppy.databinding.FragmentChatBinding;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.Adapters.AdapterSticker;
import com.example.floppy.domain.models.Estado_User;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.ui.Chat.ChatActivity;
import com.example.floppy.ui.aboutfriend.AboutFriendFragment;
import com.example.floppy.ui.factory.DialogFactory;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.R;
import com.example.floppy.ui.mastercontrol.MasterControl;
import com.example.floppy.utils.Animations;
import com.example.floppy.utils.Extensions;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;


public class MessageFragment extends Fragment implements MessageView {
    public static MessagePresenter presenter;
    public static FriendEntity     friendEntity;
    public static User             user;

    private String           idChat="";
    private GlobalPresenter  presenterMaster;
    private Activity         activity;
    private Message          message = new Message();//Create just one message and change it its properties

    private AmbilWarnaDialog dialog;

    private FragmentChatBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenterMaster = ChatActivity.presenter;
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat, container, false);
        return binding.getRoot();
    }

    public  interface CloseListeners { void closeListeners();}
    private static    CloseListeners closeListeners;
    public  static    CloseListeners getCloseListeners() { return closeListeners; }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity  = MasterControl.activity;
        presenter = new MessagePresenterImpl(view.getContext(), activity, this, presenterMaster);

        closeListeners = () -> presenter.cancelListener();
        presenter.getColorBackground();
        presenter.getBackground();

        dialog = new AmbilWarnaDialog(view.getContext(), 0xff000000,true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                setColorBackground   (color);
                presenter.chooseColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });

        binding.includeSticker.recyclerStickers . setHasFixedSize(true);
        binding.includeSticker.recyclerStickers . setLayoutManager(new GridLayoutManager(view.getContext(), 4));
        Extensions.Companion.listenerFocusChange(binding.edtMensaje, (view1,showStickers) -> {
            if(showStickers){
                Extensions.Companion.hideKeyboard(view1);
                binding.includeSticker.getRoot().setVisibility(View.VISIBLE);
            }else{ binding.includeSticker.getRoot().setVisibility(View.GONE); }
            return null;
        });

        binding.layoutInfoFriend.setOnClickListener(v->{
            AboutFriendFragment.friend = user;
            AboutFriendFragment.friendEntity = friendEntity;
            NavHostFragment.findNavController(this).navigate(R.id.action_messageFragment_to_aboutFriendFragment);

        });
        binding.btnOptions.setOnClickListener(v-> showOptions(binding.containerOptions.getRoot(),binding.containerOptions.layoutOptions));

        binding.btnSticker.setOnClickListener(view12 -> {
            if( binding.includeSticker.getRoot().getVisibility() == (View.GONE)){
                binding.edtMensaje.clearFocus();
                presenter .getStickers();
            }else{ binding.includeSticker.getRoot().setVisibility(View.GONE); }
        });
        presenter.getStateUser(user.getIdUser());

        binding.btnLogin.setOnClickListener(view1 -> {
            if(!Extensions.Companion.validateConnection()){
                presenterMaster.showMessage("No tienes acceso a internet");
                return;
            }
            if(binding.edtMensaje.getText().toString().trim().length() == 0){
                presenter.recordAudio(idChat,user );
            }else{
                message.setMessage(binding.edtMensaje.getText().toString().trim());
                message.setTypeMessage(Message.TypesMessages.TEXT);
                sendMessage();
            }
        });
        binding.toolbar2.getMenu().findItem(R.id.action_wallpaper).setOnMenuItemClickListener(menuItem -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_messageFragment_to_wallpapersFragment);
            return false;
        });
        binding.toolbar2.getMenu().findItem(R.id.action_color).setOnMenuItemClickListener(menuItem -> {
            dialog.show();
            return false;
        });


        binding.includeSticker.btnShowDialogAddSticker.setOnClickListener(view13 -> presenter.showDialogAddSticker());
        Extensions.Companion.listenerEditText(binding.edtMensaje,s -> {
            if(s.length() > 0){
                binding.btnLogin.setImageResource(R.drawable.ic_send);
            }else{
                binding.btnLogin.setImageResource(R.drawable.ic_recovery);
            }
            return null;
        });

        binding.reciclerChat . setHasFixedSize(false);
        binding.reciclerChat . setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, true));

        if (friendEntity != null){
            idChat = friendEntity.idChat;
            presenter.getMessagesLocal(idChat);
            presenter.showMessagesChat(friendEntity);
            presenter.showDataFriend(friendEntity.nick, user.getPhoto());
        }else{
            //mostrar animacion
            binding.setIsEmpty(true);
            binding.containerNoMessages.anim.setAnimation(Animations.Companion.animationRandom());
            presenter.searchChat(user);
            presenter.showDataFriend(user.getName(), user.getPhoto());
        }
    }

    private void sendMessage(){
        if(!idChat.equals("")){
            //BUSCARLO EN LA BD Y SI NO CREARLO
            presenter.searchFriend(user.getIdUser());
        }else{ presenter.createChat(user, message); }
    }

    Boolean translate = true;
    private void showOptions(View view,LinearLayout layoutAnimation) {
        if(translate){
            layoutAnimation.scheduleLayoutAnimation(); }
        Animations.Companion.animationTranslate(view,translate);
        translate = !translate;
        binding.containerOptions.btnImage.setOnClickListener(v -> presenterMaster.getImage(idChat,user , presenter));
        binding.containerOptions.btnCamera.setOnClickListener(v -> presenterMaster.takePhoto(idChat,user , presenter));
    }


    public void showDataFriend(String nick, String photo) {
        if (!photo.equals(""))
            Glide.with(activity.getApplicationContext())
                    .load(photo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imgUserChat);
        binding.txtNombreUser.setText(nick);
    }


    public void showMessages(ArrayList<Message> messages, String myId, String idChat) {
        activity.runOnUiThread(() -> {
            System.out.println("MNEEEs "+messages.isEmpty());
            binding.setIsEmpty(messages.isEmpty());

            this.idChat = idChat;
            AdapterMessage adapterMessage = new AdapterMessage(messages, myId);
            adapterMessage.setClick((view, position, message,viewHolder)-> {
                switch (message.getTypeMessage()){
                    case STICKER:
                        presenter.showDialogAddOrDeleteSticker(message);
                        break;
                    case RECORD:
                        presenter.audio(message,viewHolder);
                        break;
                    case IMAGE:
                        presenterMaster.showImage(false,false , message.getMessage());
                        break;
                }
                });
//            adapterMensage.setHasStableIds(true);
            binding.reciclerChat.setAdapter(adapterMessage);
//            binding.reciclerChat.scheduleLayoutAnimation();
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
                Animations.Companion.animVanish(binding.txtStateOffline);
                Animations.Companion.animAppear(binding.txtStateOnline);
            }else{
                Animations.Companion.animAppear(binding.txtStateOffline);
                Animations.Companion.animVanish(binding.txtStateOnline);
            }
        });
    }


    public void showStickers(ArrayList<StickersEntity> stickers) {
        activity.runOnUiThread(() -> {
            binding.includeSticker.recyclerStickers.scheduleLayoutAnimation();
            Animations.Companion.animAppear(binding.includeSticker.getRoot());

            AdapterSticker adapterSticker = new AdapterSticker(stickers);
            adapterSticker.setClick((view, stickersEntity) -> {
               message.setTypeMessage(Message.TypesMessages.STICKER);
               message.setMessage(stickersEntity.urlImage);
               if(!Extensions.Companion.validateConnection()){
                   presenterMaster.showMessage("No tienes acceso a internet");
                   return;
               }
               sendMessage();
            });
            binding.includeSticker.recyclerStickers.setAdapter(adapterSticker);
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

        if(presenter.validateInstalledApk()){ btnDownload.setText(openApk); }
        else{
            if(presenter.validateDownloadedApk()){ btnDownload.setText(installApk); }
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

    @Override
    public void sendAndInsertFriend(Boolean insertFriend) {
        if (insertFriend){
            presenter.insertFriendLocal(user,idChat);
        }
        presenter.sendMessages(idChat, message);
    }

    @Override
    public void cleanEdittext() {
        activity.runOnUiThread(() -> binding.edtMensaje.setText(""));
    }

    @Override
    public void setColorBackground(int color) { binding.background.setColorFilter(color); }

    @Override
    public void setBackgroundDrawable(int backgroundDrawable) { binding.background.setImageDrawable(getContext().getDrawable(backgroundDrawable)); }

    @Override
    public void setBackgroundPath(String backgroundPath) {
        binding.background    .setVisibility(View.GONE);
        binding.backgroundPath.setVisibility(View.VISIBLE);
        Glide.with(getContext())
                .load(backgroundPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.backgroundPath);
    }

}