package com.example.boxcounter.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxcounter.R;
import com.example.boxcounter.adapter.TurnHistoryAdapter;
import com.example.boxcounter.viewModel.TurnViewModel;

public class HistoryActivity extends AppCompatActivity {

    private TurnViewModel viewModel;
    private RecyclerView recycleView;
    private TurnHistoryAdapter adapter;

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

        adapter = new TurnHistoryAdapter();

        recycleView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this)
                .get(TurnViewModel.class);

        viewModel.getHistory().observe(this, turns -> {
            adapter.setTurns(turns);
        });
    }
}