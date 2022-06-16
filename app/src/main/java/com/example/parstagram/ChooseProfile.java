package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ChooseProfile extends AppCompatActivity {

    RadioGroup choiceList;
    Button btnUpdate;
    RadioButton finalChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile);
        choiceList=findViewById(R.id.choiceList);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idChecked = choiceList.getCheckedRadioButtonId();
                finalChoice=findViewById(idChecked);
                finalChoice.getButtonDrawable();
                Intent i = new Intent(ChooseProfile.this, FeedActivity.class);
                startActivity(i);
            }
        });
    }
}