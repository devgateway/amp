package org.dgfoundation.amp.onepager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import com.rc.retroweaver.runtime.Arrays;

public class LocaleUtils {

    public static final Set<String> countries;

    static {
        final Locale[] locales = Locale.getAvailableLocales();
        countries = new TreeSet<String>();
        for(final Locale locale : locales) {
            countries.add(locale.getDisplayCountry());
        }
    }

    public static String[] getCountryNamesMatching(String query) {
        List<String> list = new ArrayList<String>();
        for (final String country : countries) {
            if (country.toUpperCase().startsWith(query.toUpperCase())) {
                list.add(country);
            }
        }
        return list.toArray(new String[list.size()]);
    }

}
