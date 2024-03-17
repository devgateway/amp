/// <reference types="react" />
import { AnyScale } from '@nivo/scales';
import { CircularAxisConfig } from './types';
declare type CircularAxisProps = {
    type: 'inner' | 'outer';
    center?: [number, number];
    radius: number;
    startAngle: number;
    endAngle: number;
    scale: AnyScale;
} & CircularAxisConfig;
export declare const CircularAxis: ({ type, center, radius, startAngle: originalStartAngle, endAngle: originalEndAngle, scale, tickSize, tickPadding, tickComponent, }: CircularAxisProps) => JSX.Element;
export {};
//# sourceMappingURL=CircularAxis.d.ts.map