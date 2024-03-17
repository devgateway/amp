import { SpringHelperConfig } from 'react-motion';
/**
 * Decompose a color to be used with react-motion.
 *
 * @deprecated we should use `react-spring` which supports color interpolation natively.
 */
export declare const interpolateColor: (color: string, springConfig?: SpringHelperConfig | undefined) => {
    colorR: number;
    colorG: number;
    colorB: number;
} | {
    colorR: import("react-motion").OpaqueConfig;
    colorG: import("react-motion").OpaqueConfig;
    colorB: import("react-motion").OpaqueConfig;
};
/**
 * Re-assemble interpolated color components,
 * should be used to assign a color after react-motion interpolation.
 *
 * @deprecated we should use `react-spring` which supports color interpolation natively.
 */
export declare const getInterpolatedColor: ({ colorR, colorG, colorB, }: {
    colorR: number;
    colorG: number;
    colorB: number;
}) => string;
//# sourceMappingURL=motion.d.ts.map