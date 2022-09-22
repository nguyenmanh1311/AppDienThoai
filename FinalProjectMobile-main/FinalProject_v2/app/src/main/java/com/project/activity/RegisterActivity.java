package com.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.databaseDao.UserDao;
import com.project.models.User;

import java.util.Base64;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtEmail, txtUsername, txtPassword, txtRepeatPassword, txtPhone, txtAddress;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
        setEvent();
    }

    private void initialize(){
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword);
        txtPhone = findViewById(R.id.txtMobile);
        txtAddress = findViewById(R.id.txtAddress);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setEvent(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtEmail.getText().toString();
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String repeatPassword = txtRepeatPassword.getText().toString();
                String phoneNumber = txtPhone.getText().toString();
                String address = txtAddress.getText().toString();

                // CHECK DATA INPUT
                if(email.compareTo("") == 0 || username.compareTo("") == 0 || phoneNumber.compareTo("") == 0 || address.compareTo("") == 0|| password.compareTo("") == 0 || repeatPassword.compareTo("") == 0){
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.compareTo(repeatPassword) != 0){
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // CREATE NEW dao TO REGISTER
                UserDao userDao = new UserDao();
                User user = new User(username, address, email, password, phoneNumber);
                userDao.register(getApplicationContext(), user);
                Intent i =new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }
}