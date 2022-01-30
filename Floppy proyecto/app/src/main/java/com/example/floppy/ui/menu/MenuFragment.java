package com.example.floppy.ui.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.floppy.Callbacks.CallbackNavigationFragments;
import com.example.floppy.databinding.FragmentMenuBinding;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.Estado;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.Chat.ChatActivity;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.R;
import com.example.floppy.ui.mastercontrol.MasterControl;
import com.example.floppy.ui.menu.adapters.AdapterChat;
import com.example.floppy.ui.menu.adapters.AdapterEstado;
import com.example.floppy.utils.Extensions;

import java.util.ArrayList;
import java.util.Random;

public class MenuFragment extends Fragment implements MenuView{
    private GlobalPresenter presenterMaster;
    private MenuPresenter   presenter;
    private Activity        activity;
    private RelativeLayout  message;
    private TextView        txtMessage;
    private CountDownTimer  countDownTimer;
    private FragmentMenuBinding binding;

    ArrayList<FriendEntity> friendEntities;
    ArrayList<User>         friends;

    private static CallbackNavigationFragments callbackNavigationFragments;
    public  static CallbackNavigationFragments getCallbackNavigationFragments(){ return callbackNavigationFragments;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_menu, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbackNavigationFragments = () -> {
            presenterMaster.showTollbar(false);
            NavHostFragment.findNavController(this).navigate(R.id.navContacts);
        };

        presenterMaster = MasterControl.presenter;
        activity        = MasterControl.activity;
        presenter       = new MenuPresenterImpl(this, view.getContext(), activity,presenterMaster);

        presenterMaster.showHandlingGeneral(true);
        presenterMaster.showTollbar(true);
        binding.setLayoutState(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.reciclerEstados.setHasFixedSize(true);
        binding.setLayoutChat(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        binding.reciclerChats.setHasFixedSize(true);

        Extensions.Companion.listenerScroll(binding.scrollMenuFragment, up -> {
            presenterMaster.showTollbar(up);
            return  null;
        });

        binding.btnAddUser.setOnClickListener(view1 -> {

        });

        txtMessage = view.findViewById(R.id.txtMensaje);
        message    = view.findViewById(R.id.nube);
        getData();
    }

    /**
     * OBTENER LOS ESTADOS Y DATOS DEL USER COMO LOS CHATS DE LOS AMIGOS
     */
    private void getData() {
        presenter.getStates(presenter);
        presenter.getMyFriends();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(friendEntities != null){
            System.out.println("RESUME");
            for (FriendEntity friend:
                 friendEntities) {
                presenter.listenerChatFriend(friend);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.destroyAllListenersFriends();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyAllListenersFriends();
    }

    /**
     * SIRVE PARA QUE CADA 5 MINUTOS LLAME AL METODO GENERARMENSAGE
     */
    private void timer(int time) {
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                cancelTimer(this);
                generateMessage();
            }
        }.start();
    }

    private void cancelTimer(CountDownTimer countDownTimer) {
        countDownTimer.cancel();
    }

    private void generateMessage() {
        int index = new Random().nextInt(activity.getApplicationContext().getResources().getStringArray(R.array.frases).length);
        mostrarMensage(activity.getApplicationContext().getResources().getStringArray(R.array.frases)[index]);
    }

    private void mostrarMensage(String msg) {
        activity.runOnUiThread(() -> {
            txtMessage.setText(msg);
            animAparecerDesaparecer(message);
            timer(30000);
        });
    }

    private void animAparecerDesaparecer(View view) {
        activity.runOnUiThread(() -> {
            view.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_aparecer_desaparecer);
            animation.setDuration(2000);
            view.setAnimation(animation);
            view.setVisibility(View.INVISIBLE);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showState(ArrayList<ArrayList<Estado>> listEstados) {
        activity.runOnUiThread(() -> {
            presenterMaster.showHandlingGeneral(false);
            AdapterEstado adapterState = new AdapterEstado(listEstados);
            adapterState .notifyDataSetChanged();
            adapterState .setClick((vista, position, states) -> presenterMaster.showEstadoDialogo(states));
            binding.setAdapterStates(adapterState);
        });
    }

    AdapterChat adapterChat;
    @Override
    public void showChats(ArrayList<User> friends, ArrayList<FriendEntity> friendEntities) {
        activity.runOnUiThread(() -> {
            this.friendEntities = friendEntities;
            this.friends = friends;
            adapterChat  = new AdapterChat(friends,friendEntities);
//            adapterChat.setHasStableIds(true);
            adapterChat.setClick(new AdapterChat.Click() {
                @Override
                public void clickPhoto(View view, int position, User friend) {
                    if (!friend.getPhoto().equals("")) presenterMaster.showUserImageDialog(friend);
                }

                @Override
                public void clickChat(View view, int position, User friend, FriendEntity friendEntity) {
                    ChatActivity.friend       = friend;
                    ChatActivity.friendEntity = friendEntity;
                    presenterMaster.nextActivity();
                }
            });

            binding.setAdapterChats(adapterChat);
        });
    }

    @Override
    public void friendIsWriting(FriendEntity friendEntity, Message message) {
        if(adapterChat!=null){
            activity.runOnUiThread(() -> adapterChat.setFriendEntities(friendEntity, message));
        }
    }
}