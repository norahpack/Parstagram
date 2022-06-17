package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class ChooseProfile extends AppCompatActivity {

    RadioGroup choiceList;
    Button btnUpdate;
    RadioButton finalChoice;
    RadioButton rbIcon;
    RadioButton rbSpace;
    RadioButton rbGreen;
    RadioButton rbSunflower;
    RadioButton rbClouds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile);

        rbIcon=findViewById(R.id.rbIcon);
        rbIcon.setTag(R.drawable.instagramtwo);

        rbSpace=findViewById(R.id.rbSpace);
        rbSpace.setTag(R.drawable.spaced);

        rbGreen=findViewById(R.id.rbGreen);
        rbGreen.setTag(R.drawable.green);

        rbSunflower=findViewById(R.id.rbSunflower);
        rbSunflower.setTag(R.drawable.sunflower);

        rbClouds=findViewById(R.id.rbClouds);
        rbClouds.setTag(R.drawable.clouds);

        choiceList=findViewById(R.id.choiceList);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idChecked = choiceList.getCheckedRadioButtonId();
                finalChoice=findViewById(idChecked);
                System.out.println(finalChoice);
                System.out.println(finalChoice.getTag());
                int choiceImage = (int) finalChoice.getTag();
                ParseUser currentUser = ParseUser.getCurrentUser();

                currentUser.put("profilePic", choiceImage);

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                        }
                        Intent i = new Intent(ChooseProfile.this, FeedActivity.class);
                        i.putExtra("goTo", "profile");
                        startActivity(i);
                        finish();
                    }
                });

            }
        });
    }
}