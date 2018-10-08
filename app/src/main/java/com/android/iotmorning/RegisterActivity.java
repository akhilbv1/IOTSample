package com.android.iotmorning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etUsername, etAge, etPassword, etConfPassword;

    private Button btnRegister;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialiseViews();
    }

    //initalise views
    private void initialiseViews() {
        etEmail = findViewById(R.id.etEMail);
        etAge = findViewById(R.id.etAge);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    private void validateAndRegister() {
        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etAge.getText().toString().trim())) {
            Toast.makeText(this, "Please enter age", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else if (etPassword.getText().length() < 8) {
            Toast.makeText(this, "Password should be atleast 8 characters long", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etConfPassword.getText().toString().trim())) {
            Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
        } else if (!etPassword.getText().toString().trim().equalsIgnoreCase(etConfPassword.getText().toString().trim())) {
            Toast.makeText(this, "Password's donot match", Toast.LENGTH_SHORT).show();
        } else {
            registerUser();
        }

    }

    private void registerUser() {


        final LoginBody loginBody = new LoginBody();
        loginBody.setEmail(etEmail.getText().toString().trim());
        loginBody.setPassword(etPassword.getText().toString().trim());
        loginBody.setReturnSecureToken(true);

        Single<Response<LoginResponse>> regiterUser = MainApplication.getRestClient().getLoginService().registerUser(loginBody);

        regiterUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<LoginResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<LoginResponse> loginResponseResponse) {
                        if (loginResponseResponse.code() == 200) {
                            registerUserInDb(loginResponseResponse.body().getLocalId());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void registerUserInDb(String uid) {
        String userid = uid + ".json";
        final String uniqueId = Utils.getUniqueId();
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(etEmail.getText().toString().trim());
        userPojo.setAge(Integer.parseInt(etAge.getText().toString().trim()));
        userPojo.setExpId(uniqueId);
        userPojo.setUsername(etUsername.getText().toString().trim());
        userPojo.setPassword(etPassword.getText().toString().trim());

         Single<Response<Void>> regUserDb = MainApplication.getRestClient().getPostService().registerUserDb(userid, userPojo);

        regUserDb.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(RegisterActivity.this, "You have been register with Id:-" + uniqueId, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                validateAndRegister();
                break;
        }
    }
}
