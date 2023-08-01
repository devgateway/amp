import TranslationManager from '../utils/TranslationManager';

export default (k, lng) => {
    // We ignore lang since the pack is already in the navigation language
    return TranslationManager.translate(k);
}
