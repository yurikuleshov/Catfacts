package kuleshov.yuri.catfacts.networking;

import java.util.Locale;

public class MultipleFactsRequestor extends RequestBuilder {
    public MultipleFactsRequestor() {
        super(String.format(Locale.GERMAN, "facts?limit=1000&max_length=%d", Short.MAX_VALUE));
    }

    public MultipleFactsRequestor(int factMaxLen){
        super(String.format(Locale.GERMAN, "facts?limit=1000&max_length=%d", factMaxLen));
    }
}
