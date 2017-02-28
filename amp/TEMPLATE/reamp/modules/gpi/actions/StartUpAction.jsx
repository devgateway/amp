import { store } from '../script.es6';
import * as AMP from 'amp/architecture';
import { loadTranslations } from 'amp/modules/translate';
export const STATE_TRANSLATIONS_LOADED = 'STATE_TRANSLATIONS_LOADED';

const initialTranslations = {       
        'amp.gpi-data-aid-on-budget:title': 'Indicator 6',
        'amp.gpi-data-aid-on-budget:date': 'Date',
        'amp.gpi-data-aid-on-budget:donor-agency': 'Donor Agency',
        'amp.gpi-data-aid-on-budget:amount': 'Amount',
        'amp.gpi-data-aid-on-budget:currency': 'Currency',
        'amp.gpi-data-aid-on-budget:action': 'Action',
        'amp.gpi-data-aid-on-budget:validation-amount-invalid':'Amount field has an invalid input.',
        'amp.gpi-data-aid-on-budget:validation-donor-agency-required': 'Donor Agency is required',
        'amp.gpi-data-aid-on-budget:validation-currency-required': ' Currency is required',
        'amp.gpi-data-aid-on-budget:validation-date-required': 'Date is required',
        'amp.gpi-data-aid-on-budget:delete-successful': 'Record deleted successfully',
        'amp.gpi-data-aid-on-budget:save-successful': 'Record was saved successfully'
};

export function gpiStartUp() {    
    return new Promise((resolve, reject) => {
        let toTranslate = new AMP.Model().toJS();
        loadTranslations(initialTranslations).then(trns => {
            toTranslate = trns;
                store.dispatch({
                type: STATE_TRANSLATIONS_LOADED,
                actionData: { translations: toTranslate }
            });
        });
        resolve();
    });
}