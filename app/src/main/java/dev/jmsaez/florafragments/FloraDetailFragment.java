package dev.jmsaez.florafragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.textfield.TextInputEditText;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Date;

import dev.jmsaez.florafragments.databinding.FragmentSecondBinding;
import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.model.entity.ImageRowResponse;
import dev.jmsaez.florafragments.model.entity.Imagen;
import dev.jmsaez.florafragments.view.slideradapter.SliderAdapter;
import dev.jmsaez.florafragments.view.slideradapter.SliderItem;
import dev.jmsaez.florafragments.viewmodel.AddImagenViewModel;
import dev.jmsaez.florafragments.viewmodel.MainActivityViewModel;

public class FloraDetailFragment extends Fragment {
    private Toolbar toolbar;
    final String URL_IMG = "https://informatica.ieszaidinvergeles.org:10016/AD/felixRDLFapp/public/api/imagen/";
    private FragmentSecondBinding binding;

    private TextInputEditText etName, etFamilia, etIdentificacion, etAltitud, etHabitat, etFitosociologia, etBiotipo,
            etBiologiaReprod, etFloracion, etFructificacion, etExprSex, etPolinizacion, etDispersion, etNumCromo,
            etReprAsex, etDistribucion, etBiologia, etDemografia, etAmenazas, etMedidas;

    private Flora flora;
    private AddImagenViewModel aivm;
    private MainActivityViewModel mavm;
    private ActivityResultLauncher<Intent> launcher;
    private Intent resultadoImagen = null;
    private Button btImg;
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

        navigation(view);
        toolbar.inflateMenu(R.menu.edit_delete_menu);
        toolbar.setOnMenuItemClickListener( item->{
            return onOptionsItemSelected(item);
        });
        aivm = new ViewModelProvider(this).get(AddImagenViewModel.class);
        mavm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        MutableLiveData<ImageRowResponse> images = aivm.getImagesLiveData();

        ArrayList<SliderItem> sliderDataArrayList = new ArrayList<>();
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        SliderAdapter adapter = new SliderAdapter(getContext());
        sliderView.setSliderAdapter(adapter);

        Bundle bundle = getArguments();
        flora = bundle.getParcelable("flora");

        aivm.getImages(flora.getId());
        images.observe(this, image->{
            for (int i = 0; i < image.rows.length; i++) {
                sliderDataArrayList.add(new SliderItem(URL_IMG + image.rows[i].id));
            }
            adapter.setSliderList(sliderDataArrayList);
            progressBar.setVisibility(View.GONE);
        });

