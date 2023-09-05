import { RoutingStrategies } from './utils/constants';

export interface MountOptions {
    mountPoint: HTMLElement;
    routingStrategy: RoutingStrategies;
    initialPathName?: string;
}

export interface UserProfile {
    banned: boolean;
    country: string;
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    isBanned: boolean;
    notificationEmailEnabled: boolean;
    notificationEmail: string;
    address: string;
    countryIso: string;
    organizationId: number;
    organizationName: string;
    organizationTypeId: number;
    languageCode: string;
    organizationGroupId: number;
}

export interface EditUserProfile extends Omit<UserProfile, 'banned' | 'isBanned'> {
    emailConfirmation?: string;
    repeatNotificationEmail?: string;
}
