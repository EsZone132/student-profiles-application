package com.example.a40212845_ass2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a40212845_ass2.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    protected ListView accessListView;
    protected Toolbar toolbar;
    protected TextView userProfile;
    protected TextView Surname;
    protected TextView Name;
    protected TextView id;
    protected TextView gpa;
    protected TextView dateCreated;
    protected Button deleteButton;
    protected DatabaseHelper dbHelper = new DatabaseHelper(this);
    protected String surname = "";

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        accessListView = findViewById(R.id.accessListView);
        toolbar = findViewById(R.id.toolbar);
        userProfile = findViewById(R.id.userProfile);
        Surname = findViewById(R.id.Surname);
        Name = findViewById(R.id.Name);
        id = findViewById(R.id.id);
        gpa = findViewById(R.id.gpa);
        dateCreated = findViewById(R.id.dateCreated);
        deleteButton = findViewById(R.id.deleteButton);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Activity");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //Back button

        Intent intent = getIntent();
        int profile_id = intent.getIntExtra("profile_id",0);

        List<StudentProfile> students = dbHelper.getAllProfiles();
        for(int i = 0; i < students.size(); i++){
            if(profile_id == students.get(i).getProfileID()){
                surname = students.get(i).getSurname();
                Surname.setText("Surname: " + students.get(i).getSurname());
                Name.setText("Name: " + students.get(i).getName());
                id.setText("ID: " + students.get(i).getProfileID());
                gpa.setText("GPA: " + students.get(i).getGPA());
                dateCreated.setText("Profile created: " + students.get(i).getDateCreated());
            }
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteStudentProfile(surname);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd @ hh:mm:ss");
                String timestamp = dateFormat.format(new Date());
                dbHelper.insertAccess(new Access(profile_id, "Deleted", timestamp));
                finish();
                goToMainActivity();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd @ hh:mm:ss");
                String timestamp = dateFormat.format(new Date());
                dbHelper.insertAccess(new Access(profile_id, "Closed", timestamp));
                finish();
            }
        });

        List<Access> access = dbHelper.getAllAccess(profile_id);
        ArrayList<String> accessListText = new ArrayList<>();
        for(int i = 0; i < access.size(); i++){
            String temp = "";
            temp+=access.get(i).getTimestamp() + " ";
            temp+=access.get(i).getAccessType();
            accessListText.add(temp);
        }
        Collections.reverse(accessListText);
        View headerView = getLayoutInflater().inflate(R.layout.list_header, null);
        accessListView.addHeaderView(headerView);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accessListText);
        accessListView.setAdapter(arrayAdapter);
    }
    protected void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}