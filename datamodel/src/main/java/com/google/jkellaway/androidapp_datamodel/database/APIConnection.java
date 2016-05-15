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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The type Api connection.
 *
 * @author 10512691
 */
final class APIConnection {

    // URL of the web API
    private static String URI = "http://xserve.uopnet.plymouth.ac.uk/Modules/INTPROJ/PRCS251G/api/";

    // Converts the DatabaseTable Enum value to a string for use in the connection string
    private static String DBTableToString(DatabaseTable table) {
        return table.toString() + "s";
    }

    /**
     * Delete boolean.
     *
     * @param id    the id
     * @param table the table
     * @return the boolean
     */
// Allows the application to delete a record in the database
    public static boolean delete(int id, DatabaseTable table) {
        boolean ableToDelete;
        String urlToDelete = URI + DBTableToString(table) + "/" + Integer.toString(id);
        try {
            URL url = new URL(urlToDelete);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("DELETE");
            connection.connect();
            ableToDelete = true;
            connection.disconnect();

        } catch (IOException ex) {
            System.err.println(ex.toString());
            ableToDelete = false;
        }
        return ableToDelete;
    }

    /**
     * Update map.
     *
     * @param id        the id
     * @param mapToEdit the map to edit
     * @param table     the table
     * @return the map
     * @throws IOException the io exception
     */
// Allows the application to
    public static Map<String, String> update(int id, Map<String, String> mapToEdit, DatabaseTable table) throws IOException {

        Map<String, String> map;
        // URL of where to add to the table.
        String urlToPost = URI + DBTableToString(table) + "/" + Integer.toString(id);
        URL url = null;
        try {
            url = new URL(urlToPost);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            // inputValues of the JSON
            String inputLine = in.readLine();

            // split up the string into a map
            map = splitJSONString(inputLine);
        }
        connection.disconnect();
        return map;

    }

    /**
     * Add map.
     *
     * @param mapToAdd the map to add
     * @param table    the table
     * @return the map
     * @throws IOException the io exception
     */
    public static Map<String, String> add(Map<String, String> mapToAdd, DatabaseTable table) throws IOException {
        int httpCode;
        Map<String, String> map;
        String urlToPost = URI + DBTableToString(table);  // URL of where to add to the table.
        URL url = null;
        try {
            url = new URL(urlToPost);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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

        if (connection.getResponseCode() != 201)
            throw new IOException("Request violated database constraint.");

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            // inputValues of the JSON
            String inputLine = in.readLine();

            // split up the string into a map
            map = splitJSONString(inputLine);
        }
        connection.disconnect();

        return map;
    }

    /**
     * Read all list.
     *
     * @param table the table
     * @return the list
     * @throws IOException the io exception
     */
    public static List<Map<String, String>> readAll(DatabaseTable table) throws IOException {
        return Connection(URI + DBTableToString(table));
    }

