/* eslint-disable */
import { DateUtil } from './dateFn';
import * as Yup from 'yup';

Yup.setLocale({
  mixed: {
    required: 'This field is required',
    notType: 'This field is invalid',
    default: 'This field is invalid',
  },
})

// convert string to javascript date
Yup.addMethod(Yup.string, 'toJavascriptDate', function toJavascriptDate() {
  if (this._type !== 'string') {
    return this;
  }
  return this.transform((value) => DateUtil.stringToDate(value));
});


export const indicatorValidationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required'),
  description: Yup.string().optional(),
  code: Yup.string().required('Code is required'),
  ascending: Yup.boolean().required('Ascending is required'),
  creationDate: Yup.date().required('Creation date is required'),
  // sectors: Yup.array().of(Yup.number()).optional(),
  programId: Yup.number().optional(),
  base: Yup.object().shape({
    originalValue: Yup.number().optional().nullable(),
    originalValueDate: Yup.date().optional().nullable(),
    revisedValue: Yup.number().when( 'originalValue', {
      // revised value must be greater than original value if it is not null, undefined or 0
      is: (originalValue: number) => originalValue !== null && originalValue !== undefined && originalValue > 0,
      then: Yup.number().min(Yup.ref('originalValue') , 'Revised base value must be greater than original base value'),
    }).nullable(),
  }).optional().nullable(),
  target: Yup.object().shape({
    originalValue: Yup.number().optional().nullable(),
    originalValueDate: Yup.date().optional().nullable(),
    revisedValue: Yup.number().when( 'originalValue', {
      // revised value must be greater than original value if it is not null, undefined or 0
      is: (originalValue: number) => originalValue !== null && originalValue !== undefined && originalValue > 0,
      then: Yup.number().min(Yup.ref('originalValue') , 'Revised target value must be greater than original target value'),
    }).nullable(),
    revisedValueDate: Yup.date().optional().nullable().when('originalValueDate', (originalValueDate: any) => {
      if (originalValueDate) {
        console.log('originalValueDate', originalValueDate);
        return Yup.date().min(originalValueDate, 'Revised target date must be greater than original target date');
      }
      return Yup.date().optional().nullable();
    }),
  }).optional().nullable(),
});
