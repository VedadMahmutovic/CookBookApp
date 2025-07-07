package com.example.cookbook;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookbook.utils.LocaleHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;
    private EditText usernameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        usernameEditText = findViewById(R.id.usernameEditText);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> registerUser());

        ImageView flagBosnia = findViewById(R.id.flag_bosnia);
        ImageView flagUSA = findViewById(R.id.flag_usa);

        flagBosnia.setOnClickListener(v -> {
            LocaleHelper.setLocale(this, "bs");
            recreate();
        });

        flagUSA.setOnClickListener(v -> {
            LocaleHelper.setLocale(this, "en");
            recreate();
        });

    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.text_enter_email));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.text_enter_password));
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError(getString(R.string.text_password_mismatch));
            return;
        }
        String username = usernameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError(getString(R.string.text_enter_username));
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            user.updateProfile(
                                    new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build()
                            ).addOnCompleteListener(profileTask -> {
                                if (profileTask.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.text_registration_success), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.text_error_username), Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(this, getString(R.string.text_invalid_user), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.text_error) + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

}
