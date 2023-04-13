import React, { useEffect, useState } from 'react';
import DatePicker from 'react-date-picker';
import { useSelector } from 'react-redux';
import { SettingsType } from '../types';
import { Value } from 'react-date-picker/dist/cjs/shared/types';

import 'react-date-picker/dist/DatePicker.css';
import 'react-calendar/dist/Calendar.css';
import './css/DateInput.css';

export interface DateInputProps{
    value?: string | Date;
    className?: string;
    name?: string;
    onChange? : ((value: Value) => void) | undefined
}

const DateInput: React.FC<DateInputProps> = (props) => {
    const { className, value, onChange, name } = props;
    const globalSettings: SettingsType = useSelector((state: any) => state.fetchSettingsReducer.settings);
    const [dateFormat, setDateFormat] = useState('dd/MM/yyyy');

    const getDefaultDateFormat = () => {
        if (globalSettings) {
            const format = globalSettings['default-date-format'];
            console.log(format);

            if (format) {
                setDateFormat(format);
            }
        }
    }

    useEffect(() => {
        getDefaultDateFormat();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [globalSettings]);

    return (
        <>
            {value ?
                <DatePicker
                    format={dateFormat}
                    monthPlaceholder="mm"
                    dayPlaceholder="dd"
                    yearPlaceholder="yyyy"
                    className={className}
                    onChange={onChange}
                    name={name}
                    value={value}
                    minDate={new Date('1980-01-01')}
                /> :
                <DatePicker
                    format={dateFormat}
                    monthPlaceholder="mm"
                    dayPlaceholder="dd"
                    yearPlaceholder="yyyy"
                    className={className}
                    onChange={onChange}
                    name={name}
                    minDate={new Date('1970-01-01')}
                />
            }

        </>

    )
}

export default DateInput;
