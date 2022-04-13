
package org.me.gcu.grant_gemma_s2030516;

//Importing Libraries
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 *Item class which contains all attributes that belong to an item, as well as constructors used for passing out Item
 * information to other classes in the application
 * Created on the 31/03/2022 by Gemma Grant s2030516
 */
public class Item implements Serializable {

    private boolean isRoadworks;
    /**
     * Constructor which has ALL attributes of an item EXCEPT the author and comments.
     * This is used when adding a new item to the items list for the car incidents.
     * Author and Comments is excluded since they return NULL values
     * @param passedInTitle is the title of the of the item that is passed into the constructor
     * @param passedInDescription is the description of the of the item that is passed into the constructor
     * @param passedInLink is the link of the of the item that is passed into the constructor
     * @param passedInLatLong is the geographical points of the of the item that is passed into the constructor
     * @param passedInPubDate is the published date of the of the item that is passed into the constructor
     */
    public Item(String passedInTitle, String passedInDescription, String passedInLink, String passedInLatLong, LocalDate passedInPubDate){

        this.title = passedInTitle;
        this.description = passedInDescription;
        this.link = passedInLink;
        this.latLong = passedInLatLong;
        this.pubDate = passedInPubDate.toString();
        isRoadworks = false;
    }

    /**
     * Constructor which has ALL attributes of an item.
     * This is used when adding a new item to the items list for both current and planned roadworks.
     * @param title is the title of the of the item that is passed into the constructor
     * @param description is the description of the of the item that is passed into the constructor
     * @param link is the title of the of the link that is passed into the constructor
     * @param latLong is the geographical points of the of the item that is passed into the constructor
     * @param author is the author of the of the item that is passed into the constructor
     * @param comments is the comments of the of the item that is passed into the constructor
     * @param parsedDate is the parsed version of the pubDate attribute of the of the item that is passed into the constructor
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Item(String title, String description, String link, String latLong, String author, String comments, LocalDate parsedDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.latLong = latLong;
        this.author = author;
        this.comments = comments;
        this.pubDate = parsedDate.toString();

        isRoadworks = true;
        //Parse the description for the start and end time
        String sDate = parseDescription(description)[0];
        String eDate = parseDescription(description)[1];

    }

    /**
     * Empty constructor used for creating a new instance of item
     */
    public Item(String title, String description)
    {
        this.title = title;
        this.description = description;


        //String startDate = parseDescription(description)[0];
        //String endDate = parseDescription(description)[1];

        //this.startDate = startDate;
        //this.endDate = endDate;
    }

    /**
     * Declaring all properties of an item here
     */
    private String title, description, link, latLong, author, comments, pubDate,startDate, endDate;


    /**
     * Getter method for finding the title of an item in the xml data
     * @return the title of an item
     */
    public String getTitle(){
        return title;
    }

    /**
     * Setter method for setting the title of an item in the xml data
     * @param title is passed in and set to the value of the current item's title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Getter method for finding the description of an item in the xml data
     * @return the description of an item
     */
    public String getDescription(){
        return description;
    }

     /**
     * Setter method for setting the description of an item in the xml data
     * @param description is passed in and set to the value of the current item's description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Getter method for finding the link of an item in the xml data
     * @return the link of an item
     */
    public String getLink(){
        return link;
    }

    /**
     * Setter method for setting the link of an item in the xml data
     * @param link is passed in and set to the value of the current item's link to the traffic scotland website
     */
    public void setLink(String link){
        this.link = link;
    }

    /**
     * Getter method for finding the latitude and longitude of an item in the xml data
     * @return the latitude and longitude of an item's location
     */
    public String getLatLong(){
        return latLong;
    }

    /**
     * Setter method for setting the latitude and longitude of an item in the xml data
     * @param latLong is passed in and set to the value of the current item's latitude and longitude
     */
    public void setLatLong(String latLong){
        this.latLong = latLong;
    }

    /**
     * Getter method for finding the author of an item in the xml data
     * @return the author of an item
     */
    public String getAuthor(){
        return author;
    }

    /**
     * Setter method for setting the author of an item in the xml data
     * @param author is passed in and set to the value of the current item's latitude and longitude
     */
    public void setAuthor(String author){
        this.author = author;
    }

    /**
     * Getter method for finding the comments of an item in the xml data
     * @return the comments that belong to an item
     */
    public String getComments(){
        return comments;
    }

    /**
     * Setter method for setting the comments of an item in the xml data
     * @param comments is passed in and set to the value of the current item's comments
     */
    public void setComments(String comments){
        this.comments = comments;
    }

    /**
     * Getter method for finding the publish date of an item in the xml data
     * @return the publish date of an item
     */
    public String getPubDate(){
        return pubDate;
    }

