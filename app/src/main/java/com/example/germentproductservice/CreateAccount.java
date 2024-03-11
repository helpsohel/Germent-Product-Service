package com.example.germentproductservice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class CreateAccount extends AppCompatActivity {
    String createAccount = MainActivity.serverURL+"/createAccount";
    Button submit;
    Button goLogin;
    EditText first_name, last_name,email, password, address;
    JSONObject userData  = null;
    private Handler handler = new Handler();
    private Runnable runnable;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        submit = findViewById(R.id.submit);
        goLogin = findViewById(R.id.goToLogin);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        address = findViewById(R.id.address);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    userData = new JSONObject();
                    userData.put("FirstName",first_name.getText().toString());
                    userData.put("LastName",last_name.getText().toString());
                    userData.put("Email",email.getText().toString());
                    userData.put("Password",password.getText().toString());
                    userData.put("Address",address.getText().toString());

                }catch (Exception e){}
                SendRequest task = new SendRequest(createAccount,getApplicationContext());
                task.execute(userData.toString());

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        // Your code here
                        Log.d("Handler", "Code executed");
                        if (task.result != ""){

                            JSONObject res ;
                            try {
                                res = new JSONObject(task.result);
                                if (res.getBoolean("isOK")){
                                    Toast.makeText(getApplicationContext(),res.getString("result"),Toast.LENGTH_LONG).show();

                                    SharedPreferences prefs = MySharedPreferences.getInstance(CreateAccount.this);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("isLogIn",true);
                                    editor.putInt("customer_id",Integer.parseInt(res.getString("result")));
                                    editor.commit();
                                    Intent intent = new Intent(CreateAccount.this, Home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Faild "+res.getString("result"),Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception e){ }

                            handler.removeCallbacks(this);

                        }else {
                            // Repeat every 1000 milliseconds (1 second)
                            handler.postDelayed(this, 2000);
                        }

                    }
                };

                // Start the initial execution with a delay of 1000 milliseconds (1 second)
                handler.postDelayed(runnable, 1000);


            }
        });
goLogin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(CreateAccount.this, MainActivity.class));
    }
});

    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = MySharedPreferences.getInstance(CreateAccount.this);
        if (prefs.getBoolean("isLogIn",false)){
            Intent intent = new Intent(CreateAccount.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        super.onResume();
    }
}