package com.pet.att.pickapet.HTTP;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class HttpRequestsURLConnection {
    private static final String TAG = "HttpClient";
    private static int EMPTY_CONTENT = 204;


    public static String SendHttpGet(String URL) {
        JSONObject jsonObjRecv=null;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGetRequest = new HttpGet(URL);

            httpGetRequest.setHeader("Accept", "application/json");
            httpGetRequest.setHeader("Content-type", "application/json");
            httpGetRequest.setHeader("Accept-Encoding", "gzip");

            long t = System.currentTimeMillis();
            HttpResponse response = (HttpResponse) httpclient.execute(httpGetRequest);
            Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    instream = new GZIPInputStream(instream);
                }

                String resultString= convertStreamToString(instream);
                instream.close();
//                resultString = resultString.substring(1,resultString.length()-2);

//                JSONObject jsonObjRecv = new JSONObject(resultString);
//                Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

                return resultString;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public static String SendHttpPost(String URL, String jsonObjSend) {

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPostRequest = new HttpPost(URL);

            StringEntity se;
            se = new StringEntity(jsonObjSend.toString());

            httpPostRequest.setEntity(se);
            httpPostRequest.setHeader("Accept", "*/*");
            httpPostRequest.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPostRequest.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPostRequest.setHeader("Accept-Language","he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7");

            long t = System.currentTimeMillis();
            HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
            Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    instream = new GZIPInputStream(instream);
                }

                String resultString= convertStreamToString(instream);
                instream.close();
//                resultString = resultString.substring(0,resultString.length()-1);
//
//                JSONObject jsonObjRecv = new JSONObject(resultString);
//                Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

                return resultString;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String SendHttpPut(String URL, String jsonObjSend) {

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPutRequest = new HttpPut(URL);

            StringEntity se;
            se = new StringEntity(jsonObjSend, HTTP.UTF_8);

            httpPutRequest.setEntity(se);
            httpPutRequest.setHeader("Accept", "*/*");
            httpPutRequest.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httpPutRequest.setHeader("Accept-Encoding", "gzip, deflate, br");
//            httpPutRequest.setHeader("Accept-Charset", "UTF-8");
            httpPutRequest.setHeader("Accept-Language","he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7");

            long t = System.currentTimeMillis();
            HttpResponse response = (HttpResponse) httpclient.execute(httpPutRequest);
            Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    instream = new GZIPInputStream(instream);
                }

                String resultString= convertStreamToString(instream);
                instream.close();
//                resultString = resultString.substring(0,resultString.length()-1);
//
//                JSONObject jsonObjRecv = new JSONObject(resultString);
//                Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

                return resultString;
            }
            if (response.getStatusLine().getStatusCode()==EMPTY_CONTENT){
                return "Empty";
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG,e.toString());

        }
        return null;
    }




    public static String SendObjectsHttpPut(String URL, Object... objects) {

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPutRequest = new HttpPut(URL);

            File compressedImage = (File) objects[0];
            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(compressedImage), -1);
            reqEntity.setContentType("binary/octet-stream");
            httpPutRequest.setEntity(reqEntity);


            StringEntity se;
            se = new StringEntity((String)objects[2], HTTP.UTF_8);

            httpPutRequest.setEntity(se);
            httpPutRequest.setHeader("Accept", "*/*");
            httpPutRequest.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httpPutRequest.setHeader("Accept-Encoding", "gzip, deflate, br");
//            httpPutRequest.setHeader("Accept-Charset", "UTF-8");
            httpPutRequest.setHeader("Accept-Language","he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7");

            long t = System.currentTimeMillis();
            HttpResponse response = (HttpResponse) httpclient.execute(httpPutRequest);
            Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    instream = new GZIPInputStream(instream);
                }

                String resultString= convertStreamToString(instream);
                instream.close();
//                resultString = resultString.substring(0,resultString.length()-1);
//
//                JSONObject jsonObjRecv = new JSONObject(resultString);
//                Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

                return resultString;
            }
            if (response.getStatusLine().getStatusCode()==EMPTY_CONTENT){
                return "Empty";
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG,e.toString());

        }
        return null;
    }

























    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                Log.e("JSON", ""+line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}