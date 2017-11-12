package kuleshov.yuri.catfacts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class TopControlBar extends Fragment {
    private static final String MAX_FACT_LENGTH = "max_fact_2989021length";
    private static final String CURR_FACT_LENGTH = "curr_fac34290t_length";

    private int mMaxLength;
    private int mCurrentLength;

    private OnLengthChangeListener mListener;

    public TopControlBar() {
    }

    static TopControlBar newInstance(int maxLength) {
        TopControlBar fragment = new TopControlBar();
        Bundle args = new Bundle();
        args.putInt(MAX_FACT_LENGTH, maxLength);
        args.putInt(CURR_FACT_LENGTH, maxLength / 2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMaxLength = getArguments().getInt(MAX_FACT_LENGTH);
            mCurrentLength = getArguments().getInt(CURR_FACT_LENGTH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_top_control_bar, container, false);
        SeekBar sb = fragment.findViewById(R.id.seekBar);

        sb.setMax(mMaxLength);
        sb.setProgress(mCurrentLength);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCurrentLength = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mListener.onLengthChange(mCurrentLength);
            }
        });

        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putInt(MAX_FACT_LENGTH, mMaxLength);
        bundle.putInt(CURR_FACT_LENGTH, mCurrentLength);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLengthChangeListener) {
            mListener = (OnLengthChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnLengthChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLengthChangeListener {
        void onLengthChange(int newLen);
    }
}