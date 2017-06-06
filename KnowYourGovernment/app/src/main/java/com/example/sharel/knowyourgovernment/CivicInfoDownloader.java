package com.example.sharel.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Sharel on 4/6/2017.
 */

public class CivicInfoDownloader extends AsyncTask<String,Void,ArrayList<Official>> {


    private String dataURL ="https://www.googleapis.com/civicinfo/v2/representatives" ;
    private String key="AIzaSyA-TMo30LohRTZ0zz6P4MJ3wzd4L6swIbY";
    private static final String TAG = "CivicInfoDownloader";
    private  String zipcode;
    String city, state,zip;
    private String name;
    private String office;
    private String party;
    private String phone;
    private  String email;
    private String website;
    private String photo,add="";
    String postal;

   private  ArrayList<Official> officalActivityArrayList;
    private  MainActivity mainActivity;
   private String  gplus, fb,twitter, youtube;

    public CivicInfoDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.officalActivityArrayList = new ArrayList<>();
    }

    @Override
    protected ArrayList<Official> doInBackground(String... params) {
        zipcode = params[0] ;

        Uri dataUri = Uri.parse(dataURL).buildUpon()
                .appendQueryParameter("key", key)
                .appendQueryParameter("address", zipcode).build();

        String urlToUse = dataUri.toString();

        Log.d(TAG, "doInBackground: " + urlToUse);

        HttpURLConnection conn;
        StringBuilder sb = new StringBuilder();
        try {

            //dataurl = createdataurl(symbol)
            URL url = new URL(urlToUse);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //added in class
            conn.connect();

            //new String builder obj

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            //call the parser
            DataParser dataParser = new DataParser();
            dataParser.parseJSON(sb.toString().replace("\n//",""));
            String s = sb.toString().replace("\n//","");



            try {
                JSONObject object= new JSONObject(s);
                JSONObject jObjAddress= new JSONObject(object.getString("normalizedInput"));
                city = jObjAddress.get("city").toString();
                state = jObjAddress.get("state").toString();
                zip = jObjAddress.get("zip").toString();
                postal = city + ", " + state + " " + zip;
                JSONArray jObjOffices = new JSONArray(object.getString("offices"));
                JSONArray jObjOfficials = new JSONArray(object.getString("officials"));

                Log.d(TAG, "doInBackground: "+postal);
                  for (int i = 0; i < jObjOffices.length(); i++) {
                     JSONObject jo =  (JSONObject) jObjOffices.get(i);
                      office = jo.getString("name");
                      JSONArray jArr = jo.getJSONArray("officialIndices");

                    for(int j=0;j<jArr.length();j++){
                        int val = Integer.parseInt(jArr.getString(j));
                       JSONObject tempArray = (JSONObject) jObjOfficials.get(val);

                        Iterator<String> keysIt = tempArray.keys();
                        while (keysIt.hasNext())
                        {

                            String keyStr = keysIt.next();
                            if( keyStr.equals("name")){
                                name =  tempArray.getString("name");
                            }
                           else if( keyStr.equals("party")){
                                party = tempArray.getString("party");
                            }
                            else if( keyStr.equals("photoUrl")){
                              photo = tempArray.getString("photoUrl");
                            }
                            else if( keyStr.equals("phones")) {
                                JSONArray parr = (JSONArray) tempArray.get("phones");
                                phone = parr.getString(0);
                            }
                            else if( keyStr.equals("urls")) {
                                JSONArray warr = (JSONArray) tempArray.get("urls");
                                website = warr.getString(0);
                            }
                           else  if(keyStr.equals("address")){
                                JSONArray j1 =  (JSONArray)tempArray.get("address");
                                JSONObject jadd = (JSONObject) j1.get(0);
                                Iterator<String> keysIterator = jadd.keys();
                                while (keysIterator.hasNext())
                                {
                                    String keyStr1 = keysIterator.next();
                                    add = add + jadd.getString(keyStr1);
                                    add = add+", ";
                                }
                                add = add.substring(0,add.length()-2);
                                Log.d(TAG, "doInBackground: "+add);
                            }
                            else if(keyStr.equals("channels")){

                                JSONArray jObj1 = (JSONArray) tempArray.get("channels");
                                for (int m=0;m<jObj1.length();m++){
                                    JSONObject jchannel = (JSONObject) jObj1.get(m);

                                    if(jchannel.getString("type").equals("GooglePlus")){
                                        gplus = jchannel.getString("id");
                                    }
                                    else if(jchannel.getString("type").equals("YouTube")){
                                        youtube = jchannel.getString("id");
                                    }
                                    else if(jchannel.getString("type").equals("Facebook")){
                                        fb = jchannel.getString("id");
                                    }
                                    else if(jchannel.getString("type").equals("Twitter")){
                                        twitter = jchannel.getString("id");
                                    }
                                }
                            }
                        }
                        //feed data
                        officalActivityArrayList.add(new Official( name,  office,  party,add,  phone,  website, photo,gplus,fb,twitter,youtube));
                       //reset the values to feed new data
                        phone="";
                        party="Unknown";
                        website="";
                        add="";
                        gplus=null;
                        youtube=null;
                        fb=null;
                        twitter=null;
                        photo="";

                        Log.d(TAG, "doInBackground: ");
                    }
            }

            } catch (Exception e) {
                Log.d(TAG, "parseJSON: " + e.getMessage());
                e.printStackTrace();

            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        }
        catch (FileNotFoundException e){
            Log.e(TAG, "doInBackground: FileNotFoundException ",e );
        }
        catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        finally {
            //if(){
            //conn.disconnect();
            //close reader obj and connection.

            //}
        }
        Log.d(TAG, "doInBackground: " + sb.toString());
        return officalActivityArrayList;
    }


    @Override
    protected void onPostExecute(ArrayList<Official> s) {
        mainActivity.loadData(postal,s);
    }
}
