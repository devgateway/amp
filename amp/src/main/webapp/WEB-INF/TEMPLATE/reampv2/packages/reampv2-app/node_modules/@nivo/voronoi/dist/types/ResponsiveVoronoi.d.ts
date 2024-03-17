/// <reference types="react" />
import { VoronoiSvgProps } from './types';
declare type ResponsiveVoronoiProps = Partial<Omit<VoronoiSvgProps, 'data' | 'width' | 'height'>> & Pick<VoronoiSvgProps, 'data'>;
export declare const ResponsiveVoronoi: (props: ResponsiveVoronoiProps) => JSX.Element;
export {};
//# sourceMappingURL=ResponsiveVoronoi.d.ts.map