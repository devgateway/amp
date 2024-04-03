/* eslint-disable */
import { DateUtil } from './dateFn';
import * as Yup from 'yup';
import {DefaultTranslationPackTypes} from "../types";

Yup.setLocale({
  mixed: {
    required: 'This field is required',
    notType: 'This field is invalid',
    default: 'This field is invalid',
  },
});

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
  sectors: Yup.mixed().optional() ,
  programId: Yup.number().nullable().optional().notRequired(),
  base: Yup.object().shape({
    originalValue: Yup.number().optional().nullable(),
    originalValueDate: Yup.date().optional().nullable(),
    revisedValue: Yup.number().optional().nullable().when( 'originalValue', (originalValue: any) => {
      if (originalValue) {
        return Yup.number().moreThan(originalValue, 'Revised base value must be greater than original base value').optional().nullable();
      }
      return Yup.number().optional().nullable();
    }),
    revisedValueDate: Yup.date().optional().nullable().when('originalValueDate', (originalValueDate: any) => {
      if (originalValueDate) {
        return Yup.date().min(DateUtil.addDays(originalValueDate, 1), 'Revised base date must be greater than original base date').optional().nullable();
      }
      return Yup.date().optional().nullable();
    }),
  }).optional().nullable(),
  target: Yup.object().shape({
    originalValue: Yup.number().optional().nullable(),
    originalValueDate: Yup.date().optional().nullable(),
    revisedValue: Yup.number().optional().nullable().when('originalValue', (originalValue: any) => {
      if (originalValue) {
        return Yup.number().moreThan(originalValue, 'Revised target value must be greater than original target value').optional().nullable();
      }
      return Yup.number().optional().nullable();
    }),
    revisedValueDate: Yup.date().optional().nullable().when('originalValueDate', (originalValueDate: any) => {
      if (originalValueDate) {
        return Yup.date().min(DateUtil.addDays(originalValueDate, 1), 'Revised target date must be greater than original target date').optional().nullable();
      }
      return Yup.date().optional().nullable();
    }),
  }).optional().nullable(),
});

export const translatedIndicatorValidationSchema = (translations: DefaultTranslationPackTypes) => {
  return Yup.object().shape({
    name: Yup.string().required(translations["amp.indicatormanager:errors-name-required"]),
    description: Yup.string().optional(),
    code: Yup.string().required(translations["amp.indicatormanager:errors-code-required"]),
    ascending: Yup.boolean().required(translations["amp.indicatormanager:errors-ascending-required"]),
    creationDate: Yup.date().required(translations["amp.indicatormanager:errors-creation-date-required"]),
    sectors: Yup.mixed().optional() ,
    programId: Yup.number().optional(),
    base: Yup.object().shape({
      originalValue: Yup.number().optional().nullable(),
      originalValueDate: Yup.date().optional().nullable(),
      revisedValue: Yup.number().optional().nullable().when( 'originalValue', (originalValue: any) => {
        if (originalValue) {
          return Yup.number().moreThan(originalValue, translations["amp.indicatormanager:errors-revised-base-value-invalid"]).optional().nullable();
        }
        return Yup.number().optional().nullable();
      }),
      revisedValueDate: Yup.date().optional().nullable().when('originalValueDate', (originalValueDate: any) => {
        if (originalValueDate) {
          return Yup.date().min(DateUtil.addDays(originalValueDate, 1), translations["amp.indicatormanager:errors-revised-base-date-invalid"]).optional().nullable();
        }
        return Yup.date().optional().nullable();
      }),
    }).optional().nullable(),
    target: Yup.object().shape({
      originalValue: Yup.number().optional().nullable(),
      originalValueDate: Yup.date().optional().nullable(),
      revisedValue: Yup.number().optional().nullable().when('originalValue', (originalValue: any) => {
        if (originalValue) {
          return Yup.number().moreThan(originalValue, translations["amp.indicatormanager:errors-revised-target-value-invalid"]).optional().nullable();
        }
        return Yup.number().optional().nullable();
      }),
      revisedValueDate: Yup.date().optional().nullable().when('originalValueDate', (originalValueDate: any) => {
        if (originalValueDate) {
          return Yup.date().min(DateUtil.addDays(originalValueDate, 1), translations["amp.indicatormanager:errors-revised-target-date-invalid"]).optional().nullable();
        }
        return Yup.date().optional().nullable();
      }),
    }).optional().nullable(),
  });

}
