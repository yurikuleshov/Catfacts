package kuleshov.yuri.catfacts.networking;

import java.util.Locale;

public class SingleFactRequestor extends RequestBuilder{
    public SingleFactRequestor(int maxLength) {
        super(String.format(Locale.GERMAN, "fact?%d", maxLength));
    }
}