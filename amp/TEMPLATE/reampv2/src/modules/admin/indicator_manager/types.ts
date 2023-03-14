import translationPack from './config/initialTranslations.json';

export type DefaultTranslationPackTypes = typeof translationPack;

export type DefaultComponentProps = {
    translations: DefaultTranslationPackTypes;
}

export interface BaseAndTargetValueType {
    originalValue?:     number;
    originalValueDate?: string;
    revisedValue?:     number;
    revisedValueDate?:  string;
}

export interface SectorObjectType {
    id:           number;
    name:         string;
    code:         string;
    codeOfficial: string;
}

export interface IndicatorObjectType {
    id:           number;
    name:         string;
    description:  string;
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
    name:     string;
    code:     string;
    type:     "National" | "Regional";
    children: ProgramObjectType[];
}

export interface ProgramSchemeType {
    ampProgramSettingsId: number;
    name:                 string;
    programName:          string;
    allowMultiple:        boolean;
    startDate:            string;
    endDate:              null;
    children:             ProgramObjectType[];
}
