package it.federicobono.flashcards;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by federicobono on 28/02/18.
 */

@IgnoreExtraProperties
public class DatabaseObject implements Serializable{
    @Exclude
    private DocumentReference reference = null;

    public <T extends DatabaseObject> T withRef(@NonNull final DocumentReference reference) {
        this.reference = reference;
        return (T) this;
    }

    public DocumentReference getReference() {
        return reference;
    }

    public void setReference(DocumentReference reference) {
        this.reference = reference;
    }
}
