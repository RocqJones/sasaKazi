package com.intoverflown.sasakazi.ui.discussions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.intoverflown.sasakazi.R;
import com.intoverflown.sasakazi.databinding.ChatMobileBinding;
import com.intoverflown.sasakazi.ui.discussions.reusableutils.MessageViewHolder;
import com.intoverflown.sasakazi.ui.discussions.reusableutils.Messages;
import com.intoverflown.sasakazi.ui.discussions.reusableutils.MyButtonObserver;
import com.intoverflown.sasakazi.ui.discussions.reusableutils.MyScrollToBottomObserver;

import org.jetbrains.annotations.NotNull;

public class ChatMobile extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String MESSAGES_CHILD = "messages-mobile";
    public static final String ANONYMOUS = "Anonymous";
    public static String sName;
    public static String usrProfileUrl;

    private static final int REQUEST_IMAGE = 2;
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";

    private ChatMobileBinding mBinding;

    // 1. Firebase instance variables
    private FirebaseAuth mFirebaseAuth;

    // initializing the Firebase Realtime Database and adding a listener to handle changes made to the data
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<Messages, MessageViewHolder> mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use View Binding
        mBinding = ChatMobileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // 2. Initialize Firebase Auth and check if the user is signed in
        mFirebaseAuth = FirebaseAuth.getInstance();

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mBinding.recyclerView.setLayoutManager(mLinearLayoutManager);

        // Initialize Realtime Database
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = mDatabase.getReference().child(MESSAGES_CHILD);
        mDatabaseReference = mDatabase.getReference().child("Users");

        // The FirebaseRecyclerAdapter class comes from the FirebaseUI library
        // See: https://github.com/firebase/FirebaseUI-Android
        FirebaseRecyclerOptions<Messages> options =
                new FirebaseRecyclerOptions.Builder<Messages>()
                        .setQuery(messagesRef, Messages.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Messages, MessageViewHolder>(options) {
            @NotNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_message, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NotNull MessageViewHolder vh, int position, @NotNull Messages message) {
                mBinding.progressBar.setVisibility(ProgressBar.INVISIBLE);
                vh.bindMessage(message);
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mBinding.recyclerView.setLayoutManager(mLinearLayoutManager);
        mBinding.recyclerView.setAdapter(mFirebaseAdapter);

        // Scroll down when a new message arrives
        // See MyScrollToBottomObserver.java for details
        mFirebaseAdapter.registerAdapterDataObserver(
                new MyScrollToBottomObserver(mBinding.recyclerView, mFirebaseAdapter, mLinearLayoutManager));

        mBinding.inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mBinding.sendMessageBtn.setEnabled(true);
                } else {
                    mBinding.sendMessageBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Disable the send button when there's no text in the input field
        // See MyButtonObserver.java for details
        mBinding.inputMessage.addTextChangedListener(new MyButtonObserver(mBinding.sendMessageBtn));

        // When the send button is clicked, send a text message
        mBinding.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send messages on click.
                Messages messages = new
                        Messages(mBinding.inputMessage.getText().toString(),
                        getUserName(),
                        getUserPhotoUrl(),
                        null /* no image */);

                mDatabase.getReference().child(MESSAGES_CHILD).push().setValue(messages);
                mBinding.inputMessage.setText("");
            }
        });

        // When the image button is clicked, launch the image picker
        mBinding.addMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Select image for image message on click.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }

    // Once the user has selected an image, a call to the MainActivity's onActivityResult will be fired.
    // This is where you handle the user's image selection.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                final Uri uri = data.getData();
                Log.d(TAG, "Uri: " + uri.toString());

                final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                Messages tempMessage = new Messages(
                        null, getUserName(), getUserPhotoUrl(), LOADING_IMAGE_URL);

                mDatabase.getReference().child(MESSAGES_CHILD).push()
                        .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   @NotNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.w(TAG, "Unable to write message to database.",
                                            databaseError.toException());
                                    return;
                                }

                                // Build a StorageReference and then upload the file
                                String key = databaseReference.getKey();
                                StorageReference storageReference =
                                        FirebaseStorage.getInstance()
                                                .getReference(user.getUid())
                                                .child(key)
                                                .child(uri.getLastPathSegment());

                                putImageInStorage(storageReference, uri, key);
                            }
                        });
            }
        }
    }

    // Once the upload is complete you will update the message to use the appropriate image.
    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        // First upload the image to Cloud Storage
        storageReference.putFile(uri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // After the image loads, get a public downloadUrl for the image
                        // and add it to the message.
                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Messages messages = new Messages(
                                                null, getUserName(), getUserPhotoUrl(), uri.toString());
                                        mDatabase.getReference()
                                                .child(MESSAGES_CHILD)
                                                .child(key)
                                                .setValue(messages);
                                    }
                                });
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Image upload task was not successful.", e);
                    }
                });
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    @Override
    protected void onStart() {
        super.onStart();

        // store name of the user in a String var
        String mUserId = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseReference.child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sName = snapshot.child("fullname").getValue().toString();
                usrProfileUrl = snapshot.child("profileurl").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // implement the getUserPhotoUrl() amd getUserName() methods
    @Nullable
    private String getUserPhotoUrl() {
        String user = mFirebaseAuth.getCurrentUser().getUid();
        if (user != null) {
            return usrProfileUrl;
        }

        return String.valueOf(R.drawable.baseline_account_circle_black_36dp);
    }

    private String getUserName() {
        // if current user exists, set the name of the sender
        String mUserId = mFirebaseAuth.getCurrentUser().getUid();
        if (mUserId != null) {
            return sName;
        }

        return ANONYMOUS;
    }
}
