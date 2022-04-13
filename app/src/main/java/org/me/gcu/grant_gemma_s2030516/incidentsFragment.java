package org.me.gcu.grant_gemma_s2030516;
//Imported Libraries
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * ListFragment class contains all view components and methods that are needed in order to display the fragment which
 * contains all of the roadworks parsed data.
 * This class makes use of the AdapterView.OnItemClickListener so that the list is interactive and the user can click
 * on an item in the list and a more detailed description of the selected roadworks
 * Created on 08/04/2022 by Gemma Grant s2030516
 */
public class incidentsFragment extends Fragment implements AdapterView.OnItemClickListener {

    //Initiating all view components
    private ListView parsedListView;
    private TextView titleText, descriptionText, linkText, pubDateText;
    private ArrayList<Item> items;
    private IncidentAdapter arrayAdapter;

    /**
     *
     * @param inflater which contains the xml file that is being inflated into the view
     * @param container which holds the container for the fragment
     * @param savedInstanceState is used for when searching for a specific roadwork/incident as well as clicking
     *                           on a specific roadwork/incident for more details
     * @return the fragment whihc contains the list of parsed data as well as other data
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        /**
         * checks to see if the items list is empty as well as checking to see if it is null
         * if it is not null and is not null, then the items list is set to clear
         */
        if(items != null && !items.isEmpty()) {
            items.clear();
        }

        Bundle bundle = getArguments();
        items = (ArrayList<Item>) bundle.getSerializable("ITEMLIST");

        //
        //arrayAdapter = new Item_Adapter(getActivity().getApplicationContext(), R.layout.incident_row, items);
        arrayAdapter = new IncidentAdapter(getActivity().getApplicationContext(), R.layout.incident_row, items);

        //initiating variables to view components in the fragment_list.xml file
        parsedListView = (ListView) v.findViewById(R.id.parsedListView);
        titleText = (TextView) v.findViewById(R.id.titleTxt);
        descriptionText = (TextView) v.findViewById(R.id.descriptionTxt);
        linkText = (TextView) v.findViewById(R.id.linkTxt);
        pubDateText = (TextView) v.findViewById(R.id.pubDateTxt);
        SearchView searchView = (SearchView) v.findViewById(R.id.itemListSearchView);

        //Setting the arrayAdapter to the parsedListView
        parsedListView.setAdapter(arrayAdapter);
        parsedListView.setOnItemClickListener(this);


        /**
         * setOnQueryTextListener interface is used from the searchView component
         * in the onQueryTextChange method a new arrayList called filteredItems is created and then the items arrayList is
         * each item in the for loop is compared against the text entered by the user against the title of each item in the items arrayList
         * for each filteredItem that matches the string text
         */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<Item> filteredItems = new ArrayList<Item>();
                for(Item item: items)
                {
                    if(item.getTitle().toLowerCase().contains(s.toLowerCase()))
                    {
                        filteredItems.add(item);
                    }
                }

                int filterCount =filteredItems.size();
                Log.e("count" , ":"+ filterCount);
                //Item_Adapter filteredArrayAdapter = new Item_Adapter(getActivity().getApplicationContext(), 0, filteredItems);
                arrayAdapter = new IncidentAdapter(getActivity().getApplicationContext(), R.layout.row_item, filteredItems);
                parsedListView.setAdapter(arrayAdapter);
                return false;
            }
        });

        // the view being returned to the app - the list fragment will display the list of parsed items whether filtered or not
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //get the item from that index that we clicked in the listview
        Item selectedItem = arrayAdapter.getItem(i);

        //Setting the textView to the values of the selected item when a user clicks on a specific incident
        titleText.setText(selectedItem.getTitle());
        descriptionText.setText(selectedItem.getDescription());
        linkText.setText("For further details: "+selectedItem.getLink());


    } //end of onItemClick method

}