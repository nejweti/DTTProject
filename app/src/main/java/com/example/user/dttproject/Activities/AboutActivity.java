package com.example.user.dttproject.Activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.user.dttproject.R;

import java.util.regex.Pattern;


public class AboutActivity extends AppCompatActivity {
    private TextView aboutParagraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutParagraph = findViewById(R.id.aboutParagraph);

        Pattern pattern = Pattern.compile("[a-zA-Z]+.nl");
        TextView myCustomLink = new TextView(this);
        myCustomLink.setText(aboutParagraph.getText());
        Linkify.addLinks(myCustomLink,pattern,"www.rsr.nl");


        //set title on the Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("RSR Revalidatieservice");

        //back button on the Action Bar
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
