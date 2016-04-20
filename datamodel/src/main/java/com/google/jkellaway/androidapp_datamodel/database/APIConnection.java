/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
 *
 * @author 10512691
 */
public final class APIConnection {

    // URL of the web API
    private static String URI = "http://xserve.uopnet.plymouth.ac.uk/Modules/INTPROJ/PRCS251G/api/";

    // Converts the DatabaseTable Enum value to a string for use in the connection string
    private static String DBTableToString(DatabaseTable table){
        return table.toString() + "s";
    }

    // Allows the application to delete a record in the database
    public static boolean delete(int id, DatabaseTable table)
    {
        boolean ableToDelete;
        String urlToDelete = URI + DBTableToString(table) + "/" + Integer.toString(id);
        try{
            URL url = new URL(urlToDelete);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
            connection.setRequestMethod("DELETE");
            connection.connect();
            ableToDelete = true;

        }catch(IOException ex)
        {
            System.err.println(ex.toString());
            ableToDelete = false;
        }
        return ableToDelete;
    }

    // Allows the application to
    public static String update(int id, Map<String,String> mapToEdit, DatabaseTable table)
    {
       // URL of where to add to the table.
       String urlToPost = URI + DBTableToString(table) + "/"+ Integer.toString(id);
        BufferedReader br;
       try{
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
              
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            br.close();
            connection.disconnect();
            return br.toString();
       }catch(IOException x)
       {
           System.err.println("NOPE");
           System.err.println(x.getMessage());
           return "";
       }

    }
    
       public static String add(Map<String,String> mapToAdd, DatabaseTable table)
    {
       String urlToPost = URI + DBTableToString(table);  // URL of where to add to the table.
       try{
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
           BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();         

            while ((line = br.readLine()) != null) {  
                sb.append(line); 
            }       

            br.close();

           return sb.toString();
       }catch(IOException x)
       {
           System.err.println("NOPE");
           System.err.println(x.getMessage());
           return "";
       }
    }
       
    public static List<Map<String,String>> readAll(DatabaseTable table)
    {
            List<Map<String,String>> listOfEntities = new ArrayList<>();
            String urlToGet = URI +  DBTableToString(table);
            try {
                URL url = new URL(urlToGet);
                // Connect
                HttpURLConnection connection = (HttpURLConnection)   url.openConnection();
                connection.setRequestMethod("GET");
                // to return in JSON Format
                connection.setRequestProperty("Accept", "application/JSON");
                try (BufferedReader in = new BufferedReader (
                    new InputStreamReader(connection.getInputStream()))) {

                    // inputValues of the JSON
                    String inputLine = in.readLine();
                    inputLine = inputLine.replaceAll("\\[", "");
                    inputLine = inputLine.replaceAll("\\]", "");
                    String[] objArray = inputLine.split("\\},");

                    // Loops though the array and puts it into a Map
                    for (String anObjArray : objArray) {
                        Map<String, String> tempMap = splitJSONString(anObjArray);
                        listOfEntities.add(tempMap);
                    }  
                }  
            }
            catch(IOException e)
            {
                System.err.println(e.toString());
            }
         
           
            return listOfEntities;
    }

    // Accepts the ID and the table to use in the method
    public static Map<String,String> readSingle(int id, DatabaseTable table)
    {
        // creation of URL with unique values;
        String urlToGet = URI + DBTableToString(table) + "/" + Integer.toString(id);
        // initilatisation of map which stores keys and values
        Map<String,String> map = new HashMap<>();
 
        try{
            // Create URL Class
            URL url = new URL(urlToGet);
            // connect to the url
            HttpURLConnection connection = (HttpURLConnection)   url.openConnection();
            // A GET method is created
            connection.setRequestMethod("GET");
            // to return in JSON Format
            connection.setRequestProperty("Accept", "application/JSON");
           
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {

                // inputValues of the JSON
                String inputLine = in.readLine();

                // split up the string into a map
                map = splitJSONString(inputLine);
            }
            // disconnect from the URL
            connection.disconnect();

            // return the map
            return map;
        } catch (IOException e) {
        System.err.println(e.toString());
      }
       return map;
    }

    public static List<Map<String,String>> readAmount(DatabaseTable table, Integer amount, Integer lastID)
    {
        List<Map<String,String>> listOfEntities = new ArrayList<>();
        String urlToGet = URI + "functions/get" +  DBTableToString(table) + "amount/" + amount.toString() + "/" + lastID.toString();
        try {
            URL url = new URL(urlToGet);
            // Connect
            HttpURLConnection connection = (HttpURLConnection)   url.openConnection();
            connection.setRequestMethod("GET");
            // to return in JSON Format
            connection.setRequestProperty("Accept", "application/JSON");
            try (BufferedReader in = new BufferedReader (
                    new InputStreamReader(connection.getInputStream()))) {

                // inputValues of the JSON
                String inputLine = in.readLine();
                inputLine = inputLine.replaceAll("\\[", "");
                inputLine = inputLine.replaceAll("\\]", "");
                String[] objArray = inputLine.split("\\},");

                // Loops though the array and puts it into a Map
                for (String anObjArray : objArray) {
                    Map<String, String> tempMap = splitJSONString(anObjArray);
                    listOfEntities.add(tempMap);
                }
            }
        }
        catch(IOException e)
        {
            System.err.println(e.toString());
        }
        return listOfEntities;
    }

