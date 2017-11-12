package kuleshov.yuri.catfacts.async;

import android.os.AsyncTask;
import kuleshov.yuri.catfacts.FactsMetaAwaiter;
import kuleshov.yuri.catfacts.networking.MultipleFactsRequestor;
import kuleshov.yuri.catfacts.networking.Call;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllFactsReceiver extends AsyncTask<Void, Void, Integer> {
    private final FactsMetaAwaiter mListener;

    public AllFactsReceiver(FactsMetaAwaiter impatientAwaiter) {
        mListener = impatientAwaiter;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        MultipleFactsRequestor fr = new MultipleFactsRequestor();
        Call call = new Call(fr);
        JSONObject obj = call.Do();

        if (obj == null) {
            return null;
        }

        try {
            JSONArray data = obj.getJSONArray("data");

            int maxLength = 0;
            for (int a = 0; a < data.length(); a++) {
                int currLen = data.getJSONObject(a).getInt("length");
                if (currLen > maxLength) {
                    maxLength = currLen;
                }
            }

            return maxLength;
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer res) {
        mListener.setMaxPossibleLength(res);
    }
}
