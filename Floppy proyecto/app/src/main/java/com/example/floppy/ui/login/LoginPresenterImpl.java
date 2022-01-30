package com.example.floppy.ui.login;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.floppy.data.Conexion.preferences.Preferences;
import com.example.floppy.domain.entities.UserEntity;
import com.example.floppy.domain.local.InteractorLocal;
import com.example.floppy.domain.local.InteractorSqlite;
import com.example.floppy.domain.remote.Interactor;
import com.example.floppy.domain.remote.InteractorFirestoreImpl;
import com.example.floppy.domain.models.Estado_User;
import com.example.floppy.domain.models.User;
import com.example.floppy.utils.Extensions;
import com.example.floppy.utils.global.FactoryJson;

import java.io.ByteArrayOutputStream;

public class LoginPresenterImpl implements LoginPresenter {
    private final User            user = User.getInstance();
    private final LoginView       loginView;
    private final Interactor      interactor;
    private       InteractorLocal interactorLocal;
    private       Preferences     preferences;

    public LoginPresenterImpl(LoginView loginView, Context context, Activity activity) {
        this.loginView       = loginView;
        this.interactor      = new InteractorFirestoreImpl(context,null);
        this.interactorLocal = new InteractorSqlite(context);
        this.preferences     = new Preferences(context);
    }

    @Override
    public Boolean isLogged() {
        String idOwner = preferences.getIdOwner();
        if(!idOwner.equals("")){
            new Thread(() -> {
                UserEntity userEntity = interactorLocal.getUser(idOwner);

                User user = User.getInstance();
                user.setIdUser     (idOwner);
                user.setMessageUser(userEntity.messageUser);
                user.setName       (userEntity.name);
                user.setPhoto      (userEntity.photo);

                nextActivity();
            }).start();
            return true;
        }
        return false;
    }

    /**
     * Validate email and password of user
     * @param data data[0] => Email
     *             data[1] => Pass
     * @param isLogin*/
    @Override
    public void validateData(String[] data, Boolean isLogin) {
        loginView.handlingLogin(true);
        if (data[0].isEmpty() || data[1].isEmpty()) {
            loginView.showToast("Debes llenar todo");
            loginView.handlingLogin(false);
            return;
        }
        if(!Extensions.Companion.validateEmail(data[0])){
            loginView.showToast("Email incorrecto");
            loginView.handlingLogin(false);
            return;
        }
        user.setEmail(data[0]);
        user.setPass (data[1]);

        new Thread(() ->{
            if (isLogin){
                interactor.Login(user, this);
            }else{
                interactor.validateUser(user, this);
            }
        }).start();
    }

    @Override
    public void showMessage(String msg) {
        loginView.showToast(msg);
        loginView.handlingLogin(false);
    }

    @Override
    public void showInputUser() {
        loginView.showInputUser();
    }


    @Override
    public void insertInfoUser(String[] data, Bitmap bitmap) {
        loginView.showHandlingInsertData(true);
        for (String fact : data) {
            System.out.println(fact);
            if (fact.equals("")) {
                loginView.showToast("Llena todo \nlos campos");
                loginView.showHandlingInsertData(false);
                return;
            }
        }
        User user =  User.getInstance();
        user.setEstado_user(Estado_User.ONLINE);
        user.setMessageUser(data[1]);
        user.setName       (data[0]);
        user.setPhoto      ("");
        byte[] img;
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream);
            img = byteArrayOutputStream.toByteArray();
        }
        new Thread(() -> interactor.insertUserRemote(user.toMap(user), LoginPresenterImpl.this)).start();
    }

    @Override
    public void insertUserOwner(String data) {
        UserEntity userEntity = (UserEntity) new FactoryJson().fromJson(data, FactoryJson.TypeObject.USERENTITY);
        User user         = User.getInstance();
        userEntity.idUser = user.getIdUser();
        interactorLocal.insertUser(userEntity);
    }

    @Override
    public void getMyDataRemote() {
        preferences.saveIdOwner(User.getInstance().getIdUser());
        interactor .getMyData(this, User.getInstance().getIdUser());
    }

    @Override
    public void insertUserLocal(String idUser) {
        UserEntity userEntity = new UserEntity();
        User user = User.getInstance();
        user.setIdUser(idUser);
        preferences.saveIdOwner(user.getIdUser());

        userEntity.idUser       = user.getIdUser();
        userEntity.messageUser  = user.getMessageUser();
        userEntity.name         = user.getName ();
        userEntity.photo        = user.getPhoto();

        interactorLocal.insertUser(userEntity);
    }

    @Override
    public void nextActivity() {
        loginView.showHandlingInsertData(false);
        loginView.nextActivity();
    }


}
