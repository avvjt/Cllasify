package com.cllasify.cllasify.NestedRecyclerview;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.NestedRecyclerview.Adapter.ParentAdapter;
import com.cllasify.cllasify.NestedRecyclerview.Model.ParentItem;
import com.cllasify.cllasify.NestedRecyclerview.ViewModel.FirebaseViewModel;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class TestFirebaseActivity extends AppCompatActivity {
    private RecyclerView parentRecyclerView;
    private FirebaseViewModel firebaseViewModel;
    private ParentAdapter parentAdapter;

    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_main);

        Toast.makeText(TestFirebaseActivity.this, "TestFirebaseActivity", Toast.LENGTH_SHORT).show();


        parentRecyclerView = findViewById(R.id.ParentRecyclerview);

        parentRecyclerView.setHasFixedSize(true);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAdapter = new ParentAdapter();
        parentAdapter.setContext(getApplicationContext());
        parentRecyclerView.setAdapter(parentAdapter);

        firebaseViewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        firebaseViewModel.getAllData();
        firebaseViewModel.getParentItemMutableLiveData().observe(this, new Observer<List<ParentItem>>() {
            @Override
            public void onChanged(List<ParentItem> parentItemList) {
                parentAdapter.setParentItemList(parentItemList);
                parentAdapter.notifyDataSetChanged();

            }
        });
        firebaseViewModel.getDatabaseErrorMutableLiveData().observe(this, new Observer<DatabaseError>() {
            @Override
            public void onChanged(DatabaseError error) {
                Toast.makeText(TestFirebaseActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}