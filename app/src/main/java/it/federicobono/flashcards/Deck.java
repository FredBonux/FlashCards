package it.federicobono.flashcards;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
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

    public int getCardSize() {
        try {
            return new GetCardSize().execute(this).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean save() {
        Log.d("FBLOG - Deck", "Sto salvando");
        try {
            new DeckSaveTask().execute(this);
            return true;
        } catch (Exception ex) {
            Log.e("FBLOG - Deck", "Errore nel salvataggio!");
            ex.printStackTrace();
        }
        return false;
    }

    public Map<String, Object> getMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("colore", this.colore);
        data.put("titolo", this.titolo);
        data.put("creator", this.creator);
        data.put("materia", this.materia);
        return data;
    }
    private class GetCardSize extends AsyncTask<Deck, Void, Integer> {

        @Override
        protected Integer doInBackground(Deck... decks) {
            if(decks.length <= 0) return 0;
            try {
                Task<QuerySnapshot> task = decks[0].getReference().collection("Cards").get();
                QuerySnapshot query = Tasks.await(task);
                if (query.isEmpty() || query.getDocuments().isEmpty()) {
                    return 0;
                }
                return query.getDocuments().toArray().length;
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return 0;
        }
    }

    private class DeckSaveTask extends AsyncTask<Deck, Void, Void> {
        private static final String TAG = "FBLOG - DeckSaveTask";


        @Override
        protected Void doInBackground(Deck... decks) {
            if(decks.length <= 0) return null;

            try {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if(decks[0].getReference() != null) {
                    Log.d(TAG, "Update del deck in corso!");
                    Task<Void> task = decks[0].getReference().set(decks[0]);
                    Void ref = Tasks.await(task);
                    return null;
                }else {
                    DocumentReference reference = db.collection("Decks").document();
                    reference.set(decks[0].getMap());
                    Log.d(TAG, reference.getId());
                    if(reference != null && reference.getId() != null) {
                        Log.d(TAG, "collegando il deck con il creatore");
                        Task<DocumentSnapshot> task2 = decks[0].getCreator().get();
                        /*ArrayList<DocumentReference> list = (ArrayList<DocumentReference>) Tasks.await(task2).get("Decks");
                        list.add(reference);
                        Map<String, Object> objectMap = new HashMap<>();
                        objectMap.put("Decks", list);
                        decks[0].getCreator().set(objectMap);*/
                        decks[0].setReference(reference);
                        DeckList.add(decks[0]);
                        return null;
                    }
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }
    }
}
