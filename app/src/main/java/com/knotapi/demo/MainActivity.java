package com.knotapi.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.knotapi.cardonfileswitcher.CardOnFileSwitcher;
import com.knotapi.cardonfileswitcher.OnSessionEventListener;

public class MainActivity extends AppCompatActivity implements OnSessionEventListener {

    CardOnFileSwitcher cardOnFileSwitcher;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            cardOnFileSwitcher = CardOnFileSwitcher.getInstance();
            cardOnFileSwitcher.init(this);
            cardOnFileSwitcher.setOnSessionEventListener(this);
            cardOnFileSwitcher.openCardOnFileSwitcher(this, "fc3fe2a1-0795-4e33-84c6-5f34ab96f61a", new int[]{1, 3});
        });
    }

    @Override
    public void onInvalidSession(String sessionId, String errorMessage) {
        Log.d("onInvalidSession", errorMessage);
    }

    @Override
    public void onSuccess(String merchant) {
        Log.d("onSuccess from main", merchant);
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
        Log.d("onError", errorCode + " " + errorMessage);
    }

    @Override
    public void onExit() {
        Log.d("onExit", "exit");
    }

    @Override
    public void onFinished() {
        Log.d("onFinished", "finished");
    }

    @Override
    public void onEvent(String eventName, String merchantName) {
        Log.d("onEvent", eventName + " " + merchantName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", requestCode + " " + resultCode);
    }
}