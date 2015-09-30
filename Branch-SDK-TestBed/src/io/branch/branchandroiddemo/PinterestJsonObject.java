package io.branch.branchandroiddemo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sojanpr on 9/28/15.
 */
public class PinterestJsonObject extends JSONObject {
    public  JSONObject object_;
    public  PinterestJsonObject(String str){
        object_ = new JSONObject();
        try {
            object_ =  new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public JSONObject getParamJson(){
        return  object_;
    }

}
