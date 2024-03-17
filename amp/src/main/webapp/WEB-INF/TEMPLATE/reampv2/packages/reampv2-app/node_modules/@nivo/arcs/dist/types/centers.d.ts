import { SpringValue } from '@react-spring/web';
import { Arc, DatumWithArc, Point } from './types';
import { ArcTransitionMode, TransitionExtra } from './arcTransitionMode';
export declare const computeArcCenter: (arc: Arc, offset: number) => Point;
export declare const interpolateArcCenter: (offset: number) => (startAngleValue: SpringValue<number>, endAngleValue: SpringValue<number>, innerRadiusValue: SpringValue<number>, outerRadiusValue: SpringValue<number>) => import("@react-spring/web").Interpolation<string, any>;
export declare const useArcCentersTransition: <Datum extends DatumWithArc, ExtraProps = unknown>(data: Datum[], offset?: number, mode?: ArcTransitionMode, extra?: TransitionExtra<Datum, ExtraProps> | undefined) => {
    transition: import("@react-spring/web").TransitionFn<Datum, [{
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps] extends [import("@react-spring/types").Any] ? import("@react-spring/types").Lookup<any> : [object] extends [{
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps] ? import("@react-spring/types").Lookup<any> : { [P_3 in keyof { [P_2 in keyof import("@react-spring/types").Intersect<{
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps extends {
        from: infer From;
    } ? From extends () => any ? ReturnType<From> : import("@react-spring/types").ObjectType<From> : import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps) extends never ? import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<{
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps, {}>, keyof import("@react-spring/web").ReservedProps>> & ({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps extends {
        to?: any;
    } ? Exclude<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)["to"], Function | readonly any[]> extends infer To ? import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<[To] extends [object] ? To : Partial<Extract<To, object>>, {}>, keyof import("@react-spring/web").ReservedProps>> : never : unknown) : import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<{ [P_1 in keyof { [P in keyof import("@react-spring/types").Intersect<import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}>>]: import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}> extends infer U ? P extends keyof U ? U[P] : never : never; }]: { [P in keyof import("@react-spring/types").Intersect<import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}>>]: import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}> extends infer U ? P extends keyof U ? U[P] : never : never; }[P_1]; }, {}>, keyof import("@react-spring/web").ReservedProps>>>]: ({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps extends {
        from: infer From;
    } ? From extends () => any ? ReturnType<From> : import("@react-spring/types").ObjectType<From> : import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps) extends never ? import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<{
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps, {}>, keyof import("@react-spring/web").ReservedProps>> & ({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps extends {
        to?: any;
    } ? Exclude<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)["to"], Function | readonly any[]> extends infer To ? import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<[To] extends [object] ? To : Partial<Extract<To, object>>, {}>, keyof import("@react-spring/web").ReservedProps>> : never : unknown) : import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<{ [P_1 in keyof { [P in keyof import("@react-spring/types").Intersect<import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}>>]: import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}> extends infer U ? P extends keyof U ? U[P] : never : never; }]: { [P in keyof import("@react-spring/types").Intersect<import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}>>]: import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}> extends infer U ? P extends keyof U ? U[P] : never : never; }[P_1]; }, {}>, keyof import("@react-spring/web").ReservedProps>>) extends infer U ? P_2 extends keyof U ? U[P_2] : never : never; }]: { [P_2 in keyof import("@react-spring/types").Intersect<{
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps extends {
        from: infer From;
    } ? From extends () => any ? ReturnType<From> : import("@react-spring/types").ObjectType<From> : import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps) extends never ? import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<{
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps, {}>, keyof import("@react-spring/web").ReservedProps>> & ({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps extends {
        to?: any;
    } ? Exclude<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)["to"], Function | readonly any[]> extends infer To ? import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<[To] extends [object] ? To : Partial<Extract<To, object>>, {}>, keyof import("@react-spring/web").ReservedProps>> : never : unknown) : import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<{ [P_1 in keyof { [P in keyof import("@react-spring/types").Intersect<import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}>>]: import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}> extends infer U ? P extends keyof U ? U[P] : never : never; }]: { [P in keyof import("@react-spring/types").Intersect<import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}>>]: import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}> extends infer U ? P extends keyof U ? U[P] : never : never; }[P_1]; }, {}>, keyof import("@react-spring/web").ReservedProps>>>]: ({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps extends {
        from: infer From;
    } ? From extends () => any ? ReturnType<From> : import("@react-spring/types").ObjectType<From> : import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps) extends never ? import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<{
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps, {}>, keyof import("@react-spring/web").ReservedProps>> & ({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps extends {
        to?: any;
    } ? Exclude<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)["to"], Function | readonly any[]> extends infer To ? import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<[To] extends [object] ? To : Partial<Extract<To, object>>, {}>, keyof import("@react-spring/web").ReservedProps>> : never : unknown) : import("@react-spring/core/dist/declarations/src/types/common").RawValues<Omit<import("@react-spring/types").Constrain<{ [P_1 in keyof { [P in keyof import("@react-spring/types").Intersect<import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}>>]: import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}> extends infer U ? P extends keyof U ? U[P] : never : never; }]: { [P in keyof import("@react-spring/types").Intersect<import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}>>]: import("@react-spring/types").Constrain<import("@react-spring/types").ObjectType<({
        progress: number;
        startAngle: number;
        endAngle: number;
        innerRadius: number;
        outerRadius: number;
    } & ExtraProps)[import("@react-spring/web").TransitionKey & ("progress" | "startAngle" | "endAngle" | "innerRadius" | "outerRadius" | keyof ExtraProps)] extends infer T ? T extends readonly (infer Element_1)[] ? Element_1 : T extends (...args: any[]) => infer Return ? Return extends readonly (infer ReturnElement)[] ? ReturnElement : Return : T : never>, {}> extends infer U ? P extends keyof U ? U[P] : never : never; }[P_1]; }, {}>, keyof import("@react-spring/web").ReservedProps>>) extends infer U ? P_2 extends keyof U ? U[P_2] : never : never; }[P_3]; }>;
    interpolate: (startAngleValue: SpringValue<number>, endAngleValue: SpringValue<number>, innerRadiusValue: SpringValue<number>, outerRadiusValue: SpringValue<number>) => import("@react-spring/web").Interpolation<string, any>;
};
export interface ArcCenter<Datum extends DatumWithArc> extends Point {
    data: Datum;
}
/**
 * Compute an array of arc centers from an array of data containing arcs.
 *
 * If you plan to animate those, you could use `useArcCentersTransition`
 * instead, you could use the returned array with react-spring `useTransition`,
 * but this would lead to cartesian transitions (x/y), while `useArcCentersTransition`
 * will generate proper transitions using radius/angle.
 */
export declare const useArcCenters: <Datum extends DatumWithArc, ExtraProps extends Record<string, any> = Record<string, any>>({ data, offset, skipAngle, computeExtraProps, }: {
    data: Datum[];
    offset?: number | undefined;
    skipAngle?: number | undefined;
    computeExtraProps?: ((datum: Datum) => ExtraProps) | undefined;
}) => (ArcCenter<Datum> & ExtraProps)[];
//# sourceMappingURL=centers.d.ts.map