import * as Yup from 'yup';

export const editProfileSchema = Yup.object().shape({
  // firstName: Yup.string().required('⚠️ First name is required'),
  // lastName: Yup.string().required('⚠️ Last name is required'),
  // email: Yup.string().email('⚠️ Please enter a valid email').required('Email is required'),
  // repeatEmail: Yup.string().oneOf([Yup.ref('email')], 'Please enter a valid email'),
  // notificationEmailEnabled: Yup.boolean(),
  // notificationEmail: Yup.string().email().optional().when('notificationEmailEnabled', {
  //     is: true,
  //     then: (schema) => schema.required('⚠️ Please enter a valid email'),
  //     otherwise: (schema) => schema.notRequired()
  // }),
  // repeatNotificationEmail: Yup.string().email().when('notificationEmailEnabled', {
  //     is: true,
  //     then: (schema) => schema.oneOf([Yup.ref('notificationEmail')], '⚠️ Please enter a valid email'),
  //     otherwise: (schema) => schema.notRequired()
  // })
});
