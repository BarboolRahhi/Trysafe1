package com.trysafe.trysafe;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment extends Fragment {


    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    private CircleImageView circleImageView;
    private Button removeBtn;
    private Button changeBtn;
    private Button updateBtn;

    private EditText dialogEditText;
    private Button dialogBtn;

    private Dialog passwordDialog;

    private ProgressDialog  progressDialog;
    TextInputLayout userName, userEmail, userDes;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private String name, email, des, image;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);


        circleImageView = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.title);
        userEmail = view.findViewById(R.id.email);
        userDes = view.findViewById(R.id.description);
        removeBtn = view.findViewById(R.id.remove_btn);
        changeBtn = view.findViewById(R.id.image_change_btn);
        updateBtn = view.findViewById(R.id.updateProfileBtn);

        name = getArguments().getString("fullname");
        email = getArguments().getString("email");
        des = getArguments().getString("des");
        image = getArguments().getString("image");


        //loading dialog//

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //password dialog//

        passwordDialog = new Dialog(getContext());
        passwordDialog.setContentView(R.layout.password_confirmation_dialog);
        passwordDialog.setCancelable(false);
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        dialogEditText = passwordDialog.findViewById(R.id.password);
        dialogBtn = passwordDialog.findViewById(R.id.done_btn);

        Glide.with(getContext()).load(image).placeholder(R.drawable.defaultavater).into(circleImageView);
        userName.getEditText().setText(name);
        userEmail.getEditText().setText(email);
        userDes.getEditText().setText(des);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);

                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getContext()).load(R.drawable.defaultavater).into(circleImageView);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });


        userEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void checkInput() {
        if (!TextUtils.isEmpty(userEmail.getEditText().getText())) {
            if (!TextUtils.isEmpty(userName.getEditText().getText())) {
                if (!TextUtils.isEmpty(userDes.getEditText().getText())) {
                    updateBtn.setEnabled(true);
                    updateBtn.setTextColor(Color.parseColor("#54596e"));
                } else {
                    updateBtn.setEnabled(false);
                    updateBtn.setTextColor(Color.parseColor("#5054596e"));
                }
            } else {
                updateBtn.setEnabled(false);
                updateBtn.setTextColor(Color.parseColor("#5054596e"));
            }
        } else {
            updateBtn.setEnabled(false);
            updateBtn.setTextColor(Color.parseColor("#5054596e"));
        }
    }

    private void checkEmailAndPassword() {
        if (userEmail.getEditText().getText().toString().matches(emailPattern)) {

            if (userEmail.getEditText().getText().toString().toLowerCase().trim().equals(email.toLowerCase().trim())){//do not update email

            }else { // email update
                passwordDialog.show();
                dialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = dialogEditText.getText().toString();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(userEmail.getEditText().getText().toString(), password);

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                    }
                });
            }

        } else {
            userEmail.setError("Email doesn't matched");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    Glide.with(getContext()).load(uri).into(circleImageView);
                } else {
                    Toast.makeText(getActivity(), "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
