package org.me.gcu.grant_gemma_s2030516;

//Importing Libraries
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Helper class for downloading a file created on 28/03/2022 by Gemma Grant s2030516
*/
public class Downloader {

    //TAG created for LOG statements
    private static String myTag="roadworks";

    //Handler message that represents posting a progress update
    static final int POST_PROGRESS = 1;


    /**
     * Download a file from the internet and store it locally
     * @param URL is the URL of the file that is to be downloaded
     * @param fos is a FileOutputStream used to save the downloaded file to
    */
    public static void DownloadFromUrl(String URL, FileOutputStream fos){
        try{
            URL url = new URL(URL);

            //keep the start time so we can display how long it took to the log
            long startTime = System.currentTimeMillis();
            Log.d(myTag, "download beginning");

            //Open a connection to the URL
            URLConnection ucon = url.openConnection();


            Log.i(myTag, "Opened Connection");

            /**
             * Defining InputStreams to read from the URLConnection
            */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Log.i(myTag, "Got InputStream and BufferedStream");

            /**
             * Defining OutputStreams to write to the file
            */
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            Log.i(myTag, "Got FileOutputStream and BufferedOutputStream ");

            /**
             * Reading and writing the downloaded file
            */
            byte data[] = new byte[1024];
            long total = 0;
            int count;

            //loop and read the current data
            while((count = bis.read(data))!= -1){

                //keep track of size for progress
                total += count;

                bos.write(data, 0, count);
            }

            //have to call flush or file can get corrupted
            bos.flush();
            bos.close();

            //Log statement to tell how long the download will be
            Log.d(myTag, "Download ready in " +((System.currentTimeMillis() - startTime))+ " millisec");

        }catch(IOException e){
            Log.d(myTag, "Error: "+ e);
        } //end of CATCH

    } //end of DownloadFromUrl method

} //end of Downloader class
