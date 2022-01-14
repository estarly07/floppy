package com.example.floppy.ui.contacts;

import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.User;

import java.util.ArrayList;

public interface ContactsView {
    void showContacts(ArrayList<User> users);

    void showChat(FriendEntity friendEntity);
}
