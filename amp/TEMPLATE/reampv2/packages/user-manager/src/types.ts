import {RoutingStrategies} from "./utils/Constants";

export interface MountOptions {
    mountPoint: HTMLElement;
    routingStrategy: RoutingStrategies;
    initialPathName?: string;
}
