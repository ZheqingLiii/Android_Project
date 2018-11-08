package com.group5.android_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SessionPageFragment extends Fragment {
    String signIn = "Sign In";
    String signUp = "Sign Up";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.session_page, container, false);

        final TextView changeActionButton = (TextView) view.findViewById(R.id.changeActionButton);
        final Button actionButton = (Button) view.findViewById(R.id.actionButton);

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

                } else {

                }
            }
        });

        return view;
    }

}
