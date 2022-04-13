package org.me.gcu.grant_gemma_s2030516;

//Importing libraries
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This is the main class that executes the application
 * This class implements the onNavigationItemSelectedListener which is useful for the menu navigation for the application
 * Created by Gemma Grant s2030516 on 24/03/2022
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Declaring all private view components as well as array adapter
     */
    private Item_Adapter lAdapter;
    private ListView parsedListView;
    private FileXmlPullParser parser;
    //Navigation Drawer
    private DrawerLayout drawer;

    /**
     * This section holds ALL links to the Traffic Scotland RSS Data feeds
     * All links are saved as a private string.
     * plannedUrl links to the Traffic Scotland RSS Data feed that contains data about planned roadworks
     * roadworksUrl links to the Traffic Scotland RSS Data feed that contains data about planned roadworks
     * incidentsUrl links to the TrafficScotland RSS Data feed that contains data about the current incidents
     */

    private String plannedUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    private String roadworksUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";

    private String incidentsUrl = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    //String to initiate the the result which is the string that gets pulled from the XML feed
    private String result = "";

    /**
     *
     * This is declared here since the items array list will be used for many methods
     */
    ArrayList<Item> items = new ArrayList<Item>();

    /**
     * onCreate function creates the view to be displayed when app is executed - the home fragment
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting the reference to the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setting the setSupportActionbar to the toolbar view created
        setSupportActionBar(toolbar);

        //Getting th reference to the drawer layout
        drawer = findViewById(R.id.drawer_layout);

        //Getting the reference to the navigation view
        NavigationView navigationView = findViewById(R.id.navigation_view);

        //Setting the navigation view to have a navigationItemSelectedLister
        //This will allow the user to click on different options in the nav bar to choose whether they wish to see planned roadworks, current roadworks and current incidents
        navigationView.setNavigationItemSelectedListener(this);

        //the actionBarToggle helps to distinguish when the drawer layout is opened or closed
        //when opened, the menu should display several links for the user to click on to view data on the corresponding link
        //when closed, the menu should not display at all, only the header should remain on the page
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_opened, R.string.navigation_drawer_closed);

        //Adding in a addDrawerListener to the drawer view created so that we can toggle between the menu drawer beign closed and open
        //this passes in the toggle variable
        drawer.addDrawerListener(toggle);

        //Synchronizing the state of the drawer indicator with the linked DrawerLayout
        toggle.syncState();

        /**
         * checks to see if the savedInstanceState is null
         * if null, then the home fragment should display on the app
         */
        if (savedInstanceState == null) {

            //getSupportFragmentManager opens the home page view of the app and displays this to the user
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, new HomeFragment()).commit();

            //When the app executes and the home page is displayed, the default value selected item should be the nav_home
            //This is apparent when the app is run as the colour of the "home" link is highlighted purple instead of white which are like the other links
            navigationView.setCheckedItem(R.id.nav_home);

        } // end of IF statement

    } // end of OnCreate method

    /**
     * This method is in charge of closing and opening the navigation menu
     * If drawer is currently open when user clicks on a non-interactive component in the app i.e. toolbar, the navigation menu will close to display the current page fully without the navigation taking up space
     * On the other hand, if the drawer is closed when the user clicks on a n on-interactive component in the app i.e. toolbar, textview, etc.., the navigation
     */
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } // end of IF statement
        else
        {
            super.onBackPressed();
        } // of ELSE statement

    } // end of onBackPressed method

    /**
     *The onClick listener for the navigation side drawer (i.e. Current Roadworks, Planned Roadworks & Current Incidents)
     * When an option is clicked, a new thread is started which parses the xml data and then opens a fragment for the thread to display to the user
     * @param item is the item selected from the menu i.e. home, current roadworks, planned roadworks & current incidents
     * @return either a true or false boolean value
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Switch statement used to identify which of the items where selected once clicked on
        switch (item.getItemId()) {
            case R.id.current_roadworks:

                //Calls the function to start parsing that specific feed
                startRoadworksParse();

                //A toast message displays to the user that the current roadworks data is being loaded so that the user isn't just staring at a blank view
                Toast.makeText(this, "Loading Current Roadworks...", Toast.LENGTH_LONG).show();

                //onto the next case
                break;
            case R.id.planned_roadworks:

                //Call the function to start parsing that specific feed
                startPlannedRoadworksParse();

                //A toast message displays to the user that the current roadworks data is being loaded so that the user isn't just staring at a blank view
                Toast.makeText(this, "Loading Planned Roadworks...", Toast.LENGTH_LONG).show();

                //onto the next case
                break;
            case R.id.current_incidents:

                //Call the function to start parsing that specific feed
                startCurrentIncidentsParse();

                //A toast message displays to the user that the current roadworks data is being loaded so that the user isn't just staring at a blank view
                Toast.makeText(this, "Loading Current Incidents...", Toast.LENGTH_LONG).show();

                //onto the next case
                break;

            case R.id.nav_home:
                //instead of calling the parse methods, the home fragment displays instead
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, new HomeFragment()).commit();

                //onto the next case
                break;
        } //end of switch statement

        //When we click on a page from the navigation slider, we want the drawer to close again and not stay open
        drawer.closeDrawer(GravityCompat.START);

        //returns a true boolean value
        return true;
    } // end of onNavigationItemSelected

    //We call this function from our on click which starts the thread to parse the current incidents feed
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startCurrentIncidentsParse() {
        //Clear the arraylist to so the arraylist doesn't contain any items from old parses
        items.clear();

        //We need to set the result back to an empty string when we start a new thread or else
        //when we start the new string it will keep adding on to the string from the previously called thread
        result = "";

        //start the thread which takes in the url for current incidents

        //Thread for downloading the xml data for current incidents and passes in the url to the current incidents url into the thread.
        //start the thread which takes in the url for current roadworks
        new Thread(() -> {



            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            try
            {
                aurl = new URL(incidentsUrl);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                }
                in.close();
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }


            /*
             * If network is available download the xml from the Internet.
             * If not then try to use the local file from last time.
             */
            if (isNetworkAvailable()) {
                Log.i("Items", "starting download Task");

            } else {
                lAdapter = new Item_Adapter(getApplicationContext(), -1, FileXmlPullParser.getItemsFromFile(String.valueOf(MainActivity.this)));
                parsedListView.setAdapter(lAdapter);
            }
            try {

                /**
                 * Calls the DownloadFromUrl method in the Downloader.java class
                 * passes in the current incidents url into the method
                 */
                Downloader.DownloadFromUrl(incidentsUrl, openFileOutput("CurrentIncidents.xml", Context.MODE_PRIVATE));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            parseIncidentData(result);


            /**
             * This method is in charge for displaying data in the thread.
             */
            runOnUiThread(()->{


                //creating a new fragment transaction and beginning it
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                //creating a new instance of the list fragment here
                ListFragment listFragment = new ListFragment();

                //Create a new bundle which we will be used to pass in the list of items into the list view fragment
                Bundle bundle = new Bundle();
                //outSerializable sets the items list to the list view fragment
                bundle.putSerializable("ITEMLIST", items);

                Log.e("items list" ,": " + items);
                Log.e("count" ,": " + items.size());
                //Set the arguments of our fragment to bundle we created with our list
                listFragment.setArguments(bundle);

                //Tell the activity we are swapping the frameview with our fragment
                fragmentTransaction.replace(R.id.fragment_view, listFragment);

                fragmentTransaction.addToBackStack(null);

                /**
                 * commiting the fragment transaction
                 * The commit() call signals to the FragmentManager that all operations have been added to the transaction.
                 */
                fragmentTransaction.commit();

            });

        }).start();

    }

    //We call this function from our on click which starts the thread to parse the planned roadworks feed
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startPlannedRoadworksParse() {
        //Clear the arraylist to so the arraylist doesn't contain any items from old parses
        items.clear();

        //We need to set the result back to an empty string when we start a new thread or else
        //when we start the new string it will keep adding on to the string from the previously called thread
        result = "";



        new Thread(() -> {



            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            try
            {
                aurl = new URL(plannedUrl);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                }
                in.close();
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }


            /*
             * If network is available download the xml from the Internet.
             * If not then try to use the local file from last time.
             */
            if (isNetworkAvailable()) {
                try {

                    /**
                     * Calls the DownloadFromUrl method in the Downloader.java class
                     * passes in the planned roadworks url into the method
                     */
                    Downloader.DownloadFromUrl(plannedUrl, openFileOutput("PlannedRoadworks.xml", Context.MODE_PRIVATE));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Log.i("Items", "starting download Task");

            } else {
                lAdapter = new Item_Adapter(getApplicationContext(), -1, FileXmlPullParser.getItemsFromFile(String.valueOf(MainActivity.this)));
                parsedListView.setAdapter(lAdapter);
            }

             
             //calling the parseData method to display the parsed data
            parseData(result);
             runOnUiThread(()->{

//creating a new fragment transaction and beginning it
                         FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                         fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                         //creating a new instance of the list fragment here
                         ListFragment listFragment = new ListFragment();

                         //Create a new bundle which we will be used to pass in the list of items into the list view fragment
                         Bundle bundle = new Bundle();
                         //outSerializable sets the items list to the list view fragment
                         bundle.putSerializable("ITEMLIST", items);

                         Log.e("items list" ,": " + items);
                         Log.e("count" ,": " + items.size());
                         //Set the arguments of our fragment to bundle we created with our list
                         listFragment.setArguments(bundle);

                         //Tell the activity we are swapping the frameview with our fragment
                         fragmentTransaction.replace(R.id.fragment_view, listFragment);

                         fragmentTransaction.addToBackStack(null);

                         /**
                          * commiting the fragment transaction
                          * The commit() call signals to the FragmentManager that all operations have been added to the transaction.
                          */
                         fragmentTransaction.commit();
             });

        }).start();

    }

    //We call this function from our on click which starts the thread to parse the current roadworks feed
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startRoadworksParse() {
        //Clear the arraylist to so the arraylist doesn't contain any items from old parses
        items.clear();

        //We need to set the result back to an empty string when we start a new thread or else
        //when we start the new string it will keep adding on to the string from the previously called thread
        result = "";

        //start the thread which takes in the url for current roadworks
        new Thread(() -> {



                URL aurl;
                URLConnection yc;
                BufferedReader in = null;
                String inputLine = "";


                try
                {
                    aurl = new URL(roadworksUrl);
                    yc = aurl.openConnection();
                    in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                    while ((inputLine = in.readLine()) != null)
                    {
                        result = result + inputLine;
                    }
                    in.close();
            } catch (MalformedURLException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }


            /*
             * If network is available download the xml from the Internet.
             * If not then try to use the local file from last time.
             */
            if (isNetworkAvailable()) {
                Log.i("Items", "starting download Task");

            } else {
                lAdapter = new Item_Adapter(getApplicationContext(), -1, FileXmlPullParser.getItemsFromFile(String.valueOf(MainActivity.this)));
                parsedListView.setAdapter(lAdapter);
            }
            try {

                /**
                 * Calls the DownloadFromUrl method in the Downloader.java class
                 * passes in the planned roadworks url into the method
                 */
                Downloader.DownloadFromUrl(roadworksUrl, openFileOutput("CurrentRoadworks.xml", Context.MODE_PRIVATE));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            parseData(result) ;

            /**
             * This method is in charge for displaying data in the thread.
             */
            MainActivity.this.runOnUiThread(()->{


                //creating a new fragment transaction and beginning it
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                //creating a new instance of the list fragment here
                ListFragment listFragment = new ListFragment();

                //Create a new bundle which we will be used to pass in the list of items into the list view fragment
                Bundle bundle = new Bundle();
                //outSerializable sets the items list to the list view fragment
                bundle.putSerializable("ITEMLIST", items);

                Log.e("items list" ,": " + items);
                Log.e("count" ,": " + items.size());
                //Set the arguments of our fragment to bundle we created with our list
                listFragment.setArguments(bundle);

                //Tell the activity we are swapping the frameview with our fragment
                fragmentTransaction.replace(R.id.fragment_view, listFragment);

                fragmentTransaction.addToBackStack(null);

                /**
                 * commiting the fragment transaction
                 * The commit() call signals to the FragmentManager that all operations have been added to the transaction.
                 */
                fragmentTransaction.commit();

            });

        }).start();

    }

    /**
     * ParseData method accepts the string data and parses it into an array
     * @param dataToParse is the results passed in from the downlaoded xml file
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void parseData(String dataToParse) {

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();

            //Declaring all variables needed
            boolean insideOfItem = false;
            String tempTitle = "";
            String tempDescription = "";
            String tempLink = "";
            String tempLatLong = "";
            String tempAuthor = "";
            String tempComments = "";
            String tempPubDate = "";
            LocalDate tempPubDateToDate = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {

                        insideOfItem = true;

                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideOfItem) {
                            tempTitle = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (insideOfItem) {
                            tempDescription = xpp.nextText();

                            /**
                             * there are a few <br /> tags in the <description> tags and so,
                             *  the </br> will be replaced with a space in order to make the xml data more readable
                             */
                            tempDescription = tempDescription.replace("<br />", " ");
                        }
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideOfItem) {
                            tempLink = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("point")) {
                        if (insideOfItem) {
                            tempLatLong = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("author")) {
                        if (insideOfItem) {
                            tempAuthor = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("comments")) {
                        if (insideOfItem) {
                            tempComments = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("pubdate")) {
                        if (insideOfItem) {
                            tempPubDate = xpp.nextText();

                            //In the xml feed each of the publication dates has a time, and its always 00:00
                            //This is redundant and not needed so i'm going to trim it off the end of each item
                            int dateTrim = tempPubDate.indexOf("00:00");
                            tempPubDate = tempPubDate.substring(0, dateTrim - 1);

                            //Since the date is stored as a string I want it converted to a date time
                            //This date time formatter formats the specific type of string into a LocalDate
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH);
                            LocalDate date = LocalDate.parse(tempPubDate, formatter);
                            tempPubDateToDate = date;
                        }
                    }

                } else if (eventType == XmlPullParser.END_TAG
                        && xpp.getName().equalsIgnoreCase("item")) {
                    insideOfItem = false;


                    //passing in  all of the temporary attributes of an item into the constructor in the Item class
                    Item newItem = new Item(tempTitle, tempDescription, tempLink, tempLatLong, tempAuthor, tempComments, tempPubDateToDate);

                    //Once the newItem has been created, it is then added to the items list
                    items.add(newItem);


                }
                eventType = xpp.next();

            }
        }//End try
        catch (XmlPullParserException e)
        {
            Log.e("MyTag","Parsing failed. Reason: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.e("MyTag","Error: "+ e.getMessage());
        }


    }//End parseData

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void parseIncidentData(String dataToParse) {

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();

            //boolean variable is created to check if the current tag is inside of an item
            boolean insideOfItem = false;

            /**
             * Declaring the temporary attributes which will be used to hold data after it has been parsed which will then
             * be passed into an Item constructor to then be added to the items arrayList
             */
            String tempTitle = "";
            String tempDescription = "";
            String tempLink = "";
            String tempLatLong = "";
            String tempAuthor = "";
            String tempComments = "";
            String tempPubDate = "";
            LocalDate tempPubDateToDate = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {

                        insideOfItem = true;

                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideOfItem) {
                            tempTitle = xpp.nextText();
                            Log.e("temp Title", ": " + tempTitle);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (insideOfItem) {
                            tempDescription = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideOfItem) {
                            tempLink = xpp.nextText();

                        }
                    } else if (xpp.getName().equalsIgnoreCase("point")) {
                        if (insideOfItem) {
                            tempLatLong = xpp.nextText();

                        }
                    } else if (xpp.getName().equalsIgnoreCase("author")) {
                        if (insideOfItem) {
                            tempAuthor = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("comments")) {
                        if (insideOfItem) {
                            tempComments = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                        if (insideOfItem) {
                            tempPubDate = xpp.nextText();

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
                            LocalDate date = LocalDate.parse(tempPubDate, formatter);

                            tempPubDateToDate = date;
                        }
                    }
                    //checks to see if the  event type matches up with the end tag in the xml file as well as seeing if it matches the item tag
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    insideOfItem = false;

                    //Creating a new Item object, this constructor takes in everything bar the author and comments
                   // Item newItem = new Item(tempTitle, tempDescription, tempLink, tempLatLong, tempPubDateToDate);

                    Item newItem = new Item(tempTitle, tempDescription);
                    //Now push this item to our global arraylist
                    items.add(newItem);


                }
                eventType = xpp.next();

            }
        }//End try
        catch (XmlPullParserException e)
        {
            Log.e("MyTag","Parsing failed. Reason: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.e("MyTag","Error:" + e.getMessage());
        }


    }//End parseData


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                return true;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            {

                return true;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            {

                //returns a true boolean variable
                return true;
            }

        } //end if IF statement

        //retruns a false boolean variable
        return false;

    } //end of isNetworkAvailable method


} //End of MainActivity class
