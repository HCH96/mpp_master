package org.tech.mobileprogrammingproject.Monthly;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tech.mobileprogrammingproject.Daily.TodoAddedDialog;
import org.tech.mobileprogrammingproject.FIREBASEDB.categoryDB;
import org.tech.mobileprogrammingproject.FIREBASEDB.timelineDB;
import org.tech.mobileprogrammingproject.R;

import java.util.ArrayList;

public class User extends AppCompatActivity {
    DatabaseReference database = null;
    EditText radio_group1, radio_group2, radio_group3, userCategory;
    LinearLayout showCategory;
    Button addTimeline,addCategory;

    ArrayList<String> itemName = new ArrayList<>();
    ArrayList<Integer> itemID = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userseleck);

        radio_group1 = findViewById(R.id.radio_group1);
        radio_group2 = findViewById(R.id.radio_group2);
        radio_group3 = findViewById(R.id.radio_group3);
        addCategory = findViewById(R.id.addCategory);
        addCategory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(userCategory.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"값을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    categoryDB currData = new categoryDB();
                    currData.categoryName = userCategory.getText().toString();
                    database.child("category").child(userCategory.getText().toString()).setValue(currData);
                }
            }
        });
        addTimeline = findViewById(R.id.addTimeline);
        addTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radio_group1.getText().toString().equals("") | radio_group2.getText().toString().equals("") | radio_group3.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    database.child("timeline").removeValue();
                    timelineDB timelinedb = new timelineDB();
                    timelinedb.timeline = radio_group1.getText().toString();
                    database.child("timeline").child("1").setValue(timelinedb);

                    timelinedb.timeline = radio_group2.getText().toString();
                    database.child("timeline").child("2").setValue(timelinedb);

                    timelinedb.timeline = radio_group3.getText().toString();
                    database.child("timeline").child("3").setValue(timelinedb);
                }
            }
        });

        userCategory = findViewById(R.id.userCategory);

        showCategory = findViewById(R.id.showCategory);

        database = FirebaseDatabase.getInstance().getReference();

        database.child("timeline").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    radio_group1.setText("아침");
                    radio_group2.setText("점심");
                    radio_group3.setText("저녁");
                }else{
                    int cnt = 0;
                    for(DataSnapshot childData : snapshot.getChildren()){
                        timelineDB currtimeline = childData.getValue(timelineDB.class);
                        if(cnt == 0)
                            radio_group1.setText(currtimeline.timeline);
                        else if(cnt == 1)
                            radio_group2.setText(currtimeline.timeline);
                        else radio_group3.setText(currtimeline.timeline);

                        cnt++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showCategory.removeAllViews();
                itemID.clear();
                itemName.clear();
                for(DataSnapshot childData : snapshot.getChildren()) {
                    categoryDB currcategory = childData.getValue(categoryDB.class);
                    itemName.add(currcategory.categoryName);
                    LinearLayout tr = new LinearLayout(getApplicationContext());
                    tr.setOrientation(LinearLayout.HORIZONTAL);

                    TextView textView = new TextView(getApplicationContext());
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(currcategory.categoryName);

                    CheckBox cb = new CheckBox(getApplicationContext());
                    itemID.add(cb.getId());
                    cb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            database.child("category").child(itemName.get(itemID.indexOf(v.getId()))).removeValue();
                        }
                    });
                    tr.addView(textView);
                    tr.addView(cb);

                    showCategory.addView(tr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
