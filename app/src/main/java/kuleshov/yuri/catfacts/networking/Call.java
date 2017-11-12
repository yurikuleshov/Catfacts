package kuleshov.yuri.catfacts.networking;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Call {
    public static final int OK = 0;
    public static final int NETWORK_PROBLEMS = 1;
    public static final int RESPONSE_ERROR = 2;
    private int mCallState = OK;
    private RequestBuilder mBuilder;

    public Call(RequestBuilder builder) {
        mBuilder = builder;
    }

    public int getCallState() {
        return mCallState;
    }

    public JSONObject Do() {
        mCallState = OK;

        try {
            HttpURLConnection post = mBuilder.makeRequest();
            if (null == post) {  // no network available
                return null;
            }
            int HttpResult = post.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(post.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                return new JSONObject(sb.toString());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}