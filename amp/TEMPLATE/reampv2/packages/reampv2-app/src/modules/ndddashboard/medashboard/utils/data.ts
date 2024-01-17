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
    return data.find((program) => program.ampProgramSettingsId === parseInt(id.toString()));
}

export const extractFmColumnsData = (data: FmSettings, columnName: string | string[]) => {
    const { "fm-settings": fmSettings } = data;
    const fmColumns = Object.keys(fmSettings);

    if (Array.isArray(columnName)) {
        const findProgressTrackingColumns = fmColumns.filter((column) => columnName.includes(column));
        const finalArray: any[] = []
        findProgressTrackingColumns.map((column) => fmSettings[column]).forEach((column) => finalArray.push(...column));
        return finalArray;
    }
    const findProgressTrackingColumn = fmColumns.find((column) => column === columnName);

    if (!findProgressTrackingColumn) {
        return [];
    }

    return fmSettings[columnName];
}
