package com.example.floppy.ui.contacts;

import com.example.floppy.Domain.Entitys.FriendEntity;
import com.example.floppy.Domain.Models.User;

import java.util.ArrayList;

public interface ContactsView {
    void showContacts(ArrayList<User> users);

    void showChat(FriendEntity friendEntity);
}
