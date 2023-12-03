import { DateUtilsHelper, DateConstants} from 'amp-ui';
import Moment from 'moment';
let gSDateFormat;
export default class DateUtils {
    static registerSettings({lang,pGSDateFormat}){
        Moment.locale(lang);
        gSDateFormat = pGSDateFormat;
    }
    static formatDateForAPI(date) {
        return DateUtils.formatDate(date, DateConstants.API_SHORT_DATE_FORMAT);
    }

    static createFormattedDate(date) {
        return DateUtils.formatDate(date, gSDateFormat);
    }

    static createFormattedDateTime(date) {
        return DateUtils.formatDate(date, DateUtilsHelper.getDateTimeFormat(gSDateFormat));
    }

    static formatDate(date, format) {
        if (date) {
            const formattedDate = DateUtilsHelper.formatDate(date, format);
            if (formattedDate !== '') {
                return formattedDate
            } else {
                return date;
            }
        }
    }

    static getYearFromDate(date) {
        return Moment(date).year();
    }
}
