package dev.jmsaez.florafragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import dev.jmsaez.florafragments.databinding.FragmentSecondBinding;
import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.model.entity.ImageRowResponse;
import dev.jmsaez.florafragments.view.slideradapter.SliderAdapter;
import dev.jmsaez.florafragments.view.slideradapter.SliderItem;
import dev.jmsaez.florafragments.viewmodel.AddImagenViewModel;

public class FloraDetailFragment extends Fragment {
    private Toolbar toolbar;
    final String URL_IMG = "https://informatica.ieszaidinvergeles.org:10016/AD/felixRDLFapp/public/api/imagen/";
    private FragmentSecondBinding binding;

    EditText etName, etFamilia, etIdentificacion, etAltitud, etHabitat, etFitosociologia, etBiotipo
            , etBiologiaReprod, etFloracion, etFructificacion, etExprSex, etPolinizacion, etDispersion, etNumCromo,
            etReprAsex, etDistribucion, etBiologia, etDemografia, etAmenazas, etMedidas;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar progressBar = view.findViewById(R.id.progressBar2);


        toolbar = view.findViewById(R.id.toolbar2);
        AddImagenViewModel aivm = new ViewModelProvider(this).get(AddImagenViewModel.class);
        MutableLiveData<ImageRowResponse> images = aivm.getImagesLiveData();


        ArrayList<SliderItem> sliderDataArrayList = new ArrayList<>();
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        SliderAdapter adapter = new SliderAdapter(getContext());
        sliderView.setSliderAdapter(adapter);

        Bundle bundle = getArguments();
        Flora flora = bundle.getParcelable("flora");

        aivm.getImages(flora.getId());
        images.observe(this, image->{
            for (int i = 0; i < image.rows.length; i++) {
                sliderDataArrayList.add(new SliderItem(URL_IMG + image.rows[i].id));
                Log.v(":::IMGOBSERVER", URL_IMG+image.rows[i].id);
            }

            adapter.setSliderList(sliderDataArrayList);
            progressBar.setVisibility(View.GONE);
        });

        initialize(view);
        showItem(flora);
    }

    void initialize(View view){
        etName = view.findViewById(R.id.etNombre);
        etFamilia = view.findViewById(R.id.etFamiliaFlora);
        etIdentificacion = view.findViewById(R.id.etIdentificacionFlora);
        etAltitud = view.findViewById(R.id.etAltitudFlora);
        etHabitat = view.findViewById(R.id.etHabitatFlora);
        etFitosociologia = view.findViewById(R.id.etFitoFlora);
        etBiotipo = view.findViewById(R.id.etBiotipoFlora);
        etBiologiaReprod = view.findViewById(R.id.etBioReproFlora);
        etFloracion = view.findViewById(R.id.etFloracionFlora);
        etFructificacion = view.findViewById(R.id.etFructuacionFlora);
        etExprSex = view.findViewById(R.id.etExprSexFlora);
        etPolinizacion = view.findViewById(R.id.etPolinizacionFlora);
        etDispersion = view.findViewById(R.id.etDispersionFlora);
        etNumCromo = view.findViewById(R.id.etNumCromoFlora);
        etReprAsex = view.findViewById(R.id.etReprodAsexFlora);
        etDistribucion = view.findViewById(R.id.etDistribucionFlora);
        etBiologia = view.findViewById(R.id.etBiologiaFlora);
        etDemografia = view.findViewById(R.id.etDemografiaFlora);
        etAmenazas = view.findViewById(R.id.etAmenazasFlora);
        etMedidas = view.findViewById(R.id.etMedidasFlora);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void showItem(Flora flora){
        etName.setText(flora.getNombre());
        etFamilia.setText(flora.getFamilia());
        etIdentificacion.setText(flora.getIdentificacion());
        etAltitud.setText(flora.getAltitud());
        etHabitat.setText(flora.getHabitat());
        etFitosociologia.setText(flora.getFitosociologia());
        etBiotipo.setText(flora.getBiotipo());
        etBiologiaReprod.setText(flora.getBiologia_reproductiva());
        etFloracion.setText(flora.getFloracion());
        etFructificacion.setText(flora.getFructificacion());
        etExprSex.setText(flora.getExpresion_sexual());
        etPolinizacion.setText(flora.getPolinizacion());
        etDispersion.setText(flora.getDispersion());
        etNumCromo.setText(flora.getNumero_cromosomatico());
        etReprAsex.setText(flora.getReproduccion_asexual());
        etDistribucion.setText(flora.getDistribucion());
        etBiologia.setText(flora.getBiologia());
        etDemografia.setText(flora.getDemografia());
        etAmenazas.setText(flora.getAmenazas());
        etMedidas.setText(flora.getMedidas_propuestas());
    }



}