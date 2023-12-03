let translations;
export default class TranslationManager {
    static initializeTranslations(pTranslations) {
        translations = pTranslations;
    }
    static translate(key) {
        if (translations) {
            return translations[key] ? translations[key] : key;
        } else {
            return key;
        }
    }
}
