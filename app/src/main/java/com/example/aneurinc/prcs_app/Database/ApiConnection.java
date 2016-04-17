package com.example.aneurinc.prcs_app.Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dominic on 14/04/2016.
 */
public class APIConnection {

    String URI = "http://xserve.uopnet.plymouth.ac.uk/Modules/INTPROJ/PRCS251G/api/"; // locaton of api
    String table = "";   // table directory name

    public APIConnection(DatabaseTable table) {

        this.table = table.toString() + "s";  // sets up new connection with that table name
    }

    public boolean delete(int id) {
        boolean ableToDelete = false;
        String urlToDelete = URI + table + "/" + Integer.toString(id);
        try {
            URL url = new URL(urlToDelete);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            ableToDelete = true;
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("DELETE");
            connection.connect();
            ableToDelete = true;

        } catch (Exception ex) {
        }
        return ableToDelete;
    }

    public void update(int id, Map<String, String> mapToEdit) {
        String urlToPost = URI + table + "/" + Integer.toString(id);  // URL of where to add to the table.
        try {
            URL url = new URL(urlToPost);
            //Connect
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.connect();

            //WRITE
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(createJsonString(mapToEdit));
            writer.close();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); // needs this to work
            br.close();
            connection.disconnect();

        } catch (Exception x) {
            System.out.println("NOPE");
            System.out.println(x.getMessage());
        }

    }

    public void add(Map<String, String> mapToAdd) {
        String urlToPost = URI + table;  // URL of where to add to the table.
        try {
            URL url = new URL(urlToPost);
            //Connect
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();

            //WRITE
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(createJsonString(mapToAdd));
            writer.close();
            os.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();


        } catch (Exception x) {
            System.out.println("NOPE");
            System.out.println(x.getMessage());
        }
    }

    public List<Map<String, String>> readAll() {
        List<Map<String, String>> listOfEntities = new ArrayList<>();
        String urlToGet = URI + table;
        try {
            URL url = new URL(urlToGet);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // connect to the url
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/JSON");    // to return in JSON Format
            InputStream data = connection.getInputStream();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String inputLine = in.readLine();   // inputValues of the JSON
                inputLine = inputLine.replaceAll("\\[", "");
                inputLine = inputLine.replaceAll("\\]", "");
                String[] objArray = inputLine.split("\\},");

                for (int i = 0; i < objArray.length; i++) {
                    Map<String, String> tempMap = splitJSONString(objArray[i]);
                    listOfEntities.add(tempMap);
                }
            }
        } catch (Exception e) {

        }


        return listOfEntities;
    }

    public Map<String, String> readSingle(int id)    // takes in individual id value;
    {
        String urlToGet = URI + table + "/" + Integer.toString(id);     // creation of URL with unique values;
        Map<String, String> map = new HashMap<>();                       // initilatisation of map which stores keys and values

        try {
            URL url = new URL(urlToGet);                        // Create URL Class
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // connect to the url
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

    public List<Map<String, String>> getArtistAmount(int amount, int lowestID) {
        List<Map<String, String>> listOfEntities = new ArrayList<>();
        String urlToGet = URI + "functions/getartistsamount/" + Integer.toString(amount) + "/" + Integer.toString(lowestID);
        try {
            URL url = new URL(urlToGet);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // connect to the url
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/JSON");    // to return in JSON Format
            InputStream data = connection.getInputStream();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String inputLine = in.readLine();   // inputValues of the JSON
                inputLine = inputLine.replaceAll("\\[", "");
                inputLine = inputLine.replaceAll("\\]", "");
                String[] objArray = inputLine.split("\\},");

                for (int i = 0; i < objArray.length; i++) {
                    Map<String, String> tempMap = splitJSONString(objArray[i]);
                    listOfEntities.add(tempMap);
                }
            }
        } catch (Exception e) {

        }


        return listOfEntities;
    }


    private Map<String, String> splitJSONString(String input) {
        Map<String, String> map = new HashMap<>();
        // initilatisation of map which stores keys and values
        String[] splitArray = input.split(","); // split up the string into the different columns
        splitArray[0] = splitArray[0].replaceAll("\\{", "");    // remove the beginning brace
        splitArray[splitArray.length - 1] = splitArray[splitArray.length - 1].replaceAll("\\}", "");// remove the end brace

        for (int i = 0; i < splitArray.length; i++) {
            String temp = splitArray[i].replaceAll("\"", ""); // removes quote marks from json string
            String[] splitString = temp.split(":", 2);     // splits each strig into key and value
            map.put(splitString[0], splitString[1]); // place values into the Map
        }

        return map;
    }


    public String createJsonString(Map<String, String> map) {
        String strToReturn = "{";
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        boolean isAnInteger;
        for (int i = 0; i < keys.length; i++) {

            String endValue = ",";
            String tempLine = "\"" + keys[i] + "\"";

            if (i == keys.length - 1) {
                endValue = "}";
            }

            try {
                Integer.parseInt(values[i].toString());
                isAnInteger = true;

            } catch (Exception ex) {
                isAnInteger = false;
            }
            if (isAnInteger == false) {
                tempLine += ":" + "\"" + values[i] + "\"" + endValue;
            } else {
                tempLine += ":" + values[i] + endValue;
            }


            strToReturn += tempLine;
        }
        //        System.out.println(strToReturn);  // DEBUG here
        return strToReturn;
    }

}


