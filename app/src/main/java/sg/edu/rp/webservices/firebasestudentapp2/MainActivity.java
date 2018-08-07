package sg.edu.rp.webservices.firebasestudentapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itemListRef;
    private ListView lvItem;
    private ArrayAdapter<Item> aaItem;
    private ArrayList<Item> alItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        itemListRef = firebaseDatabase.getReference("itemList");
        lvItem = findViewById(R.id.lvItem);
        alItem = new ArrayList<Item>();
        aaItem = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, alItem);
        lvItem.setAdapter(aaItem);

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = alItem.get(position);
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("Item", item);
                startActivityForResult(intent, 1);
            }
        });

        itemListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Item item = dataSnapshot.getValue(Item.class);
                if(item != null){
                    item.setId(dataSnapshot.getKey());
                    alItem.add(item);
                    aaItem.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String selectedId = dataSnapshot.getKey();
                Item item = dataSnapshot.getValue(Item.class);
                if (item != null){
                    for (int i=0; i <alItem.size(); i++){
                        if (alItem.get(i).getId().equals(selectedId)){
                            item.setId(selectedId);
                            alItem.set(i, item);
                        }
                    }
                    aaItem.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String selectedId = dataSnapshot.getKey();
                for (int i=0; i <alItem.size(); i++){
                    if (alItem.get(i).getId().equals(selectedId)){
                        alItem.remove(i);
                    }
                }
                aaItem.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            firebaseAuth.signOut();

            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
        }
        else if (id == R.id.action_view) {

            Intent i = new Intent(getBaseContext(), ViewMyProfileActivity.class);

            startActivity(i);

        }
        else if (id == R.id.action_addItem) {

            Intent i = new Intent(getBaseContext(), AddItemActivity.class);

            startActivity(i);

        }


        return super.onOptionsItemSelected(item);
    }

}
