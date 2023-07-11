import translations from '../../config/initialTranslations.json';

export type DefaultTranslations = typeof translations;

export type MarginProps = {
    top?: number;
    right?: number;
    bottom?: number;
    left?: number;
}
