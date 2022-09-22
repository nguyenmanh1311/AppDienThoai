package com.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.databaseDao.UserDao;
import com.project.util.Const;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnForwardRegister;
    private EditText txtEmail, txtPassword;
    private UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        setEvent();

    }

    private void initialize(){
        userDao = new UserDao();
        btnLogin = findViewById(R.id.btnLogin);
        btnForwardRegister = findViewById(R.id.btnForwardRegister);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
    }

    private void setEvent(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString().trim();
                String pass = txtPassword.getText().toString().trim();
                // login
                if(email.compareTo("") != 0 && pass.compareTo("") != 0){
                    SharedPreferences ref = getApplicationContext().getSharedPreferences(Const.SHARED_PREFERENCE, MODE_PRIVATE);
                    userDao.login(getApplicationContext(), ref, email, pass);

                }else{
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * forward to Register if user want register new accout
         */
        btnForwardRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }



}