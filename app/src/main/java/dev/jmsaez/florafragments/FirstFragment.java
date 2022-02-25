package dev.jmsaez.florafragments;

import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import dev.jmsaez.florafragments.databinding.FragmentFirstBinding;
import dev.jmsaez.florafragments.model.entity.DeleteResponse;
import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.view.adapter.FloraAdapter;
import dev.jmsaez.florafragments.view.adapter.LookupClass;
import dev.jmsaez.florafragments.viewmodel.MainActivityViewModel;

public class FirstFragment extends Fragment {

    private Toolbar toolbar;
    private FloatingActionButton fabAdd, fabImg;
    private RecyclerView rvFlora;
    private SelectionTracker<Long> tracker;
    private ActionMode actionMode;
    private FloraAdapter floraAdapter;
    private MainActivityViewModel mavm;
    private MutableLiveData<ArrayList<Flora>> floraList;
    private ActionMode.Callback actionModeCallback;
    private ProgressBar progressBar;
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initialize(View view) {

        toolbar = view.findViewById(R.id.toolbar);
        progressBar = view.findViewById(R.id.progressBar);
        mavm = new ViewModelProvider(this).get(MainActivityViewModel.class);

        rvFlora = view.findViewById(R.id.rvFlora);
        rvFlora.setLayoutManager(new LinearLayoutManager(this.getContext()));
        floraAdapter = new FloraAdapter(this.getContext(), mavm);

        rvFlora.setAdapter(floraAdapter);

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        createTracker();
        createObserver(view);
        observeList();


        fabAdd = view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_addFloraFragment);
        });

        fabImg = view.findViewById(R.id.fabImg);
        fabImg.setOnClickListener(e->{
            NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_addImageFragment);
        });

        MutableLiveData<Integer> delete = mavm.getSecondDelete();
        delete.observe(FirstFragment.this, integer -> {
            Log.v(":::LIVEDATA", integer+"");
            refreshFragment();
        });

    }

    void createTracker(){
        tracker = new SelectionTracker.Builder<>("selection-1",
                rvFlora,
                new StableIdKeyProvider(rvFlora),
                new LookupClass(rvFlora),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();

        floraAdapter.setTracker(tracker);
    }

    void createObserver(View view){
        tracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onSelectionChanged() {
                if(tracker.hasSelection()){
                    if(actionMode == null){
                        actionMode = view.startActionMode(actionModeCallback());
                    }
                    updateContextualTitle();
                } else {
                    actionMode.finish();
                }
            }
        });
    }

    void observeList(){
        mavm.getFlora();
        floraList = mavm.getFloraLiveData();
        floraList.observe(this, floras -> {
            floraAdapter.setListFlora(floras);
            Log.v(":::XXY", floras.toString());
            progressBar.setVisibility(View.GONE);
        });
    }


    private int delete(){
        AtomicInteger value = new AtomicInteger();
        floraAdapter.delete();
        MutableLiveData<Integer> delete = mavm.getSecondDelete();
        delete.observe(getViewLifecycleOwner(), integer -> {
            value.set(integer);
        });
        return value.get();
    }

    private void updateContextualTitle(){
        this.actionMode.setTitle(tracker.getSelection().size()+"");
    }

    private ActionMode.Callback actionModeCallback(){
        return actionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.opt_delete:{
                        floraAdapter.delete();
                        tracker.clearSelection();
                        actionMode.finish();
                        return true;
                    }

                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode p0) {
                tracker.clearSelection();
                actionMode = null;
            }
        };
    }

    public void refreshFragment(){
        NavHostFragment.findNavController(FirstFragment.this).popBackStack();
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.main_fragment);
    }
}