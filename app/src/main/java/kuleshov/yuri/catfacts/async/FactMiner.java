package kuleshov.yuri.catfacts.async;

import android.os.AsyncTask;
import kuleshov.yuri.catfacts.FactDemonstrator;
import kuleshov.yuri.catfacts.networking.Call;
import kuleshov.yuri.catfacts.networking.MultipleFactsRequestor;
import kuleshov.yuri.catfacts.networking.SingleFactRequestor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FactMiner extends AsyncTask<Integer, Void, String[]> {

    private final FactDemonstrator mDemonstrator;

    public FactMiner(FactDemonstrator demonstrator) {
        mDemonstrator = demonstrator;
    }

    @Override
    protected String[] doInBackground(Integer... integers) {
        MultipleFactsRequestor fr = new MultipleFactsRequestor(integers[0]);
        Call call = new Call(fr);
        JSONObject obj = call.Do();

        if (obj == null) {
            return null;
        }

        try {
            JSONArray data = obj.getJSONArray("data");
            String[] facts = new String[data.length()];
            for(int a = 0; a < data.length(); a++){
                facts[a] = data.getJSONObject(a).getString("fact");
            }
            return facts;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] res) {
        mDemonstrator.showFacts(res);
    }
}
