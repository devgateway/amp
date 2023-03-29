/* eslint-disable import/prefer-default-export */
import * as Yup from 'yup';

export const indicatorValidationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required'),
  description: Yup.string().optional(),
  code: Yup.string().required('Code is required'),
  ascending: Yup.boolean().required('Ascending is required'),
  creationDate: Yup.date().required('Creation date is required'),
  sectors: Yup.array().of(Yup.number()).optional(),
  programId: Yup.number().optional(),
  base: Yup.object().shape({
    originalValue: Yup.number().optional(),
    originalValueDate: Yup.date().optional(),
    revisedValue: Yup.number().when( 'originalValue', {
      // revised value must be greater than original value if it is not null, undefined or 0
      is: (originalValue: number) => originalValue !== null && originalValue !== undefined && originalValue > 0,
      then: Yup.number().min(Yup.ref('originalValue') , 'Revised base value must be greater than original base value'),
    }).nullable(),
    revisedValueDate: Yup.date().optional().when('originalValueDate', {
      is: (originalValueDate: Date) => originalValueDate !== null && originalValueDate !== undefined,
      then: Yup.date().min(Yup.ref('originalValueDate'), 'Revised base date must be greater than original base date'),
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
    revisedValueDate: Yup.date().optional().when('originalValueDate', {
      is: (originalValueDate: Date) => originalValueDate !== null && originalValueDate !== undefined,
      then: Yup.date().min(Yup.ref('originalValueDate'), 'Revised target date must be greater than original target date'),
    }).nullable(),
  }).optional().nullable(),
});
