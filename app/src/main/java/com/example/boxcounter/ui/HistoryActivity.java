package com.example.boxcounter.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxcounter.R;
import com.example.boxcounter.adapter.ShiftHistoryAdapter;
import com.example.boxcounter.viewModel.ShiftViewModel;

public class HistoryActivity extends AppCompatActivity {

    private ShiftViewModel viewModel;
    private RecyclerView recycleView;
    private ShiftHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recycleView = findViewById(R.id.rvHistory);

        recycleView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new ShiftHistoryAdapter();

        recycleView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this)
                .get(ShiftViewModel.class);

        viewModel.getHistory().observe(this, shifts -> {
            adapter.setTurns(shifts);
        });
    }
}