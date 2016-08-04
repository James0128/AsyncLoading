package com.example.yifanyang.asyncloading;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListview;
    private static  String URL= "http://www.imooc.com/api/teacher?type=4&num=30";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListview= (ListView) findViewById(R.id.Lv_main);
        new NewsAsyncTask().execute(URL);
    }
    private List<NewsBean> getJasonData(String URL){
        List<NewsBean> newsBeanList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(URL).openStream());
            JSONObject jsonObject;
            NewsBean newsBean;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray =jsonObject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    newsBean =new NewsBean();
                    newsBean.newsIconUrl=jsonObject.getString("picSmall");
                    newsBean.newsTitle=jsonObject.getString("name");
                    newsBean.newsContent=jsonObject.getString("description");
                    newsBeanList.add(newsBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsBeanList;
    }

    private String readStream(InputStream is){
        InputStreamReader isr;
        String result = "";
        try {
            String line="";
            isr = new InputStreamReader(is,"utf-8");
            BufferedReader br= new BufferedReader(isr);
            try {
                while ((line=br.readLine())!=null){
                    result +=line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }
    class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>>{

        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return getJasonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeen) {
            super.onPostExecute(newsBeen);
            NewsAdapter adapter=new NewsAdapter(MainActivity.this,newsBeen,mListview);
            mListview.setAdapter(adapter);
        }
    }
}
