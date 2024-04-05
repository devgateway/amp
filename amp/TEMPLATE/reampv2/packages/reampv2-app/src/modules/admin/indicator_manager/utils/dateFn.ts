/* eslint-disable import/prefer-default-export */
import dayjs from 'dayjs';

export class DateUtil {
    static stringToDate = (date: string, format: string = 'dd/MM/yyyy') => {
        // convert string to javascript date using the specified format
        return dayjs(date, format).toDate();
    };

    static dateToString = (date: Date | string, format: string = 'dd/MM/yyyy') => {
        // convert javascript date to string using the specified format
        if (typeof date === 'string') {
            return date;
        }
        format = format.toUpperCase();
        return dayjs(date).format(format);

    };

    static getCurrentDate = () => dayjs().toDate();

    static getCurrentStrDate = () => dayjs().format('YYYY-MM-DD');

    static backendDateToJavascriptDate = (date: string) => {
        // convert date from DD/MM/YYYY to YYYY-MM-DD
        const [day, month, year] = date.split('/');
        return `${year}-${month}-${day}`;
    }

    static formatJavascriptDate = (date: string, format?: string) => dayjs(date).format(format ?? 'DD/MM/YYYY');

    static addDays = (date: Date, days: number) => dayjs(date).add(days, 'day').toDate();

    static toISO8601 = (dateString: string, dateFormat: string) => {
        let date: string = '';

        switch(dateFormat) {
            case 'dd/MM/yyyy':
                const splitDate = dateString.split('/');
                date = new Date(`${splitDate[2]}-${splitDate[1]}-${splitDate[0]}`).toISOString();
                break;
            case 'yyyy-MM-dd':
                date = dateString;
                break;
            default:
                date = dateString;
        }

        return date;
    }
}
