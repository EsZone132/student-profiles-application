package com.example.a40212845_ass2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a40212845_ass2.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected ListView studentsListView;
    protected TextView totalProfilesView;
    protected FloatingActionButton addStudentFloatingButton;
    protected DatabaseHelper dbHelper = new DatabaseHelper(this);
    private boolean displayMode = true;
    private String accessType = null;
    private static ArrayList<String> orderedStudentsListText;
    private int profile_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        studentsListView = findViewById(R.id.studentsListView);
        totalProfilesView = findViewById(R.id.totalProfiles);
        addStudentFloatingButton = findViewById(R.id.addStudentFloatingButton);

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main Activity");

        loadListView();
        addStudentFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show dialog fragment to add new profile when floating button is clicked
                InsertProfileDialogFragment dialogFragment = new InsertProfileDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "Insert Fragment");
            }
        });
        studentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get String of the clicked item on the list
                String selectedItem = orderedStudentsListText.get(position);
                String[] split = selectedItem.split("\\.");
                // takes full name without the order number
                String studentFullName = split[1];
                //Takes all profiles in database
                List<StudentProfile> students = dbHelper.getAllProfiles();
                // Goes through all the profiles to check which name matches with the clicked item
                for (int i = 0; i < students.size(); i++) {
                    String fullName = " ";
                    fullName += students.get(i).getSurname() + ", " + students.get(i).getName();
                    if(studentFullName.equals(fullName)){
                        profile_id = students.get(i).getProfileID(); // When the profile is found, id is saved
                    }
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd @ hh:mm:ss");
                String timestamp = dateFormat.format(new Date());
                dbHelper.insertAccess(new Access(profile_id, "Opened", timestamp)); // Creates opened entry when profile from listView is clicked
                goToProfileActivity();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Toggle option
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toggle_modes, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem editItem){
        if (editItem.getItemId() == R.id.toggleModes) {
            // When toggle mode is clicked
            displayMode = !displayMode; // indicates that display mode has changed to function changeList
            changeList(displayMode);
            return true;
        }
        return super.onOptionsItemSelected(editItem);
    }

    private void changeList(boolean displayMode) {

        if (!displayMode) {

            List<StudentProfile> students = dbHelper.getAllProfiles();
            ArrayList<String> studentsListText = new ArrayList<>();
            ArrayList<String> orderedStudentsListText = new ArrayList<>();

            //String that takes ID and adds it in a List for each created profile
            for (int i = 0; i < students.size(); i++) {
                String temp = "";
                temp += students.get(i).getProfileID();
                studentsListText.add(temp);
            }

            Collections.sort(studentsListText);// Sort list in ascending order

            // Add the order number of the id as a list
            for (int i = 0; i < students.size(); i++) {
                String temp = "";
                temp += i + 1 + ". " + studentsListText.get(i);
                orderedStudentsListText.add(temp);
            }

            // Display ordered list in main activity
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderedStudentsListText);
            studentsListView.setAdapter(arrayAdapter);
            totalProfilesView.setText(dbHelper.getTotalProfiles() + " Profiles, by ID"); // Sets the total number of profiles in textView
        }
        else
            loadListView();
    }
    public void loadListView() {

        List<StudentProfile> students = dbHelper.getAllProfiles(); //Puts StudentProfile objects in a list
        ArrayList<String> studentsListText = new ArrayList<>();
        orderedStudentsListText = new ArrayList<>();

        // Go through student profile list to get their surname and name
        for(int i = 0; i < students.size(); i++){
            String temp = "";
            temp+=students.get(i).getSurname() + ", ";
            temp+=students.get(i).getName();
            studentsListText.add(temp); // adds surname and name only in a list
        }

        Collections.sort(studentsListText); // Sort list in alphabetical order

        // Add the order number of the names as a list
        for(int i = 0; i < students.size(); i++){
            String temp = "";
            temp+= i+1 + ". " + studentsListText.get(i);
            orderedStudentsListText.add(temp);
        }

        // Display ordered list in main activity
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderedStudentsListText);
        studentsListView.setAdapter(arrayAdapter);
        totalProfilesView.setText(dbHelper.getTotalProfiles() + " Profiles, by Surname"); // Sets the total number of profiles in textView
    }
    protected void goToProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("profile_id", profile_id); // Pass the profile id of the clicked profile in the list the the profile activity
        startActivity(intent);
    }
}