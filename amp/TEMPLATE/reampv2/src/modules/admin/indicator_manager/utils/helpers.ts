import { ProgramObjectType, ProgramSchemeType } from "../types";

const extractChildren = (children: ProgramObjectType[]) => {
    const childrenArray: ProgramObjectType[] = [];
    children.forEach((child) => {
        childrenArray.push(child);
        if (child.children.length > 0) {
            childrenArray.push(...extractChildren(child.children));
        }
    });
    return childrenArray;
}

export const extractChildrenFromProgramScheme = (programScheme: ProgramSchemeType | Array<ProgramSchemeType>)=> {
    if (Array.isArray(programScheme)) {
        const childrenArray: ProgramObjectType[] = [];
        programScheme.forEach((scheme) => {
            childrenArray.push(...extractChildren(scheme.children));
        });
        return childrenArray;
    }

    const { children } = programScheme;
    return extractChildren(children);
}

export const getProgamSchemeForChild = (programSchemes: Array<ProgramSchemeType>, childId: string | number) => {
    let programScheme: ProgramSchemeType | undefined;
    programSchemes.forEach((scheme) => {
        const children = extractChildrenFromProgramScheme(scheme);
        const child = children.find((child) => child.id.toString() === childId.toString());
        if (child) {
            programScheme = scheme;
        }
    });
    return programScheme;
}