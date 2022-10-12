package com.knotapi.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.knotapi.cardonfileswitcher.CardOnFileSwitcher;
import com.knotapi.cardonfileswitcher.Environment;
import com.knotapi.cardonfileswitcher.OnSessionEventListener;
import com.knotapi.cardonfileswitcher.SubscriptionCanceler;
import com.knotapi.cardonfileswitcher.model.Customization;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnSessionEventListener {

    Button btnOpenCardSwitcher, btnOpenSubscriptionCanceller;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpenCardSwitcher = findViewById(R.id.btnOpenCardSwitcher);
        btnOpenSubscriptionCanceller = findViewById(R.id.btnOpenSubscriptionCanceller);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        btnOpenCardSwitcher.setOnClickListener(view -> callCreateUserAPI(false));
        btnOpenSubscriptionCanceller.setOnClickListener(view -> callCreateUserAPI(true));
    }

    private void callCreateUserAPI(Boolean openSubCanceller) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.first_name = "Nikunj";
        createUserRequest.last_name = "Patel";
        createUserRequest.email = "production@knotapi.com";
        createUserRequest.phone_number = "+18024687679";
        createUserRequest.password = "password";
        createUserRequest.address1 = "348 W 57th St";
        createUserRequest.address2 = "#367";
        createUserRequest.state = "NY";
        createUserRequest.city = "New York";
        createUserRequest.postal_code = "10019";

        progressDialog.show();
        Call<CreateUserResponse> call = RetrofitClient.getInstance().getMyApi().createUserAPI(createUserRequest);
        call.enqueue(new Callback<CreateUserResponse>() {
            @Override
            public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                CreateUserResponse createUserResponse = response.body();
                callCreateSessionAPI(createUserResponse.getToken(), openSubCanceller);
            }

            @Override
            public void onFailure(Call<CreateUserResponse> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callCreateSessionAPI(String token, Boolean openSubCanceller) {
        Call<CreateSessionResponse> call = RetrofitClient.getInstance().getMyApi().createSessionAPI("Bearer " + token);
        call.enqueue(new Callback<CreateSessionResponse>() {
            @Override
            public void onResponse(Call<CreateSessionResponse> call, Response<CreateSessionResponse> response) {
                CreateSessionResponse createSessionResponse = response.body();
                progressDialog.hide();
                if (openSubCanceller) {
                    openSubscriptionCanceller(createSessionResponse.getSession());
                } else {
                    openCardSwitcher(createSessionResponse.getSession());
                }
            }

            @Override
            public void onFailure(Call<CreateSessionResponse> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openCardSwitcher(String sessionId) {
        Customization customization = new Customization();
        customization.setTextColor("#ffffff");
        customization.setPrimaryColor("#000000");
        customization.setCompanyName("Millions");

        CardOnFileSwitcher cardOnFileSwitcher = CardOnFileSwitcher.getInstance();
        cardOnFileSwitcher.init(this, "ab86955e-22f4-49c3-97d7-369973f4cb9e", Environment.SANDBOX);
        cardOnFileSwitcher.setOnSessionEventListener(this);
        cardOnFileSwitcher.setCustomization(customization);
        cardOnFileSwitcher.openCardOnFileSwitcher(sessionId, new int[]{});
    }

    private void openSubscriptionCanceller(String sessionId) {
        Customization customization = new Customization();
        customization.setTextColor("#fff000");
        customization.setPrimaryColor("#ff0000");
        customization.setCompanyName("Millions");

        SubscriptionCanceler subscriptionCanceler = SubscriptionCanceler.getInstance();
        subscriptionCanceler.setCustomization(customization);
        subscriptionCanceler.setOnSessionEventListener(this);
        subscriptionCanceler.init(this, "ab86955e-22f4-49c3-97d7-369973f4cb9e", Environment.SANDBOX);
        subscriptionCanceler.openCardOnFileSwitcher(sessionId);
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