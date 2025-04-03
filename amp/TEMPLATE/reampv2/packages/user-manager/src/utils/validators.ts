import * as Yup from 'yup';

export const editProfileSchema = Yup.object().shape({
  firstName: Yup.string().required('⚠️   First name is required'),
  lastName: Yup.string().required('⚠️   Last name is required'),
  email: Yup.string().required('⚠️   Please enter a valid email').email('⚠️   Please enter a valid email'),
  // @ts-ignore
  emailConfirmation: Yup.string().required('⚠️   Please enter a valid email').oneOf([Yup.ref('email')], '⚠️   Emails do not match'),
  notificationEmailEnabled: Yup.boolean(),
  notificationEmail: Yup.string().email('⚠️   Please enter a valid email').when('notificationEmailEnabled', {
    is: true,
    then: (schema) => schema.required('⚠️ Please enter a valid email'),
    otherwise: (schema) => schema.notRequired()
  }),
  repeatNotificationEmail: Yup.string().email('⚠️   Please enter a valid email').when('notificationEmailEnabled', {
    is: true,
    // @ts-ignore
    then: (schema) => schema.oneOf([Yup.ref('notificationEmail')], '⚠️ Please enter a valid email'),
    otherwise: (schema) => schema.notRequired()
  })
});
