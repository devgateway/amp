import React, { useEffect, useState } from 'react';
import DatePicker from 'react-date-picker';
import { useSelector } from 'react-redux';
import { SettingsType } from '../types';
import { Value } from 'react-date-picker/dist/cjs/shared/types';
import 'react-date-picker/dist/DatePicker.css';
import './css/React-Calendar.css';
import './css/DateInput.css';

export interface DateInputProps {
    id?: string;
    value?: string | Date;
    defaultValue?: string | Date;
    className?: string;
    name?: string;
    onChange?: ((value: Value) => void) | undefined;
    disabled?: boolean;
    onBlur?: (e: any) => void;
    clearIcon?: React.ReactElement | string | null | undefined;
    calendarIcon?: React.ReactElement | null | undefined;
    inputRef?: React.RefObject<HTMLInputElement>;
    minDate?: Date;
    maxDate?: Date;
    onClear?: () => void;
}

const DateInput: React.FC<DateInputProps> = (props) => {
    const {
        id,
        className,
        value,
        onChange,
        name,
        defaultValue,
        disabled,
        clearIcon,
        calendarIcon,
        inputRef,
        minDate,
        maxDate,
        onClear
    } = props;
    const globalSettings: SettingsType = useSelector((state: any) => state.fetchSettingsReducer.settings);
    const [dateFormat, setDateFormat] = useState<string | undefined>();
    const [inputValue, setInputValue] = useState<string | Date | undefined>(value);

    console.log("inputValue", inputValue);

    const getDefaultDateFormat = () => {
        if (globalSettings) {
            const format = globalSettings['default-date-format'];

            if (format) {
                setDateFormat(format);
            }
        }
    }

    const clearInputValue = () => {
        setInputValue(undefined);
    }

    useEffect(() => {
        setInputValue(value);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [value]);

    useEffect(() => {
        if (!inputValue){
            if (onClear) {
                onClear();
            }
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [inputValue]);

    useEffect(() => {
        getDefaultDateFormat();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [globalSettings]);

    const ClearInputButton = () => {
        return (
            <svg xmlns="http://www.w3.org/2000/svg" onClick={clearInputValue} width="19" height="19" viewBox="0 0 19 19" stroke="black" strokeWidth="2"
            className="react-date-picker__clear-button__icon react-date-picker__button__icon">
                <line x1="4" x2="15" y1="4" y2="15"></line>
                <line x1="15" x2="4" y1="4" y2="15"></line>
            </svg>
        )
    }

    console.log("dateformat", dateFormat);
    console.log("inputValue====>", inputValue);

    return (
        <div>
            {dateFormat && (
                <DatePicker
                    id={id}
                    format={dateFormat}
                    monthPlaceholder="mm"
                    dayPlaceholder="dd"
                    yearPlaceholder="yyyy"
                    className={className}
                    onChange={onChange}
                    name={name}
                    value={inputValue}
                    minDate={minDate && new Date('1970-01-01')}
                    maxDate={maxDate && new Date('2200-12-31')}
                    defaultValue={defaultValue}
                    disabled={disabled}
                    clearIcon={clearIcon === null ? null : <ClearInputButton />}
                    monthAriaLabel="Month"
                    dayAriaLabel="Day"
                    yearAriaLabel="Year"
                    closeCalendar
                    clearAriaLabel="Clear Date"
                    calendarAriaLabel="Toggle Calendar"
                    inputRef={inputRef}
                    locale="en-US"
                />
            )}
        </div>


    )
}

export default DateInput;
