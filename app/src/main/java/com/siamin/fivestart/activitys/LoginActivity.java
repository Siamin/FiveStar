package com.siamin.fivestart.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.siamin.fivestart.MyActivity;
import com.siamin.fivestart.R;
import com.siamin.fivestart.models.ErrorModel;

public class LoginActivity extends MyActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void LoginSubmit(View view){
        EditText password = findViewById(R.id.loginPassword);
        ErrorModel errorModel = passwordController.checkPassword(password.getText().toString());
        if (errorModel.Status){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else{
            message.Snackbar(errorModel.Message,null,R.color.colorRed,R.drawable.messagestyle_error);
        }
    }
}
