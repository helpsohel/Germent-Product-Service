package com.example.germentproductservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SendRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = "ServerInteractionTask";
    public  String result = "";
    private final String serverUrl;
    private  Context context;

    public SendRequest(String serverUrl, Context cntx) {
        this.serverUrl = serverUrl;
        this.context = cntx;
    }

    @Override
    protected String doInBackground(String... params) {
        String data = params[0];


        try {
            // Create URL object
            URL url = new URL(serverUrl);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Send data
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                result = response.toString();
            }

            // Close connection
            connection.disconnect();
        } catch (IOException e) {
            Log.e(TAG, "Error sending POST request to server", e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "Server response: " + result);
    }
}