    /**
     * Read single map.
     *
     * @param id    the id
     * @param table the table
     * @return the map
     */
// Accepts the ID and the table to use in the method
    public static Map<String, String> readSingle(int id, DatabaseTable table) {
        // creation of URL with unique values;
        String urlToGet = URI + DBTableToString(table) + "/" + Integer.toString(id);
        // initialisation of map which stores keys and values
        Map<String, String> map = new HashMap<>();

        try {
            // Create URL Class
            URL url = new URL(urlToGet);
            // connect to the url
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

    /**
     * Read amount list.
     *
     * @param table  the table
     * @param amount the amount
     * @param lastID the last id
     * @return the list
     * @throws IOException the io exception
     */
    public static List<Map<String, String>> readAmount(DatabaseTable table, Integer amount, Integer lastID) throws IOException {
        return Connection(URI + "functions/get" + DBTableToString(table) + "amount/" + amount.toString() + "/" + lastID.toString());
    }

    /**
     * Read objects reviews list.
     *
     * @param table    the table
     * @param objectID the object id
     * @return the list
     * @throws IOException the io exception
     */
    public static List<Map<String, String>> readObjectsReviews(DatabaseTable table, Integer objectID) throws IOException {
        if (table != DatabaseTable.ARTIST && table != DatabaseTable.PARENT_EVENT && table != DatabaseTable.VENUE)
            throw new IllegalArgumentException("Table must be ARTIST/PARENT_EVENT/VENUE.");

        return Connection(URI + "functions/getReviewsOf" + table.toString() + "/" + objectID.toString());
    }

    /**
     * Search list.
     *
     * @param searchText     the search text
     * @param amountToSearch the amount to search
     * @param table          the table
     * @return the list
     * @throws IOException the io exception
     */
    public static List<Map<String, String>> search(String searchText, Integer amountToSearch, DatabaseTable table) throws IOException {
        return Connection(URI + "functions/search" + DBTableToString(table) + "/" + searchText + "/" + amountToSearch.toString());
    }

    /**
     * Compare password list.
     *
     * @param email    the email
     * @param password the password
     * @param table    the table
     * @return the list
     * @throws IOException the io exception
     */
    public static List<Map<String, String>> comparePassword(String email, String password, DatabaseTable table) throws IOException {
        return Connection(URI + "functions/compare" + table.toString() + "Passwords/" + email + "/" + password);
    }

    /**
     * Gets objects of object.
     *
     * @param artistID     the artist id
     * @param objectsToGet the objects to get
     * @param objectToUse  the object to use
     * @return the objects of object
     * @throws IOException the io exception
     */
    public static List<Map<String, String>> getObjectsOfObject(Integer artistID, DatabaseTable objectsToGet, DatabaseTable objectToUse) throws IOException {
        return Connection(URI + "functions/get" + DBTableToString(objectsToGet) + "Of" + objectToUse.toString() + "/" + artistID.toString());
    }

    /**
     * Read ticket amount list.
     *
     * @param orderID      the order id
     * @param amountToRead the amount to read
     * @param lowestID     the lowest id
     * @return the list
     * @throws IOException the io exception
     */
    public static List<Map<String, String>> readTicketAmount(Integer orderID, Integer amountToRead, Integer lowestID) throws IOException {
        return Connection(URI + "functions/getBookingsOfCustomerAmount/" + orderID.toString() + "/" + amountToRead.toString() + "/" + lowestID.toString());
    }

    /**
     * Gets string.
     *
     * @param string the string
     * @return the string
     * @throws IOException the io exception
     */
    public static List<Map<String, String>> getSTRING(String string) throws IOException {
        return Connection(URI + "functions/get" + string);
    }

    /**
     * Create contract boolean.
     *
     * @param artistID     the artist id
     * @param childEventID the child event id
     * @return the boolean
     * @throws IOException the io exception
     */
    public static Boolean createContract(Integer artistID, Integer childEventID) throws IOException {
        URL url;
        boolean result;
        try {
            url = new URL(URI + "functions/createContract/" + artistID.toString() + "/" + childEventID.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new MalformedURLException("Error With URL");
        }
        // Connect
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.connect();


        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write("{}");
        writer.close();
        os.close();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            result = Boolean.parseBoolean(in.readLine());
            System.out.println(result);
        }
        connection.disconnect();
        return result;
    }

    private static List<Map<String, String>> Connection(String urlText) throws IOException {

        URL url;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new MalformedURLException("Error With URL");
        }
        // Connect
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // to return in JSON Format
        connection.setRequestProperty("Accept", "application/JSON");
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            List<Map<String, String>> listOfEntities = JSONBreakDown(in.readLine());
            connection.disconnect();
            return listOfEntities;
        }
    }

    private static List<Map<String, String>> JSONBreakDown(String JSONString) {

        JSONString = JSONString.replaceAll("\\[", "").replaceAll("\\]", "");

        final List<Map<String, String>> listOfEntities = new ArrayList<>();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<Map<String, String>>> futures = new LinkedList<>();

        if (JSONString.equals(null)) {
            return null;
        } else if (!JSONString.isEmpty()) {
            String[] objArray = JSONString.split("\\},");
            // Loops though the array and puts it into a Map
            for (final String anObjArray : objArray) {
                Callable<Map<String, String>> callable = new Callable<Map<String, String>>() {
                    @Override
                    public Map<String, String> call() throws Exception {
                        return splitJSONString(anObjArray);
                    }
                };
                futures.add(service.submit(callable));
            }

            for (Future<Map<String, String>> future : futures) {
                try {
                    listOfEntities.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println(e.toString());
                }
            }
        }
        return listOfEntities;
    }

    private static Map<String, String> splitJSONString(String input) {
        // Initializes of map which stores keys and values
        Map<String, String> map = new HashMap<>();
        // split up the string into the different columns
        String[] splitArray = input.split(",\"");

        for (int i = 1; i < splitArray.length; i++) {
            splitArray[i] = "\"" + splitArray[i];
        }

        // remove the beginning brace
        splitArray[0] = splitArray[0].replaceAll("\\{", "");
        // remove the end brace
        splitArray[splitArray.length - 1] = splitArray[splitArray.length - 1].replaceAll("\\}", "");

        for (String aSplitArray : splitArray) {
            String temp = aSplitArray.replaceAll("\"", ""); // removes quote marks from json string
            String[] splitString = temp.split(":", 2);     // splits each string into key and value
            map.put(splitString[0], splitString[1]); // place values into the Map
        }
        return map;
    }

    /**
     * Create json string string.
     *
     * @param map the map
     * @return the string
     */
    public static String createJsonString(Map<String, String> map) {
        String strToReturn = "{";
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        boolean isAnInteger;
        for (int i = 0; i < keys.length; i++) {

            String endValue = ",";
            String tempLine = "\"" + keys[i] + "\"";

            if (i == keys.length - 1)
                endValue = "}";

            try {
                Integer.parseInt(values[i].toString());
                isAnInteger = true;

            } catch (Exception ex) {
                isAnInteger = false;
            }
            if (!isAnInteger)
                tempLine += ":" + "\"" + values[i] + "\"" + endValue;
            else
                tempLine += ":" + values[i] + endValue;

            strToReturn += tempLine;
        }
        return strToReturn;
    }
}
