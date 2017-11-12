package kuleshov.yuri.catfacts.networking;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestBuilder {
    protected String mServiceUri = NetworkConstants.ROOT;
    protected String mMethod = "";

    public RequestBuilder(String method) {
        mMethod = method;
    }

    public HttpURLConnection makeRequest() {
        try {
            URL url = new URL(mServiceUri + mMethod);
            HttpURLConnection post = (HttpURLConnection) url.openConnection();
            post.setConnectTimeout(10000);       // todo: settings!
            post.setRequestMethod("GET");
            post.setRequestProperty("Accept", "application/json");
            post.setRequestProperty("Content-type", "application/json; charset=utf-8");

            return post;
        } catch (ConnectException ce){
            ce.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}