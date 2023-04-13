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
        console.log('format', format);
        return dayjs(date).format(format);

    };
    
    static getCurrentDate = () => dayjs().format('YYYY-MM-DD');
    
    static backendDateToJavascriptDate = (date: string) => {
        // convert date from DD/MM/YYYY to YYYY-MM-DD
        const [day, month, year] = date.split('/');
        return `${year}-${month}-${day}`;
    }
    
    static formatJavascriptDate = (date: string, format?: string) => dayjs(date).format(format ?? 'DD/MM/YYYY');
}
