package it.federicobono.flashcards;

        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ListView;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.EventListener;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.FirebaseFirestoreException;
        import com.google.firebase.firestore.QuerySnapshot;

        import org.json.JSONArray;
        import org.json.JSONException;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.LinkedList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FBLOG - Main";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final List<Deck> decksList = new LinkedList<>();
    public ListView listView;
    HomeListAdapter adapter;

    FloatingActionButton mfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DeckList.setMainActivity(this);

        setContentView(R.layout.activity_main);
        mfab = (FloatingActionButton) findViewById(R.id.homeFAB);
        final Intent detIntent = new Intent(this, NewDeckActivity.class);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(detIntent);
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Nessun utente loggato!
        if(user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        adapter = new HomeListAdapter(this, R.layout.home_list_item, decksList);
        listView = (ListView)findViewById(R.id.homeListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);

        fetchDecks();
    }

    private void fetchDecks() {
        DeckList.refreshList();
    }

    public void notifyDataSetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((HomeListAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
    }
}
