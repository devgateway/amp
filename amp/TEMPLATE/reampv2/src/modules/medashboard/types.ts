import translationPack from './config/initialTranslations.json';

export type DefaultTranslationPackTypes = typeof translationPack;

export type DefaultComponentProps = {
    translations: DefaultTranslationPackTypes;
};

export interface SettingsType {
    "use-icons-for-sectors-in-project-list":    boolean;
    "project-sites":                            boolean;
    "max-locations-icons":                      number;
    "number-format":                            string;
    "gs-number-format":                         string;
    "number-group-separator":                   string;
    "number-decimal-separator":                 string;
    "number-divider":                           number;
    "amount-in-thousands":                      number;
    language:                                   string;
    "default-language":                         string;
    "rtl-direction":                            boolean;
    multilingual:                               boolean;
    "default-date-format":                      string;
    "hide-editable-export-formats-public-view": boolean;
    "hide-contacts-public-view":                boolean;
    "download-map-selector":                    boolean;
    "gap-analysis-map":                         boolean;
    "has-ssc-workspaces":                       boolean;
    "public-version-history":                   boolean;
    "public-change-summary":                    boolean;
    "effective-currency":                       EffectiveCurrency;
    "reorder-funding-item":                     number;
    "team-id":                                  number| string |null;
    "team-lead":                                number| string |null;
    "team-validator":                           null;
    "show-activity-workspaces":                 boolean;
    cross_team_validation:                      string;
    "workspace-type"?:                          string;
    "workspace-prefix":                         null;
    "workspace-default-records-per-page":       null;
    "ndd-mapping-indirect-level":               number;
    "ndd-mapping-program-level":                number;
    "calendar-id":                              number;
    "calendar-is-fiscal":                       boolean;
    "dashboard-default-max-date":               null;
    "dashboard-default-max-year-range":         string;
    "dashboard-default-min-date":               null;
    "dashboard-default-min-year-range":         string;
    "gis-default-max-date":                     null;
    "gis-default-max-year-range":               string;
    "gis-default-min-date":                     null;
    "gis-default-min-year-range":               string;
}

export interface EffectiveCurrency {
    id:   number;
    code: string;
}
