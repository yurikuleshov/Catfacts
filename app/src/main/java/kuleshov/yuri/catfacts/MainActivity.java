package kuleshov.yuri.catfacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import kuleshov.yuri.catfacts.async.AllFactsReceiver;
import kuleshov.yuri.catfacts.async.FactMiner;

import static kuleshov.yuri.catfacts.R.string.select_recipient;

public class MainActivity extends AppCompatActivity implements TopControlBar.OnLengthChangeListener, FactDemonstrator, FactsMetaAwaiter {
    private static final String PARAM_FACT_LENGTH = "fle24239879n";
    private static final String PARAM_MAX_LENGTH = "mle238742n";
    private static final String PARAM_FACTS = "fa98298371cts";

    private RecyclerView mFacts;
    private CoordinatorLayout mCoordinator;

    private int mFactLength;
    private int mMaxLength;
    private View.OnClickListener mOnShareFact = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String text = (String) view.getTag();
            if (text == null) {  // is this possible at all?
                return;
            }

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getString(select_recipient)));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFacts = (RecyclerView) findViewById(R.id.facts);

        if (savedInstanceState != null) {
            mFactLength = savedInstanceState.getInt(PARAM_FACT_LENGTH);
            mMaxLength = savedInstanceState.getInt(PARAM_MAX_LENGTH);
            String[] facts = savedInstanceState.getStringArray(PARAM_FACTS);
            if (facts != null) {
                mFacts.setAdapter(new CatFactsAdapter(this, facts, this));
            } else {
                mFacts.setAdapter(new CatFactsAdapter(this, new String[0], this));
                new AllFactsReceiver(this).execute();
            }

        } else {
            new AllFactsReceiver(this).execute();
            mFacts.setAdapter(new CatFactsAdapter(this, new String[0], this));
        }

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        mFacts.setLayoutManager(mLayoutManager);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
    }

    private void beginGettingFactsAsync() {
        FactMiner f2 = new FactMiner(this);
        f2.execute(mFactLength);
    }

    @Override
    public void onLengthChange(int newLen) {
        mFactLength = newLen;
        beginGettingFactsAsync();
    }

    @Override
    public void showFacts(String[] facts) {
        if (null == facts) {
            return;
        }

        mFacts.setAdapter(new CatFactsAdapter(this, facts, this));
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putInt(PARAM_FACT_LENGTH, mFactLength);
        bundle.putInt(PARAM_MAX_LENGTH, mMaxLength);
        bundle.putStringArray(PARAM_FACTS, ((CatFactsAdapter) mFacts.getAdapter()).getDataset());
    }

    @Override
    public void showSingleFact(String fact) {
        Snackbar bar = Snackbar.make(mCoordinator, fact, Snackbar.LENGTH_LONG);
        View root = bar.getView();
        TextView tv = root.findViewById(android.support.design.R.id.snackbar_text);
        tv.setMaxLines(10);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        bar.setAction(R.string.share, mOnShareFact);
        Button b = root.findViewById(android.support.design.R.id.snackbar_action);
        b.setBackgroundColor(Color.WHITE);
        b.setTag(fact);

        bar.show();
    }

    @Override
    public void setMaxPossibleLength(int length) {
        mMaxLength = length;
        mFactLength = mMaxLength / 2;

        Fragment fragment = TopControlBar.newInstance(length);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

        beginGettingFactsAsync();
    }

    public static class CatFactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int NORMAL_GROUP = 648255;

        Context mCtx;
        FactDemonstrator mListener;
        String[] mDataset;
        private View.OnClickListener mItemClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.showSingleFact((String) v.getTag());
                }
            }
        };

        CatFactsAdapter(Context ctx, String[] facts, FactDemonstrator listener) {
            mDataset = facts;
            mCtx = ctx;
            mListener = listener;
        }

        String[] getDataset() {
            return mDataset;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // all view types are the same.
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fact_template, parent, false);
            CardView cv = v.findViewById(R.id.card_view);
            cv.setOnClickListener(mItemClicked);
            return new NormalViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String fact = mDataset[position];
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            TextView tv = normalViewHolder.mFact.findViewById(R.id.fact_text);
            CardView cv = normalViewHolder.mFact.findViewById(R.id.card_view);
            cv.setTag(fact);
            tv.setText(fact);
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return NORMAL_GROUP;
        }

        static class NormalViewHolder extends RecyclerView.ViewHolder {
            View mFact;

            NormalViewHolder(View fact) {
                super(fact);
                mFact = fact;
            }
        }
    }
}