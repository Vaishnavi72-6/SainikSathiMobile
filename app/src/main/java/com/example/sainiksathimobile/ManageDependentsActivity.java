package com.example.sainiksathimobile;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sainiksathimobile.db.DependentDbHelper;

import java.util.List;

public class ManageDependentsActivity extends AppCompatActivity {

    EditText editName, editRelation, editDob;
    Button btnAdd;
    ListView listDependents;
    ArrayAdapter<String> adapter;
    DependentDbHelper dbHelper;
    List<String> dependents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_dependents);

        editName = findViewById(R.id.editName);
        editRelation = findViewById(R.id.editRelation);
        editDob = findViewById(R.id.editDob);
        btnAdd = findViewById(R.id.btnAdd);
        listDependents = findViewById(R.id.listDependents);

        dbHelper = new DependentDbHelper(this);
        loadDependents();

        btnAdd.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String relation = editRelation.getText().toString().trim();
            String dob = editDob.getText().toString().trim();

            if (name.isEmpty() || relation.isEmpty() || dob.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.insertDependent(name, relation, dob);
            Toast.makeText(this, "Dependent added", Toast.LENGTH_SHORT).show();
            editName.setText("");
            editRelation.setText("");
            editDob.setText("");
            loadDependents();
        });

        listDependents.setOnItemLongClickListener((parent, view, position, id) -> {
            String selected = dependents.get(position);
            String name = selected.split("\n")[0].replace("Name: ", "").trim();
            showDeleteDialog(name);
            return true;
        });
    }

    private void loadDependents() {
        dependents = dbHelper.getAllDependents();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dependents);
        listDependents.setAdapter(adapter);
    }

    private void showDeleteDialog(String name) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Dependent")
                .setMessage("Delete dependent \"" + name + "\"?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteDependent(name);
                    loadDependents();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
