import React, { useEffect, useState } from 'react';
import DatePicker from 'react-date-picker';
import { useSelector } from 'react-redux';
import { SettingsType } from '../types';
import { Value } from 'react-date-picker/dist/cjs/shared/types';
import 'react-date-picker/dist/DatePicker.css';
import 'react-calendar/dist/Calendar.css';
import './css/DateInput.css';

export interface DateInputProps {
    value?: string | Date;
    defaultValue?: string | Date;
    className?: string;
    name?: string;
    onChange?: ((value: Value) => void) | undefined;
    disabled?: boolean;
    onBlur?: (e: any) => void;
    clearIcon?:  React.ReactElement | null | undefined;
    calendarIcon?: React.ReactElement | null | undefined;
}

const DateInput: React.FC<DateInputProps> = (props) => {
    const { className, value, onChange, name, defaultValue, disabled, clearIcon, calendarIcon } = props;
    const globalSettings: SettingsType = useSelector((state: any) => state.fetchSettingsReducer.settings);
    const [dateFormat, setDateFormat] = useState('dd/MM/yyyy');

    const getDefaultDateFormat = () => {
        if (globalSettings) {
            const format = globalSettings['default-date-format']; 

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
        <DatePicker
            format={dateFormat}
            monthPlaceholder="mm"
            dayPlaceholder="dd"
            yearPlaceholder="yyyy"
            className={className}
            onChange={onChange}
            name={name}
            value={value ? value: null}
            minDate={new Date('1970-01-01')}
            defaultValue={defaultValue}
            disabled={disabled}
            clearIcon={clearIcon}
            calendarIcon={calendarIcon}
        />

    )
}

export default DateInput;
