package com.ryabov.garage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class SignInPage extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, View.OnFocusChangeListener {

    EditText Ed_email, Ed_password;
    Button signIn_btn;
    private static final String TAG = "MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        Ed_email=findViewById(R.id.editTextTextEmailAddress);
        Ed_password=findViewById(R.id.editTextTextPassword);

        signIn_btn=findViewById(R.id.sign_in_button);
        signIn_btn.setOnClickListener(this);

        //Слушатель ввода текста в EditTExt
        Ed_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Ed_email.setHintTextColor(0xFF8E8E8E);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Ed_email.setTextColor(0xFF333333);
                Ed_email.setBackgroundResource(R.drawable.tile_separator_edit);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Ed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Ed_password.setTextColor(0xFF333333);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Ed_password.setTextColor(0xFF333333);
                Ed_password.setBackgroundResource(R.drawable.tile_separator_edit);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Слушатель нажатия на клавишу Enter
        Ed_password.setOnKeyListener(this);
        //Слушатель появления фокуса на EditText
        Ed_email.setOnFocusChangeListener(this);
        Ed_password.setOnFocusChangeListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.sign_in_button:
                String emails= "Garage1";
                String passwords = "12345";
                if(emails.equals(Ed_email.getText().toString()) && passwords.equals(Ed_password.getText().toString())){
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans, R.anim.alpha);
                    finish();
                } else if (emails.equals(Ed_email.getText().toString()) == false) {
                            Ed_email.setBackgroundResource(R.drawable.tile_separator_edit_error);
                            Ed_email.setTextColor(Color.RED);
                            Ed_email.setHintTextColor(Color.RED);
                             Toast.makeText(this, "Неправильный Email", Toast.LENGTH_SHORT).show();
                } else if (passwords.equals(Ed_password.getText().toString()) == false) {
                            Ed_password.setBackgroundResource(R.drawable.tile_separator_edit_error);
                            Ed_password.setTextColor(Color.RED);
                            Ed_password.setHintTextColor(Color.RED);
                            Toast.makeText(this, "Неправильный пароль", Toast.LENGTH_SHORT).show();
        }
                break;
        }

    }

    //Обработчик нажатия на клавиатуру
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        //Фильтр на двойное нажатие, без него активити Main вызывался 2 раза
        if (event.getAction()!=KeyEvent.ACTION_DOWN)
            return true;
        //Нажатие на Enter на клавиатуре
        if (keyCode==KEYCODE_ENTER) {
            String emails = "Garage1";
            String passwords = "12345";
            if (emails.equals(Ed_email.getText().toString()) && passwords.equals(Ed_password.getText().toString())) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans, R.anim.alpha);
                finish();
            } else if (emails.equals(Ed_email.getText().toString()) == false) {
                        Ed_email.setBackgroundResource(R.drawable.tile_separator_edit_error);
                        Ed_email.setTextColor(Color.RED);
                        Ed_email.setHintTextColor(Color.RED);
                        Toast.makeText(this, "Неправильный Email", Toast.LENGTH_SHORT).show();
            } else if (passwords.equals(Ed_password.getText().toString()) == false) {
                        Ed_password.setBackgroundResource(R.drawable.tile_separator_edit_error);
                        Ed_password.setTextColor(Color.RED);
                        Ed_password.setHintTextColor(Color.RED);
                        Toast.makeText(this, "Неправильный пароль", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    //Слушатель появления фокуса на EditText
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.editTextTextEmailAddress:
                if (hasFocus){
                    Ed_email.setBackgroundResource(R.drawable.tile_separator_edit);
                    Ed_email.setTextColor(0xFF333333);
                    Ed_email.setHintTextColor(0xFF8E8E8E);
                }
                break;
            case R.id.editTextTextPassword:
                if (hasFocus){
                    Ed_password.setBackgroundResource(R.drawable.tile_separator_edit);
                    Ed_password.setTextColor(0xFF333333);
                    Ed_password.setHintTextColor(0xFF8E8E8E);
                }
                break;
        }
    }
}