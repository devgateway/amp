/// <reference types="react" />
import { AnyScale } from '@nivo/scales';
import { RadialAxisConfig } from './types';
declare type RadialAxisProps = {
    type: 'start' | 'end';
    center: [number, number];
    angle: number;
    scale: AnyScale;
} & RadialAxisConfig;
export declare const RadialAxis: ({ type, center, angle: rawAngle, scale, tickSize, tickPadding, tickRotation: extraRotation, tickComponent, }: RadialAxisProps) => JSX.Element;
export {};
//# sourceMappingURL=RadialAxis.d.ts.map