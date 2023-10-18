/// <reference types="react-scripts" />

import * as Yup from 'Yup';

declare global {
    interface Window {
        __REDUX_DEVTOOLS_EXTENSION_COMPOSE__: any;
    }
  }

declare module 'Yup' {
    interface StringSchema extends Yup.StringSchema {
        toJavascriptDate(): StringSchema;
    }
}

declare module '@murasoftware/react-bootstrap-table2-toolkit';
