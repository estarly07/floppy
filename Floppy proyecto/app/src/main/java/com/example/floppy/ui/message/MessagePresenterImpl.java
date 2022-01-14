package com.example.floppy.ui.message;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.example.floppy.BuildConfig;
import com.example.floppy.databinding.ItemMensajeBinding;
import com.example.floppy.domain.entities.ChatEntity;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.entities.MessageEntity;
import com.example.floppy.domain.entities.UserEntity;
import com.example.floppy.domain.local.InteractorLocal;
import com.example.floppy.domain.local.InteractorSqlite;
import com.example.floppy.domain.models.StateMessage;
import com.example.floppy.domain.remote.Interactor;
import com.example.floppy.domain.remote.InteractorFirestoreImpl;
import com.example.floppy.domain.models.Chat;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.domain.models.User;
import com.example.floppy.data.Services.ServiceDownload;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.utils.Animations;
import com.example.floppy.utils.Extensions;
import com.example.floppy.utils.Global.GlobalUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class MessagePresenterImpl implements MessagePresenter {
    private Context            context;
    private MessageView        messageView;
    private Interactor         interactor;
    private InteractorLocal    interactorLocal;
    private GlobalPresenter    globalPresenter;
    private ArrayList<Message> messages = new ArrayList<>();
    private MediaPlayer        player;

    public MessagePresenterImpl(Context context, Activity activity, MessageView messageView, GlobalPresenter globalPresenter) {
        player               = new MediaPlayer();
        this.context         = context;
        this.messageView     = messageView;
        this.interactor      = new InteractorFirestoreImpl(context, globalPresenter, activity);
        this.interactorLocal = new InteractorSqlite(context);
        this.globalPresenter = globalPresenter;
    }

    public void showStateUser(String response) {
        messageView.showStateUser(response);
    }

    public void sendMessages(String idChat, Message message) {
        if (message.getMessage().trim().isEmpty()) { return; }
        message.setUser(User.getInstance().getIdUser());
        message.setHora(GlobalUtils.getHour());
        message.setIdMessage(UUID.randomUUID().toString());
        message.setState(StateMessage.DELIVERED);
        allMessages.add(0,message);

        Gson gson = new Gson();

        new Thread(() -> {
            interactor.sendMessages(idChat,gson.toJson(allMessages));
            messageView.cleanEdittext();
        }).start();
    }

    public void showDataFriend(String nick, String photo) { messageView.showDataFriend(nick, photo); }


    public void getStateUser(String idUser) {
        interactor.getEstadoUser(idUser);
    }


    public void cancelListener() { interactor.cancelListener(); }

    @Override
    public void getMessages(Chat chat) {
        interactor.getMessages(this, chat);
    }

    @Override
    public void getMessagesLocal(String idChat) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            List<MessageEntity> messageEntities = interactorLocal.getMessages(idChat);
            for (MessageEntity m : messageEntities) {
                if(m.state == StateMessage.CHECK){
                    System.out.println(" "+m.idMessage);
                    Message message = new Message();
                    message.setIdMessage  (m.idMessage);
                    message.setMessage    (m.message);
                    message.setTypeMessage(m.typeMessage);
                    message.setDate       (m.date);
                    message.setUser       (m.user);
                    message.setHora       (m.hour);
                    message.setState      (m.state);

                    this.messages.add(message);
                }
            }
            System.out.println("CANTIDAD DE MENSAJES "+this.messages.size());
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ArrayList<Message> allMessages = new ArrayList<>();

    @Override
    public void showMessages(ArrayList<Message> messages, String idChat) {
        allMessages = messages;

        messageView.showMessages(messages, User.getInstance().getIdUser(), idChat);

//        new Thread(() -> {
//            if(interactorLocal.getChat(idChat) !=null) {
//                for (Message message : messages) {
//                    if(message.getState() == StateMessage.CHECK && interactorLocal.getMessages(message.getIdMessage()) == null){
//                        System.out.println(" "+message.getIdMessage());
//                        MessageEntity messageEntity = new MessageEntity();
//                        messageEntity.idMessage = message.getIdMessage();
//                        messageEntity.message = message.getMessage();
//                        messageEntity.typeMessage = message.getTypeMessage();
//                        messageEntity.date = message.getDate();
//                        messageEntity.user = message.getUser();
//                        messageEntity.fk_idChat = idChat;
//                        messageEntity.hour = message.getHora();
//                        messageEntity.state = message.getState();
//
////                        interactorLocal.insertMessage(messageEntity);
//                    }
//                }
//            }
//        }).start();
    }

    public void getStickers() {
        new Thread(() -> {
            interactorLocal.getStickers(this, User.getInstance().getIdUser());
        }).start();
    }

    @Override
    public void getChatLocal(String idChat) {
        new Thread(() -> {
            for (MessageEntity message:
            interactorLocal.getMessages(idChat)) {
                Message newMessage = new Message();
                newMessage.setIdMessage(message.idMessage);
                newMessage.setMessage  (message.message);
                newMessage.setDate     (message.date);
                newMessage.setUser     (message.user);
                newMessage.setHora     (message.hour);
                newMessage.setState    (message.state);
                newMessage.setTypeMessage(message.typeMessage);
                messages.add(newMessage);
            }

            messageView.showMessages(messages,User.getInstance().getIdUser(),idChat);
        }).start();
    }

    public void showStickers(ArrayList<StickersEntity> list) { messageView.showStickers(list); }

    @Override
    public void searchChat(User userSelect) {
        String[] users = new String[2];
        users [0]      = User.getInstance().getIdUser();
        users [1]      = userSelect.getIdUser();
        Chat chat      = new Chat();

        chat.setIdChat  ("");
        chat.setUsers   (users);
        chat.setMensajes(new Message[]{});

        new Thread(() -> interactor.searchChat(chat,this)).start();
    }

    @Override
    public void createChat(User userSelect, Message message) {
        String[] users = new String[2];
        users [0]      = User.getInstance().getIdUser();
        users [1]      = userSelect.getIdUser();
        Chat chat      = new Chat();

        chat.setUsers   (users);
        chat.setMensajes(new Message[]{});

        new Thread(() -> interactor.createChat(chat, userSelect, message, this)).start();
    }

    @Override
    public void insertFriendLocal(User friend, String idChat) {
        UserEntity   userEntity   = new UserEntity();
        FriendEntity friendEntity = new FriendEntity();
        ChatEntity   chatEntity   = new ChatEntity();

        userEntity.idUser       = friend.getIdUser();
        userEntity.messageUser  = friend.getMessageUser();
        userEntity.name         = "";
        userEntity.photo        = friend.getPhoto();

        interactorLocal.insertUser(userEntity);

        friendEntity.idFriend   = friend.getIdUser();
        friendEntity.idOwner    = User.getInstance().getIdUser();
        friendEntity.idChat     = idChat;
        friendEntity.nick       = friend.getName();

        chatEntity.idChat       = idChat;
        chatEntity.idFriend     = friend.getIdUser();
        chatEntity.idOwner      = User.getInstance().getIdUser();

        interactorLocal.insertFriend(friendEntity);
        interactorLocal.insertChat  (chatEntity);
    }

    @Override
    public void addSticker(String image) {
        if(image.isEmpty() || Extensions.Companion.validateUrl(image)){ return; }
        new Thread(() -> {
            StickersEntity stickersEntity = new StickersEntity();
            stickersEntity.fk_idUser = User.getInstance().getIdUser();
            stickersEntity.urlImage  = image;

            interactorLocal.insertSticker(stickersEntity);
        getStickers(); }).start();
    }

    @Override
    public void showDialogAddSticker() { messageView.showDialogAddSticker();}

    @Override
    public void downloadApp() {
        ServiceDownload serviceDownload = new ServiceDownload(this);
        globalPresenter.beginDownloadApp(serviceDownload.onDownloadComplete);
        serviceDownload.beginDownload(context);
    }

    @Override
    public Boolean validateInstalledApk() { return Extensions.Companion.validateInstallApk(GlobalUtils.packageStickerApk, context); }

    @Override
    public void openApk() {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(GlobalUtils.packageStickerApk);
        if(intent == null){
            //TODO no open app message
        }else{
            context.startActivity(intent);
        }
    }

    @Override
    public void installApk() {
        File file = new File(context.getExternalFilesDir(null), GlobalUtils.nameStickerApk);
        Intent intentApk ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            intentApk = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intentApk.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentApk.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentApk.setData(path);
        }else{
            Uri apkUri = Uri.fromFile(file);
            intentApk = new Intent(Intent.ACTION_VIEW);
            intentApk.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intentApk.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(intentApk);
        } catch (ActivityNotFoundException e) {
        }
    }

    @Override
    public boolean validateDownloadedApk() {
        File file = new File(context.getExternalFilesDir(null), GlobalUtils.nameStickerApk);
        return file.exists();
    }

    @Override
    public void searchFriend(String idUser) {
        new Thread(() -> {
            messageView.sendAndInsertFriend(interactorLocal.getFriend(idUser) == null);
        }).start();
    }

    @Override
    public void recordAudio(String idChat) {
        globalPresenter.recordAudio(idChat,this);
    }

    @Override
    public void showDialogAddOrDeleteSticker(Message message) {
        messageView.showDialogAddOrDeleteSticker(context,message, message.getUser().equals(User.getInstance().getIdUser()));
    }

    @Override
    public void deleteSticker(String sticker) {
        new Thread(() -> {
            interactorLocal.deleteSticker(sticker);
            getStickers();
        }).start();
    }

    public void showMessagesChat(FriendEntity friendEntity) {
        String[] users = new String[2];
        users [0]      = User.getInstance().getIdUser();
        users [1]      = friendEntity.idFriend;
        Chat chat      = new Chat();
        System.out.println("ID CHAT "+friendEntity.idChat);
        chat.setIdChat  (friendEntity.idChat);
        chat.setUsers   (users);
        chat.setMensajes(new Message[]{});
        new Thread(() -> interactor.getMessages(this, chat)).start();
    }
    int position;
    String path="";
    AdapterMessage.ViewHolder oldViewHolder;
    @Override
    public void audio(Message message, AdapterMessage.ViewHolder viewHolder) {
        if (this.path.equals(message.getMessage())&& player != null){
            if (player.isPlaying()) {
                position = player.getCurrentPosition();
                player.pause();
            } else {
                player.seekTo(position);
                player.start();
            }
           viewHolder.binding.msgAudio.setIsPlaying(player.isPlaying());
            return;
        }

        if (!path.equals("")&&!path.equals(message.getMessage())&& player!=null && player.isPlaying()){
            player.pause();
            oldViewHolder.binding.msgAudio.setIsPlaying(player.isPlaying());
        }
        oldViewHolder = viewHolder;
        path = message.getMessage();
        player.reset();

        player.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build());
        try {
            player.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.start();
                viewHolder.binding.msgAudio.setIsPlaying(player.isPlaying());
            });
            player.setDataSource(context.getExternalFilesDir(null)+"/audios/"+path+".mp3");
            player.prepareAsync();
            player.setOnCompletionListener(mp -> {
                viewHolder.binding.msgAudio.setIsPlaying(player.isPlaying());
                path="";
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
