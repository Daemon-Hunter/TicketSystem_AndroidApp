package com.example.aneurinc.prcs_app.Database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joshua Kellaway on 14/03/2016.
 * Database CRUD connection class
 */
public class ApiConnection {
    String URI = "http://xserve.uopnet.plymouth.ac.uk/Modules/INTPROJ/PRCS251G/api/"; // locaton of api
    String table = "";   // table directory name

    ApiConnection(DatabaseTable table)
    {

        this.table = table.toString();  // sets up new connection with that table name
    }

    public void add()
    {

    }

    public List<Map<String,String>> readAll()
    {
        List<Map<String,String>> listOfEntities = new ArrayList<>();
        String urlToGet = URI+  table;
        try{
            URL url = new URL(urlToGet);
            HttpURLConnection connection = (HttpURLConnection)   url.openConnection();    // connect to the url
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/JSON");    // to return in JSON Format
            InputStream data = connection.getInputStream();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {

                String inputLine = in.readLine();   // inputValues of the JSON
                inputLine = inputLine.replaceAll("\\[", "");
                inputLine = inputLine.replaceAll("\\]", "");
                String[] objArray = inputLine.split("\\},");

                for (String anObjArray : objArray) {

                    Map<String, String> tempMap;
                    tempMap = splitJSONString(anObjArray);
                    listOfEntities.add(tempMap);

                }


            }
        }catch(Exception e)
        {
            throw new RuntimeException(e);

        }


        return listOfEntities;
    }

    public Map<String,String> readSingle(int id)    // takes in individual id value;
    {
        String urlToGet = URI + table + "/" + Integer.toString(id);     // creation of URL with unique values;
        Map<String,String> map;                       // initilatisation of map which stores keys and values

        try{
            URL url = new URL(urlToGet);                        // Create URL Class
            HttpURLConnection connection = (HttpURLConnection)   url.openConnection();    // connect to the url
            connection.setRequestMethod("GET");                     // A GET method is created
            connection.setRequestProperty("Accept", "application/JSON");    // to return in JSON Format
            InputStream data = connection.getInputStream();  // the returning data

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {

                String inputLine = in.readLine();   // inputValues of the JSON

                map = splitJSONString(inputLine); // split up the string into a map
            }
            connection.disconnect();// disconnect from the URL
            return map; // return the map
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private Map<String,String> splitJSONString(String input)
    {
        Map<String,String> map = new HashMap<>();
        // initilatisation of map which stores keys and values
        String[] splitArray = input.split(","); // split up the string into the different columns
        splitArray[0] = splitArray[0].replaceAll("\\{", "");    // remove the beginning brace
        splitArray[splitArray.length -1] = splitArray[splitArray.length -1 ].replaceAll("\\}", "");// remove the end brace


        for (String aSplitArray : splitArray) {
            String temp = aSplitArray.replaceAll("\"", ""); // removes quote marks from json string
            String[] splitString = temp.split(":");     // splits each strig into key and value
            map.put(splitString[0], splitString[1]); // place values into the Map
        }

        return map;
    }
}
