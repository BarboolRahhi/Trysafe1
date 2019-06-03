package com.trysafe.trysafe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;


public class UpdatePasswordFragment extends Fragment {


    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    private TextInputEditText oldPassword, newPassword, confirmPassword;
    private Button updatePasswordBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_update_password, container, false);

        oldPassword  = view.findViewById(R.id.oldPassword);
        newPassword  = view.findViewById(R.id.newPassword);
        confirmPassword  = view.findViewById(R.id.closeBtn);

        updatePasswordBtn = view.findViewById(R.id.updatePasswordBtn);

//        oldPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                checkInput();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        newPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                checkInput();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        confirmPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                checkInput();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        return view;
    }

    private void checkInput() {
        if (!TextUtils.isEmpty(oldPassword.getText()) && oldPassword.length() >= 8){
            if (!TextUtils.isEmpty(newPassword.getText()) && newPassword.length() >= 8){
                if (!TextUtils.isEmpty(confirmPassword.getText()) && confirmPassword.length() >= 8){
                        updatePasswordBtn.setEnabled(true);
                        updatePasswordBtn.setTextColor(Color.parseColor("#ffffff"));
                }else {
                    updatePasswordBtn.setEnabled(false);
                    updatePasswordBtn.setTextColor(Color.parseColor("#50ffffff"));
                }
            }else {
                updatePasswordBtn.setEnabled(false);
                updatePasswordBtn.setTextColor(Color.parseColor("#50ffffff"));
            }
        }else{
            updatePasswordBtn.setEnabled(false);
            updatePasswordBtn.setTextColor(Color.parseColor("#50ffffff"));
        }
    }

    private void checkEmailAndPassword(){
            if (newPassword.getText().toString().equals(confirmPassword.getText().toString())){


            }else {
                confirmPassword.setError("Password doesn't matched");
            }

    }

}
