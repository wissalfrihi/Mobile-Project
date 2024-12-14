package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Initialiser FirebaseAuth et Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.Password);
        Button submitButton = findViewById(R.id.Submit);

        submitButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un email et un mot de passe", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("MainActivity", "Connexion réussie. ID de l'utilisateur : " + user.getUid());
                            // Une fois connecté, récupérer le rôle depuis Firestore
                            fetchUserRole(user.getUid());
                        }
                    } else {
                        Log.e("MainActivity", "Échec de l'authentification : ", task.getException());
                        Toast.makeText(this, "Échec de l'authentification. Essayez à nouveau.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserRole(String userId) {
        // Accéder à la collection "Users" et récupérer le document par userId
        DocumentReference userDoc = db.collection("Users").document(userId);

        userDoc.get().addOnSuccessListener(document -> {
            if (document.exists()) {

                Log.d("MainActivity", "Données du document utilisateur : " + document.getData());

                String role = document.getString("role");
                if (role != null) {
                    handleRoleNavigation(role);
                } else {
                    Toast.makeText(this, "Rôle de l'utilisateur introuvable dans Firestore.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("MainActivity", "Le document utilisateur n'existe pas.");
                Toast.makeText(this, "Document utilisateur introuvable.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("MainActivity", "Échec de la récupération du rôle de l'utilisateur : ", e);
            Toast.makeText(this, "Erreur lors de la récupération des données utilisateur.", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleRoleNavigation(String role) {
        if (role == null) {
            Toast.makeText(this, "Le rôle est nul. Vérifiez votre base de données Firestore.", Toast.LENGTH_SHORT).show();
            return;
        }


        switch (role) {
            case "tracking agent":
                navigateToTrackingAgentActivity();
                break;
            case "teacher":
                navigateToTeacherAbsenceActivity();
                break;
            case "admin":
                navigateToDashboard();
                break;

            default:
                Toast.makeText(this, "Rôle non reconnu ou non autorisé.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToTrackingAgentActivity() {
        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToTeacherAbsenceActivity() {
        Intent intent = new Intent(MainActivity.this, TeacherAbsenceActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

}
