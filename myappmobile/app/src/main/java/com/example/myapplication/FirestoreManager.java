package com.example.myapplication;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {
    private FirebaseFirestore db;

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    // Stocker l'emploi du temps dans Firestore
    public void storeTimetableToFirestore(String[][] schedule) {
        if (schedule == null || schedule.length == 0) {
            System.err.println("L'emploi du temps est vide ou nul. Impossible de le stocker dans Firestore.");
            return;
        }

        // Créer une carte pour structurer les données de l'emploi du temps
        Map<String, Object> timetableData = new HashMap<>();

        for (int i = 0; i < schedule.length; i++) {
            Map<String, String> timeSlot = new HashMap<>();
            timeSlot.put("Time", schedule[i][0]); // La première colonne est l'heure


            timeSlot.put("Monday", schedule[i][1]);
            timeSlot.put("Tuesday", schedule[i][2]);
            timeSlot.put("Wednesday", schedule[i][3]);
            timeSlot.put("Thursday", schedule[i][4]);
            timeSlot.put("Friday", schedule[i][5]);

            // Stocker ce créneau avec une clé unique
            timetableData.put("Slot_" + (i + 1), timeSlot);
        }

        db.collection("schedules")
                .add(timetableData)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("L'emploi du temps a été ajouté avec succès à Firestore avec l'ID : " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    System.err.println("Erreur lors de l'ajout de l'emploi du temps à Firestore : " + e.getMessage());
                });
    }
}
