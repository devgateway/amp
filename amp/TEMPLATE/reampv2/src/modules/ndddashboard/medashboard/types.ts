import translations from '../config/initialTranslations.json';

export type DefaultTranslations = typeof translations;

export type MarginProps = {
    top?: number;
    right?: number;
    bottom?: number;
    left?: number;
}

export enum ProgramChildType {
    National = "National",
}

export type ProgramConfig = {
    ampProgramSettingsId: number;
    name:                 string;
    programName:          string;
    allowMultiple:        boolean;
    startDate:            null;
    endDate:              null;
    children:             ProgramConfigChild[];
}

export type ProgramConfigChild = {
    id:       number;
    name:     string;
    code:     string;
    type:     ProgramChildType;
    deleted:  boolean;
    children: ProgramConfigChild[];
}

export type InitialState = {
    loading: boolean;
    error: any;
}
