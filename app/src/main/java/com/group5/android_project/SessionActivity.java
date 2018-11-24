package com.group5.android_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SessionActivity extends AppCompatActivity {
    final String signIn = "Sign In";
    final String signUp = "Sign Up";
    final String TAG = "PmdLogTag";
    FirebaseAuth firebaseAuth;
    TextView changeActionButton;
    Button actionButton;
    AutoCompleteTextView emailAutoCompleteTextView;
    EditText passwordEditText, nameEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_page);

        setUpElements();
    }

    void setUpElements() {
        changeActionButton = (TextView) findViewById(R.id.changeActionButton);
        actionButton = (Button) findViewById(R.id.actionButton);
        firebaseAuth = FirebaseAuth.getInstance();
        nameEditText = (EditText) findViewById(R.id.name);
        emailAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        changeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionButton.getText() == signIn) {
                    changeActionButton.setText(signIn);
                    actionButton.setText(signUp);
                    nameEditText.setVisibility(View.VISIBLE);
                } else {
                    changeActionButton.setText(signUp);
                    actionButton.setText(signIn);
                    nameEditText.setVisibility(View.GONE);
                }
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SIGN IN USER
                if (actionButton.getText() == signIn) {
                    firebaseAuth.signInWithEmailAndPassword(emailAutoCompleteTextView.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                handleAuthResult(task);
                            }
                        });
                // SIGN UP USER
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(emailAutoCompleteTextView.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                updateName();
                                handleAuthResult(task);
                            }
                        });
                }
            }
        });
    }

    void handleAuthResult(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithEmail:success");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            // TODO: Redirect...
//            updateUI(user);
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.getException());
            // TODO: Error showing...
//            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                    Toast.LENGTH_SHORT).show();
//            updateUI(null);
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    void updateName() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nameEditText.getText().toString().trim())
                    .build();

            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("TESTING", "User profile updated");
                    }
                }
            });
        }
    }
}
