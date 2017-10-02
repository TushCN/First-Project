package com.example.cryptx.networking;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by CryptX on 01-10-2017.
 */

public class CourseAsyncTask extends AsyncTask<String,Void,ArrayList<Courses>> {

    public CoursesDownloadListener coursesDownloadListener;

    public CourseAsyncTask(CoursesDownloadListener listener) {
        coursesDownloadListener = listener;
    }

    @Override
    protected ArrayList<Courses> doInBackground(String... strings) { //DIFFERENT THREAD
        //CODE

        Log.i("Course AsyncTask","Inside Async Task ");
        String urlString = strings[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            Log.i("Courses Response:","Connection Started");
            urlConnection.connect();
            Log.i("Courses Response:","Connection Complete");
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String response = "";
            while (scanner.hasNext()){
                response += scanner.next();
            }
            Log.i("Courses Response:",response);
            ArrayList<Courses> courses = parseCourses(response);
            return  courses;



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<Courses> parseCourses(String response) throws JSONException{
        ArrayList<Courses> courses = null;
        JSONObject rootJSONArray = new JSONArray(response);
        JSONObject dataObject = rootJSONArray.getJSONObject("data");
        JSONArray coursesJSONArray = dataObject.getJSONArray("courses");
        if(coursesJSONArray!=null){
            courses = new ArrayList<>();
            for(int i = 0;i<coursesJSONArray.length();i++){
                JSONObject courseObject = coursesJSONArray.getJSONObject(i);
                int id = courseObject.getInt("id");
                String email = courseObject.getString("email");
                String name = courseObject.getString("name");
                String body = courseObject.getString("body");
                Courses course = new Courses(id,name,email,body);
                courses.add(course);
            }
        }
        return courses;
    }

    @Override
    protected void onPreExecute() { //MAIN THREAD
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Courses> courses) {//MAIN THREAD

        Log.i("Courses Array Size",courses.size() +"");
        coursesDownloadListener.onDownload(courses);
    }



    public static interface CoursesDownloadListener{

        void onDownload(ArrayList<Courses> courses);

    }

}
