package it.federicobono.flashcards;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ListView;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

        import java.util.LinkedList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Nessun utente loggato!
        if(user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        ListView listView = (ListView)findViewById(R.id.homeListView);

        List<FlashCard> list = new LinkedList<FlashCard>();
        list.add(new FlashCard("Storia - Rivoluzione Francese","Cards per la verifica del 12 Marzo"));
        list.add(new FlashCard("Informatica - Database","Definizioni per i database"));
        list.add(new FlashCard("Italiano - Autori","Cards con riassunti degli autori"));
        HomeListAdapter adapter = new HomeListAdapter(this, R.layout.home_list_item, list);
        listView.setAdapter(adapter);
    }
}
