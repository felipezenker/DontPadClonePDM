package br.com.zenker.dontpadclone_pdm.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.zenker.dontpadclone_pdm.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public TextInputLayout tagTextInputLayout;
    public TextInputEditText tagTextInputEditText;
    public EditText textContentEditText;

    public FirebaseFirestore db;
    public CollectionReference dontPadClone;
    public DocumentReference docRef;

    String tagText;
    String content;
    Map<String, Object> newDoc = new HashMap<>();

    public void setupFirebase() {
        Map<String, Object> welcome = new HashMap<>();
        welcome.put("content", "Welcome, create or find your tags to share your content!");
        db = FirebaseFirestore.getInstance();
        dontPadClone = db.collection("dontPadClone");
        dontPadClone.document("welcome").set(welcome);
        docRef = dontPadClone.document("welcome");
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()) {
                                content = doc.getString("content");
                                textContentEditText.setText(content);
                            }
                            else {
                                newDoc.put("content", "");
                                tagText = tagTextInputEditText.getText().toString();
                                dontPadClone.document(tagText).set(newDoc);
                            }
                        }
                        else {
                            textContentEditText.setText(task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        setupFirebase();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        tagTextInputLayout = root.findViewById(R.id.tagTextInputLayout);
        textContentEditText = root.findViewById(R.id.textContentEditText);
        textContentEditText.setFocusable(true);
        textContentEditText.addTextChangedListener(textWatcher);

        tagTextInputEditText = root.findViewById(R.id.tagTextInputEditText);
        //tagTextInputEditText.addTextChangedListener(textWatcher);
        tagTextInputEditText.setOnFocusChangeListener(onFocusChangeListener);


        tagTextInputEditText.setText("welcome");

        return root;
        //final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
    }

    private TextInputEditText.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            tagText = tagTextInputEditText.getText().toString();
            docRef = dontPadClone.document(tagText);
            if(!hasFocus) {
                if(!tagTextInputEditText.getText().toString().isEmpty()) {
                    docRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot doc = task.getResult();
                                if(doc.exists()) {
                                    String content = doc.getString("content");
                                    textContentEditText.setText(content);
                                }
                                else {
                                    newDoc.put("content", "");
                                    tagText = tagTextInputEditText.getText().toString();
                                    dontPadClone.document(tagText).set(newDoc);
                                }
                            }
                            else {
                                textContentEditText.setText(task.getException().getMessage());
                            }
                        }
                    });
                }
            }
            else {
                if(!textContentEditText.getText().toString().isEmpty()) {

                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            content = textContentEditText.getText().toString();
            tagText = tagTextInputEditText.getText().toString();
            newDoc.put("content", content);
            dontPadClone.document(tagText).set(newDoc);
        }

        @Override
        public void afterTextChanged(Editable editable) {
//
        }
    };

}