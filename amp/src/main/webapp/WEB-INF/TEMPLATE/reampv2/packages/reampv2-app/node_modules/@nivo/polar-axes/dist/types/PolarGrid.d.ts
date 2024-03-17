/// <reference types="react" />
import { AnyScale } from '@nivo/scales';
interface PolarGridProps {
    center: [number, number];
    enableRadialGrid: boolean;
    enableCircularGrid: boolean;
    angleScale: AnyScale;
    radiusScale: AnyScale;
    startAngle: number;
    endAngle: number;
}
export declare const PolarGrid: ({ center, enableRadialGrid, enableCircularGrid, angleScale, radiusScale, startAngle, endAngle, }: PolarGridProps) => JSX.Element;
export {};
//# sourceMappingURL=PolarGrid.d.ts.map