        initialize(view);
        showItem();
        setStyle();
    }

    void initialize(View view){
        launcher = getLauncher();

        etName = view.findViewById(R.id.etNombreFlora);
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

        btImg = view.findViewById(R.id.btImg);
        btImg.setOnClickListener( l ->{
            selectImage();
        });

        editObserver();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void showItem(){
        etName.setText(flora.getNombre()+"");
        etFamilia.setText(flora.getFamilia()+"");
        etIdentificacion.setText(flora.getIdentificacion()+"");
        etAltitud.setText(flora.getAltitud()+"");
        etHabitat.setText(flora.getHabitat()+"");
        etFitosociologia.setText(flora.getFitosociologia()+"");
        etBiotipo.setText(flora.getBiotipo()+"");
        etBiologiaReprod.setText(flora.getBiologia_reproductiva()+"");
        etFloracion.setText(flora.getFloracion()+"");
        etFructificacion.setText(flora.getFructificacion()+"");
        etExprSex.setText(flora.getExpresion_sexual()+"");
        etPolinizacion.setText(flora.getPolinizacion()+"");
        etDispersion.setText(flora.getDispersion()+"");
        etNumCromo.setText(flora.getNumero_cromosomatico()+"");
        etReprAsex.setText(flora.getReproduccion_asexual()+"");
        etDistribucion.setText(flora.getDistribucion()+"");
        etBiologia.setText(flora.getBiologia()+"");
        etDemografia.setText(flora.getDemografia()+"");
        etAmenazas.setText(flora.getAmenazas()+"");
        etMedidas.setText(flora.getMedidas_propuestas()+"");
    }

    void setStyle(){
        btImg.setVisibility(View.GONE);
        etName.setEnabled(false);
        etFamilia.setEnabled(false);
        etIdentificacion.setEnabled(false);
        etAltitud.setEnabled(false);
        etHabitat.setEnabled(false);
        etFitosociologia.setEnabled(false);
        etBiotipo.setEnabled(false);
        etBiologiaReprod.setEnabled(false);
        etFloracion.setEnabled(false);
        etFructificacion.setEnabled(false);
        etExprSex.setEnabled(false);
        etPolinizacion.setEnabled(false);
        etDispersion.setEnabled(false);
        etNumCromo.setEnabled(false);
        etReprAsex.setEnabled(false);
        etDistribucion.setEnabled(false);
        etBiologia.setEnabled(false);
        etDemografia.setEnabled(false);
        etAmenazas.setEnabled(false);
        etMedidas.setEnabled(false);
    }

    void unsetStyle(){
        btImg.setVisibility(View.VISIBLE);
        etName.setEnabled(true);
        etFamilia.setEnabled(true);
        etIdentificacion.setEnabled(true);
        etAltitud.setEnabled(true);
        etHabitat.setEnabled(true);
        etFitosociologia.setEnabled(true);
        etBiotipo.setEnabled(true);
        etBiologiaReprod.setEnabled(true);
        etFloracion.setEnabled(true);
        etFructificacion.setEnabled(true);
        etExprSex.setEnabled(true);
        etPolinizacion.setEnabled(true);
        etDispersion.setEnabled(true);
        etNumCromo.setEnabled(true);
        etReprAsex.setEnabled(true);
        etDistribucion.setEnabled(true);
        etBiologia.setEnabled(true);
        etDemografia.setEnabled(true);
        etAmenazas.setEnabled(true);
        etMedidas.setEnabled(true);


    }

    void navigation(View view){
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_opt: {
                mavm.deleteFlora(flora.getId());
                mavm.getSecondDelete().observe(this, integer ->{
                    NavHostFragment.findNavController(this).popBackStack();
                });
                return true;
            }
            case R.id.edit_option:{
                unsetStyle();
                Menu menu = toolbar.getMenu();
                menu.clear();
                toolbar.inflateMenu(R.menu.edit_menu);
                return true;
            }

            case R.id.save_opt:{
                updateFlora();

                return true;
            }

            case R.id.cancel_option:{
                Menu menu = toolbar.getMenu();
                menu.clear();
                setStyle();
                toolbar.inflateMenu(R.menu.edit_delete_menu);
                return true;
            }
        }

        return false;
    }


    void updateFlora(){

        flora.setNombre(etName.getText().toString());
        flora.setFamilia(etFamilia.getText().toString());
        flora.setIdentificacion(etIdentificacion.getText().toString());
        flora.setAltitud(etAltitud.getText().toString());
        flora.setHabitat(etHabitat.getText().toString());
        flora.setFitosociologia(etFitosociologia.getText().toString());
        flora.setBiotipo(etBiotipo.getText().toString());
        flora.setBiologia_reproductiva(etBiologiaReprod.getText().toString());
        flora.setFloracion(etFloracion.getText().toString());
        flora.setFructificacion(etFructificacion.getText().toString());
        flora.setExpresion_sexual(etExprSex.getText().toString());
        flora.setPolinizacion(etPolinizacion.getText().toString());
        flora.setDispersion(etDispersion.getText().toString());
        flora.setNumero_cromosomatico(etNumCromo.getText().toString());
        flora.setReproduccion_asexual(etReprAsex.getText().toString());
        flora.setDistribucion(etDistribucion.getText().toString());
        flora.setBiologia(etBiologia.getText().toString());
        flora.setDemografia(etDemografia.getText().toString());
        flora.setAmenazas(etAmenazas.getText().toString());
        flora.setMedidas_propuestas(etMedidas.getText().toString());

        mavm.editFlora(flora.getId(), flora);
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

    private void uploadDataImage(long id) {
        Imagen imagen = new Imagen();
        imagen.nombre = String.valueOf(new Date().getTime());
        imagen.descripcion = "descripcion";
        imagen.idflora = id;
        aivm.saveImagen(resultadoImagen, imagen);
    }

    void editObserver(){

        mavm.getEditLiveData().observe(this, edit->{
            if(resultadoImagen != null) {
                uploadDataImage(flora.getId());
            }
            NavHostFragment.findNavController(this).popBackStack();
        });
    }
}