package com.example.floppy.ui.contacts;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.floppy.databinding.FragmentContactsFragmentsBinding;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.Chat.ChatActivity;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.R;
import com.example.floppy.ui.mastercontrol.MasterControl;


import java.util.ArrayList;


public class ContactsFragments extends Fragment implements ContactsView {
    private GlobalPresenter   presenterMaster;
    private ContactsPresenter presenter;
    private Activity          activity;
    private AdapterContacts   adapterContacts;
    private FragmentContactsFragmentsBinding  binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_contacts_fragments, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity         = MasterControl.activity;
        presenterMaster  = MasterControl.presenter;
        presenter        = new ContactsPresenterImpl( getContext(),this, presenterMaster, activity);

        binding.reciclerContacts . setHasFixedSize(true);
        binding.setLayout(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        presenter.getAllUsers();
    }

    @Override
    public void showContacts(ArrayList<User> users) {
        activity.runOnUiThread(() -> {
            adapterContacts = new AdapterContacts(users);
            adapterContacts . setHasStableIds(true);
            adapterContacts . notifyItemRangeInserted(0,users.size()-1);
            binding         . setAdapter(adapterContacts);

            adapterContacts .setClick((view, position, user) -> {
                ChatActivity.friend = user;
                //buscar si el amigo esta guardado en la bd local
                presenter.getFriend(user.getIdUser());
            });
        });
    }

    @Override
    public void showChat(FriendEntity friendEntity) {
        ChatActivity.friendEntity = friendEntity;
        presenterMaster.nextActivity();
    }
}