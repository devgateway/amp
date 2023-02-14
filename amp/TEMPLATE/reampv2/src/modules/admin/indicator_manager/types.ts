import translationPack from './config/initialTranslations.json';

export type DefaultTranslationPackTypes = typeof translationPack;

export type DefaultComponentProps = {
    translations: DefaultTranslationPackTypes;
}

interface TranslatableObject {
    [key: string]: string;
}

export interface BaseAndTargetValueType {
    originalValue:     number;
    originalValueDate: string;
    revisedlValue:     number;
    revisedValueDate:  string;
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
    base:         BaseAndTargetValueType | null;
    target:       BaseAndTargetValueType | null;
    programs:     number[];
}

export interface ProgramObjectType {
    id:       number;
    name:     TranslatableObject;
    code:     string;
    type:     "National" | "Secondary" | "Tertiary" | "Primary";
    children: ProgramObjectType[];
}
