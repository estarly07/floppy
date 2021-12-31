package com.example.floppy.ui.menu;

import android.app.Activity;
import android.content.Context;

import com.example.floppy.data.Interactor.local.InteractorLocal;
import com.example.floppy.data.Interactor.local.InteractorSqlite;
import com.example.floppy.data.Interactor.remote.Interactor;
import com.example.floppy.data.Interactor.remote.InteractorFirestoreImpl;
import com.example.floppy.data.Models.Estado;
import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Models.User;
import com.example.floppy.ui.global_presenter.GlobalPresenter;

import java.util.ArrayList;

public class MenuPresenterImpl implements MenuPresenter {
    final private MenuView        view;
    final private Interactor      interactor;
    final private GlobalPresenter presenterMaster;
    final private InteractorLocal interactorLocal;
    ArrayList<FriendEntity> friends = new ArrayList<>();


    public MenuPresenterImpl(MenuView view, Context context, Activity activity, GlobalPresenter presenterMaster) {
        this.view            = view;
        this.interactor      = new InteractorFirestoreImpl(context,presenterMaster,activity);
        this.presenterMaster = presenterMaster;
        this.interactorLocal = new InteractorSqlite(context);
    }

    @Override
    public void showState(ArrayList<ArrayList<Estado>> estados) {
        view.showState(estados);
    }
    @Override
    public void getStates(MenuPresenter presenter) {
        new Thread(() -> interactor.getEstados("aMPe5aVk1QGzqYAWznC4",this, presenterMaster)).start();
    }


    @Override
    public void getMyFriends() {
        new Thread(() -> {
            ArrayList<FriendEntity> friends = (ArrayList<FriendEntity>) interactorLocal.getFriends(User.getInstance().getIdUser());
            for (FriendEntity friendEntity :friends) {
                interactor.getFriends(friendEntity.idFriend,this );
            }
        }).start();
    }

    @Override
    public void showChats(User friend) {
        new Thread(() -> {
            FriendEntity friendEntity = interactorLocal.getFriend(friend.getIdUser());
            view.showChats(friend, friendEntity);
        }).start();

    }

}
