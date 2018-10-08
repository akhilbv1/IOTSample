package com.android.iotmorning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;

    private TextView tvRegister;

    private Button btnLogin;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialiseViews();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //views intialisation
    private void initialiseViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(this);
        underlineText();
    }

    private void underlineText() {
        SpannableString content = new SpannableString(tvRegister.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvRegister.setText(content);
    }

    private void validateAndLogin() {
        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else {
            login();
        }
    }

    private void login() {

        final LoginBody loginBody = new LoginBody();
        loginBody.setEmail(etEmail.getText().toString().trim());
        loginBody.setPassword(etPassword.getText().toString().trim());
        loginBody.setReturnSecureToken(true);
        Single<Response<LoginResponse>> loginUserFirebase = MainApplication.getRestClient().getLoginService().loginUser(loginBody);

        loginUserFirebase.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<LoginResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<LoginResponse> loginResponse) {
                        if (loginResponse.code() == 200) {
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Log.i("message",""+loginResponse.message());
                        }
                        else if(loginResponse.code()==400)
                        {
                            if(loginResponse.message().equalsIgnoreCase("EMAIL_NOT_FOUND"))
                            {
                                Toast.makeText(LoginActivity.this, "User not registered yet", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                validateAndLogin();
                break;

            case R.id.tvRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
