package com.example.floppy.ui.global_presenter;

import android.app.Activity;
import android.content.Context;

import com.example.floppy.data.Interactor.remote.Interactor;
import com.example.floppy.data.Interactor.remote.InteractorFirestoreImpl;
import com.example.floppy.data.Models.Estado;
import com.example.floppy.data.Models.Estado_User;
import com.example.floppy.data.Models.User;
import com.example.floppy.ui.GlobalView;

import java.util.ArrayList;

public class GlobalPresenterImpl implements GlobalPresenter {
    private User       user = User.getInstance();
    private GlobalView globalView;
    private Context    context;
    private Interactor interactor;

    public GlobalPresenterImpl(GlobalView globalView, Context context, Activity activity) {
        this.globalView = globalView;
        this.context = context;
     //   this.interactor = new InteractorImpl( context, activity);/**INTERACTOR QUE LLAMA A FIREBASE*/
        this.interactor = new InteractorFirestoreImpl(context,this,  activity);/**INTERACTOR QUE LLAMA A FIRESTORE*/
    }

    @Override
    public void showMessage(String msg) {
        globalView.showToast(msg);
    }

    @Override
    public void showEstadoDialogo(ArrayList<Estado> estados) {
        globalView.showStateDialog(estados);
    }

    @Override
    public void showHandlingGeneral(boolean show) {
        globalView.showHandlingGeneral(show);
    }


    @Override
    public void showUserImageDialog(User friend) {
        globalView.showUsesDialog(friend);
    }

    @Override
    public void showTollbar(boolean show) {
        globalView.animToolbar(show);
    }


    @Override
    public void showContacts(ArrayList<User> users) {
        globalView.showContacts(users);
    }

    @Override
    public void updateState(Estado_User estado_user) {
        interactor.updateEstado(User.getInstance().getIdUser(), estado_user);
    }

    @Override
    public void nextActivity() {
        globalView.nextActivity();
    }


}