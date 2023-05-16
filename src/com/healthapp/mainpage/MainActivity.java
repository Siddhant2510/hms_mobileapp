package com.healthapp.mainpage;

import com.healthapp.homepage.R;
import com.healthapp.homepage.BaseActivity;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.io.IOException;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
// import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
// import okhttp3.WebSocket;
// import okhttp3.WebSocketListener;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private Handler handler = new Handler();
    int i = 1;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Update your activity here
            // ...
            // int i = 1;


            OkHttpClient client = new OkHttpClient();

            // Create a request
            Request request = new Request.Builder()
                .url("https://api.thingspeak.com/channels/2143383/feeds.json?api_key=CPLK8FV6EV3IPMPL")
                .build();

            // Make the request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Handle failure
                    Log.e("error","Ioexp",e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Get the response body as a string
                    Log.d("debug","onmyresponse");
                    if(!response.isSuccessful()) {
                        Log.d("debug","unsecc");
                        // Handle unsuccessful response
                        // You can update the UI to show an error message here
                        return;
                    }
                    String responseBody = response.body().string();

                    // Parse the JSON response
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        JSONArray feeds = json.getJSONArray("feeds");
                        JSONObject lastFeed = feeds.getJSONObject(feeds.length() - 1);
                        String field1 = lastFeed.getString("field1");
                        String field2 = lastFeed.getString("field2");
                        Log.e("error","checking");
                        // Element temp = (Element) findViewById(R.id.temperature);
                        // temp.setText(Integer.toString(i++));
                        Log.d("debug","bpm = "+field1+" temp = "+field2);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Element temp = (Element) findViewById(R.id.temperature);
                                if(field1 != null){
                                    temp.setText(field1);
                                }
                                else{
                                    temp.setText("--");
                                }

                                Element heartbeat = (Element) findViewById(R.id.heartbeat);
                                if(field1 != null){
                                    heartbeat.setText(field2);
                                }
                                else{
                                    heartbeat.setText("--");
                                }
                            }
                        });
                    } catch (JSONException e) {
                        // Handle JSON parsing error
                        Log.e("error","jsonexp",e);
                    }
                }
            });


            // Element temp = (Element) findViewById(R.id.temperature);
            // temp.setText(Integer.toString(i++));
            handler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////Websocket Code Starts//////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // OkHttpClient client = new OkHttpClient();
        // Request request = new Request.Builder()
        //     .url("ws://192.168.43.12:2510")
        //     .build();
        // WebSocketListener listener = new WebSocketListener() {
        //     @Override
        //     public void onOpen(WebSocket webSocket, Response response) {
        //         // handle onOpen event
        //         webSocket.send("Connected");
        //     }

        //     private Handler handler = new Handler(Looper.getMainLooper());

        //     @Override
        //     public void onMessage(WebSocket webSocket, String text) {
        //         // handle incoming message
        //         handler.post(new Runnable() {
        //             @Override
        //             public void run() {
        //                 if(text.charAt(0) == 't'){
        //                     Element temp = (Element) findViewById(R.id.temperature);
        //                     temp.setText(text.substring(1));
        //                 }
        //                 else if(text.charAt(0) == 'h'){
        //                     Element heart = (Element) findViewById(R.id.heartbeat);
        //                     heart.setText(text.substring(1));
        //                 }
        //                 else if(text.charAt(0) == 'o'){
        //                     Element spo2 = (Element) findViewById(R.id.spo2);
        //                     spo2.setText(text.substring(1));
        //                 }
        //                 else if(text.charAt(0) == 'e'){
        //                     Element ecg = (Element) findViewById(R.id.ecg);
        //                     ecg.setText(text.substring(1));
        //                 }
        //             }
        //         });
        //     }

        //     @Override
        //     public void onClosing(WebSocket webSocket, int code, String reason) {
        //         // handle closing event
        //         webSocket.send("Disconnected");
        //     }

        //     @Override
        //     public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        //         // handle failure event
        //         webSocket.send("failure");
        //     }
        // };
        // WebSocket ws = client.newWebSocket(request, listener);

                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////Websocket Code Starts//////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

}
