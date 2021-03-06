package it.federicobono.flashcards;

import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.federicobono.flashcards.Deck;
import it.federicobono.flashcards.HomeListAdapter;

/**
 * Created by federicobono on 06/03/18.
 */

public class DeckList{
    static final String TAG = "DeckList";
    static final ArrayList<Deck> lista = new ArrayList<Deck>();
    static MainActivity mainActivity;

    public static ArrayList<Deck> getLista() {
        return lista;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        DeckList.mainActivity = mainActivity;
    }
}
