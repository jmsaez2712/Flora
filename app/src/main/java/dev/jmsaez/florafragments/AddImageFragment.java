package dev.jmsaez.florafragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import dev.jmsaez.florafragments.model.entity.Imagen;
import dev.jmsaez.florafragments.viewmodel.AddImagenViewModel;


public class AddImageFragment extends Fragment {

    private ActivityResultLauncher<Intent> launcher;
    private Intent resultadoImagen = null;
    private EditText etNombre, etDescripcion, etIdFlora;
    private AddImagenViewModel aivm;
    private Toolbar toolbar;
    ImageView ivImagen;
    long floraid;

    public AddImageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    private void initialize(View view) {

        toolbar = view.findViewById(R.id.toolbar4);
        toolbar.inflateMenu(R.menu.edit_menu);
        toolbar.setOnMenuItemClickListener(item -> {return onOptionsItemSelected(item);});

        ivImagen = view.findViewById(R.id.ivUpload);
        Bundle bundle = getArguments();
        floraid = bundle.getLong("idflora");

        launcher = getLauncher();
        Button btSelectImage;

        btSelectImage = view.findViewById(R.id.btSelectImage);
        btSelectImage.setOnClickListener(v -> {
            selectImage();
        });
//        Button btAddImagen = view.findViewById(R.id.btAddImagen);
//        btAddImagen.setText("Subir img");
//        btAddImagen.setOnClickListener(v -> {
//            uploadDataImage();
//        });
        aivm = new ViewModelProvider(this).get(AddImagenViewModel.class);
    }

    private void uploadDataImage() {


            Imagen imagen = new Imagen();
            imagen.nombre = floraid+"";
            imagen.descripcion = "descripcion";
            imagen.idflora = floraid;
            aivm.saveImagen(resultadoImagen, imagen);

    }

    ActivityResultLauncher<Intent> getLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //respuesta al resultado de haber seleccionado una imagen
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        //copyData(result.getData());
                        resultadoImagen = result.getData();

                    }
                }
        );
    }

    Intent getContentIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        ivImagen.setImageURI(intent.getData());
        return intent;
    }

    void selectImage() {
        Intent intent = getContentIntent();
        launcher.launch(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_opt:{
                uploadDataImage();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}