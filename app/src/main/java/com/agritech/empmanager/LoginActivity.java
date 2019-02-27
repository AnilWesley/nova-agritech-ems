package com.agritech.empmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityLoginBinding;
import com.agritech.empmanager.utils.Constants;
import com.agritech.empmanager.utils.PrefUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    public static void start(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void start(Context context, int flag) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
    }

    public void loginUser(final View view) {

        String userEmail = binding.etLoginUsername.getText().toString();
        String password = binding.etLoginPassword.getText().toString();

        if (userEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Snackbar.make(binding.getRoot(), "Email id is empty or incorrect", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Password is empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        view.setVisibility(View.GONE);
        binding.pbLoading.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this, task -> {

                    view.setVisibility(View.VISIBLE);
                    binding.pbLoading.setVisibility(View.GONE);


                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Snackbar.make(binding.getRoot(), "Failed " + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();

                    }

                    // ...
                });


    }


    private void updateUI(final FirebaseUser user) {

       /* Home.start(LoginActivity.this);
        finish();
        if (true)
            return;*/

        if (user == null) {
            return;
        }

        user.getIdToken(true).addOnSuccessListener(result -> {
            String idToken = result.getToken().split("\\.")[1];

            try {
                byte[] data = Base64.decode(idToken, Base64.DEFAULT);

                JSONObject jsonObject = new JSONObject(new String(data, "UTF-8"));

                //String client = jsonObject.getJSONArray("type").getString(0);
                String type = jsonObject.getString("status");


               /* if (type.equals("" + 2)) {
                    Snackbar.make(binding.getRoot(), "This app is only for admin", Snackbar.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    return;
                }*/

                if (type != null && !type.isEmpty()) {

                    Constants.type = type;
                    //PrefUtilities.with(LoginActivity.this).saveClientName(client);
                    PrefUtilities.with(LoginActivity.this).saveUserId(user.getUid());
                    PrefUtilities.with(LoginActivity.this).saveName(user.getDisplayName());

                    String fcmId = PrefUtilities.with(LoginActivity.this).getFCMId();

                    FirebaseFirestore.getInstance().collection("employees").document(user.getUid()).update("fcm_id", fcmId);

                    HomeNewActivity.start(LoginActivity.this);
                    finish();

                } else {
                    Snackbar.make(binding.getRoot(), "Try again. or contact developer", Snackbar.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Snackbar.make(binding.getRoot(), "Try again. " + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            binding.llBody.setVisibility(View.VISIBLE);
        } else {
            binding.llBody.setVisibility(View.GONE);
        }
        updateUI(currentUser);

    }

}
