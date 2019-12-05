package de.gx.buyresell.io.util;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component
public class NumberFormatConverter {

    public String format(Double object) throws Exception {
        return String.valueOf(object);
    }

    public Double parse(String string) throws Exception {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
        return nf.parse(string).doubleValue();
    }
}
