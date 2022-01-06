package com.example.floppy.ui.aboutfriend;

import android.content.Context;

import com.example.floppy.domain.local.InteractorLocal;
import com.example.floppy.domain.local.InteractorSqlite;

public class AboutFriendPresenterImpl implements AboutFriendPresenter {
    private final AboutFriendView aboutFriendView;
    private final InteractorLocal interactorLocal;

    public AboutFriendPresenterImpl(Context context, AboutFriendView aboutFriendView) {
        this.aboutFriendView = aboutFriendView;
        this.interactorLocal = new InteractorSqlite(context);
    }

    @Override
    public void updateFriend(String idFriend) { aboutFriendView.showDialog(idFriend); }

    @Override
    public Boolean updateNick(String nick, String idFriend) {
        if(nick.isEmpty()){ return false; }
        new Thread(()-> interactorLocal.updateNickFriend(nick, idFriend)).start();
        return true;
    }
}
