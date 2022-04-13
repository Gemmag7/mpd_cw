package org.me.gcu.grant_gemma_s2030516;

//Importing Libraries
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Custom Adapter class that is responsible for holding the list of sites after they
 * get parsed out of XML and returns the selected parsed data in a view fragment
 * Created on the 01/04/2022 by Gemma Grant s2030516
 */
public class Item_Adapter extends ArrayAdapter<Item>
{

    /**
     * Declaring variables
     */
    private Context ctx;
    private final List<Item> items;
    int adResource;

    /**
     * Constructor which
     * @param context is the context that has been passed in from the MainActivity class.
     * @param resource
     * @param items is the list of items that has been parsed into Item_Adapter class after being parsed from the xml data
     *              and is then collated into a list
     */
    public Item_Adapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        this.items = items;
        ctx = context;
        adResource = resource;

    }

    /**
     * This gets method gets the view to display the parsed xml data to the user
     * Build.VERSION_CODES.0 is used in order to parse the description into two localDate attributes - startDate and endDate
     * @param position is the position of each item in the list (i.e. 0 to the end of the items list
     * @param convertView is the returned view that will display all of the corresponding items to the user when they have
     *                    selected either planned or current roadworks
     * @param parent is the viewgroup
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        /**
         * Getting the title, startDate, endDate and description of each item in the items List
         * startDate and endDate are collated by the use of parsing the description of the item in the List
         */
        String title = getItem(position).getTitle();

        String description = getItem(position).getDescription();
        String startDate = getItem(position).parseDescription(getItem(position).getDescription())[0];
        String endDate = getItem(position).parseDescription(getItem(position).getDescription())[1];


        /**
         * Inflating the views from the layout in order to display the listview
         */
        LayoutInflater inflater = LayoutInflater.from(ctx);
        convertView = inflater.inflate(adResource, parent, false);



        /**
         * Initiating the textviews from the .xml file in the layout folder
         */
        TextView titleText = (TextView) convertView.findViewById(R.id.titleTxt);
        TextView startDateText = (TextView) convertView.findViewById(R.id.startDateTxt);
        TextView endDateText = (TextView) convertView.findViewById(R.id.endDateTxt);
      //  TextView descriptionText = (TextView) convertView.findViewById(R.id.descriptionTxt);
       // TextView descriptionTxt = (TextView) convertView.findViewById(R.id.descriptionTxt);

        /**
         *Setting the text of the textviews to the data extracted from the items List
         * Each item in the List will conatin text specific to each item due to the position variable
         */
        titleText.setText(title);
        startDateText.setText(startDate);
        endDateText.setText(endDate);
    //    descriptionText.setText(description);
      ///  descriptionTxt.setText(description);

        /**
         * returns the convertView which will populate the textviews with the data in the items List
         */
        return convertView;
    } // end of getView method


    /**
     * This method calculates the count of the number of items in the item list and returns it back to the user
     * @return the number of items in the item list
     */
    @Override
    public int getCount()
    {

        return items.size();

    } // end of getCount method

} //end of Item_Adpater class