package it.federicobono.flashcards;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewCardActivity extends AppCompatActivity {
    public static final String TAG = "FBLOG - NewCardActivity";

    int index;
    Deck deck;

    CardView cardTop;
    CardView cardBottom;
    FloatingActionButton fab;
    EditText frontText;
    EditText backText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        setTitle(R.string.newCard);

        cardTop = (CardView) findViewById(R.id.cardTop);
        cardBottom = (CardView) findViewById(R.id.cardBottom);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        frontText = (EditText) findViewById(R.id.frontSideText);
        backText = (EditText) findViewById(R.id.backSideText);

        index = getIntent().getIntExtra("index", 0);
        deck = DeckList.getLista().get(index);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String front = frontText.getText().toString();
                String back = backText.getText().toString();
                if(front.length() <= 0 || back.length() <= 0) return;
                deck.getReference().collection("Cards").add(new Card(front, back).getMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, documentReference.getId() + " salvato!");
                        setResult(1);
                        finish();
                    }
                });
                Toast.makeText(NewCardActivity.this, R.string.cardUploading, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
