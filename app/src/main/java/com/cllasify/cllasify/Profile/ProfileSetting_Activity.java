package com.cllasify.cllasify.Profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetting_Activity extends AppCompatActivity {


    RelativeLayout ll_Name, ll_Instituion, ll_Email, ll_Location, ll_Bio, ll_UserName;
    DatabaseReference refUserStatus;
    FirebaseUser currentUser;
    String userID, userName, userEmail;
    TextView tv_UserName, tv_Name, tv_Email, tv_Institution, tv_Location, tv_Bio, tv_ShowUserName, tv_ChangeProfileImage;

    ImageButton btn_Cancel;

    CircleImageView prof_pic;
    StorageReference storageReference;
    int TAKE_IMAGE_CODE = 1001;
    Uri imageUrl, userPhoto;


    DatabaseReference root;
    ProgressBar progBar;


    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_profile_setting);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmail = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();

        root = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
//        DatabaseReference root=FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        storageReference = FirebaseStorage.getInstance().getReference().child(userID);

        progBar = findViewById(R.id.progBar);

        tv_ShowUserName = findViewById(R.id.tv_ShowUserName);
        tv_Bio = findViewById(R.id.tv_Bio);
        tv_UserName = findViewById(R.id.tv_UserName);
        tv_Name = findViewById(R.id.tv_Name);
        tv_Location = findViewById(R.id.tv_Location);
        tv_Institution = findViewById(R.id.tv_Institution);
        tv_Email = findViewById(R.id.tv_Email);
        tv_ChangeProfileImage = findViewById(R.id.tv_ChangeProfileImage);
        prof_pic = findViewById(R.id.prof_pic);

        btn_Cancel = findViewById(R.id.btn_Cancel);

        ll_UserName = findViewById(R.id.ll_UserName);
        ll_Name = findViewById(R.id.ll_Name);
        ll_Location = findViewById(R.id.ll_Location);
        ll_Instituion = findViewById(R.id.ll_Institution);
        ll_Email = findViewById(R.id.ll_Email);
        ll_Bio = findViewById(R.id.ll_Bio);


        // Change profile pic
        tv_ChangeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new AccountSettingFragment());
//                //transaction.addToBackStack(null);
//                transaction.commit();
                Intent i = new Intent(ProfileSetting_Activity.this, AccountSetting_Activity.class);
                startActivity(i);
                (ProfileSetting_Activity.this).overridePendingTransition(0, 0);

            }
        });

        DatabaseReference refUserName = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);

        refUserName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Name").exists()) {
                    tv_ShowUserName.setText(snapshot.child("Name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        tv_ShowUserName.setText(userEmail);

        ll_UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = tv_UserName.getText().toString();
                editUsername("NickName", userName);
            }
        });
        ll_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = tv_Name.getText().toString();
                editName("Name", Name);

            }
        });
        ll_Instituion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Institution = tv_Institution.getText().toString();
                editInstitution("Insitution", Institution);

            }
        });
        ll_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Location = tv_Location.getText().toString();
