import { Arc } from '@nivo/arcs';
import { ComputedBar, RadialBarCommonProps, RadialBarDataProps, RadialBarCustomLayerProps, RadialBarTrackDatum, RadialBarDatum } from './types';
export declare const useRadialBar: <D extends RadialBarDatum = RadialBarDatum>({ data, maxValue: maxValueDirective, valueFormat, startAngle, endAngle, innerRadiusRatio, padding, padAngle, cornerRadius, width, height, colors, tracksColor, }: {
    data: import("./types").RadialBarSerie<D>[];
    maxValue: number | "auto";
    valueFormat?: import("@nivo/core").ValueFormat<number, void> | undefined;
    startAngle: number;
    innerRadiusRatio: number;
    padding: number;
    padAngle: number;
    cornerRadius: number;
    endAngle: number;
    width: number;
    height: number;
    colors: import("@nivo/colors").OrdinalColorScaleConfig<Omit<ComputedBar<D>, "color">>;
    tracksColor: string;
}) => {
    center: [number, number];
    outerRadius: number;
    innerRadius: number;
    bars: ComputedBar<D>[];
    arcGenerator: import("d3-shape").Arc<any, Arc>;
    radiusScale: import("@nivo/scales").ScaleBand<string>;
    valueScale: import("@nivo/scales").ScaleLinear<number>;
    tracks: RadialBarTrackDatum[];
    legendData: {
        id: string;
        label: string;
        color: string | undefined;
    }[];
    customLayerProps: RadialBarCustomLayerProps<D>;
};
//# sourceMappingURL=hooks.d.ts.map