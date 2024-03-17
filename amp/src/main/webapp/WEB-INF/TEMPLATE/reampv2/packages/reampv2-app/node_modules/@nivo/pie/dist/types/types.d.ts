import * as React from 'react';
import { Box, Dimensions, Theme, SvgDefsAndFill, ModernMotionProps, ValueFormat, PropertyAccessor } from '@nivo/core';
import { Arc, ArcGenerator, ArcTransitionMode, ArcLabelsProps, ArcLinkLabelsProps } from '@nivo/arcs';
import { OrdinalColorScaleConfig, InheritedColorConfig } from '@nivo/colors';
import { LegendProps } from '@nivo/legends';
export declare type DatumId = string | number;
export interface DefaultRawDatum {
    id: DatumId;
    value: number;
}
export interface PieArc extends Arc {
    index: number;
    angle: number;
    angleDeg: number;
    thickness: number;
    padAngle: number;
}
export interface ComputedDatum<RawDatum> {
    id: DatumId;
    label: DatumId;
    value: number;
    formattedValue: string;
    color: string;
    fill?: string;
    data: RawDatum;
    arc: PieArc;
    hidden: boolean;
}
export interface DataProps<RawDatum> {
    data: RawDatum[];
}
export interface PieTooltipProps<RawDatum> {
    datum: ComputedDatum<RawDatum>;
}
export declare type MouseEventHandler<RawDatum, ElementType = HTMLCanvasElement> = (datum: ComputedDatum<RawDatum>, event: React.MouseEvent<ElementType>) => void;
export declare type PieLayerId = 'arcLinkLabels' | 'arcs' | 'arcLabels' | 'legends';
export interface PieCustomLayerProps<RawDatum> {
    dataWithArc: ComputedDatum<RawDatum>[];
    centerX: number;
    centerY: number;
    radius: number;
    innerRadius: number;
    arcGenerator: ArcGenerator;
}
export declare type PieCustomLayer<RawDatum> = React.FC<PieCustomLayerProps<RawDatum>>;
export declare type PieLayer<RawDatum> = PieLayerId | PieCustomLayer<RawDatum>;
export declare type CommonPieProps<RawDatum> = {
    id: PropertyAccessor<RawDatum, DatumId>;
    value: PropertyAccessor<RawDatum, number>;
    valueFormat?: ValueFormat<number>;
    margin: Box;
    sortByValue: boolean;
    innerRadius: number;
    padAngle: number;
    cornerRadius: number;
    startAngle: number;
    endAngle: number;
    fit: boolean;
    activeInnerRadiusOffset: number;
    activeOuterRadiusOffset: number;
    colors: OrdinalColorScaleConfig<Omit<ComputedDatum<RawDatum>, 'color' | 'fill' | 'arc'>>;
    theme: Theme;
    borderWidth: number;
    borderColor: InheritedColorConfig<ComputedDatum<RawDatum>>;
    enableArcLabels: boolean;
    enableArcLinkLabels: boolean;
    isInteractive: boolean;
    tooltip: React.FC<PieTooltipProps<RawDatum>>;
    legends: LegendProps[];
    role: string;
    renderWrapper: boolean;
} & Partial<ArcLabelsProps<ComputedDatum<RawDatum>>> & Partial<ArcLinkLabelsProps<ComputedDatum<RawDatum>>>;
export declare type PieHandlers<RawDatum, ElementType> = {
    onClick?: MouseEventHandler<RawDatum, ElementType>;
    onMouseEnter?: MouseEventHandler<RawDatum, ElementType>;
    onMouseMove?: MouseEventHandler<RawDatum, ElementType>;
    onMouseLeave?: MouseEventHandler<RawDatum, ElementType>;
};
export declare type PieSvgCustomComponents<RawDatum> = {
    arcLinkLabelComponent?: ArcLinkLabelsProps<ComputedDatum<RawDatum>>['component'];
};
export declare type PieSvgProps<RawDatum> = DataProps<RawDatum> & Dimensions & Partial<CommonPieProps<RawDatum>> & SvgDefsAndFill<ComputedDatum<RawDatum>> & PieHandlers<RawDatum, SVGPathElement> & {
    layers?: PieLayer<RawDatum>[];
    animate?: boolean;
    motionConfig?: ModernMotionProps['motionConfig'];
    transitionMode?: ArcTransitionMode;
} & PieSvgCustomComponents<RawDatum>;
export declare type CompletePieSvgProps<RawDatum> = DataProps<RawDatum> & Dimensions & CommonPieProps<RawDatum> & SvgDefsAndFill<ComputedDatum<RawDatum>> & PieHandlers<RawDatum, SVGPathElement> & {
    layers: PieLayer<RawDatum>[];
    animate: boolean;
    motionConfig: ModernMotionProps['motionConfig'];
    transitionMode: ArcTransitionMode;
} & PieSvgCustomComponents<RawDatum>;
export declare type PieCanvasProps<RawDatum> = DataProps<RawDatum> & Dimensions & Partial<CommonPieProps<RawDatum>> & Pick<PieHandlers<RawDatum, HTMLCanvasElement>, 'onClick' | 'onMouseMove'> & {
    pixelRatio?: number;
};
export declare type CompletePieCanvasProps<RawDatum> = DataProps<RawDatum> & Dimensions & CommonPieProps<RawDatum> & Pick<PieHandlers<RawDatum, HTMLCanvasElement>, 'onClick' | 'onMouseMove'> & {
    pixelRatio: number;
};
//# sourceMappingURL=types.d.ts.map