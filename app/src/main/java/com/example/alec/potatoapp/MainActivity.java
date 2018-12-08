package com.example.alec.potatoapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    FirebaseStorage storage;
//    StorageReference storageReference;
    DatabaseReference databaseUploads = FirebaseDatabase.getInstance().getReference("uploads");


//    private int IMAGE_GALLERY_REQUEST = 20;
    EditText editImageName;
    Button buttonAdd;
    Spinner spinnerUploads;
    FloatingActionButton uploadButton;

    ListView listViewPotatoes;

    List<PotatoCard> potatoCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        uploadButton = (FloatingActionButton) findViewById(R.id.uploadButton);
        editImageName = (EditText) findViewById(R.id.editImageName);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        spinnerUploads = (Spinner) findViewById(R.id.spinnerUploads);

        listViewPotatoes = (ListView) findViewById(R.id.listViewPotatoes);

        potatoCardList = new ArrayList<>();

        listViewPotatoes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PotatoCard potatoCard = potatoCardList.get(i);
                showUpdateDeleteDialog(potatoCard.getId(), potatoCard.getCardName());
                return true;
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseUploads.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                potatoCardList.clear();
                for(DataSnapshot potatoSnapshot : dataSnapshot.getChildren()) {
                    PotatoCard potatoCard = potatoSnapshot.getValue(PotatoCard.class);
                    potatoCardList.add(potatoCard);
                }
                PotatoList adapter = new PotatoList(MainActivity.this, potatoCardList);
                listViewPotatoes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addImage() {
        String name = editImageName.getText().toString().trim();
        String upload = spinnerUploads.getSelectedItem().toString();

        if(!name.isEmpty()) {
            String id = databaseUploads.push().getKey();
            PotatoCard potatoCard = new PotatoCard(name, "test url", upload, id);

            assert id != null;
            databaseUploads.child(id).setValue(potatoCard);

            Toast.makeText(this, "Image uploaded", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enter an image name", Toast.LENGTH_LONG).show();
        }
    }

    private boolean updatePotato(String id, String name, String genre) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("uploads").child(id);

        //updating artist
        PotatoCard potatoCard = new PotatoCard(id, name, genre, "test id");
        dR.setValue(potatoCard);
        Toast.makeText(getApplicationContext(), "Potato Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deletePotato(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("uploads").child(id);

        //removing artist
        dR.removeValue();
        return true;
    }

    private void showUpdateDeleteDialog(final String id, String name) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdatePotato);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeletePotato);

        dialogBuilder.setTitle(name);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updatePotato(id, name, "test genre");
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePotato(id);
                b.dismiss();

            }
        });
    }
//
//        FirebaseRecyclerOptions<PotatoCard> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<PotatoCard>()
//                .setQuery(myRef.child("Uploads"), PotatoCard.class)
//                .build();
//    }

//    public void onUploadClicked(View v) {
//        Intent photoPickerIntent= new Intent(Intent.ACTION_PICK);
//        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        String pictureDirectoryPath = pictureDirectory.getPath();
//        Uri data = Uri.parse(pictureDirectoryPath);
//        photoPickerIntent.setDataAndType(data, "image/*");
//        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode== RESULT_OK) {
//            if (requestCode == IMAGE_GALLERY_REQUEST) {
//                Uri imageUri = data.getData();
//
//                InputStream inputStream;
//
//                try {
//                    inputStream = getContentResolver().openInputStream(imageUri);
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                    imgView.setImageBitmap(bitmap);
//                    uploadImage(imageUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }

//    public void uploadImage(Uri file) {
//        if(file != null)
//        {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
//            ref.putFile(file)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss();
//                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
//                                    .getTotalByteCount());
//                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
//                        }
//                    });
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
