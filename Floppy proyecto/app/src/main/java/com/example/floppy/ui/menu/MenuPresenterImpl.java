package com.example.floppy.ui.menu;

import android.app.Activity;
import android.content.Context;

import com.example.floppy.domain.local.InteractorLocal;
import com.example.floppy.domain.local.InteractorSqlite;
import com.example.floppy.domain.models.Estado;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.remote.Interactor;
import com.example.floppy.domain.remote.InteractorFirestoreImpl;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.utils.global.FactoryJson;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MenuPresenterImpl implements MenuPresenter {
    final private MenuView        view;
    final private Interactor      interactor;
    final private GlobalPresenter presenterMaster;
    final private InteractorLocal interactorLocal;
    final private FactoryJson factoryJson= new FactoryJson();
    ArrayList<FriendEntity> friendEntities = new ArrayList<>();
    ArrayList<User>         friends        = new ArrayList<>();


    public MenuPresenterImpl(MenuView view, Context context, Activity activity, GlobalPresenter presenterMaster) {
        this.view            = view;
        this.interactor      = new InteractorFirestoreImpl(context,presenterMaster);
        this.presenterMaster = presenterMaster;
        this.interactorLocal = new InteractorSqlite(context);
    }

    @Override
    public void showState(String states) {
        view.showState((ArrayList<ArrayList<Estado>>) factoryJson.fromJson(states,FactoryJson.TypeObject.STATES));
    }
    @Override
    public void getStates(MenuPresenter presenter) {
        new Thread(() -> interactor.getEstados("aMPe5aVk1QGzqYAWznC4",this, presenterMaster)).start();
    }


    @Override
    public void getMyFriends() {
        new Thread(() -> {
            ArrayList<FriendEntity> friends = (ArrayList<FriendEntity>) interactorLocal.getFriends(User.getInstance().getIdUser());
            view.showChats(this.friends,friendEntities);
            for (FriendEntity friendEntity :friends) {
                interactor.getFriends(friendEntity.idFriend,this );
            }
        }).start();
    }

    @Override
    public void addChats(String user) {
        User friend = new Gson().fromJson(user,User.class) ;
        new Thread(() -> {
            FriendEntity friendEntity = interactorLocal.getFriend(friend.getIdUser());
            this.friends.add(friend);
            this.friendEntities.add(friendEntity);
            listenerChatFriend(friendEntity);
        }).start();
    }

    @Override
    public void friendIsWriting(FriendEntity friendEntity, String message) {
        view.friendIsWriting(friendEntity, (Message) new FactoryJson().fromJson(message, FactoryJson.TypeObject.MESSAGE));
    }

    @Override
    public void listenerChatFriend(FriendEntity friendEntity) {
        interactor.listenerChatFriend(friendEntity,this);
    }

    @Override
    public void destroyAllListenersFriends() { interactor.destroyAllListenersFriends(); }
}
