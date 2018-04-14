package com.pet.att.pickapet.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
        ArrayList<BasicNameValuePair> nameValuePairs=null;

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPutRequest = new HttpPut(URL);

//            File compressedImage = (File) objects[1];
//            InputStreamEntity reqEntity = new InputStreamEntity(
//                    new FileInputStream(compressedImage), -1);
//            reqEntity.setContentType("binary/octet-stream");
//            httpPutRequest.setEntity(reqEntity);

//
//            Bitmap bm = BitmapFactory.decodeFile(objects[1].toString());
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, output); //bm is the bitmap object
//            byte[] bytes = output.toByteArray();
//
//            String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
//            nameValuePairs.add(new BasicNameValuePair("pic", base64Image));
//            nameValuePairs.add(new BasicNameValuePair("aid",(String)objects[0]));
//            StringEntity stringEntity = new StringEntity("pic="+base64Image, HTTP.UTF_8);
//            httpPutRequest.setEntity(stringEntity);

            StringEntity se;
            se = new StringEntity((String)objects[0], HTTP.UTF_8);
            httpPutRequest.setEntity(se);
//            httpPutRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpPutRequest.setHeader("Accept", "text/javascript, */*; q=0.01");
            httpPutRequest.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httpPutRequest.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPutRequest.setHeader("X-Requested-With", "XMLHttpRequest");
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



//
//    public String uploadFile(String Url,Object... objects) {
//
//        int serverResponseCode = 0;
//        String fileName = (String) objects[0];
//
//        HttpURLConnection conn = null;
//        DataOutputStream dos = null;
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "*****";
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
//        File sourceFile = new File((String) objects[0]);
//
//        if (!sourceFile.isFile()) {
//
//
//            return null;
//
//        }
//        else
//        {
//            try {
//
//                // open a URL connection to the Servlet
//                FileInputStream fileInputStream = new FileInputStream(sourceFile);
//                URL url = new URL(Url);
//
//                // Open a HTTP  connection to  the URL
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setDoInput(true); // Allow Inputs
//                conn.setDoOutput(true); // Allow Outputs
//                conn.setUseCaches(false); // Don't use a Cached Copy
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                conn.setRequestProperty("uploaded_file", fileName);
//
//                dos = new DataOutputStream(conn.getOutputStream());
//
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; filename="
//                                + fileName + "" + lineEnd);
//
//                        dos.writeBytes(lineEnd);
//
//                // create a buffer of  maximum size
//                bytesAvailable = fileInputStream.available();
//
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                buffer = new byte[bufferSize];
//
//                // read file and write it into form...
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                while (bytesRead > 0) {
//
//                    dos.write(buffer, 0, bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                }
//
//                // send multipart form data necesssary after file data...
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                // Responses from the server (code and message)
//                serverResponseCode = conn.getResponseCode();
//                String serverResponseMessage = conn.getResponseMessage();
//
//                Log.i("uploadFile", "HTTP Response is : "
//                        + serverResponseMessage + ": " + serverResponseCode);
//
//                if(serverResponseCode == 200){
//                    return serverResponseMessage;
//
//                }
//
//                //close the streams //
//                fileInputStream.close();
//                dos.flush();
//                dos.close();
//
//            } catch (MalformedURLException ex) {
//                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
//            } catch (Exception e) {
//                    e.printStackTrace();
//            }
//            return null;
//
//        } // End else block
//    }






















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