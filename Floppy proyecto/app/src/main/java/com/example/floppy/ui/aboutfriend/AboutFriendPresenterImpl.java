package com.example.floppy.ui.aboutfriend;

import android.content.Context;

import com.example.floppy.data.Interactor.local.InteractorLocal;
import com.example.floppy.data.Interactor.local.InteractorSqlite;

public class AboutFriendPresenterImpl implements AboutFriendPresenter {
    private final AboutFriendView aboutFriendView;
    private final InteractorLocal interactorLocal;

    public AboutFriendPresenterImpl(Context context, AboutFriendView aboutFriendView) {
        this.aboutFriendView = aboutFriendView;
        this.interactorLocal = new InteractorSqlite(context);
    }

    @Override
    public void updateFriend() { aboutFriendView.showDialog(); }

    @Override
    public Boolean updateNick(String nick) {
        if(nick.isEmpty()){ return false; }
        new Thread(()-> interactorLocal.updateNickFriend(nick)).start();
        return true;
    }
}
