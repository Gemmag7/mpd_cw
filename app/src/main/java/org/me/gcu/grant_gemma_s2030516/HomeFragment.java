package org.me.gcu.grant_gemma_s2030516;

//Importing Libraries
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to create an instance of this fragment.
 * Created on 25/03/2022 by Gemma Grant s2030516
 */
public class HomeFragment extends Fragment {

    /**
     * Empty constructor that is required
     */
    public HomeFragment() {

    }

    // on new instance create a new HomeFragment

    /**
     * When a new instance is called, a new HomeFragment() is created
     * @return the newly created fragment
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    /**
     * When the onCreate function is called, it called the super method which will pass in the savedInstanceState
     * @param savedInstanceState passed into the super method and will display the current savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     * @param inflater which contains the xml file that is being inflated into the view
     * @param container which holds the container for the fragment
     * @param savedInstanceState the current savedInstanceState
     * @return the inflated view to the app
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    } //end of the onCreateView method

} //end of HomeFragment
