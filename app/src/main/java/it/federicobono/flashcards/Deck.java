package it.federicobono.flashcards;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by federicobono on 28/02/18.
 */

public class Deck extends DatabaseObject implements Serializable{
    private String colore;
    private String titolo;
    private String materia;
    private DocumentReference creator;

    @Exclude
    private Integer size = null;
    @Exclude
    private List<Card> carte = null;

    public Deck() {}

    public Deck(String colore, String titolo, String materia, DocumentReference creator) {
        this.colore = colore;
        this.titolo = titolo;
        this.materia = materia;
        this.creator = creator;
    }

    public Deck(String colore, String titolo, String materia) {
        this.colore = colore;
        this.titolo = titolo;
        this.materia = materia;
        this.creator = FirebaseFirestore.getInstance().document("Utenti/" + FirebaseAuth.getInstance().getUid());
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public DocumentReference getCreator() {
        return creator;
    }

    public void setCreator(DocumentReference creator) {
        this.creator = creator;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void addToSize(Integer add) {
        size+=add;
    }

    public void save() {
        try {
            new DeckSaveTask().execute(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, Object> getMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("colore", this.colore);
        data.put("titolo", this.titolo);
        data.put("creator", this.creator);
        data.put("materia", this.materia);
        return data;
    }

    public List<Card> getCards() {
        if(this.carte != null && this.carte.size() == this.size) return this.carte;

        try {
            Log.d("FBLOG - Deck", "getCards: doing");
            AsyncTask task = (new GetCardsTask()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this);
            List<Card> res = (List<Card>) task.get();
            this.carte = res;
            return res;
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    private class GetCardsTask extends AsyncTask<Deck, Void, List<Card>> {

        @Override
        protected List<Card> doInBackground(Deck... decks) {
            List<Card> result = new ArrayList<Card>();

            Log.d("FBLOG - CardsTask", "doInBackground: fetching");
            if(decks.length <= 0) return result;
            try {
                Log.d("FBLOG - CardsTask", "doInBackground: fetching");
                Task<QuerySnapshot> task = decks[0].getReference().collection("Cards").get();
                QuerySnapshot query = Tasks.await(task);
                if (query.isEmpty() || query.getDocuments().isEmpty()) {
                    Log.d("FBLOG - CardsTask", "doInBackground: isEmpty");
                    return result;
                }

                for (DocumentSnapshot document: query) {
                    Card c = document.toObject(Card.class);
                    result.add(c);
                    Log.d("FBLOG - CardsTask", c.frontText + " => " + c.backText);
                }
                return result;
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }
    }

    private class DeckSaveTask extends AsyncTask<Deck, Void, Void> {
        @Override
        protected Void doInBackground(Deck... decks) {
            if(decks.length <= 0) return null;
            try {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if(decks[0].getReference() != null) {
                    decks[0].getReference().set(decks[0]);
                    return null;
                }else {
                    DocumentReference reference = db.collection("Decks").document();
                    reference.set(decks[0].getMap());
                    decks[0].setReference(reference);
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }
}
