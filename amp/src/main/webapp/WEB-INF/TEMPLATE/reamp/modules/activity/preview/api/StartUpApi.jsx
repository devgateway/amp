import { SETTINGS_API, GLOBAL_SETTINGS_API, CALENDAR_API } from '../common/ReampConstants';
import { fetchJson, postJson } from 'amp/tools/index';

export default class StartUpApi {
    static fetchSettings() {
        const url = SETTINGS_API;
        return StartUpApi._fetchData(url);
    }
    static fetchGlobalSettings() {
        const url = GLOBAL_SETTINGS_API;
        return StartUpApi._fetchData(url);
    }
    static fetchCalendar(id) {
        const url = `${CALENDAR_API}?id=${id}`;
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
