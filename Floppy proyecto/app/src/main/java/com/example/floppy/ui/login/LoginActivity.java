package com.example.floppy.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.floppy.R;
import com.example.floppy.ui.mastercontrol.MasterControl;
import com.example.floppy.utils.Animations;
import com.example.floppy.utils.Extensions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private LinearLayout        layoutEditText, layoutInfoApp;
    private LoginPresenterImpl  presenter;
    private Button              btnLogin,btnRegister, btnStart;
    private RelativeLayout      layout;
    private Bitmap              bitmapImageUser = null;
    private EditText            edtName;
    private EditText            edtState;
    private String              stickersReceived;
    enum Pages {
        INIT,
        EMAIL,
        DATA
    }
    private Pages page = Pages.INIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter        = new LoginPresenterImpl(this, this, this);
        stickersReceived = getSticker();
        presenter.isLogged();

        btnLogin        = findViewById(R.id.btnLogin);
        btnRegister     = findViewById(R.id.btnRegister);
        btnStart        = findViewById(R.id.btnComenzar);
        layoutEditText  = findViewById(R.id.layoutEditLogin);
        layoutInfoApp   = findViewById(R.id.layoutInfoApp);
        layout          = findViewById(R.id.includeInput);

        Animations.Companion.animAppear(layoutInfoApp);

        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPass  = findViewById(R.id.txtPass);
        btnLogin.setOnClickListener(view -> {
            page = Pages.DATA;
            presenter.validateData(new String[]{
                    txtEmail.getText().toString(),
                    txtPass.getText().toString(),
            },true);
        });
        btnRegister.setOnClickListener(view -> {
            page = Pages.DATA;
            presenter.validateData(new String[]{
                    txtEmail.getText().toString(),
                    txtPass.getText().toString(),
            },false);
        });
        btnStart.setOnClickListener(view -> {
            page = Pages.EMAIL;
            Animations.Companion.animVanish(layoutInfoApp);
            Animations.Companion.animAppear(layoutEditText);
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBackPressed() {
        System.out.println(page.toString());
        switch (page) {
            case INIT:
                super.onBackPressed();
                break;
            case EMAIL:
                page = Pages.INIT;
                Animations.Companion.animationCascadeFade((LinearLayout) findViewById(R.id.layoutInfoApp));
                Animations.Companion.animVanish(findViewById(R.id.layoutEditLogin));
                Animations.Companion.animAppear(findViewById(R.id.layoutInfoApp));
                break;
            case DATA:
                page = Pages.EMAIL;
                clearEdittext();
                ImageView imgUser = findViewById(R.id.imgUser);
                imgUser.setImageDrawable(getDrawable(R.drawable.ic_user1));
                bitmapImageUser = null;
                handlingLogin(false);

                Animations.Companion.animVanish(layout);
                Animations.Companion.animVanish(findViewById(R.id.includeInput));
                Animations.Companion.animAppear(findViewById(R.id.layoutEditLogin));
                Animations.Companion.animAppear(findViewById(R.id.layoutBotonValidacion));

                Animations.Companion.animationCascadeFade((RelativeLayout) findViewById(R.id.includeInput));

                break;
        }
    }

    @Override
    public void showToast(String msg) {
        //TODO: Extensions.Companion.showMessage(msg,this,R.layout.toas)
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void handlingLogin(boolean show) {
        runOnUiThread(() -> {
            ProgressBar progressBar = findViewById(R.id.progressLogin);
            if (show) {
                Animations.Companion.animVanish(findViewById(R.id.layoutButtons));
                Animations.Companion.animAppear(progressBar);
            } else {
                Animations.Companion.animVanish(progressBar);
                Animations.Companion.animAppear(findViewById(R.id.layoutButtons));
            }
        });
    }

    final private int CODE_GALLERY = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GALLERY && resultCode == RESULT_OK) {
            RoundedImageView img = findViewById(R.id.imgUser);
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            try {
                bitmapImageUser = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            img.setImageBitmap(bitmapImageUser);

        }
    }


    private void showGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CODE_GALLERY);
    }

    @Override
    public void showInputUser() {
        runOnUiThread(() -> {
            RelativeLayout    layoutValidation  = findViewById(R.id.layoutBotonValidacion);
            RelativeLayout    include           = findViewById(R.id.includeInput);
            RelativeLayout    cloud             = findViewById(R.id.nube);
            ImageView         floppy            = findViewById(R.id.floppy);
            RoundedImageView  photo             = findViewById(R.id.imgUser);
            TextView          txtMessage        = findViewById(R.id.txtMensaje);
            RelativeLayout    btnNext           = findViewById(R.id.btnNext);
            edtName = findViewById(R.id.edtNombre);
            edtState = findViewById(R.id.edtEstado);

            include.setVisibility(View.VISIBLE);
            clearEdittext();

            Animations.Companion.animAppear(layout);
            Animations.Companion.animVanish(layoutValidation);
            Animations.Companion.animationCascadeFade((RelativeLayout) findViewById(R.id.includeInput));
            animAppearAndDisappear(cloud);

            floppy.setOnClickListener(view -> {
                String[] sentences = getResources().getStringArray(R.array.frases);
                txtMessage.setText(sentences[new Random().nextInt(sentences.length)]);
                animAppearAndDisappear(cloud);
            });
            photo.setOnClickListener(view -> showGallery());

            btnNext.setOnClickListener(view -> {
                presenter.insertInfoUser(
                        new String[]{edtName.getText().toString().trim(),edtState.getText().toString().trim() }
                        , bitmapImageUser); }
            );

        });
    }

    @Override
    public void showHandlingGeneral(boolean show) {

    }

    @Override
    public void showHandlingInsertData(boolean show) {
        runOnUiThread(() -> {
            if (show) {
                Animations.Companion.animVanish(findViewById(R.id.btnNext));
                Animations.Companion.animAppear(findViewById(R.id.progress_insert));
            } else {
                Animations.Companion.animVanish(findViewById(R.id.progress_insert));
                Animations.Companion.animAppear(findViewById(R.id.btnNext));
            }
        });
    }

    @Override
    public void nextActivity() {
        runOnUiThread(() -> {
            Intent intent = new Intent(LoginActivity.this, MasterControl.class);
            MasterControl.stickersReceiver = stickersReceived;
            startActivity(intent);
            finish();
        });
    }

    /**Clean the EditText*/
    private void clearEdittext() {
        Extensions.Companion.cleanEditText(findViewById(R.id.txtEmail));
        Extensions.Companion.cleanEditText(findViewById(R.id.edtNombre));
        Extensions.Companion.cleanEditText(findViewById(R.id.edtEstado));
        Extensions.Companion.cleanEditText(findViewById(R.id.txtPass));
    }

    private void animAppearAndDisappear(View view) {Animations.Companion.animAppearAndVanish(view);}

    private void animTraslationY(boolean show, View view) {
        float moveX = show ? 0 : (view.getWidth() * -2);
        view.animate().translationX(moveX).setStartDelay(0).setDuration(1000).start();
    }
    /**OBTENER LO COMPARTIDO POR LA APP Sticker Floppy */
    private String getSticker() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedStickers = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedStickers != null) { 
                    return sharedStickers;               
                }
            }
        }
        return  "";
    }

}