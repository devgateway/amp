package org.dgfoundation.amp.aitranslation;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MACHINE_TRANSLATION_MAX_CHARACTERS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.MachineTranslation;
import org.digijava.module.aim.dbentity.MachineTranslationCharactersUsed;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * This a IMachineTranslationService wrapper that uses caches translations and limits monthly usage is there is any.
 *
 * @author Octavian Ciubotaru
 */
public class CachedMachineTranslationService implements MachineTranslationService {

    public static final int BATCH_SIZE = 1000;
    private final MachineTranslationService delegate;

    public CachedMachineTranslationService(MachineTranslationService delegate) {
        this.delegate = delegate;
    }

    @Override
    public CompletableFuture<Map<String, String>> translate(String srcLang, String destLang, List<String> contents) {
        return CompletableFuture.supplyAsync(() -> loadCachedTranslations(srcLang, destLang, contents))
                .thenCompose(p -> delegateTranslate(srcLang, destLang, p.getRight())
                            .thenApplyAsync(t -> mergeResults(srcLang, destLang, p.getLeft(), t)));
    }

    /**
     * Returns a completable future that will translate the contents by delegating the call to the service.
     * If contents is empty then will return immediately.
     */
    private CompletableFuture<Map<String, String>> delegateTranslate(String srcLang, String destLang,
            List<String> contents) {
        if (contents.isEmpty()) {
            return CompletableFuture.completedFuture(ImmutableMap.of());
        } else {
            return delegate.translate(srcLang, destLang, contents);
        }
    }

    /**
     * Load cached translations. Returns a pair of values. Left value holds all cached translations, right value are
     * the texts for which we don't have a translation cached.
     *
     * @param srcLang source language
     * @param destLang target language
     * @param contents contents to be translated
     * @return left=translations, right=texts without a translation
     */
    private Pair<Map<String, String>, List<String>> loadCachedTranslations(
            String srcLang, String destLang, List<String> contents) {

        Map<String, String> translated = PersistenceManager.supplyInTransaction(() -> {
            List<Object> translations = new ArrayList<>();

            for (int i = 0; i < contents.size(); i += BATCH_SIZE) {
                List<String> batchContents = contents.subList(i,  Math.min(i + BATCH_SIZE, contents.size()));
                translations.addAll(PersistenceManager.getSession()
                        .createCriteria(MachineTranslation.class)
                        .add(Restrictions.eq("sourceLanguage", srcLang))
                        .add(Restrictions.eq("targetLanguage", destLang))
                        .add(Restrictions.in("text", batchContents))
                        .list());
            }

            return translations.stream().map(o -> ((MachineTranslation) o))
                    .collect(toMap(MachineTranslation::getText, MachineTranslation::getTranslatedText));
        });

        int charsAllowed = PersistenceManager.supplyInTransaction(() -> {
            int maxCharacters = FeaturesUtil.getGlobalSettingValueInteger(MACHINE_TRANSLATION_MAX_CHARACTERS);
            if (maxCharacters <= 0) {
                return -1;
            }

            return Math.max(maxCharacters - getCharactersUsedThisMonth(), 0);
        });

        List<String> contentsLeft = contents.stream().filter(t -> !translated.containsKey(t))
                .filter(upToCodePoints(charsAllowed))
                .collect(toList());

        return Pair.of(translated, contentsLeft);
    }

    /**
     * Accepts strings until sum of their chars adds up to charsAllowed. Counts unicode code points.
     * If charsAllowed is 0, only empty string are accepted.
     * If charsAllowed is negative, all strings are accepted.
     */
    private Predicate<? super String> upToCodePoints(int charsAllowed) {
        if (charsAllowed < 0) {
            return s -> true;
        } else {
            MutableInt charsLeft = new MutableInt(charsAllowed);
            return s -> {
                int cp = s.codePointCount(0, s.length());
                if (cp <= charsLeft.getValue()) {
                    charsLeft.subtract(cp);
                    return true;
                } else {
                    return false;
                }
            };
        }
    }

    private int getCharactersUsedThisMonth() {
        MachineTranslationCharactersUsed used = (MachineTranslationCharactersUsed) PersistenceManager.getSession()
                .createCriteria(MachineTranslationCharactersUsed.class)
                .add(Restrictions.eq("id", 1L))
                .uniqueResult();
        if (used != null && used.getMonth().equals(getCurrentMonth())) {
            return used.getUsed();
        } else {
            return 0;
        }
    }

    private Map<String, String> mergeResults(String sourceLanguage, String targetLanguage,
            Map<String, String> cachedTranslations, Map<String, String>  newTranslations) {

        PersistenceManager.inTransaction(() -> {
            Session session = PersistenceManager.getSession();
            newTranslations.forEach(
                    (k, v) -> session.save(new MachineTranslation(sourceLanguage, targetLanguage, k, v)));

            int maxCharacters = FeaturesUtil.getGlobalSettingValueInteger(MACHINE_TRANSLATION_MAX_CHARACTERS);
            if (maxCharacters >= 0) {
                int n = newTranslations.keySet().stream().mapToInt(s -> s.codePointCount(0, s.length())).sum();
                incrementCharactersUsedThisMonth(n);
            }
        });

        Map<String, String> result = new HashMap<>();
        result.putAll(cachedTranslations);
        result.putAll(newTranslations);
        return result;
    }

    private void incrementCharactersUsedThisMonth(int n) {
        MachineTranslationCharactersUsed used = (MachineTranslationCharactersUsed) PersistenceManager.getSession()
                .createCriteria(MachineTranslationCharactersUsed.class)
                .add(Restrictions.eq("id", 1L))
                .uniqueResult();

        String currentMonth = getCurrentMonth();
        if (used == null) {
            used = new MachineTranslationCharactersUsed();
            used.setId(1L);
            used.setMonth(currentMonth);
            used.setUsed(n);
            PersistenceManager.getSession().save(used);
        } else {
            if (used.getMonth().equals(currentMonth)) {
                used.setUsed(used.getUsed() + n);
            } else {
                used.setMonth(currentMonth);
                used.setUsed(n);
            }
        }
    }

    private String getCurrentMonth() {
        LocalDate now = LocalDate.now();
        return String.format("%04d%02d", now.getYear(), now.getMonthValue());
    }
}
