import { DatumWithArc } from './types';
import { ArcTransitionMode, TransitionExtra } from './arcTransitionMode';
/**
 * This hook can be used to animate a group of arcs,
 * if you want to animate a single arc,
 * please have a look at the `useAnimatedArc` hook.
 */
export declare const useArcsTransition: <Datum extends DatumWithArc, ExtraProps = unknown>(data: Datum[], mode?: ArcTransitionMode, extra?: TransitionExtra<Datum, ExtraProps> | undefined) => {
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
    interpolate: (startAngleValue: import("@react-spring/web").SpringValue<number>, endAngleValue: import("@react-spring/web").SpringValue<number>, innerRadiusValue: import("@react-spring/web").SpringValue<number>, outerRadiusValue: import("@react-spring/web").SpringValue<number>, arcGenerator: import("./types").ArcGenerator) => import("@react-spring/web").Interpolation<string | null, any>;
};
//# sourceMappingURL=useArcsTransition.d.ts.map