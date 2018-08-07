package sg.edu.rp.webservices.firebasestudentapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditItemActivity extends AppCompatActivity {

    private EditText etItemName, etUnitCost;
    private Button btnUpdate, btnDelete;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itemListRef;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etItemName = findViewById(R.id.editTextItemName1);
        etUnitCost = findViewById(R.id.editTextUnitCost1);
        btnDelete = findViewById(R.id.buttonDelete);
        btnUpdate = findViewById(R.id.buttonUpdate);

        firebaseDatabase = FirebaseDatabase.getInstance();
        itemListRef = firebaseDatabase.getReference("/itemList");

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("Item");
        etItemName.setText(item.getName());
        etUnitCost.setText(String.valueOf(item.getUnitCost()));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = item.getId();
                item.setId(null);
                item.setName(etItemName.getText().toString());
                item.setUnitCost(Double.parseDouble(etUnitCost.getText().toString()));

                itemListRef.child(id).setValue(item);

                Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListRef.child(item.getId()).removeValue();
                Toast.makeText(getApplicationContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
