package org.dgfoundation.amp.ar.amp212;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringJoiner;

import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.test.categories.DatabaseTests;
import org.dgfoundation.amp.testutils.InTransactionRule;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(DatabaseTests.class)
public class DateTimeTests {

    @Rule
    public InTransactionRule inTransactionRule = new InTransactionRule();

    private final static Set<String> PATTERNS = AmpDateFormatter.generateSupportedPatterns();
    
    private final static List<String> LIMITED_PATTERNS = Arrays.asList("dd/MMM/yyyy", "MMM/dd/yyyy", "yyyy/MMM/dd");
    
    private final static List<LocalDate> LIMITED_DATES = generateLimitedDates();
    
    private static List<LocalDate> generateLimitedDates() {
        List<LocalDate> res = new ArrayList<>();
        res.add(LocalDate.of(1990, 4, 11));
        res.add(LocalDate.of(1990, 11, 11));
        res.add(LocalDate.of(1990, 4, 2));
        res.add(LocalDate.of(1990, 1, 2));
        return res;
    }
    
    
    private final static List<LocalDate> DATES = generateDates();
    private static List<LocalDate> generateDates() {
        List<LocalDate> res = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            res.add(LocalDate.of(1990, i, 1));
            res.add(LocalDate.of(2018, i, 11));
        }
        return res;
    }

    @BeforeClass
    public static void staticSetUp() {
        StandaloneAMPInitializer.initialize();
    }

    @Test
    public void testLocalizedWithPattern() {
        for (LocalDate ld : DATES) {
            for (String pattern : PATTERNS) {
                AmpDateFormatter formatter = AmpDateFormatterFactory.getLocalizedFormatter(pattern);
                String fm = formatter.format(ld);
                LocalDate defm = formatter.parseDate(fm);
                assertEquals(ld, defm);
            }
        }
    }


    
    private void runLocalizedWithPattern(Locale locale) {
        for (LocalDate ld : DATES) {
            for (String pattern : PATTERNS) {
                AmpDateFormatter formatter = AmpDateFormatterFactory.getLocalizedFormatter(pattern, locale);
                String fm = formatter.format(ld);
                LocalDate defm = formatter.parseDate(fm);
                assertEquals(ld, defm);
            }
        }
    }
    
    @Test
    public void testLocalizedWithPatternRussian() {
        runLocalizedWithPattern(Locale.forLanguageTag("ru"));
    }
    
    @Test
    public void testLocalizedWithPatternTimor() {
        runLocalizedWithPattern(Locale.forLanguageTag("tm"));
    }
    
    @Test
    public void testLocalizedWithPatternFrench() {
        runLocalizedWithPattern(Locale.FRENCH);
    }
    
    @Test
    public void testUnsupportedPatternYears() {
        try {
            AmpDateFormatterFactory.getLocalizedFormatter("yyyyy-mmm-dd", Locale.forLanguageTag("tm"));
            fail("should have caught the invalid formatter");
        } catch(IllegalArgumentException e) {
        }
    }
    
    @Test
    public void testUnsupportedPatternMonths() {
        try {
            AmpDateFormatterFactory.getLocalizedFormatter("yyyy-mmmmm-dd", Locale.forLanguageTag("tm"));
            fail("should have caught the invalid formatter");
        } catch(IllegalArgumentException e) {
        }
    }
    
    @Test
    public void testUnsupportedPatternYears2() {
        try {
            AmpDateFormatterFactory.getLocalizedFormatter("yy-mmm-dd", Locale.forLanguageTag("tm"));
            fail("should have caught the invalid formatter");
        } catch(IllegalArgumentException e) {
        }
    }
    
    @Test
    public void testLocalizedWithPatternOneWayRu() {
        List<String> cor = Arrays.asList("11/апр/1990","апр/11/1990","1990/апр/11","11/ноя/1990","ноя/11/1990","1990/ноя/11",
                "02/апр/1990","апр/02/1990","1990/апр/02","02/янв/1990","янв/02/1990","1990/янв/02");
        runShortLocalizedWithPattern(Locale.forLanguageTag("ru"), cor);
    }

    @Test
    public void testLocalizedWithPatternOneWayTm() {
        List<String> cor = Arrays.asList("11/Abr/1990","Abr/11/1990","1990/Abr/11","11/Nov/1990","Nov/11/1990","1990/Nov/11",
                "2/Abr/1990","Abr/2/1990","1990/Abr/2","2/Jan/1990","Jan/2/1990","1990/Jan/2");
        runShortLocalizedWithPattern(Locale.forLanguageTag("tm"), cor);
    }
    
    @Test
    public void testLocalizedWithPatternOneWayFr() {
        List<String> cor = Arrays.asList("11/avr./1990","avr./11/1990","1990/avr./11","11/nov./1990","nov./11/1990","1990/nov./11",
                "02/avr./1990","avr./02/1990","1990/avr./02","02/janv./1990","janv./02/1990","1990/janv./02");
        runShortLocalizedWithPattern(Locale.forLanguageTag("fr"), cor);
    }
    
    @Test
    public void testFormatterWithAmpFormats() {
        for (LocalDate ld : DATES) {
            for (String pattern : PATTERNS) {
                AmpDateFormatter formatter = AmpDateFormatterFactory.getDefaultFormatter(pattern);
                String fm = formatter.format(ld);
                LocalDate defm = formatter.parseDate(fm);
                assertEquals(ld, defm);
            }
        }
    }

    private void runShortLocalizedWithPattern(Locale locale, List<String> cor) {
        List<String> res = new ArrayList<>();
        for (LocalDate ld : LIMITED_DATES) {
            for (String pattern : LIMITED_PATTERNS) {
                AmpDateFormatter formatter = AmpDateFormatterFactory.getLocalizedFormatter(pattern, locale);
                String fm = formatter.format(ld);
                res.add(fm);
//              System.out.println(String.format("For input %s on pattern %s got result %s", ld.toString(), pattern, fm));
            }
        }
        if (!cor.equals(res)) {
            printCorrect(res, "Correct output for " + locale.getLanguage() + " :");
        }
        assertEquals(cor, res);
    }
    
    private void printCorrect(List<String> values, String message) {
        System.out.println(message);
        StringJoiner sj = new StringJoiner(",");
        
        for (String p : values) {
            sj.add("\"" + p + "\"");
        }
        System.out.println("Arrays.asList(" + sj.toString() +  ");");
    }
}
