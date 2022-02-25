package dev.jmsaez.florafragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import dev.jmsaez.florafragments.model.entity.Imagen;
import dev.jmsaez.florafragments.viewmodel.AddImagenViewModel;


public class AddImageFragment extends Fragment {

    private ActivityResultLauncher<Intent> launcher;
    private Intent resultadoImagen = null;
    private EditText etNombre, etDescripcion, etIdFlora;
    private AddImagenViewModel aivm;

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

        Bundle bundle = getArguments();
        long floraid = bundle.getLong("idflora");

        launcher = getLauncher();
        Button btSelectImage;
        etDescripcion = view.findViewById(R.id.etDescripcion);
        etNombre = view.findViewById(R.id.etNombreImagen);
        etIdFlora = view.findViewById(R.id.etIdFlora);
        etIdFlora.setText(String.valueOf(floraid));
        btSelectImage = view.findViewById(R.id.btSelectImage);
        btSelectImage.setOnClickListener(v -> {
            selectImage();
        });
        Button btAddImagen = view.findViewById(R.id.btAddImagen);
        btAddImagen.setText("Subir img");
        btAddImagen.setOnClickListener(v -> {
            uploadDataImage();
        });
        aivm = new ViewModelProvider(this).get(AddImagenViewModel.class);
    }

    private void uploadDataImage() {
        String nombre = etNombre.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String idFlora = etIdFlora.getText().toString();
        if(!(nombre.trim().isEmpty() ||
                idFlora.trim().isEmpty() ||
                resultadoImagen == null)) {
            Imagen imagen = new Imagen();
            imagen.nombre = nombre;
            imagen.descripcion = descripcion;
            imagen.idflora = Long.parseLong(idFlora);
            aivm.saveImagen(resultadoImagen, imagen);
        }
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
        return intent;
    }

    void selectImage() {
        Intent intent = getContentIntent();
        launcher.launch(intent);
    }
}