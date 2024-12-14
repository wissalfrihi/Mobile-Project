package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        db = FirebaseFirestore.getInstance();

        // Référencer les vues de titre, résumé et graphique en barres
        TextView statisticsTitle = findViewById(R.id.statistics_title);
        TextView statisticsSummary = findViewById(R.id.statistics_summary);
        barChart = findViewById(R.id.statistics_bar_chart);

        // Récupérer et afficher les statistiques
        fetchStatistics(statisticsSummary);
    }

    private void fetchStatistics(TextView statisticsSummary) {
        db.collection("Absences")
                .get()
                .addOnSuccessListener(absenceSnapshots -> {
                    Map<String, Integer> absencesByTeacher = new HashMap<>();

                    // Parcourir les documents d'absences et les organiser par enseignant
                    for (QueryDocumentSnapshot absenceDoc : absenceSnapshots) {
                        String teacherId = absenceDoc.getString("teacherId");

                        absencesByTeacher.put(
                                teacherId,
                                absencesByTeacher.getOrDefault(teacherId, 0) + 1
                        );
                    }


                    displayStatistics(absencesByTeacher, statisticsSummary);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de la récupération des statistiques.", Toast.LENGTH_SHORT).show();
                    Log.e("StatisticsActivity", "Erreur lors de la récupération des statistiques : " + e.getMessage());
                });
    }

    private void displayStatistics(Map<String, Integer> absencesByTeacher, TextView statisticsSummary) {
        StringBuilder statsBuilder = new StringBuilder();

        statsBuilder.append("Absences par enseignant (ID enseignant) :\n\n");
        List<BarEntry> barEntries = new ArrayList<>();
        int index = 0;

        // Parcourir les absences et les ajouter au rapport
        for (Map.Entry<String, Integer> entry : absencesByTeacher.entrySet()) {
            String teacherId = entry.getKey();
            int absences = entry.getValue();

            statsBuilder.append("ID enseignant : ").append(teacherId)
                    .append(" - Absences : ").append(absences)
                    .append("\n");

            // Ajouter les données pour le graphique en barres
            barEntries.add(new BarEntry(index, absences));
            index++;
        }

        // Mettre à jour le résumé avec les statistiques
        statisticsSummary.setText(statsBuilder.toString());


        BarDataSet barDataSet = new BarDataSet(barEntries, "Absences par ID enseignant");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate(); // Actualiser le graphique
    }
}
