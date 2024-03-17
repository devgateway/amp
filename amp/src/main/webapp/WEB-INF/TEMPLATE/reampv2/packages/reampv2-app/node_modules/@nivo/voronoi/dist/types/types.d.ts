import * as React from 'react';
import { Theme, Box } from '@nivo/core';
import { Delaunay, Voronoi } from 'd3-delaunay';
export declare type VoronoiDatum = {
    id: string | number;
    x: number;
    y: number;
};
export declare type VoronoiDomain = [number, number];
export declare type VoronoiLayerId = 'links' | 'cells' | 'points' | 'bounds';
export interface VoronoiCustomLayerProps {
    points: {
        x: number;
        y: number;
        data: VoronoiDatum;
    }[];
    delaunay: Delaunay<Delaunay.Point>;
    voronoi: Voronoi<Delaunay.Point>;
}
export declare type VoronoiCustomLayer = React.FC<VoronoiCustomLayerProps>;
export declare type VoronoiLayer = VoronoiLayerId | VoronoiCustomLayer;
export declare type VoronoiCommonProps = {
    data: VoronoiDatum[];
    width: number;
    height: number;
    margin?: Box;
    xDomain: VoronoiDomain;
    yDomain: VoronoiDomain;
    layers: VoronoiLayer[];
    theme?: Theme;
    enableLinks: boolean;
    linkLineWidth: number;
    linkLineColor: string;
    enableCells: boolean;
    cellLineWidth: number;
    cellLineColor: string;
    enablePoints: boolean;
    pointSize: number;
    pointColor: string;
    role: string;
};
export declare type VoronoiSvgProps = VoronoiCommonProps;
//# sourceMappingURL=types.d.ts.map