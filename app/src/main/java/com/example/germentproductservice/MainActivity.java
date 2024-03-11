package com.example.germentproductservice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static String serverURL = "http://192.168.0.108:3000";
    String createAccount = serverURL+"/logIn";
    Button submit;
    Button goCreateAccount;
    EditText email, password;
    JSONObject userData  = null;
    private Handler handler = new Handler();
    private Runnable runnable;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = findViewById(R.id.submit);
        goCreateAccount = findViewById(R.id.goToCreateAccount);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    userData = new JSONObject();

                    userData.put("Email",email.getText().toString());
                    userData.put("Password",password.getText().toString());

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

                                    SharedPreferences prefs = MySharedPreferences.getInstance(MainActivity.this);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("isLogIn",true);
                                    editor.putInt("customer_id",Integer.parseInt(res.getString("result")));
                                    editor.commit();
                                    Intent intent = new Intent(MainActivity.this, Home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    return;

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

   goCreateAccount.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, CreateAccount.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
});
    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = MySharedPreferences.getInstance(MainActivity.this);
        if (prefs.getBoolean("isLogIn",false)){
            Intent intent = new Intent(MainActivity.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        super.onResume();
    }
}