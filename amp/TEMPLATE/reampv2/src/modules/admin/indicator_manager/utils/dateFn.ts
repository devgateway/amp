/* eslint-disable import/prefer-default-export */
import dayjs from 'dayjs';

export const getCurrentDate = () => dayjs().format('YYYY-MM-DD');

export const backendDateToJavascriptDate = (date: string) => {
    // convert date from DD/MM/YYYY to YYYY-MM-DD
    const [day, month, year] = date.split('/');
    return `${year}-${month}-${day}`;
}

export const formatJavascriptDate = (date: string) => dayjs(date).format('DD/MM/YYYY');
