package com.matrixdeveloper.aivita.Wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.matrixdeveloper.aivita.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WithdrawConvert extends AppCompatActivity {

    RadioButton convert,withdraw;
    Spinner spinner_type;
    String type= "diamond";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_convert);

        convert = findViewById(R.id.convert);
        withdraw = findViewById(R.id.withdraw);
        spinner_type = findViewById(R.id.spinner_type);

        spinner_type = (Spinner) findViewById(R.id.spinner_country_code);
        // ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("diamond");
        arrayList.add("usd");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(arrayAdapter);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==1){
                    Toast.makeText(WithdrawConvert.this, "Withdrawal is coming soon!!!", Toast.LENGTH_SHORT).show();
                }
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                type = parent.getItemAtPosition(position).toString();
                // Toast.makeText(parent.getContext(), "Selected: " + mode, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });

    }
}
