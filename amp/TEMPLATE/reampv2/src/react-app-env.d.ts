/// <reference types="react-scripts" />

import * as Yup from 'Yup';

declare module 'Yup' {
    interface StringSchema extends Yup.StringSchema {
        toJavascriptDate(): StringSchema;
    }
}
