///<reference types="react" />

declare  module 'ampoffline/AmpOfflineApp' {
    const AmpOfflineApp: React.ComponentType;
    export const mount: ({ el, standalone = false } : { el: HTMLElement | null, standalone?: boolean }) => () => void;
    export default AmpOfflineApp;
}

declare module  'reampv2App/Reampv2App' {
    const ReampV2App: React.ComponentType;
    export const mount: ({ mountPoint, initialPathName, standalone = false } : { mountPoint: HTMLElement | null, initialPathName?: string, standalone?: boolean }) => () => void;
    export default ReampV2App;
}

declare module  'userManager/UserManagerApp' {
    const UserManagerApp: React.ComponentType;
    export const mount: ({ mountPoint, initialPathName, standalone = false } : { mountPoint: HTMLElement | null, initialPathName?: string, standalone?: boolean }) => () => void;
    export default UserManagerApp;
}

declare module  'userManager/EditProfileModal' {
    import { Store } from "redux";
    const EditProfileModal: React.FC<{ show: boolean,
        setShow: React.Dispatch<React.SetStateAction<boolean>>, store: Store }>;
    export default EditProfileModal;

}
