///<reference types="react" />

declare  module 'ampoffline/AmpOfflineApp' {
    const AmpOfflineApp: React.ComponentType;
    export const mount: (el: HTMLElement | null) => () => void;
    export default AmpOfflineApp;
}

declare module  'reampv2App/Reampv2App' {
    const ReampV2App: React.ComponentType;
    export const mount: ({ el, standalone } : {el: HTMLElement | null, standalone?: boolean }) => () => void;
    export default ReampV2App;
}

declare module  'reampv2App/Reampv2Routes' {
    import { RouteObject } from "react-router-dom";
    const routes: RouteObject[];
    export default routes;
}