    /**
     * Setter method for setting the publish date of an item in the xml data
     * @param pubDate is passed in and set to the value of the current item's publish date
     */
    public void setPubDate(String pubDate){
        this.pubDate = pubDate;
    }

    /**
     * Getter method for finding the start date of an item in the xml data
     * @return the start date of an incident/roadwork
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Setter method for setting the startDate of an item in the xml data
     * @param startDate is passed in and set to the value of the current item's start date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter method for finding the end date of an item in the xml data
     * @return the end date of an incident/roadwork
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Setter method for setting the endDate of an item in the xml data
     * @param endDate is passed in and set to the value of the current item's endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Method is used to format String variable pubDate into a SimpleDateFormat
     * @return pubDateFormatted - the new date variable that will no longer be a String value, instead,
     * it will be SimpleDateFormat
     */
    public String getPubDateFormatted() {
        /**
         * Try method is used to check if there is a pubDate
         * If pubDate exists, then pubDate will be parsed into a SimpleDateFormat date variable which will be returned
         */
        try {
            if (pubDate != null) {
                Date date = dateInitialFormat.parse(pubDate);
                String pubDateFormatted = dateAfterFormatted.format(date);
                return pubDateFormatted;
            }
            /**
             * If there is no pubDate value, then a message should display to user that there is no date found in xml data
             */
            else {
                return "No date found in xml data";
            }
        }
        /**
         * If there is an error with parsing the date, then a message should display to user stating that there was an issue parsing the date.
         */
        catch (ParseException e) {
            return "ERROR: there was an issue parsing date in xml feed";
        }
    }

    /**
     * ToString method creates a string value of an item with each attribute passed into the arrayList
     * @return a string value which holds data for an item and is then passed into the items arrayList
     */
    @Override
    public String toString() {
        if (isRoadworks) {
            return title + "\n" + description + "\n" + link + "\n" + latLong + "\n" + author + "\n" + comments + "\n" + pubDate + "\n" + startDate + "\n" + endDate;
        }
        return title + "\n" + description + "\n" + link + "\n" + latLong + "\n" + pubDate;
    }

    /**
     * This method deals with the formatting of the description of an item
     * This involves extracting the startDate and endDate of an item as well as removing unnecessary text
     * such as <br > tags
     * @param descIn is the description of an item that is passed into the method from the arrayList
     * @return a string array of which contains the startDate and endDate of each roadwork/incident from the RSSfeed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String[] parseDescription(String passedInDescription) {
        /**
         * indexOf is used to find the position of the start and end of a startDate
         * the start of a startDate is identified by the symbol ":"
         * the end of a startDate is identified by the symbol "-"
         */
        int findEndOfStartDate = passedInDescription.indexOf("-");
        int findStartDate = passedInDescription.indexOf(":");


        /**
         * creating a substring which is based on the indexes found from thr startEndDate and startEndStart variables
         * the numeric values "+2" and "-1" are used to remove the white space around the startDate
         * the startDate variable is then declared by using the substring method and the position variables declared
         * above to find the startDate in the description
         */

          String startDateStr = passedInDescription.substring(findStartDate + 2, findEndOfStartDate - 1);

        /**
         * indexOf is used to find the position of the start and end of a endDate
         * the start of an endDate    is identified by the string text "End Date: "
         * the end of a endDate is identified by the symbol "- 00:00"
         */
        int findEndDate = passedInDescription.indexOf("End Date: ");
        int findEndOfEndDate = passedInDescription.lastIndexOf("- 00:00");

        /**
         * creating a substring which is based on the indexes found from thr startEndDate and startEndStart variables
         * the numeric values "+2" and "-1" are used to remove the white space around the startDate
         * the startDate variable is then declared by using the substring method and the position variables declared
         * above to find the startDate in the description
         */
        String endDateStr = passedInDescription.substring(findEndDate, findEndOfEndDate);

        //Find the ":" so from the substring we just created to trim of the "End Date:" part of the string
        int startEndDate = endDateStr.indexOf(":");

        //Create a substring based on the two index found and the end of the endDateString. +2 and -1 are to delete the space around the date
        endDateStr = endDateStr.substring(startEndDate + 2, endDateStr.length() - 1);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH);

        LocalDate date1 = LocalDate.parse(startDateStr, formatter2);
        LocalDate date2 = LocalDate.parse(endDateStr, formatter2);


        //return these two strings in an array of dates which can be extracted for displaying data
        return new String[]{ startDateStr, endDateStr };
    }

    /**
     * First format method for formatting the date
     */
    private SimpleDateFormat dateInitialFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

    /**
     * Format method after dateInitialFormat is processed
     */
    private SimpleDateFormat dateAfterFormatted = new SimpleDateFormat("EEEE h:mm a (MMM d)");
}
