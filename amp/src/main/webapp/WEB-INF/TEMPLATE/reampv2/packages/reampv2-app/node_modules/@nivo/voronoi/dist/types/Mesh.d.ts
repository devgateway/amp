import * as React from 'react';
import { XYAccessor } from './computeMesh';
declare type MouseHandler<Datum> = (datum: Datum, event: React.MouseEvent) => void;
interface MeshProps<Datum> {
    nodes: Datum[];
    width: number;
    height: number;
    x?: XYAccessor<Datum>;
    y?: XYAccessor<Datum>;
    onMouseEnter?: MouseHandler<Datum>;
    onMouseMove?: MouseHandler<Datum>;
    onMouseLeave?: MouseHandler<Datum>;
    onClick?: MouseHandler<Datum>;
    debug?: boolean;
}
export declare const Mesh: <Datum>({ nodes, width, height, x, y, onMouseEnter, onMouseMove, onMouseLeave, onClick, debug, }: MeshProps<Datum>) => JSX.Element;
export {};
//# sourceMappingURL=Mesh.d.ts.map