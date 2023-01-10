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
import com.knotapi.cardonfileswitcher.SubscriptionCanceler;
import com.knotapi.cardonfileswitcher.interfaces.OnSessionEventListener;
import com.knotapi.cardonfileswitcher.models.Configuration;
import com.knotapi.cardonfileswitcher.models.Environment;
import com.knotapi.cardonfileswitcher.models.Options;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnSessionEventListener {

    Button btnOpenCardSwitcher, btnOpenSubscriptionCanceller;
    private ProgressDialog progressDialog;
    CardOnFileSwitcher cardOnFileSwitcher;
    SubscriptionCanceler subscriptionCanceler;

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
        CreateSession createSession = new CreateSession();
        if (openSubCanceller) {
            createSession.type = "subscription_canceller";
        } else {
            createSession.type = "card_switcher";
        }

        Call<CreateSessionResponse> call = RetrofitClient.getInstance().getMyApi().createSessionAPI("Bearer " + token, createSession);
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
        Configuration switcherConfig = new Configuration(Environment.SANDBOX, "3f4acb6b-a8c9-47bc-820c-b0eaf24ee771", sessionId);
        Options options = initOptions();

        cardOnFileSwitcher = CardOnFileSwitcher.getInstance();
        cardOnFileSwitcher.init(this, switcherConfig, options, this);
        cardOnFileSwitcher.openCardOnFileSwitcher();
    }

    private void openSubscriptionCanceller(String sessionId) {
        Configuration cancelerConfig = new Configuration(Environment.SANDBOX, "3f4acb6b-a8c9-47bc-820c-b0eaf24ee771", sessionId);
        Options options = initOptions();
        subscriptionCanceler = SubscriptionCanceler.getInstance();
        subscriptionCanceler.init(this, cancelerConfig, options, this);
        subscriptionCanceler.openSubscriptionCanceller();
    }

    public Options initOptions() {
        Options options = new Options();
        options.setPrimaryColor("#000000");
        options.setTextColor("#ffffff");
        options.setCompanyName("Millions");
        options.setUseCategories(false);
        return options;
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