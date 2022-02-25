package dev.jmsaez.florafragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.viewmodel.AddFloraViewModel;


public class AddFloraFragment extends Fragment {

    EditText etNombre;
    Button btAdd;

    public AddFloraFragment() {
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
        return inflater.inflate(R.layout.fragment_add_flora, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    private void initialize(View view) {
        AddFloraViewModel afvm = new ViewModelProvider(this).get(AddFloraViewModel.class);
        afvm.getAddFloraLiveData().observe(this, flora ->{
            if(flora > 0){
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                Toast.makeText(getContext(), "Something petated muy duro", Toast.LENGTH_SHORT).show();
            }
        });

        etNombre = view.findViewById(R.id.etNombre);
        btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(v -> {
            Flora flora = new Flora();
            flora.setNombre(etNombre.getText().toString());
            afvm.createFlora(flora);
        });

    }
}