package com.example.floppy.ui.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.floppy.R;
import com.example.floppy.databinding.ActivityChatBinding;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.Estado;
import com.example.floppy.domain.models.Estado_User;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.GlobalView;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.ui.global_presenter.GlobalPresenterImpl;
import com.example.floppy.ui.message.MessageFragment;
import com.example.floppy.ui.message.MessagePresenter;
import com.example.floppy.utils.Animations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements GlobalView {

    public static User                friend;
    public static FriendEntity        friendEntity;
    public static GlobalPresenter     presenter;
    public        BroadcastReceiver   broadcastReceiver = null;
    private       MediaRecorder       recorder;
    private       ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (friend != null)
            MessageFragment.user = friend;

        MessageFragment.friendEntity = friendEntity;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat);
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
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
        presenter.updateState(Estado_User.OFFLINE);

        super.onDestroy();
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
        runOnUiThread(() ->{
            if (show)
                binding.setIsStop(true);
            else
                Animations.Companion.animVanish(binding.btnStop);
        });
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
    File file;
    @Override
    public void recordAudio(String name, String idChat, MessagePresenter messagePresenter) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

        } else {
            binding.setIsStop(false);
            Animations.Companion.animAppear(binding.btnStop);
            binding.setIsStop(false);
            recorder = new MediaRecorder();
            recorder.setAudioSource (MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            file = new File(getExternalFilesDir(null), "/audios");
            file.mkdirs();

            recorder.setOutputFile(file.getAbsolutePath()+"/"+name+".mp3");

            try {
                recorder.prepare();
                recorder.start();
                initTimerRecord(new String[]{file.getAbsolutePath(),name}, idChat,messagePresenter);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        binding.btnStop.setOnClickListener(v -> {
            if(!binding.getIsStop()){
                presenter.stopRecord(new String[]{file.getAbsolutePath(),name}, idChat,messagePresenter);
            }
        });

    }
    CountDownTimer countDownTimer;

    private void initTimerRecord(String[] data, String idChat, MessagePresenter messagePresenter) {
        countDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                binding.txtTime.setText((int) (l/1000)+"s");
            }

            @Override
            public void onFinish() {
                presenter.stopRecord(data, idChat,messagePresenter);
            }
        }.start();
    }

    @Override
    public void stopAudio() {
        if(recorder != null ){
            if(countDownTimer!=null && !binding.txtTime.getText().equals("0s")){
                countDownTimer.cancel();
            }
            recorder.stop();
            recorder.release();
        }
    }
}