/* eslint-disable import/prefer-default-export */
import * as Yup from 'yup';

export const newIndicatorValidationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required'),
  description: Yup.string().required('Description is required'),
  indicatorCode: Yup.string().required('Indicator code is required'),
  indicatorType: Yup.string().required('Indicator type is required'),

});
