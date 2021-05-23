package com.cllasify.cllasify.Fragment.DashboardTab;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cllasify.cllasify.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DobutTab extends Fragment {


    SearchView sv_SearchDoubt;
    RecyclerView rv_DoubtDash;
    FloatingActionButton fab_addDoubtQ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dobut_tab, container, false);

        sv_SearchDoubt=view.findViewById(R.id.sv_SearchDoubt);
        rv_DoubtDash=view.findViewById(R.id.rv_DoubtDash);
        fab_addDoubtQ=view.findViewById(R.id.fab_addDoubtQ);


        return view;
    }
}