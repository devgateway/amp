///<reference types="react" />

declare  module 'ampoffline/AmpOfflineApp' {
    const AmpOfflineApp: React.ComponentType;
    export const mount: (el: HTMLElement | null) => () => void;
    export default AmpOfflineApp;
}
