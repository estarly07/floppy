package com.example.floppy.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.floppy.databinding.ActivityMainBinding;
import com.example.floppy.ui.mastercontrol.MasterControl;
import com.example.floppy.utils.Animations;
import com.example.floppy.utils.Extensions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private LoginPresenterImpl  presenter;
    private Bitmap              bitmapImageUser = null;
    private String              stickersReceived;
    private ActivityMainBinding binding;

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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        presenter        = new LoginPresenterImpl(this, this, this);
        stickersReceived = getSticker();

        new Handler().postDelayed(() -> {
            if(!presenter.isLogged()) { Animations.Companion.animVanish(binding.splash); }
        },3000);

        Animations.Companion.animAppear(binding.layoutInfoApp);

        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPass  = findViewById(R.id.txtPass);
        binding.btnLogin.setOnClickListener(view -> {
            page = Pages.DATA;
            presenter.validateData(new String[]{
                    txtEmail.getText().toString(),
                    txtPass.getText().toString(),
            },true);
        });
        binding.btnRegister.setOnClickListener(view -> {
            page = Pages.DATA;
            presenter.validateData(new String[]{
                    txtEmail.getText().toString(),
                    txtPass.getText().toString(),
            },false);
        });
        binding.btnComenzar.setOnClickListener(view -> {
            page = Pages.EMAIL;
            Animations.Companion.animVanish(binding.layoutInfoApp);
            Animations.Companion.animAppear(binding.layoutEditLogin);
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
                Animations.Companion.animationCascadeFade(binding.layoutInfoApp);
                Animations.Companion.animVanish(binding.layoutEditLogin);
                Animations.Companion.animAppear(binding.layoutInfoApp);
                break;
            case DATA:
                page = Pages.EMAIL;
                clearEdittext();
                binding.includeInput.imgUser.setImageDrawable(getDrawable(R.drawable.ic_user1));
                bitmapImageUser = null;
                handlingLogin(false);

                Animations.Companion.animVanish(binding.includeInput.getRoot());
                Animations.Companion.animVanish(binding.includeInput.getRoot());
                Animations.Companion.animAppear(binding.layoutEditLogin);
                Animations.Companion.animAppear(binding.layoutBotonValidacion);

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
            if (show) {
                Animations.Companion.animVanish(binding.layoutButtons);
                Animations.Companion.animAppear(binding.progressLogin);
            } else {
                Animations.Companion.animVanish(binding.progressLogin);
                Animations.Companion.animAppear(binding.layoutButtons);
            }
        });
    }

    final private int CODE_GALLERY = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GALLERY && resultCode == RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            try {
                bitmapImageUser = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.includeInput.imgUser.setImageBitmap(bitmapImageUser);

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
            RelativeLayout    cloud             = findViewById(R.id.nube);
            ImageView         floppy            = findViewById(R.id.floppy);
            TextView          txtMessage        = findViewById(R.id.txtMensaje);

            binding.includeInput.getRoot().setVisibility(View.VISIBLE);
            clearEdittext();

            Animations.Companion.animAppear(binding.includeInput.getRoot());
            Animations.Companion.animVanish(binding.layoutBotonValidacion);
            Animations.Companion.animationCascadeFade((RelativeLayout) findViewById(R.id.includeInput));
            animAppearAndDisappear(cloud);

            floppy.setOnClickListener(view -> {
                String[] sentences = getResources().getStringArray(R.array.frases);
                txtMessage.setText(sentences[new Random().nextInt(sentences.length)]);
                animAppearAndDisappear(cloud);
            });
            binding.includeInput.imgUser.setOnClickListener(view -> showGallery());

            binding.includeInput.btnNext.setOnClickListener(view -> presenter.insertInfoUser(
                    new String[]{binding.includeInput.edtNombre.getText().toString().trim(),binding.includeInput.edtEstado.getText().toString().trim() }
                    , bitmapImageUser)
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
                Animations.Companion.animVanish(binding.includeInput.btnNext);
                Animations.Companion.animAppear(binding.includeInput.progressInsert);
            } else {
                Animations.Companion.animVanish(binding.includeInput.progressInsert);
                Animations.Companion.animAppear(binding.includeInput.btnNext);
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
        Extensions.Companion.cleanEditText(binding.txtEmail);
        Extensions.Companion.cleanEditText(binding.includeInput.edtNombre);
        Extensions.Companion.cleanEditText(binding.includeInput.edtEstado);
        Extensions.Companion.cleanEditText(binding.txtPass);
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