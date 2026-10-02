package com.example.boxcounter.ui.activities;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxcounter.R;
import com.example.boxcounter.adapter.ShiftHistoryAdapter;
import com.example.boxcounter.model.entity.Shift;
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

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        TextView tvHeaderStart = findViewById(R.id.tvHeaderStart);
        TextView tvHeaderQuantity = findViewById(R.id.tvHeaderQuantity);

        tvHeaderStart.setOnClickListener(v -> {
            adapter.sortBy("DATE");
            tvHeaderStart.setText(adapter.isAscending() ? "Inicio ↑" : "Inicio ↓");
            tvHeaderQuantity.setText("Cajas");
        });

        tvHeaderQuantity.setOnClickListener(v -> {
            adapter.sortBy("QUANTITY");
            tvHeaderQuantity.setText(adapter.isAscending() ? "Cajas ↑" : "Cajas ↓");
            tvHeaderStart.setText("Inicio");
        });

        setupSwipeToDelete();
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Shift shiftToDelete = adapter.getShiftAt(position);

                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("Eliminar registro")
                        .setMessage("¿Estás seguro de que deseas eliminar este registro del historial?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            viewModel.deleteShift(shiftToDelete);
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> {
                            adapter.notifyItemChanged(position);
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                if (dX < 0) {
                    ColorDrawable background = new ColorDrawable(Color.parseColor("#D32F2F"));
                    Drawable deleteIcon = ContextCompat.getDrawable(HistoryActivity.this,
                            android.R.drawable.ic_menu_delete);

                    int itemView = viewHolder.itemView.getRight();
                    int backgroundCornerOffset = 20;

                    if (deleteIcon != null) {
                        int iconMargin = (viewHolder.itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int iconTop = viewHolder.itemView.getTop() +
                                (viewHolder.itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

                        int iconLeft = viewHolder.itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                        int iconRight = viewHolder.itemView.getRight() - iconMargin;
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                        background.setBounds(viewHolder.itemView.getRight() +
                                        ((int) dX) - backgroundCornerOffset,
                                viewHolder.itemView.getTop(), viewHolder.itemView.getRight(),
                                viewHolder.itemView.getBottom());
                        background.draw(c);
                        deleteIcon.draw(c);
                    }
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recycleView);
    }
}