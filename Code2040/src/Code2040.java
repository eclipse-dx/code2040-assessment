

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import org.json.*;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by Tumi Lengoasa on 30/10/2016.
 */
public class Code2040 {
    public static final String token = "55146322851dae633b519926a8bf1d61";

    public static String postRequest(String link, JSONObject jsonObject) throws IOException{

        URL url = new URL(link);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        OutputStream outputStream = conn.getOutputStream();
        outputStream.flush();
        outputStream.write( jsonObject.toString().getBytes("UTF-8") );

        BufferedReader bReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        System.out.println(url + ": " + conn.getResponseMessage());
        return (bReader.readLine());

    }

    public static void register() throws Exception{

        String link = "http://challenge.code2040.org/api/register";
        String repository = "https://github.com/eclipse-dx/code2040-assessment";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("github", repository);

        try {

            postRequest(link, jsonObject);

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void reverseString() throws Exception{

        String link = "http://challenge.code2040.org/api/reverse";
        String valLink = "http://challenge.code2040.org/api/reverse/validate";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);

        String string = postRequest(link, jsonObject);
        System.out.println("Original string: " + string);
        String reversed = "";

        for(int i = string.length() - 1; i >= 0; i--) {
            reversed += string.charAt(i);
        }
        System.out.println("Reversed string: " + reversed);

        jsonObject.put("string", reversed);
        postRequest(valLink, jsonObject);
    }

    public static void haystack() throws Exception{
        String link = "http://challenge.code2040.org/api/haystack";
        String valLink = "http://challenge.code2040.org/api/haystack/validate";
        int pos = -1;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);

        try {

            JSONObject data = new JSONObject(postRequest(link,jsonObject));

            JSONArray haystack = (JSONArray) data.get("haystack");
            String needle = (String) data.get("needle");

            System.out.println("Haystack: " + haystack);
            System.out.println("Needle:" + needle);

            for (int i = 0; i < haystack.length(); i++) {
                if ( haystack.getString(i).equals(needle) ) {
                    pos = i;
                    System.out.println("Index = " + i);
                }
            }
            JSONObject position = new JSONObject();
            position.put("token", token);
            position.put("needle", pos);

            postRequest(valLink, position);

        } catch (Exception e) {

        }

    }

    public static void prefix() throws Exception{

        String link = "http://challenge.code2040.org/api/prefix";
        String valLink = "http://challenge.code2040.org/api/prefix/validate";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);

        JSONObject data = new JSONObject(postRequest(link,jsonObject));

        String prefix = data.getString("prefix");
        System.out.println("Prefix: " + prefix);
        JSONArray array = data.getJSONArray("array");
        System.out.println("Array: " + array);

        JSONArray newArray = new JSONArray();
        int newPos = 0;

        for (int i = 0; i < array.length(); i++) {

            if ( !array.getString(i).startsWith(prefix) ){
                newArray.put(newPos, array.getString(i));
                newPos++;
            }

        }
        System.out.println("New array: " + newArray);

        JSONObject dictionary = new JSONObject();

        dictionary.put("token", token);
        dictionary.put("array", (JSONArray) newArray);

        System.out.println(dictionary);

        postRequest(valLink, dictionary);

    }

    public static void dating() throws Exception{

        String link = "http://challenge.code2040.org/api/dating";
        String valLink = "http://challenge.code2040.org/api/dating/validate";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        JSONObject input = new JSONObject(postRequest(link, jsonObject));

        String datestamp = input.getString("datestamp");
        System.out.println("Datestamp: " + datestamp);
        int interval = input.getInt("interval");
        System.out.println("Interval: " + interval);

        Calendar date = DatatypeConverter.parseDateTime(datestamp);
        date.add(Calendar.SECOND, interval);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String newDatestamp = dateFormat.format(date.getTime());

        System.out.println(newDatestamp);

        JSONObject output = new JSONObject();
        output.put("token", token);
        output.put("datestamp", newDatestamp);
        postRequest(valLink, output);


    }

    public static void main(String[] args) throws Exception {

        try {
            register();
            reverseString();
            haystack();
            prefix();
            dating();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
