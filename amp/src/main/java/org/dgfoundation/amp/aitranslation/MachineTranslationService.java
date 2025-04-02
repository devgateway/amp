package org.dgfoundation.amp.aitranslation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Octavian Ciubotaru
 */
public interface MachineTranslationService {

    CompletableFuture<Map<String, String>> translate(String srcLang, String destLang, List<String> contents);
}
