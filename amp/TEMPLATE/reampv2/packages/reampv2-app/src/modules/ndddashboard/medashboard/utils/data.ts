import {FmSettings, ProgramConfig, ProgramConfigChild} from '../types';

export const extractLv1Children = (program: ProgramConfig) => {
    const { children } = program;
    if (!children || children.length === 0) {
        return [];
    }

    const lv1Children = children.map((child) => {
        const { children: lv2Children, ...rest } = child;
        return rest;
    });

    return lv1Children as ProgramConfigChild[];
};

export const findProgramConfig = (id: number, data: ProgramConfig[]) => {
    return data.find((program) => program.ampProgramSettingsId === id);
}

export const extractFmColumnsData = (data: FmSettings, columnName: string) => {
    const { "fm-settings": fmSettings } = data;
    const fmColumns = Object.keys(fmSettings);

    const findProgressTrackingColumn = fmColumns.find((column) => column === columnName);

    if (!findProgressTrackingColumn) {
        return [];
    }

    return fmSettings[columnName];
}
