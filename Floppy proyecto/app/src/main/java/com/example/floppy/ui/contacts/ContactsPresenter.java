package com.example.floppy.ui.contacts;
import com.example.floppy.domain.models.User;

import java.util.ArrayList;

public interface ContactsPresenter {

    void getAllUsers();

    void showContacts(ArrayList<User> users);

    void getFriend(String idUser);
}
