package com.singapore.career;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class AddJobActivity extends AppCompatActivity {

    public static final String DATABASE_NAME = "myjobdatabase";

    EditText editTextName, editTextDescription, editTextSalary;
    Spinner spinnerDepartment;
    String saved = "0";
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        //add back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextSalary = (EditText) findViewById(R.id.editTextSalary);
        spinnerDepartment = (Spinner) findViewById(R.id.spinnerDepartment);

        findViewById(R.id.buttonAddJob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJob();
            }
        });

        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

    }

    //this method will validate the name and salary
    //dept does not need validation as it is a spinner and it cannot be empty
    private boolean inputsAreCorrect(String name, String salary) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a job title");
            editTextName.requestFocus();
            return false;
        }

        if (salary.isEmpty() || Integer.parseInt(salary) <= 0) {
            editTextSalary.setError("Please enter salary");
            editTextSalary.requestFocus();
            return false;
        }
        return true;
    }

    //In this method we will do the create operation
    private void addJob() {

        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String salary = editTextSalary.getText().toString().trim();
        String dept = spinnerDepartment.getSelectedItem().toString();

        //getting the current time for creating date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String createddate = sdf.format(cal.getTime());

        //validating the inptus
        if (inputsAreCorrect(name, salary)) {

            String insertSQL = "INSERT INTO jobs \n" +
                    "(name, department, description, createddate, salary,saved)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?,?);";

            //using the same method execsql for inserting values
            //this time it has 6 parameters
            //first is the sql string and second is the parameters that is to be binded with the query
            try {
                mDatabase.execSQL(insertSQL, new String[]{name, dept, description, createddate, salary, saved});
            } catch (SQLiteException e) {
                Log.e("Exception", e.getMessage());
            }
            editTextName.setText("");
            editTextDescription.setText("");
            editTextSalary.setText("");
            Toast.makeText(this, "Job Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    //this method used when you click on top left back arrow
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
