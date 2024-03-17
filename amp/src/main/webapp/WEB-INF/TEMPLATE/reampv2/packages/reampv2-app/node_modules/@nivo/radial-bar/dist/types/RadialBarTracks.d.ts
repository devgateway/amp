/// <reference types="react" />
import { ArcGenerator, ArcTransitionMode } from '@nivo/arcs';
import { RadialBarTrackDatum } from './types';
interface RadialBarTracksProps {
    center: [number, number];
    tracks: RadialBarTrackDatum[];
    arcGenerator: ArcGenerator;
    transitionMode: ArcTransitionMode;
}
export declare const RadialBarTracks: ({ center, tracks, arcGenerator, transitionMode, }: RadialBarTracksProps) => JSX.Element;
export {};
//# sourceMappingURL=RadialBarTracks.d.ts.map