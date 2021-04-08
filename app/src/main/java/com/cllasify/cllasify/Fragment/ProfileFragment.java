package com.cllasify.cllasify.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Fragment.Feed.Dashboard;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Fragment.Feed.User_Question;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ProfileFragment extends Fragment {

    private Button btn_signout,btn_feed;
    private TextView name,email;
    private ImageView prof_pic;
    FirebaseAuth firebaseAuth;
    Button btn_userques_Category,btn_user_question;
    RecyclerView rv_Questionlist;

    GoogleSignInClient googleSignInClient;
    ChipNavigationBar chipNavigationBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        chipNavigationBar = getActivity().findViewById(R.id.bottom_nav_menu);

        chipNavigationBar.setItemSelected(R.id.bottom_nav_profile,true);


        rv_Questionlist=view.findViewById(R.id.rv_userques_category);
        btn_user_question=view.findViewById(R.id.btn_user_question);
        btn_userques_Category=view.findViewById(R.id.btn_userques_Category);
        prof_pic=view.findViewById(R.id.prof_pic);
        name=view.findViewById(R.id.tv_user_name);
        btn_signout=view.findViewById(R.id.btn_signout);
        email=view.findViewById(R.id.prof_email);

        firebaseAuth=FirebaseAuth.getInstance();

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            Glide.with(getActivity())
                    .load(firebaseUser.getPhotoUrl())
                    .into(prof_pic);
            name.setText(firebaseUser.getDisplayName());
            email.setText(firebaseUser.getEmail());

        }

        btn_user_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), User_Question.class));

            }
        });
        googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseAuth.signOut();
                        Toast.makeText(getActivity(), "User SUcessfully signout", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), Dashboard.class));


                    }
                });
            }
        });
//        btn_feed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Opening User Profile ", Toast.LENGTH_SHORT).show();
//                Fragment fragment=null;
//                FragmentTransaction ft;
//                fragment = new FeedFragment();
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.addToBackStack(null);
//                ft.replace(R.id.fragment_container, fragment);
//                ft.commit();
//            }
//        });
        return view;
    }

}
