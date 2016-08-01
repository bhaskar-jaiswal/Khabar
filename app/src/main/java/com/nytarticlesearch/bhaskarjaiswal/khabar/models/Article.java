package com.nytarticlesearch.bhaskarjaiswal.khabar.models;

import com.nytarticlesearch.bhaskarjaiswal.khabar.constants.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by bjaiswal on 7/26/2016.
 */
@Parcel
public class Article {
    public String webUrl;
    public String headline;
    public String thumbNail;
    public String snippet;
    public String imageWidth;
    public String imageHeight;

    public Article(){}

    public Article(JSONObject jsonObject){
        try{
//            setWebUrl(jsonObject.getString("web-url"));
//            setHeadline(jsonObject.getJSONObject("headline").getString("main"));

            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.snippet = jsonObject.getString("snippet").trim();

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            setThumbNail("");
            for(int i=0;i<multimedia.length();i++){
                JSONObject legacy = multimedia.getJSONObject(i).getJSONObject("legacy");
                if (legacy.has("xlarge")){
                    setThumbNail(Config.NYTIMES_URL+legacy.getString("xlarge"));
                    setImageWidth(legacy.getString("xlargewidth"));
                    setImageHeight(legacy.getString("xlargeheight"));
                    break;
                }
            }

           /* if(multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                setThumbNail(Config.NYTIMES_URL+multimediaJson.getString("url"));
            }else*/

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> results = new ArrayList<>();
        for(int x=0;x<array.length();x++){
            try {
                results.add(new Article(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }
}
