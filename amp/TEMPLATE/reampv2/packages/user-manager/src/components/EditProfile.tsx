import React, { useEffect, useRef, useState } from 'react';
import {
  Button, Checkbox, Form, Modal,
} from 'semantic-ui-react';
import { Formik, FormikProps } from 'formik';
import { useNavigate } from 'react-router-dom';
import styles from './css/Modal.module.css';
import { useAppDispatch, useAppSelector } from '../utils/hooks';
import { EditUserProfile, UserProfile } from '../types';
import { editProfileSchema } from '../utils/validators';
import { editUserProfile } from '../reducers/editUserProfileReducer';
import ResultModal from './ResultModal';
import { updateUser } from '../reducers/fetchUserProfileReducer';

const EditProfile: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const translations = useAppSelector((state) => state.translations.translations);
  const editUserProfileState = useAppSelector((state) => state.editUserProfile);

  const modalRef = useRef(null);

  const userProfile = useAppSelector((state) => state.userProfile.user);

  const [show, setShow] = useState(false);
  const [showResultModal, setShowResultModal] = useState(false);
  const [resultModalContent, setResultModalContent] = useState('');

  useEffect(() => {
    setShow(true);
  }, []);

  const getNotificationEmailEnabled = () => {
    if (userProfile) {
      return userProfile.notificationEmailEnabled;
    }
    return false;
  };
  const [useNotificationEmail, setUseNotificationEmail] = useState(getNotificationEmailEnabled());
  const formikRef = useRef<FormikProps<EditUserProfile>>(null);

  const handleResult = () => {
    if (!editUserProfileState.loading && editUserProfileState.error) {
      setResultModalContent('Error updating profile.');
      setShowResultModal(true);
    }

    if (!editUserProfileState.loading && !editUserProfileState.error) {
      dispatch(updateUser(editUserProfileState.user as UserProfile));
      setResultModalContent('Profile updated successfully.');
      setShowResultModal(true);
    }
  };

  useEffect(() => {
    handleResult();
  }, [editUserProfileState]);

  if (!userProfile) {
    return;
  }

  const handleClose = () => {
    const previousPath = localStorage.getItem('currentPath');
    if (previousPath) {
      const goBackEvent = new CustomEvent('[UserManager] navigated', { detail: previousPath });
      window.dispatchEvent(goBackEvent);
    } else {
      navigate(-1);
    }

    setShow(false);
  };

  // eslint-disable-next-line no-unused-vars
  const handleSetAltEmail = () => setUseNotificationEmail(!useNotificationEmail);

  const handleSubmit = (values: EditUserProfile) => {
    dispatch(editUserProfile(values));
  };

  const initialValues = {
    firstName: userProfile.firstName,
    lastName: userProfile.lastName,
    email: userProfile.email,
    emailConfirmation: userProfile.email,
    notificationEmail: userProfile.notificationEmail,
    repeatNotificationEmail: userProfile.notificationEmail,
    notificationEmailEnabled: useNotificationEmail,
    id: userProfile.id,
    address: userProfile.address,
    countryIso: userProfile.countryIso,
    country: userProfile.country,
    languageCode: userProfile.languageCode,
    organizationId: userProfile.organizationId,
    organizationName: userProfile.organizationName,
    organizationTypeId: userProfile.organizationTypeId,
    organizationGroupId: userProfile.organizationGroupId,
  };

  return (
        <>
            {userProfile
                && (
                    <div>
                        <Modal
                            onClose={() => handleClose()}
                            onOpen={() => setShow(true)}
                            open={show}
                            ref={modalRef}
                            closeIcon
                            dimmer="blurring"
                        >
                            <Modal.Header>
                                Edit Profile
                            </Modal.Header>
                            <Formik
                                validationSchema={editProfileSchema}
                                initialValues={initialValues}
                                innerRef={formikRef}
                                onSubmit={(values) => handleSubmit(values)}
                            >
                                {(props) => (
                                    <>
                                      <Modal.Description>
                                        <Form noValidate onSubmit={props.handleSubmit}>
                                            <Modal.Content image>
                                                    <div className={styles.form_section}>
                                                        <Form.Group>
                                                            <Form.Field className={styles.view_item} required>
                                                                <label htmlFor="fistName" className={styles.input_label}>
                                                                    {translations['amp.user-manager:first-name']}
                                                                </label>
                                                                <input
                                                                    type="text"
                                                                    placeholder={translations['amp.user-manager:enter-first-name']}
                                                                    name="firstName"
                                                                    id="firstName"
                                                                    onChange={props.handleChange}
                                                                    onBlur={props.handleBlur}
                                                                    className={`${styles.input_field} ${(props.errors.firstName && props.touched.firstName) && styles.text_is_invalid}`}
                                                                    defaultValue={props.values.firstName}
                                                                />
                                                              {
                                                                props.errors.firstName && props.touched.firstName
                                                                    && (
                                                                        <h6>
                                                                            {props.errors.firstName}
                                                                        </h6>
                                                                    )
                                                              }
                                                            </Form.Field>

                                                            <Form.Field className={styles.view_item} required>
                                                                <label htmlFor="lastName" className={styles.input_label}>
                                                                  {translations['amp.user-manager:last-name']}
                                                                </label>
                                                                <input
                                                                    type="text"
                                                                    placeholder={translations['amp.user-manager:enter-last-name']}
                                                                    name="lastName"
                                                                    onChange={props.handleChange}
                                                                    onBlur={props.handleBlur}
                                                                    className={`${styles.input_field} ${(props.errors.lastName && props.touched.lastName) && styles.text_is_invalid}`}
                                                                    defaultValue={props.values.lastName}
                                                                />

                                                              {
                                                                  props.errors.lastName && props.touched.lastName
                                                                  && (
                                                                      <h6>
                                                                            {props.errors.lastName}
                                                                        </h6>
                                                                  )
                                                              }
                                                            </Form.Field>
                                                        </Form.Group>

                                                        <Form.Group>
                                                            <Form.Field className={styles.view_item} required>
                                                                <label htmlFor="email" className={styles.input_label}>
                                                                    {translations['amp.user-manager:email']}
                                                                </label>
                                                                <input
                                                                    type="email"
                                                                    placeholder="john@doe.com"
                                                                    name="email"
                                                                    onChange={props.handleChange}
                                                                    onBlur={props.handleBlur}
                                                                    className={`${styles.input_field} ${(props.errors.email && props.touched.email) && styles.text_is_invalid}`}
                                                                    defaultValue={props.values.email}
                                                                />

                                                              {
                                                                  props.errors.email && props.touched.email
                                                                  && (
                                                                      <h6>
                                                                            {props.errors.email}
                                                                        </h6>
                                                                  )
                                                              }
                                                            </Form.Field>

                                                            <Form.Field className={styles.view_item} required>
                                                                <label htmlFor="emailConfirmation" className={styles.input_label}>
                                                                  {translations['amp.user-manager:repeat-email']}
                                                                </label>
                                                                <input
                                                                    type="email"
                                                                    placeholder="john@doe.com"
                                                                    name="emailConfirmation"
                                                                    onChange={props.handleChange}
                                                                    onBlur={props.handleBlur}
                                                                    className={`${styles.input_field} ${(props.errors.emailConfirmation && props.touched.emailConfirmation) && styles.text_is_invalid}`}
                                                                    defaultValue={props.values.email}
                                                                />

                                                              {
                                                                  props.errors.emailConfirmation && props.touched.emailConfirmation
                                                                  && (
                                                                      <h6>
                                                                            {props.errors.emailConfirmation}
                                                                        </h6>
                                                                  )
                                                              }
                                                            </Form.Field>
                                                        </Form.Group>
                                                        <br />
                                                        <br />
                                                    </div>

                                                    <div className={styles.form_section}>
                                                        <Form.Group className={styles.checkbox_wrapper}>
                                                            <Form.Field
                                                                control={Checkbox}
                                                                label={translations['amp.user-manager:receive-notifications']}
                                                                defaultChecked={props.values.notificationEmailEnabled ?? false}
                                                                name="notificationEmailEnabled"
                                                                id="notificationEmailEnabled"
                                                                onChange={props.handleChange}
                                                                onBlur={props.handleBlur}
                                                            />
                                                        </Form.Group>

                                                        {
                                                            props.values.notificationEmailEnabled && (
                                                                <Form.Group>
                                                                    <Form.Field className={styles.view_item}>
                                                                        <label htmlFor="notificationEmail" className={styles.input_label}>
                                                                          {translations['amp.user-manager:alternative-email']}
                                                                        </label>
                                                                        <input
                                                                            type="email"
                                                                            placeholder="john@doe.com"
                                                                            name="notificationEmail"
                                                                            onChange={props.handleChange}
                                                                            onBlur={props.handleBlur}
                                                                            className={`${styles.input_field} ${(props.errors.notificationEmail && props.touched.notificationEmail) && styles.text_is_invalid}`}
                                                                            defaultValue={props.values.notificationEmail}
                                                                        />

                                                                      {
                                                                          props.errors.notificationEmail && props.touched.notificationEmail
                                                                          && (
                                                                              <h6>
                                                                            {props.errors.notificationEmail}
                                                                        </h6>
                                                                          )
                                                                      }
                                                                    </Form.Field>

                                                                    <Form.Field className={styles.view_item}>
                                                                        <label className={styles.input_label}>
                                                                          {translations['amp.user-manager:repeat-alternative-email']}
                                                                        </label>
                                                                        <input
                                                                            type="email"
                                                                            placeholder="john@doe.com"
                                                                            name="repeatNotificationEmail"
                                                                            onChange={props.handleChange}
                                                                            onBlur={props.handleBlur}
                                                                            className={`${styles.input_field} ${(props.errors.repeatNotificationEmail && props.touched.repeatNotificationEmail) && styles.text_is_invalid}`}
                                                                            defaultValue={props.values.notificationEmail}
                                                                        />

                                                                      {
                                                                          props.errors.repeatNotificationEmail && props.touched.repeatNotificationEmail
                                                                          && (
                                                                              <h6>
                                                                            {props.errors.repeatNotificationEmail}
                                                                        </h6>
                                                                          )
                                                                      }

                                                                    </Form.Field>
                                                                </Form.Group>
                                                            )
                                                        }

                                                    </div>
                                            </Modal.Content>
                                        </Form>
                                      </Modal.Description>

                                      <Modal.Actions>
                                          <Button
                                              negative
                                              onClick={handleClose}
                                              className={styles.modal_button}
                                          >
                                            {translations['amp.user-manager:cancel']}
                                          </Button>
                                          <Button
                                              type="submit"
                                              positive
                                              className={styles.modal_button}
                                              // @ts-ignore
                                              onClick={props.handleSubmit}
                                          >
                                            {translations['amp.user-manager:save']}
                                          </Button>
                                      </Modal.Actions>

                                      <ResultModal content={resultModalContent} open={showResultModal} />

                                    </>
                                )}

                            </Formik>
                        </Modal>

                    </div>
                )}
        </>

  );
};

export default EditProfile;
