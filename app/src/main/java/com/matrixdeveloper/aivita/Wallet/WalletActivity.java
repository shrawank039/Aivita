package com.matrixdeveloper.aivita.Wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.matrixdeveloper.aivita.R;

public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
    }

    public void callOnbackpressed(View view) {
        onBackPressed();
    }

    public void withdrawConverty(View view) {
        startActivity(new Intent(getApplicationContext(),WithdrawConvert.class));
    }
}
