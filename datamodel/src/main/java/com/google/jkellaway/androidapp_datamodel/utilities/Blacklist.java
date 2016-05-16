/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Blacklist is used to check if input strings contain curse words or unfavorable words.
 * This is also an example of the Servant Pattern.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public class Blacklist {

    private static List<String> blacklist;

    private Blacklist() {
    }

    private static List<String> getListFromFile() {
        try (Scanner r = new Scanner(new File("blacklist.txt"))) {
            List<String> l = new ArrayList();


            while (r.hasNextLine()) {
                l.add(r.nextLine());
            }

            r.close();

            return l;

        } catch (FileNotFoundException ex) {
        }

        return new ArrayList();
    }


    /**
     * Checks an string or paragraph input against a list of bad words.
     *
     * @param input A word, sentence, or paragraph.
     * @return True if a bad word is found.
     */
    public static Boolean contains(String input) {
        if (blacklist == null) {
            blacklist = getListFromFile();
        }

        for (String word : blacklist) {
            if (input.contains(word)) {
                return true;
            }
        }

//        // Split the inputted string into seperate words.
//        String[] words = input.split(" ");
//        
//        // Iterate through the words and remove possible extensions of
//        // blacklisted words.
//        for (String word : words) {
//            if (word.endsWith("s")) {
//                word = word.substring(0, word.length() - 2);
//            }
//            if (word.endsWith("ing")) {
//                word = word.substring(0, word.length() - 4);
//            }
//            
//            // If the word is in the list of bad words, return true.
//            if (blacklist.contains(word)) {
//                return true;
//            }
//        }
        return false;
    }
}
