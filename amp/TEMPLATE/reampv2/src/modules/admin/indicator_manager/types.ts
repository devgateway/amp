import translationPack from './config/initialTranslations.json';

export type DefaultTranslationPackTypes = typeof translationPack;

export type DefaultComponentProps = {
    translations: DefaultTranslationPackTypes;
}
