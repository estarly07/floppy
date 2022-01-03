package com.example.floppy.ui.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.floppy.R;
import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Models.Estado;
import com.example.floppy.data.Models.Estado_User;
import com.example.floppy.data.Models.User;
import com.example.floppy.ui.GlobalView;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.ui.global_presenter.GlobalPresenterImpl;
import com.example.floppy.ui.message.MessageFragment;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements GlobalView {

    public static User            friend;
    public static FriendEntity    friendEntity;
    public static GlobalPresenter presenter;
    public BroadcastReceiver      broadcastReceiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (friend != null)
            MessageFragment.user = friend;
        if (friendEntity != null)
            MessageFragment.friendEntity = friendEntity;

        setContentView(R.layout.activity_chat);

        presenter = new GlobalPresenterImpl(this, this, this);
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
        MessageFragment.getCloseListeners().closeListeners();
        super.onBackPressed();
    }

    @Override
    public void showAlertDialog() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showHandlingGeneral(boolean show) {

    }

    @Override
    public void showStateDialog(ArrayList<Estado> states) {

    }

    @Override
    public void showUsesDialog(User friend) {

    }

    @Override
    public void animToolbar(boolean show) {

    }

    @Override
    public void showContacts(ArrayList<User> users) {

    }

    @Override
    public void nextActivity() {

    }

    @Override
    public void beginDownload(BroadcastReceiver broadcastReceiver) {
        registerReceiver(broadcastReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}