package com.cllasify.cllasify.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cllasify.cllasify.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class FriendsFragment extends Fragment {

    ChipNavigationBar chipNavigationBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friends, container, false);
        chipNavigationBar = getActivity().findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_friends,true);

        return view;
    }
}