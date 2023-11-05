package com.example.a40212845_ass2;

import static android.text.TextUtils.isEmpty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.a40212845_ass2.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.List;

public class InsertProfileDialogFragment extends DialogFragment {
    protected EditText studentSurnameEditText;
    protected EditText studentNameEditText;
    protected EditText studentIDEditText;
    protected EditText gpaEditText;
    protected Button saveButton;
    protected Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_student_profile, container, false);

        studentSurnameEditText = view.findViewById(R.id.surnameEditText);
        studentNameEditText = view.findViewById(R.id.nameEditText);
        studentIDEditText = view.findViewById(R.id.studentIDEditTextNumber);
        gpaEditText = view.findViewById(R.id.gpaEditTextNumberDecimal);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String surname = studentSurnameEditText.getText().toString();
                String name = studentNameEditText.getText().toString();
                int id = Integer.parseInt(studentIDEditText.getText().toString());
                float gpa = Float.parseFloat(gpaEditText.getText().toString());

                if (id < 10000000 || id > 99999999) {
                    Toast.makeText(getContext(), "Invalid ID", Toast.LENGTH_SHORT).show();
                } else if (gpa == 0 || gpa > 4.31) {
                    Toast.makeText(getContext(), "Invalid GPA", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(surname) || isEmpty(name) || String.valueOf(id).equals("") || String.valueOf(gpa).equals("")) {
                    Toast.makeText(getContext(), "Empty Field(s)", Toast.LENGTH_SHORT).show();
                } else if (idIsValid(id)) {
                    Toast.makeText(getContext(), "ID already exists", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd @ hh:mm:ss");
                    String dateTimeCreated = dateFormat.format(new Date());
                    DatabaseHelper dbHelper = new DatabaseHelper((getActivity()));
                    dbHelper.insertStudentProfile(new StudentProfile(surname, name, id, gpa, dateTimeCreated));
                    String accessType = "Created";
                    dbHelper.insertAccess(new Access(id, accessType, dateTimeCreated));
                    ((MainActivity) getActivity()).loadListView();
                    getDialog().dismiss();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return view;
    }
    public boolean idIsValid(int id){
        DatabaseHelper dbHelper = new DatabaseHelper((getActivity()));
        List<StudentProfile> students = dbHelper.getAllProfiles();
        boolean invalid = false;
        for(int i = 0; i < students.size(); i++){
            if(id == students.get(i).getProfileID()){
                invalid = true;
            }
        }
        return invalid;
    }
}
