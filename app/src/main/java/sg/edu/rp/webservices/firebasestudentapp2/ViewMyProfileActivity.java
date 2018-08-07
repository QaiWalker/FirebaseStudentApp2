package sg.edu.rp.webservices.firebasestudentapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewMyProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvContactNo, tvHobbies;
    private Button btnEdit;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userProfileRef;

    private UserProfile userProfile;

    private static final String TAG = "ViewMyProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_profile);

        setTitle("View Profile");

        tvName = findViewById(R.id.textViewName);
        tvEmail = findViewById(R.id.textViewEmail);
        tvContactNo = findViewById(R.id.textViewContactNo);
        tvHobbies = findViewById(R.id.textViewHobbies);
        btnEdit = findViewById(R.id.buttonEdit);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tvEmail.setText(firebaseUser.getEmail());
        tvContactNo.setText(firebaseUser.getPhoneNumber());
        tvName.setText(firebaseUser.getDisplayName());

        firebaseDatabase = FirebaseDatabase.getInstance();
        userProfileRef = firebaseDatabase.getReference("profiles/" + firebaseUser.getUid());

        tvEmail.setText(firebaseUser.getEmail());

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "userProfileRef.addValueEventListener -- onDataChange()");
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                if (userProfile != null) {
                    Log.i(TAG, "profile: " + userProfile.toString());

                    tvName.setText(userProfile.getName());
                    tvContactNo.setText(userProfile.getContactNo());
                    tvHobbies.setText(userProfile.getHobbies());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error occurred", databaseError.toException());
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), EditProfileActivity.class);
                i.putExtra("name", tvName.getText().toString());
                i.putExtra("contactNo", tvContactNo.getText().toString());
                i.putExtra("hobbies", tvHobbies.getText().toString());
                startActivity(i);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_main) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);

        } else if (id == R.id.action_logout) {

            // TODO: User log out
            firebaseAuth.signOut();

            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
