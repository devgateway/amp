import { SETTINGS_API } from '../common/ReampConstants';
import { fetchJson, postJson } from 'amp/tools/index';

export default class StartUpApi {
    static fetchSettings() {
        const url = SETTINGS_API;
        return StartUpApi._fetchData(url);
    }
    static _fetchData(url) {
        return new Promise((resolve, reject) => {
            return fetchJson(url).then((result) => {
                resolve(result)
            }).catch((error) => {
                return reject(error);
            });
        });
    }
}
