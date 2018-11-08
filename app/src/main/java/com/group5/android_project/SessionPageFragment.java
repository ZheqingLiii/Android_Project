package com.group5.android_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class SessionPageFragment extends Fragment {
    final String signIn = "Sign In";
    final String signUp = "Sign Up";
    final String TAG = "PmdLogTag";
    FirebaseAuth firebaseAuth;
    TextView changeActionButton;
    Button actionButton;
    AutoCompleteTextView emailAutoCompleteTextView;
    EditText passwordEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.session_page, container, false);

        setUpElements(view);

        return view;
    }

    void setUpElements(View view) {
        changeActionButton = (TextView) view.findViewById(R.id.changeActionButton);
        actionButton = (Button) view.findViewById(R.id.actionButton);
        firebaseAuth = FirebaseAuth.getInstance();
        emailAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.email);
        passwordEditText = (EditText) view.findViewById(R.id.password);

        changeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionButton.getText() == signIn) {
                    changeActionButton.setText(signIn);
                    actionButton.setText(signUp);
                } else {
                    changeActionButton.setText(signUp);
                    actionButton.setText(signIn);
                }
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionButton.getText() == signIn) {
                    firebaseAuth.signInWithEmailAndPassword(emailAutoCompleteTextView.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                handleAuthResult(task);
                            }
                        });
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(emailAutoCompleteTextView.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
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
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.getException());
            // TODO: Error showing...
//            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                    Toast.LENGTH_SHORT).show();
//            updateUI(null);
        }
    }
}
