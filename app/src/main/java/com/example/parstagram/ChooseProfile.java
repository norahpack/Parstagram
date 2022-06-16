package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioGroup;

public class ChooseProfile extends AppCompatActivity {

    RadioGroup choiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile);
        choiceList=findViewById(R.id.choiceList);
        int idChecked = choiceList.getCheckedRadioButtonId();
    }
}