package com.example.floppy.ui.contacts;

import android.app.Activity;
import android.content.Context;

import com.example.floppy.data.Interactor.remote.Interactor;
import com.example.floppy.data.Interactor.remote.InteractorFirestoreImpl;
import com.example.floppy.data.Models.Chat;
import com.example.floppy.data.Models.Message;
import com.example.floppy.data.Models.User;
import com.example.floppy.ui.global_presenter.GlobalPresenter;

import java.util.ArrayList;

public class ContactsPresenterImpl implements ContactsPresenter {

    private ContactsView contactsView;
    private Context      context;
    private Interactor   interactor;
    public ContactsPresenterImpl(Context context, ContactsView contactsView, GlobalPresenter globalPresenter, Activity activity) {
        this.contactsView = contactsView;
        this.context    = context;
        this.interactor = new InteractorFirestoreImpl(context, globalPresenter, activity);/**INTERACTOR QUE LLAMA A FIRESTORE*/
    }

    @Override
    public void getAllUsers() { new Thread(() -> interactor.getAllUsers(this)).start(); }

    @Override
    public void showContacts(ArrayList<User> users) { contactsView.showContacts(users);}
}

