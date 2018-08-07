package sg.edu.rp.webservices.firebasestudentapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etUnitCost;
    private Button btnAdd;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itemListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.editTextItemName);
        etUnitCost = findViewById(R.id.editTextUnitCost);
        btnAdd = findViewById(R.id.buttonAdd);

        firebaseDatabase = FirebaseDatabase.getInstance();
        itemListRef = firebaseDatabase.getReference("/itemList");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etItemName.getText().toString();
                double unitCost = Double.parseDouble(etUnitCost.getText().toString());
                Item item = new Item(name, unitCost);

                itemListRef.push().setValue(item);

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