    public static List<Map<String,String>> readObjectsReviews(DatabaseTable table, Integer objectID)
    {

        if (table != DatabaseTable.ARTIST && table != DatabaseTable.PARENT_EVENT && table != DatabaseTable.VENUE)
            throw new IllegalArgumentException("Table must be ARTIST/PARENT_EVENT/VENUE.");


        List<Map<String,String>> listOfEntities = new ArrayList<>();
        String urlToGet = URI + "functions/get" +  DBTableToString(table) + "reveiws/" + objectID.toString();
        try {
            URL url = new URL(urlToGet);
            // Connect
            HttpURLConnection connection = (HttpURLConnection)   url.openConnection();
            connection.setRequestMethod("GET");
            // to return in JSON Format
            connection.setRequestProperty("Accept", "application/JSON");
            try (BufferedReader in = new BufferedReader (
                    new InputStreamReader(connection.getInputStream()))) {

                // inputValues of the JSON
                String inputLine = in.readLine();
                inputLine = inputLine.replaceAll("\\[", "");
                inputLine = inputLine.replaceAll("\\]", "");
                String[] objArray = inputLine.split("\\},");

                // Loops though the array and puts it into a Map
                for (String anObjArray : objArray) {
                    Map<String, String> tempMap = splitJSONString(anObjArray);
                    listOfEntities.add(tempMap);
                }
            }
        }
        catch(IOException e)
        {
            System.err.println(e.toString());
        }
        return listOfEntities;
    }

    public static List<Map<String,String>> getChildEventsViaParent(Integer parentEventID)
    {
        List<Map<String,String>> listOfEntities = new ArrayList<>();
        String urlToGet = URI + "functions/getChild_EventsViaParent/" + parentEventID.toString();
        try {
            URL url = new URL(urlToGet);
            // Connect
            HttpURLConnection connection = (HttpURLConnection)   url.openConnection();
            connection.setRequestMethod("GET");
            // to return in JSON Format
            connection.setRequestProperty("Accept", "application/JSON");
            try (BufferedReader in = new BufferedReader (
                    new InputStreamReader(connection.getInputStream()))) {

                // inputValues of the JSON
                String inputLine = in.readLine();
                inputLine = inputLine.replaceAll("\\[", "");
                inputLine = inputLine.replaceAll("\\]", "");

                if(!inputLine.isEmpty()) {
                    String[] objArray = inputLine.split("\\},");
                    // Loops though the array and puts it into a Map
                    for (String anObjArray : objArray) {
                        Map<String, String> tempMap = splitJSONString(anObjArray);
                        listOfEntities.add(tempMap);
                    }
                }
            }
        }
        catch(IOException e)
        {
            System.err.println(e.toString());
        }
        return listOfEntities;
    }
   
    private static Map<String,String> splitJSONString(String input)
    {
        // Initializes of map which stores keys and values
        Map<String,String> map = new HashMap<>();
        // split up the string into the different columns
        String[] splitArray = input.split(",");
        // remove the beginning brace
        splitArray[0] = splitArray[0].replaceAll("\\{", "");
        // remove the end brace
        splitArray[splitArray.length -1] = splitArray[splitArray.length -1 ].replaceAll("\\}", "");

        for (String aSplitArray : splitArray) {
            String temp = aSplitArray.replaceAll("\"", ""); // removes quote marks from json string
            String[] splitString = temp.split(":", 2);     // splits each strig into key and value
            map.put(splitString[0], splitString[1]); // place values into the Map
        }

        return map;
    }
    
    
    public static String createJsonString(Map<String,String> map)
    {
        String strToReturn = "{";
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        boolean isAnInteger;
        for(int i = 0; i < keys.length; i++)
        {
            
           String endValue = ",";
           String tempLine ="\""+ keys[i] + "\""; 

            if(i == keys.length - 1)
                endValue = "}";

            try{
                Integer.parseInt(values[i].toString());
                isAnInteger = true;

                }
            catch(Exception ex){isAnInteger = false;}
            if(!isAnInteger)
                tempLine += ":" + "\"" + values[i] + "\"" + endValue;
            else
                tempLine+= ":" + values[i] + endValue;

            strToReturn += tempLine;
        }
        return strToReturn;
    }
}
