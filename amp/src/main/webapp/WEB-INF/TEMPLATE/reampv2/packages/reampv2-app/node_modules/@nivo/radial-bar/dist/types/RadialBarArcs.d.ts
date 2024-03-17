/// <reference types="react" />
import { ArcGenerator } from '@nivo/arcs';
import { ComputedBar, RadialBarCommonProps, RadialBarDatum } from './types';
interface RadialBarArcsProps<D extends RadialBarDatum> {
    center: [number, number];
    bars: ComputedBar<D>[];
    borderWidth: RadialBarCommonProps<D>['borderWidth'];
    borderColor: RadialBarCommonProps<D>['borderColor'];
    arcGenerator: ArcGenerator;
    isInteractive: RadialBarCommonProps<D>['isInteractive'];
    tooltip: RadialBarCommonProps<D>['tooltip'];
    onClick?: RadialBarCommonProps<D>['onClick'];
    onMouseEnter?: RadialBarCommonProps<D>['onMouseEnter'];
    onMouseMove?: RadialBarCommonProps<D>['onMouseMove'];
    onMouseLeave?: RadialBarCommonProps<D>['onMouseLeave'];
    transitionMode: RadialBarCommonProps<D>['transitionMode'];
}
export declare const RadialBarArcs: <D extends RadialBarDatum>({ center, bars, borderWidth, borderColor, arcGenerator, isInteractive, tooltip, onClick, onMouseEnter, onMouseMove, onMouseLeave, transitionMode, }: RadialBarArcsProps<D>) => JSX.Element;
export {};
//# sourceMappingURL=RadialBarArcs.d.ts.map