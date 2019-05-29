package com.example.retrofitapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import api.EmployeeAPI;
import model.Employee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateDelete extends AppCompatActivity{
    private final static String BASE_URL = "http://dummy.restapiexample.com/api/v1/";
    private Button btnSearch, btnUpdate, btnDelete;
    private EditText etEmpNo, etEmpName, etEmpAge, etEmpSalary;
    Retrofit retrofit;
    EmployeeAPI employeeAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        btnSearch = findViewById(R.id.btnUSearch);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        etEmpName = findViewById(R.id.etEmpName);
        etEmpSalary = findViewById(R.id.etEmpSalary);
        etEmpAge = findViewById(R.id.etEmpAge);
        etEmpNo = findViewById(R.id.etEmpID);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee();
            }
        });
    }



    private void loadData() {
        CreateInstance();
        Call<Employee> employeeCall = employeeAPI.getEmployeeByID(Integer.parseInt(etEmpNo.getText().toString()));
        employeeCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                etEmpName.setText(response.body().getEmployee_name());
                etEmpAge.setText(Integer.toString(response.body().getEmployee_age()));
                etEmpSalary.setText(Float.toString(response.body().getEmployee_salary()));
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(UpdateDelete.this, "Error :" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateEmployee(){

        CreateInstance();

        Employee employee = new Employee(
                etEmpName.getText().toString(),
                Float.parseFloat(etEmpSalary.getText().toString()),
                Integer.parseInt(etEmpAge.getText().toString())
        );

        Call<Void> voidCall = employeeAPI.updateEmployee(Integer.parseInt(etEmpNo.getText().toString()), employee);

        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UpdateDelete.this, "Successfully updated", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateDelete.this, "Error :"+ t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    private void deleteEmployee(){
        CreateInstance();
        Call<Void> voidCall = employeeAPI.deleteEmployee(Integer.parseInt(etEmpNo.getText().toString()));
        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UpdateDelete.this, "Successfully deleted",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateDelete.this, " Error :"+ t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void CreateInstance(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employeeAPI = retrofit.create(EmployeeAPI.class);
    }
}
