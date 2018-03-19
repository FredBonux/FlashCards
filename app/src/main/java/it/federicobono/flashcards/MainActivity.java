package it.federicobono.flashcards;

        import android.content.Intent;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.firebase.ui.auth.data.model.User;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.EventListener;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.FirebaseFirestoreException;
        import com.google.firebase.firestore.FirebaseFirestoreSettings;
        import com.google.firebase.firestore.ListenerRegistration;
        import com.google.firebase.firestore.QuerySnapshot;
        import java.util.LinkedList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FBLOG - Main";
    private static final int NEW_DECK = 1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ListView listView;
    HomeListAdapter adapter;

    FloatingActionButton mfab;

    ListenerRegistration deckListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        DeckList.setMainActivity(this);

        setContentView(R.layout.activity_main);
        mfab = (FloatingActionButton) findViewById(R.id.homeFAB);
        final Intent detIntent = new Intent(this, NewDeckActivity.class);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(detIntent, MainActivity.NEW_DECK);
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Nessun utente loggato!
        if(user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 0);
        }else {
            bindListener();
        }

        adapter = new HomeListAdapter(this, R.layout.home_list_item, DeckList.getLista());
        listView = (ListView)findViewById(R.id.homeListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);

        Boolean isListPopulated = false;
        if(savedInstanceState != null)
            isListPopulated = savedInstanceState.getBoolean("isListPopulated");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isListPopulated", true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) {
            bindListener();
        }
    }

    private void bindListener() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference ref = db.collection("Utenti").document(user.getUid());
        if(deckListener != null)
            deckListener.remove();
        deckListener = db.collection("Decks").whereEqualTo("creator", ref).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                DeckList.getLista().clear();
                for (DocumentSnapshot doc: documentSnapshots) {
                    Deck d = doc.toObject(Deck.class).withRef(doc.getReference());
                    Log.d(TAG, "added: " + d.getTitolo());
                    DeckList.getLista().add(d);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bindListener();
    }
}
