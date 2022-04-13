    package org.me.gcu.grant_gemma_s2030516;

    //Importing Libraries
    import android.content.Context;
    import android.os.Build;
    import android.util.Log;

    import androidx.annotation.RequiresApi;

    import org.xmlpull.v1.XmlPullParser;
    import org.xmlpull.v1.XmlPullParserException;
    import org.xmlpull.v1.XmlPullParserFactory;

    import java.io.BufferedReader;
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.io.StringReader;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.Locale;

    /**
     * The FileXmlPullParser class is used to read in the xml data and return a parsed version of the data intop an array
     * This is useful for displaying the parsed data into an array list
     */
    public class FileXmlPullParser {

        /**
         * This is declared here since the items array list will be used for many methods
         */
        ArrayList<Item> items = new ArrayList<Item>(
        );

        /**
         * This method is in charge of obtaining
         * @param dataToParse is the xml data passed in from the MainActivity.java class
         * @return
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        public static ArrayList<Item> getItemsFromFile(String dataToParse) {

            ArrayList<Item> items = new ArrayList<Item>();

            //temp holder for current item whilst parsing
            Item currentItem = null;

            //temporary holder for current text value whilst parsing
            String currentText = "";


                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new StringReader(dataToParse));
                    int eventType = xpp.getEventType();
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

                            //No we add all these temporary item information attributes into a new item
                            //Even though the xml feed doesn't have an author or comments for any of the items we will add them to this constructor
                            //This is to make the constructor different from the current incidents constructor

                            //In this constructor we will call a function which parses the description to calculate how long each roadwork will take
                            //The current incident feed doesn't have this information so we can't call that function to parse it
                            //So we add the author and comments to differentiate the constructors

                            //public Item(String titleIn, String desIn, String linkIn, String geoIn,
                            // String authorIn, String commentsIn, String pubDateIn)
                            Item newItem = new Item(tempTitle, tempDescription, tempLink, tempLatLong, tempAuthor, tempComments, tempPubDateToDate);

                            //Now push that new item into our global ArrayList.
                            items.add(newItem);


                        }
                        eventType = xpp.next();

                    }
                }//End try
                catch (XmlPullParserException err) {
                    Log.e("MyTag", "Parsing failed. Reason: " + err.toString());
                } catch (IOException err) {
                    Log.e("MyTag", "IO Error.");
                }

                return items;
            } //end of getItemsFromFile

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static void parseData(String dataToBeParsed)
        {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(dataToBeParsed));
                int eventType = xpp.getEventType();

                //declaring instide of item to be false as data has not been parsed yet
                //this is useful when parsing so that the application will know when it is inside of item tag that is to be parsed
                //instead of
                boolean insideOfItem = false;

                //declaring all temp variables here
                String tempTitle = "";
                String tempDescription = "";
                String tempLink = "";
                String tempLatLong = "";
                String tempAuthor = "";
                String tempComments = "";
                String tempPubDate = "";
                LocalDate tempPubDateToDate = null;
                ArrayList<Item> items = new ArrayList<Item>();

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {

                            //Setting the boolean variable insideOfItem to true since we are in the item start tag
                            insideOfItem = true;

                        }else if (xpp.getName().equalsIgnoreCase("title")) {
                            /**
                             * if we are inside of the item then set the temporary title of the item to the nextText
                             */
                            if (insideOfItem) {
                                tempTitle = xpp.nextText();
                            }
                        }else if (xpp.getName().equalsIgnoreCase("description")) {
                            /**
                             * if we are inside of the item then set the temporary description of the item to the nextText
                             */
                            if (insideOfItem) {
                                tempDescription = xpp.nextText();

                                /**
                                 * there are a few <br /> tags in the <description> tags and so,
                                 *  the </br> will be replaced with a space in order to make the xml data more readable
                                 */
                                tempDescription = tempDescription.replace("<br />", " ");
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            /**
                             * if we are inside of the item then set the temporary link of the item to the nextText
                             */
                            if (insideOfItem) {
                                tempLink = xpp.nextText();
                            }
                        } else if (xpp.getName().equalsIgnoreCase("point")) {
                            /**
                             * if we are inside of the item then set the temporary latLong value of the item to the nextText
                             */
                            if (insideOfItem) {
                                tempLatLong = xpp.nextText();
                            }
                        } else if (xpp.getName().equalsIgnoreCase("author")) {
                            /**
                             * if we are inside of the item then set the temporary author of the item to the nextText
                             */
                            if (insideOfItem) {
                                tempAuthor = xpp.nextText();
                            }
                        } else if (xpp.getName().equalsIgnoreCase("comments")) {
                            /**
                             * if we are inside of the item then set the temporary comments of the item to the nextText
                             */
                            if (insideOfItem) {
                                tempComments = xpp.nextText();
                            }
                        } else if (xpp.getName().equalsIgnoreCase("pubdate")) {
                            /**
                             * if we are inside of the item then set the temporary publish date of the item to the nextText
                             */
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

                        //No we add all these temporary item information attributes into a new item
                        //Even though the xml feed doesn't have an author or comments for any of the items we will add them to this constructor
                        //This is to make the constructor different from the current incidents constructor

                        //In this constructor we will call a function which parses the description to calculate how long each roadwork will take
                        //The current incident feed doesn't have this information so we can't call that function to parse it
                        //So we add the author and comments to differentiate the constructors


                        Item newItem = new Item(tempTitle, tempDescription, tempLink, tempLatLong, tempAuthor, tempComments, tempPubDateToDate);

                        //Now push that new item into our global ArrayList.
                        items.add(newItem);

                    } //end of else if statement
                    eventType = xpp.next();

                } //end of WHILE LOOP
            }//End of TRY
            catch (XmlPullParserException err)
            {
                Log.e("MyTag","Parsing failed. Reason: " + err.toString());
            } //Enf of CATCH
            catch (IOException err)
            {
                Log.e("MyTag","IO Error.");
            } //End of CATCH
        } //end of parseData method

        } //end of FileXmlPullParser class
