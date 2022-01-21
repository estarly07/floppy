package com.example.floppy.ui.contacts;

import android.app.Activity;
import android.content.Context;

import com.example.floppy.domain.local.InteractorLocal;
import com.example.floppy.domain.local.InteractorSqlite;
import com.example.floppy.domain.models.User;
import com.example.floppy.domain.remote.Interactor;
import com.example.floppy.domain.remote.InteractorFirestoreImpl;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.utils.Global.FactoryJson;

import java.util.ArrayList;

public class ContactsPresenterImpl implements ContactsPresenter {

    private ContactsView    contactsView;
    private Context         context;
    private Interactor      interactor;
    private InteractorLocal interactorLocal;
    public ContactsPresenterImpl(Context context, ContactsView contactsView, GlobalPresenter globalPresenter, Activity activity) {
        this.contactsView    = contactsView;
        this.context         = context;
        this.interactor      = new InteractorFirestoreImpl(context, globalPresenter);/**INTERACTOR QUE LLAMA A FIRESTORE*/
        this.interactorLocal = new InteractorSqlite(context);
    }

    @Override
    public void getAllUsers() { new Thread(() -> interactor.getAllUsers(this, User.getInstance().getIdUser() )).start(); }

    @Override
    public void showContacts(String users) { contactsView.showContacts((ArrayList<User>) new FactoryJson().fromJson(users, FactoryJson.TypeObject.USERS));}

    @Override
    public void getFriend(String idUser)
    {
        new Thread(() -> contactsView.showChat(interactorLocal.getFriend(idUser))).start();
    }
}

