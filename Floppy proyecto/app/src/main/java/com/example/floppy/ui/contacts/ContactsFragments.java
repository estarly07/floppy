package com.example.floppy.ui.contacts;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Models.User;
import com.example.floppy.ui.Chat.ChatActivity;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.R;
import com.example.floppy.ui.mastercontrol.MasterControl;


import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class ContactsFragments extends Fragment implements ContactsView {
    GlobalPresenter presenterMaster;
    ContactsPresenter presenter;
    Activity activity;
    RecyclerView recyclerContacts;
    AdapterContacts adapterContacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts_fragments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity         = MasterControl.activity;
        presenterMaster  = MasterControl.presenter;
        presenter        = new ContactsPresenterImpl( getContext(),this, presenterMaster, activity);
        recyclerContacts = view.findViewById(R.id.reciclerContacts);
        recyclerContacts . setHasFixedSize(true);
        recyclerContacts . setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        presenter.getAllUsers();
    }

    @Override
    public void showContacts(ArrayList<User> users) {
        activity.runOnUiThread(() -> {
            adapterContacts = new AdapterContacts(users);
            adapterContacts . setHasStableIds(true);
            adapterContacts . notifyItemRangeInserted(0,users.size()-1);
            recyclerContacts. setAdapter(adapterContacts);
            adapterContacts .setClick((view, position, user) -> {
                ChatActivity.friend = user;
                //buscar si el amigo esta guardado en la bd local
                presenter.getFriend(user.getIdUser());
            });
        });
    }

    @Override
    public void showChat(FriendEntity friendEntity) {
        if(friendEntity != null){
            System.out.println("YA ESTA BB");
            ChatActivity.friendEntity = friendEntity;
        }
        presenterMaster.nextActivity();
    }
}