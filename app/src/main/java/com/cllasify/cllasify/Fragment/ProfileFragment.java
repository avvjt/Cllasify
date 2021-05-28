package com.cllasify.cllasify.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cllasify.cllasify.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ProfileFragment extends Fragment {

    ChipNavigationBar chipNavigationBar;

    ImageButton ib_EditProfile,ib_Logout;
    TextView tv_User_Name,tv_User_Status,tv_user_Notify;

//    ,tv_User_Email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        chipNavigationBar = getActivity().findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_profile,true);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = currentUser.getUid();
        final String userName = currentUser.getDisplayName();
        final String userEmail = currentUser.getEmail();
        final Uri userPhoto = currentUser.getPhotoUrl();


        tv_User_Name= view.findViewById(R.id.tv_User_Name);
        tv_User_Status= view.findViewById(R.id.tv_User_Status);
        ib_EditProfile=view.findViewById(R.id.ib_EditProfile);
        ib_Logout=view.findViewById(R.id.ib_Logout);
        tv_user_Notify=view.findViewById(R.id.tv_user_Notify);

        tv_User_Name.setText(userName);
//        tv_User_Email.setText(userEmail);

        ib_EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new HomeFragment());
                //transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        ib_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new HomeFragment());
                //transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        tv_user_Notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new NotificationFragment());
                //transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return view;
      }
    }
