/// <reference types="react" />

import React from 'react';

declare module 'ampoffline/AmpOfflineApp' {
    const AmpOfflineApp: React.ComponentType;
    export const mount: ({ el, standalone = false } : { el: HTMLElement | null, standalone?: boolean }) => () => void;
    export default AmpOfflineApp;
}

declare module 'reampv2App/Reampv2App' {
    const ReampV2App: React.ComponentType;
    export const mount: ({ mountPoint, initialPathName, standalone = false } : { mountPoint: HTMLElement | null, initialPathName?: string, standalone?: boolean }) => () => void;
    export default ReampV2App;
}
