package com.example.cs230gamewithjavafx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
  * Manage getting the Message of the day from the API.
   * Space for efficiency improvements will exist.
 * @author Joseph Dawson
 * @version 1.0.0
 *

 */
public class MessageOfTheDay {
    /**
     * the url to get the puzzle code from
     */
    private final static String firstUrl = "http://cswebcat.swansea.ac.uk/puzzle";
    /**
     * the url to send the puzzle answer to
     */
    private final static String secondUrl = "http://cswebcat.swansea.ac.uk/message?solution=";

    /**
     * controls getting the message of the day, including the http requests
     * and calling the decryption method
     * @return the message of the day as a String
     */
    public static String getMessageOfTheDay() {
        try {
            URL url = new URL(getFirstUrl());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            //System.out.println("GET Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //System.out.println(response.toString());

                String solution = solvePuzzle(response.toString());

                url = new URL(getSecondUrl() + solution);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                responseCode = httpURLConnection.getResponseCode();
                //System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    String justMessageResponse = response.toString().replaceAll("\\([^)]*\\)", "");
                    //System.out.println(response.toString());
                    return justMessageResponse;
                }

            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * solves the puzzle code provided from the API
     * @param code the encoded message from the website
     * @return the decoded message.
     */
    private static String solvePuzzle (String code) {
        //System.out.println(code);
        char[] ch = code.toCharArray();
        for (int i = 0; i < code.length(); i++) {
            if (i % 2 == 0) {
                // even so shift backwards
                if (ch[i] - (i+1) < 65) {
                    int toDeduct = 65 - (ch[i] - (i+1));
                    ch[i] = (char) (91 - toDeduct);
                } else {
                    ch[i] -= i + 1;
                }

            } else {
                //odd so shift forward
                if (ch[i] + (i+1) > 90) {
                    int toAdd = (ch[i] + (i+1)) - 91;

                    ch[i] = (char) (65 + toAdd);
                } else {
                    ch[i] += i + 1;
                }

            }
        }

        String s = new String(ch);
        //System.out.println(s);
        s = s + "CS-230";
        s = s.length() + s;
        //System.out.println(s);

        return s;
    }


    /**
     * @return the firstURL needed
     */
    private static String getFirstUrl() {
        return firstUrl;
    }

    /**
     * @return the second url needed
     */
    private static String getSecondUrl() {
        return secondUrl;
    }
}

