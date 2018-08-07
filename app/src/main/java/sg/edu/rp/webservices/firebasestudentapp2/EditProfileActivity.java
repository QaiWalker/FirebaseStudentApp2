package sg.edu.rp.webservices.firebasestudentapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etName, etContactNo, etHobbies;
    private TextView tvEditEmail;
    private Button btnUpdate;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userProfileRef;

    private static final String TAG = "EditProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.editTextName);
        etContactNo = findViewById(R.id.editTextContactNo);
        etHobbies = findViewById(R.id.editTextHobbies);
        btnUpdate = findViewById(R.id.buttonUpdate);
        tvEditEmail = findViewById(R.id.textViewEmail);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tvEditEmail.setText(firebaseUser.getEmail());
        firebaseDatabase = FirebaseDatabase.getInstance();
        userProfileRef = firebaseDatabase.getReference("profiles/" + firebaseUser.getUid());

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String contactNo = i.getStringExtra("contactNo");
        String hobbies = i.getStringExtra("hobbies");

        etName.setText(name);
        etContactNo.setText(contactNo);
        etHobbies.setText(hobbies);

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error occurred", databaseError.toException());
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile userProfile = new UserProfile(firebaseUser.getUid().toString(), etName.getText().toString(), etContactNo.getText().toString(), etHobbies.getText().toString());
                userProfileRef.setValue(userProfile);
                Intent intent =  new Intent(EditProfileActivity.this, ViewMyProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}
