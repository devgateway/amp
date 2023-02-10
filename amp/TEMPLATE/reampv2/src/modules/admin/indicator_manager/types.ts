import translationPack from './config/initialTranslations.json';

export type DefaultTranslationPackTypes = typeof translationPack;

export type DefaultComponentProps = {
    translations: DefaultTranslationPackTypes;
}

interface TranslatableObject {
    [key: string]: string;
}

export interface SectorObjectType {
    id:           number;
    name:         TranslatableObject;
    code:         string;
    codeOfficial: string;
}

export interface IndicatorObjectType {
    id:           number;
    name:         TranslatableObject;
    description:  TranslatableObject;
    code:         string;
    ascending:    boolean;
    creationDate: string;
    sectors:      number[];
    base:         any;
    target:       any;
    programs:     any[];
}
