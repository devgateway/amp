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

export const extractChildrenFromProgramScheme = (programScheme: ProgramSchemeType)=> {
    const { children } = programScheme;
    return extractChildren(children);
}