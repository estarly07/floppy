package com.example.floppy.ui.contacts;

import com.example.floppy.data.Models.User;

import java.util.ArrayList;

public interface ContactsView {
    void showContacts(ArrayList<User> users);
}
