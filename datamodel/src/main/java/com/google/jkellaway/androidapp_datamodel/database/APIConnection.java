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
     * Deletes a record in the database
     *
     * @param id    the if of the row that is to be deleted
     * @param table the table that the record is to be deleted from
     * @return boolean value dependant on whether the record was deleted or not
     */
// Allows the application to delete a record in the database
    public static boolean delete(int id, DatabaseTable table) {
        boolean ableToDelete;   // flag to make sure that the deletion was valid
        String urlToDelete = URI + DBTableToString(table) + "/" + Integer.toString(id); // creation of the url string
        try {
            URL url = new URL(urlToDelete);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // create and open the connection

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("DELETE");  // making sure that the request is a DELETE
            connection.connect();// open the connection and delete the record
            ableToDelete = true;
            connection.disconnect();    // close the connection to prevent heavy resource loas on the sessions on the db

        } catch (IOException ex) {
            System.err.println(ex.toString());
            ableToDelete = false;   // if unable to delete the record return false
        }
        return ableToDelete;
    }

    /**
     * Update the record in the database.
     *
     * @param id        The primary ID of the row that you wish to update
     * @param mapToEdit the updated map of the object that you want to update
     * @param table     the table that the object you wish to update is held in
     * @return the updated map that the changes has been made to
     * @throws IOException if unable to update the record
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
        connection.setRequestProperty("Content-Type", "application/json"); // set the data to be sent as a JSON string
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT"); // Set http method to a PUT Request
        connection.connect();   // open the connection

        //WRITE
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));   // create a new writer
        writer.write(createJsonString(mapToEdit));  // convert the map that needs to be updates to a JSON string
        writer.close(); // write the json string to the url
        os.close();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {  // read back the updated record's map

            // inputValues of the JSON
            String inputLine = in.readLine();

            // split up the string into a map
            map = splitJSONString(inputLine);
        }
        connection.disconnect();// disconnect the connection and return the updated map
        return map;

    }

    /**
     * Add a new record to the database.
     *
     * @param mapToAdd the map of the new record that is being added
     * @param table    the table that the new record will be added to
     * @return the new map that is created - will have the correct primary ID
     * @throws IOException throws if there is an error in adding to the database
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
     * @param string the string of the function you wish to use
     * @return the created string of the new URL Connection
     * @throws IOException throws if there are errors in the connection
     */
    public static List<Map<String, String>> getSTRING(String string) throws IOException {
        return Connection(URI + "functions/get" + string);
    }

    /**
     * Create contract boolean.
     *
     * @param artistID     the primary ID of the artist that will be added to an events lineup
     * @param childEventID the primary ID of the child event that the contract will be made for
     * @return Boolean dependant on whether the contract was created or not
     * @throws IOException will throw if a connection error occurs.
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

    /**
     *
     * @param input The string that you wish to split up into a map
     * @return a map of the keys and values form the JSON string that the api receives
     */

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
     * Create json string from the map.
     *
     * @param map contains an object that has been split up into VariableName and Contents
     * @return a string of the JSON values ready to be dealt with by the API
     */
    public static String createJsonString(Map<String, String> map) {
        String strToReturn = "{"; // starts the string with the parenthesises
        Object[] keys = map.keySet().toArray(); // set an array of the keys in the map
        Object[] values = map.values().toArray(); // sets an array of the values in the map
        boolean isAnInteger; // flag to determine whether to wrap "" around the value
        for (int i = 0; i < keys.length; i++) { // for loop for all values

            String endValue = ",";
            String tempLine = "\"" + keys[i] + "\"";    // place quotes around the keys

            if (i == keys.length - 1)
                endValue = "}"; // if its the end value append a parenthesises

            try {
                Integer.parseInt(values[i].toString()); // convert string to a integer and if it cant it isnt an integer
                isAnInteger = true;

            } catch (Exception ex) {
                isAnInteger = false;
            }
            if (!isAnInteger)
                tempLine += ":" + "\"" + values[i] + "\"" + endValue;   // if not a number wrap around commas
            else
                tempLine += ":" + values[i] + endValue;

            strToReturn += tempLine;    // append the current value and key to the end of the string
        }
        return strToReturn; // return the string of the map as a JSON String
    }
}
