/* eslint-disable import/prefer-default-export */
import * as Yup from 'yup';

export const indicatorValidationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required'),
  description: Yup.string().optional(),
  code: Yup.string().required('Code is required'),
  ascending: Yup.boolean().required('Ascending is required'),
  creationDate: Yup.date().required('Creation date is required'),
  // sectors: Yup.array().of(Yup.number()).optional(),
  // base: Yup.object().shape({
  //   originalValue: Yup.number().required('Original value is required'),
  //   originalValueDate: Yup.date().required('Original value date is required'),
  //   revisedlValue: Yup.number().required('Revised value is required'),
  //   revisedValueDate: Yup.date().required('Revised value date is required'),
  // }).optional().nullable(),
  // target: Yup.object().shape({
  //   originalValue: Yup.number().required('Original value is required'),
  //   originalValueDate: Yup.date().required('Original value date is required'),
  //   revisedlValue: Yup.number().required('Revised value is required'),
  //   revisedValueDate: Yup.date().required('Revised value date is required'),
  // }).optional().nullable(),
  // programs: Yup.array().of(Yup.number()).required('Programs are required'),
});
