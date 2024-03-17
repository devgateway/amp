import { ColorSchemeId } from './schemes';
/**
 * Static color.
 */
export declare type OrdinalColorScaleConfigStaticColor = string;
/**
 * User defined function, receiving the current datum.
 */
export declare type OrdinalColorScaleConfigCustomFunction<Datum> = (d: Datum) => string;
/**
 * Pre-defined color scheme.
 */
export interface OrdinalColorScaleConfigScheme {
    scheme: ColorSchemeId;
    size?: number;
}
/**
 * User defined colors.
 */
export declare type OrdinalColorScaleConfigCustomColors = string[];
/**
 * Get color from datum.
 */
export interface OrdinalColorScaleConfigDatumProperty {
    datum: string;
}
export declare type OrdinalColorScaleConfig<Datum = any> = OrdinalColorScaleConfigStaticColor | OrdinalColorScaleConfigCustomFunction<Datum> | OrdinalColorScaleConfigScheme | OrdinalColorScaleConfigCustomColors | OrdinalColorScaleConfigDatumProperty;
export declare type DatumIdentityAccessor<Datum> = (datum: Datum) => string | number;
export declare type OrdinalColorScale<Datum> = (d: Datum) => string;
/**
 * Compute an ordinal color scale
 */
export declare const getOrdinalColorScale: <Datum = any>(config: OrdinalColorScaleConfig<Datum>, identity?: string | DatumIdentityAccessor<Datum> | undefined) => OrdinalColorScale<Datum>;
export declare const useOrdinalColorScale: <Datum = any>(config: OrdinalColorScaleConfig<Datum>, identity: string | DatumIdentityAccessor<Datum>) => OrdinalColorScale<Datum>;
//# sourceMappingURL=ordinalColorScale.d.ts.map