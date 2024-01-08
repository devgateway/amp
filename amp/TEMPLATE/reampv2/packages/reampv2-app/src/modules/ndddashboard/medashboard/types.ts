import translations from '../config/initialTranslations.json';
import {SectorObjectType} from "../../admin/indicator_manager/types";

export type DefaultTranslations = typeof translations;

export interface ComponentProps {
  translations: DefaultTranslations
}

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

export interface ActualValue {
    year: string | number;
    value: number
}

export interface YearValues {
    actualValues: ActualValue [];
    baseValue: number;
    targetValue: number;
    indicatorId: number;
}

export interface YearValue {
    year:   string;
    detail: number;
}
export interface Values {
    actual: YearValue;
    base:   YearValue;
    target: YearValue;
}

export interface FmColumn {
    [key: string]: string[];
}

export interface  FmSettings {
    "fm-settings": FmColumn;
}

export interface LineChartData {
    id:    string;
    color: string;
    data: {
        x: string;
        y: number;
    } [];
}

export interface SectorClassifcation {
    id:           number;
    name:         string;
    description:  string | null;
    multisector:  boolean;
    primary:      boolean;
    sectorScheme: SectorScheme;
}

export interface SectorScheme {
    ampSecSchemeId:  number;
    secSchemeCode:   string;
    secSchemeName:   string;
    showInRMFilters: boolean;
    used:            boolean;
    children: SectorObjectType[];
}

export interface SectorReport {
    values:         SectorReportValue[];
    total:          number;
    sumarizedTotal: string;
    currency:       string;
    maxLimit:       number;
    totalPositive:  number;
    name:           string;
    title:          string;
    source:         null;
}

export interface SectorReportValue {
    id:              number;
    name:            string;
    amount:          number;
    formattedAmount: string;
}

export enum ClassificationType {
    PRIMARY = 'Primary',
    SECONDARY = 'Secondary'
}
export interface FundingType {
    id: number;
    name: string;
    value: string;
}