//                TestLocation fragment = new TestLocation();
//                fragment.show(getSupportFragmentManager(), "TAG");
                editLocation("Location", Location);

            }
        });

        ll_Bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Bio = tv_Bio.getText().toString();
                editBio("Bio", Bio);
            }
        });


        showProfile();

    }

    private void showProfile() {
        refUserStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {

                    if (snapshot.child("Bio").exists()) {
                        String bio = snapshot.child("Bio").getValue().toString();
                        tv_Bio.setText(bio);
                    } else {
                        tv_Bio.setText("Add");
                    }
                    if (snapshot.child("Insitution").exists()) {
                        String Institute = snapshot.child("Insitution").getValue().toString();
                        tv_Institution.setText(Institute);
                    } else {
                        tv_Institution.setText("Add");
                    }
                    if (snapshot.child("uniqueUserName").exists()) {
                        String NickName = snapshot.child("uniqueUserName").getValue().toString();
                        tv_UserName.setText(NickName);
                    } else {
                        tv_UserName.setText("Add");
                    }
                    if (snapshot.child("Name").exists()) {
                        String Name = snapshot.child("Name").getValue().toString();
                        tv_Name.setText(Name);
                    } else {
                        tv_Name.setText("Add");
                    }
                    if (snapshot.child("Location").exists()) {
                        String Location = snapshot.child("Location").getValue().toString();
                        tv_Location.setText(Location);
                    } else {
                        tv_Location.setText("Add");
                    }
                    if (snapshot.child("userEmailId").exists()) {
                        String email = snapshot.child("userEmailId").getValue().toString();
                        tv_Email.setText(email);
                    } else {
                        tv_Location.setText("Email");
                    }
                    if (snapshot.child("profilePic").exists()) {
                        String profilePic = snapshot.child("profilePic").getValue().toString();
                        if (!(ProfileSetting_Activity.this).isFinishing()) {
                            Glide.with(ProfileSetting_Activity.this).load(profilePic).into(prof_pic);
                        }
                    } else {
                        if (!(ProfileSetting_Activity.this).isFinishing()) {
                            Glide.with(ProfileSetting_Activity.this).load(R.drawable.maharaji).into(prof_pic);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    boolean validateUsername(String input) {
        Pattern pattern = Pattern.compile("^[a-z_]([a-z0-9_-]{0,31}|[a-z0-9_-]{0,30}\\$)$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    // BottomSheet for username
    private void editUsername(String title, String userData) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.bottomsheet_username);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button btn_Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView tv_subTitle = dialog.findViewById(R.id.editTitle);
        EditText et_NewDetails = dialog.findViewById(R.id.et_NewDetails);
        Button btn_Submit = dialog.findViewById(R.id.btn_submit);

        et_NewDetails.setHint(userData);

        et_NewDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateUsername(et_NewDetails.getText().toString())) {
                    btn_Submit.setEnabled(true);
                    btn_Submit.setTextColor(Color.parseColor("#FF6200EE"));
                    btn_Submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (title.equals("NickName")) {
                                String username = et_NewDetails.getText().toString().trim();
                                Query uniqueUserName = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").orderByChild("uniqueUserName").equalTo(username);
                                uniqueUserName.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getChildrenCount() > 0) {
                                            et_NewDetails.setError("Please choose a different userName");
                                        } else {
                                            refUserStatus.child("uniqueUserName").setValue(username);
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                        }
                    });

                } else {
                    btn_Submit.setEnabled(false);
                    btn_Submit.setTextColor(Color.parseColor("#eeeeee"));
                    Toast.makeText(ProfileSetting_Activity.this, "Usernames can only use letters,numbers and underscores.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        moveTaskToBack(false);

    }

    // BottomSheet for name
    private void editName(String title, String userData) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.bottomsheet_name);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView tv_subTitle = dialog.findViewById(R.id.editTitle);
        EditText et_NewDetails = dialog.findViewById(R.id.et_NewDetails);
        Button btn_Submit = dialog.findViewById(R.id.btn_submit);

        et_NewDetails.setHint(userData);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equals("Name")) {
                    String name = et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Name").setValue(name);
                    dialog.dismiss();
                } else {
                    Toast.makeText(ProfileSetting_Activity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        moveTaskToBack(false);

    }

    // BottomSheet for location
    private void editLocation(String title, String userData) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.bottomsheet_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final String[] pp = {"Bhandup", "Mumbai", "Visakhapatnam", "Coimbatore",
                "Delhi", "Bangalore", "Pune", "Nagpur", "Lucknow", "Vadodara", "Indore", "Jalalpur", "Bhopal",
                "Kolkata", "Kanpur", "New Delhi", "Faridabad", "Rajkot", "Ghaziabad", "Chennai", "Meerut",
                "Agra", "Jaipur", "Jabalpur", "Varanasi", "Allahabad", "Hyderabad", "Noida", "Howrah",
                "Thane", "Patiala", "Chakan", "Ahmedabad", "Manipala", "Mangalore", "Panvel", "Udupi",
                "Rishikesh", "Gurgaon", "Mathura", "Shahjahanpur", "Bagpat", "Sriperumbudur", "Chandigarh",
                "Ludhiana", "Palakkad", "Kalyan", "Valsad", "Ulhasnagar", "Bhiwani", "Shimla", "Dehradun", "Patna",
                "Unnao", "Tiruvallur", "Kanchipuram", "Jamshedpur", "Gwalior", "Karur", "Erode", "Gorakhpur", "Ooty",
                "Haldwani", "Bikaner", "Puducherry", "Nalbari", "Bellary", "Vellore", "Naraina", "Mandi", "Rupnagar",
                "Jodhpur", "Roorkee", "Aligarh", "Indraprast", "Karnal", "Tanda", "Amritsar", "Raipur", "Pilani",
                "Bilaspur", "Srinagar", "Guntur", "Kakinada", "Warangal", "Tirumala - Tirupati", "Nizamabad", "Kadapa",
                "Kuppam", "Anantpur", "Nalgonda", "Potti", "Nellore", "Rajahmundry", "Bagalkot", "Kurnool", "Secunderabad",
                "Mahatma", "Bharuch", "Miraj", "Nanded", "Anand", "Gandhinagar", "Bhavnagar", "Morvi", "Aurangabad", "Modasa",
                "Patan", "Solapur", "Kolhapur", "Junagadh", "Akola", "Bhuj", "Karad", "Jalgaon Jamod", "Chandrapur", "Maharaj",
                "Dhule", "Ponda", "Dahod", "Navsari", "Panjim", "Patel", "Nashik", "Amravati", "Somnath", "Ganpat", "Karwar",
                "Davangere", "Raichur", "Nagara", "Kushalnagar", "Hassan", "Hubli", "Bidar", "Belgaum", "Mysore", "Dharwad",
                "Kolar", "TumkÅ«r", "Tiruchi", "Thiruvananthapuram", "Kozhikode", "Thrissur", "Madurai", "Thalassery", "Kannur",
                "Karaikudi", "Thanjavur", "Manor", "Idukki", "Thiruvarur", "Alappuzha", "Gandhigram", "Kochi", "Annamalainagar",
                "Amet", "Kottarakara", "Kottayam", "Tirunelveli", "Mohan", "Salem", "Attingal", "Chitra", "Chengannur", "Guwahati",
                "Kalam", "Ranchi", "Shillong", "Gangtok", "Srikakulam", "Tezpur", "Bhubaneswar", "Imphal", "Sundargarh", "Arunachal",
                "Manipur", "Bihar Sharif", "Mandal", "Dibrugarh", "Darbhanga", "Gaya", "Bhagalpur", "Kunwar", "Barddhaman", "Jadabpur", "Kalyani", "Cuttack",
                "Barpeta", "Jorhat", "Kharagpur", "Medinipur", "Agartala", "Saranga", "Machilipatnam", "Dhanbad", "Silchar", "Dumka", "Kokrajhar", "Bankura",
                "Jalpaiguri", "Durgapur", "Kalinga", "Palampur", "Jammu", "Dwarka", "Faridkot", "Udaipur", "Raigarh", "Hisar", "Solan", "Ajmer", "Lala",
                "Gurdaspur", "Sultanpur", "Jhansi", "Vidisha", "Jagdalpur", "Dipas", "Sawi", "Etawah", "Saharanpur", "Ujjain", "Kangra", "Bhilai", "Rohtak",
                "Haryana", "Ambala", "Bareilly", "Bhoj", "Kapurthala Town", "Sangrur", "Pusa", "Sagar", "Rewa", "Bhawan", "Rampur", "Bhadohi", "Cuddalore",
                "Khopoli", "Bali", "Bhiwandi", "Vasai", "Badlapur", "Sambalpur", "Raurkela", "Brahmapur", "Visnagar", "Surendranagar", "Ankleshwar", "Dahanu",
                "Silvassa", "Jamnagar", "Dhansura", "Muzaffarpur", "Wardha", "Bodhan", "Parappanangadi", "Malappuram", "Vizianagaram", "Mavelikara", "Pathanamthitta",
                "Satara", "Janjgir", "Gold", "Himatnagar", "Bodinayakkanur", "Gandhidham", "Mahabalipuram", "Nadiad", "Virar", "Bahadurgarh", "Kaithal", "Siliguri",
                "Tiruppur", "Ernakulam", "Jalandhar", "Barakpur", "Kavaratti", "Ratnagiri", "Moga", "Hansi", "Sonipat", "Bandra", "Aizawl", "Itanagar", "Nagar",
                "Ghatkopar", "Chen", "Powai", "Bhimavaram", "Bhongir", "Medak", "Karimnagar", "Narsapur", "Vijayawada", "Markapur", "Mancherial", "Sangli", "Moradabad",
                "Alipur", "Ichalkaranji", "Devgarh", "Yavatmal", "Hinganghat", "Madgaon", "Verna", "Katra", "Bilaspur", "Uttarkashi", "Muktsar", "Bhatinda",
                "Pathankot", "Khatauli", "Vikasnagar", "Kollam", "Kovilpatti", "Kovvur", "Paloncha", "Vasco", "Alwar", "Bijapur", "Tinsukia", "Ratlam", "Kalka",
                "Ladwa", "Rajpura", "Batala", "Hoshiarpur", "Katni", "Bhilwara", "Jhajjar", "Lohaghat", "Mohali", "Dhuri", "Thoothukudi", "Sivakasi", "Coonoor",
                "Shimoga", "Kayamkulam", "Namakkal", "Dharmapuri", "Aluva", "Antapur", "Tanuku", "Eluru", "Balasore", "Hingoli", "Quepem", "Assagao", "Betim",
                "Cuncolim", "Ahmednagar", "Goa", "Caranzalem", "Chopda", "Petlad", "Raipur", "Villupuram", "Shoranur", "Dasua", "Gonda", "Yadgir", "Palladam",
                "Nuzvid", "Kasaragod", "Paonta Sahib", "Sarangi", "Anantapur", "Kumar", "Kaul", "Panipat", "Uppal", "Teri", "Tiruvalla", "Jamal", "Chakra",
                "Narasaraopet", "Dharamsala", "Ranjan", "Garhshankar", "Haridwar", "Chinchvad", "Narela", "Aurangabad", "Sion", "Kalamboli", "Chittoor",
                "Wellington", "Nagapattinam", "Karaikal", "Pollachi", "Thenkasi", "Aranmula", "Koni", "Ariyalur", "Ranippettai", "Kundan", "Lamba Harisingh",
                "Surana", "Ghana", "Lanka", "Kataria", "Kotian", "Khan", "Salt Lake City", "Bala", "Vazhakulam", "Paravur", "Nabha", "Ongole", "Kaladi",
                "Jajpur", "Thenali", "Mohala", "Mylapore", "Bank", "Khammam", "Ring", "Maldah", "Kavali", "Andheri", "Baddi", "Mahesana", "Nila", "Gannavaram",
                "Cumbum", "Belapur", "Phagwara", "Rander", "Siuri", "Bulandshahr", "Bilimora", "Guindy", "Pitampura", "Baharampur",
                "Dadri", "Boisar", "Shiv", "Multi", "Bhadath", "Ulubari", "Palghar", "Puras", "Sikka", "Saha", "Godhra",
                "Dam Dam", "Ekkattuthangal", "Sahibabad", "Kalol", "Bardoli", "Wai", "Shirgaon", "Nehra", "Mangalagiri", "Latur", "Kottakkal", "Rewari",
                "Ponnani", "Narayangaon", "Hapur", "Kalpetta", "Khurja", "Ramnagar", "Neral", "Sendhwa", "Talegaon Dabhade", "Kargil", "Manali",
                "Jalalabad", "Palani", "Sirkazhi", "Krishnagiri", "Hiriyur", "Muzaffarnagar", "Kashipur", "Gampalagudem", "Siruseri", "Manjeri"
                , "Raniganj", "Mahim", "Bhusawal", "Tirur", "Sattur", "Angul", "Puri", "Khurda", "Dharavi", "Ambur", "Vashi", "Arch", "Colaba",
                "Hosur", "Kota", "Hugli", "Anantnag", "Murshidabad", "Jharsuguda", "Jind", "Neyveli", "Vaniyambadi", "Srikalahasti", "Liluah", "Pali",
                "Bokaro", "Sidhi", "Asansol", "Darjeeling", "Kohima", "Shahdara", "Chandannagar", "Nadgaon", "Haripad", "Sitapur", "Vapi", "Bambolim",
                "Baidyabati", "Connaught Place", "Singtam", "Shyamnagar", "Sikar", "Choolai", "Mayapur", "Puruliya", "Habra", "Kanchrapara", "Goregaon",
                "Tiptur", "Kalpakkam", "Serampore", "Konnagar", "Port Blair", "Canning", "Mahad", "Alibag", "Pimpri", "Panchgani", "Karjat", "Vaikam", "Mhow",
                "Lakhimpur", "Madhoganj", "Kheri", "Gudivada", "Avanigadda", "Nayagarh", "Bemetara", "Bhatapara", "Ramgarh", "Dhubri", "Goshaingaon", "Bellare",
                "Puttur", "Narnaul", "Porbandar", "Keshod", "Dhrol", "Kailaras", "Morena", "Deolali", "Banda", "Orai", "Fatehpur", "Mirzapur", "Adilabad", "Pithapuram",
                "Ramavaram", "Amalapuram", "Champa", "Ambikapur", "Korba", "Pehowa", "Yamunanagar", "Shahabad", "Hamirpur", "Gulbarga", "Sagar", "Bhadravati", "Sirsi",
                "Honavar", "Siruguppa", "Koppal", "Gargoti", "Kankauli", "Jalna", "Parbhani", "Koraput", "Barpali", "Jaypur", "Banswara", "Tindivanam", "Mettur",
                "Srirangam", "Deoria", "Basti", "Padrauna", "Budaun", "Bolpur", "Gujrat", "Balurghat", "Binnaguri", "Guruvayur", "Chandauli", "Madikeri", "Piduguralla",
                "Vinukonda", "Berasia", "Sultans Battery", "Ramanagaram", "Angadipuram", "Mattanur", "Gobichettipalayam", "Banga", "Sibsagar", "Namrup", "North Lakhimpur",
                "Dhenkanal", "Karanja", "Cheyyar", "Vandavasi", "Arakkonam", "Tiruvannamalai", "Akividu", "Tadepallegudem", "Madanapalle", "Puttur", "Edavanna", "Kodungallur",
                "Marmagao", "Sanquelim", "Sakri", "Shahdol", "Satna", "Thasra", "Bundi", "Kishangarh", "Firozpur", "Kot Isa Khan", "Barnala", "Sunam", "Pithoragarh", "Jaspur",
                "Jhargram", "Dimapur", "Churachandpur", "Raxaul", "Motihari", "Munger", "Purnea", "Mannargudi", "Kumbakonam", "Eral", "Nagercoil", "Kanniyakumari", "Ramanathapuram",
                "Sivaganga", "Rajapalaiyam", "Srivilliputhur", "Suratgarh", "Gohana", "Sirsa", "Fatehabad", "Nurpur", "Chamba", "Khergam", "Dindigul", "Pudukkottai", "Kaimganj",
                "Tarn Taran", "Khanna", "Irinjalakuda", "Sehore", "Parra", "Dicholi", "Chicalim", "Saligao", "Changanacheri", "Igatpuri", "Sangamner", "Ganganagar", "Kanhangad",
                "Chidambaram", "Chittur", "Nilambur", "Arvi", "Jalesar", "Kasganj", "Chandausi", "Beawar", "Bharatpur", "Kathua", "Chalisgaon", "Karamsad", "Peranampattu", "Arani",
                "Payyanur", "Pattambi", "Pattukkottai", "Pakala", "Vikarabad", "Bhatkal", "Rupnarayanpur", "Kulti", "Koch Bihar", "Nongstoin", "Budbud", "Balangir", "Kharar",
                "Mukerian", "Mansa", "Punalur", "Mandya", "Nandyal", "Dhone", "Candolim", "Aldona", "Solim", "Daman", "Koothanallur", "Sojat", "Alanallur", "Kagal", "Jhunjhunun",
                "Sirhind", "Kurali", "Khinwara", "Machhiwara", "Talwandi Sabo", "Malpur", "Dhar", "Medarametla", "Pileru", "Yercaud", "Ottappalam", "Alangulam", "Palus",
                "Chiplun", "Durg", "Damoh", "Ambarnath", "Haveri", "Mundgod", "Mandvi", "Behala", "Fort", "Bela", "Balana", "Odhan", "Mawana", "Firozabad", "Bichpuri", "Almora",
                "Pauri", "Azamgarh", "Phaphamau", "Nongpoh", "Gangrar", "Jhalawar", "Nathdwara", "Jaisalmer", "Pushkar", "Sirohi", "Baroda", "Ambah", "Ambejogai", "Ambad",
                "Osmanabad", "Betalbatim", "Gangapur", "Dindori", "Yeola", "Pandharpur", "Neri", "Umred", "Patelguda", "Patancheru", "Singarayakonda", "Peddapuram", "Gadag",
                "ChikmagalÅ«r", "Chikodi", "Amer", "Chintamani", "Tambaram", "Palayam", "Karamadai", "Omalur", "Kuzhithurai", "Faizabad", "Thirumangalam", "Kodaikanal", "Devipattinam",
                "Dharapuram", "Rudrapur", "Talcher", "Haldia", "Karsiyang", "Sandur", "Bapatla", "Shamsabad", "Kandi", "Ramapuram", "Anchal", "Trimbak", "Calangute", "Arpora", "Khargone",
                "Mandla", "Kalan", "Pachmarhi", "Dhamtari", "Kumhari", "Aundh", "Tala", "Tuljapur", "Botad", "Sidhpur", "Sanand", "Nagwa", "Mussoorie", "Vadamadurai",
                "Sholavandan", "Pochampalli", "Perundurai", "Lalgudi", "Ponneri", "Mount Abu", "Vadner", "Shanti Grama", "Nalagarh", "Pahalgam", "Dinanagar", "Jatani",
                "Ganga", "Barmer", "Hoshangabad", "Khajuraho Group of Monuments", "Betul", "Sangola", "Tirumala", "Mirza Murad", "Attur", "Budha", "Pala", "Tonk", "Koni", "Rajpur",
                "Shrigonda", "Hazaribagh", "Nagaur", "Mandapeta", "Nabadwip", "Nandurbar", "Nazira", "Kasia", "Bargarh", "Kollegal", "Shahkot", "Jagraon",
                "Channapatna", "Madurantakam", "Kamalpur", "Ranaghat", "Mundra", "Mashobra", "Rama", "Chirala", "Bawana", "Dhaka", "Mahal",
                "Chitradurga", "Mandsaur", "Dewas", "Sachin", "Andra",
                "Kalkaji Devi", "Pilkhuwa", "Mehra", "Chhachhrauli", "Samastipur", "Bangaon", "Ghatal", "Jayanti", "Belgharia", "Kamat",
                "Dhariwal", "Morinda", "Kottagudem", "Suriapet", "Mahesh", "Sirwani", "Kanakpura", "Mahajan", "Sodhi", "Chand", "Nagal", "Hong", "Raju", "Tikamgarh", "Parel", "Jaynagar", "Mill", "Khambhat",
                "Ballabgarh", "Begusarai", "Shahapur", "Banka", "Golaghat", "Palwal", "Kalra", "Chandan", "Maru", "Nanda", "Chopra", "Kasal", "Rana", "Chetan", "Charu", "Arora", "Chhabra",
                "Bishnupur", "Manu", "Karimganj", "Ellora Caves", "Adwani", "Amreli", "Soni", "Sarwar", "Balu", "Rawal", "Darsi", "Nandigama", "Mathan", "Panchal", "Jha Jha", "Hira", "Manna",
                "Amal", "Kheda", "Abdul", "Roshan", "Bhandari", "Binavas", "Hari", "Nandi", "Rajapur", "Suman", "Sakri", "Khalapur", "Dangi", "Thiruthani", "Bawan", "Basu", "Kosamba",
                "Medchal", "Kakdwip", "Kamalpura", "Dogadda", "Charan", "Basirhat", "Nagari", "Kangayam", "Sopara", "Nadia", "Mahulia", "Alipur", "Hamirpur", "Fatehgarh", "Bagh", "Naini",
                "Karari", "Ajabpur", "Jaunpur", "Iglas", "Pantnagar", "Dwarahat", "Dasna", "Mithapur", "Bali", "Nilokheri", "Kolayat", "Haripur", "Dang", "Chhota Udepur", "Matar", "Sukma",
                "Guna", "Dona Paula", "Navelim", "Vainguinim", "Curchorem", "Balaghat", "Bhagwan", "Vijapur", "Sinnar", "Mangaon", "Hadadi", "Bobbili", "Yanam", "Udaigiri", "Balanagar",
                "Kanigiri", "Muddanuru", "Panruti", "Proddatur", "Puliyur", "Perambalur", "Turaiyur", "Tiruchchendur", "Shadnagar", "Markal", "Sultan", "Rayagada", "Kaniyambadi",
                "Vandalur", "Sangam", "Katoya", "Gudur", "Farakka", "Baramati", "Tohana"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, pp);

        Button btn_Cancel = dialog.findViewById(R.id.btn_cancel);
//        TextView tv_subTitle = bottomSheetDialoglogin.findViewById(R.id.tv_subTitle);
//        TextView tv_SettingTitle = bottomSheetDialoglogin.findViewById(R.id.tv_SettingTitle);
        AutoCompleteTextView et_NewDetails = dialog.findViewById(R.id.actv);
        Button btn_Submit = dialog.findViewById(R.id.btn_submit);

        et_NewDetails.setHint(userData);

        assert et_NewDetails != null;
        et_NewDetails.setAdapter(adapter);


//        tv_subTitle.setText("Enter new " + title);
//        tv_SettingTitle.setText("Please update " + userData);

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = et_NewDetails.getText().toString().trim();
                refUserStatus.child("Location").setValue(location);

                dialog.dismiss();
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        moveTaskToBack(false);

    }


    // BottomSheet for institution
    private void editInstitution(String title, String userData) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.bottomsheet_institution);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button btn_Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView tv_subTitle = dialog.findViewById(R.id.editTitle);
        EditText et_NewDetails = dialog.findViewById(R.id.et_NewDetails);
        Button btn_Submit = dialog.findViewById(R.id.btn_submit);


        et_NewDetails.setHint(userData);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equals("Insitution")) {
                    String institution = et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Insitution").setValue(institution);
                    dialog.dismiss();

                } else {
                    Toast.makeText(ProfileSetting_Activity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();

                }

            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        moveTaskToBack(false);

    }

    // BottomSheet for bio
    private void editBio(String title, String userData) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.bottomsheet_bio);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button btn_Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView tv_subTitle = dialog.findViewById(R.id.editTitle);
        EditText et_NewDetails = dialog.findViewById(R.id.et_NewDetails);
        Button btn_Submit = dialog.findViewById(R.id.btn_submit);

        et_NewDetails.setHint(userData);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equals("Bio")) {
                    String bio = et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Bio").setValue(bio);
                    dialog.dismiss();

                } else {
                    Toast.makeText(ProfileSetting_Activity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        moveTaskToBack(false);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();

                uploadImageToFirebaseStorage(imageUri);


            }
        }

    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        // upload Image To FirebaseStorage
        progBar.setVisibility(View.VISIBLE);
        final StorageReference fileRef = storageReference.child("users profile pic/" + userID + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (!(ProfileSetting_Activity.this).isFinishing()) {
                            Log.d("PROFPIC", "onSuccess: " + uri);
                            DatabaseReference refSaveProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                            refSaveProfPic.child("profilePic").setValue(uri.toString());
                            Glide.with(ProfileSetting_Activity.this).load(uri).into(prof_pic);
                            progBar.setVisibility(View.GONE);

                            Toast.makeText(ProfileSetting_Activity.this,"DP updated successfully",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileSetting_Activity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private String getFileExtention(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(mUri));
    }

}



