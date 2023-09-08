import React, { useEffect, useRef, useState } from 'react';
import {
  Button, Checkbox, Form, Modal,
} from 'semantic-ui-react';
import { Formik, FormikProps } from 'formik';
import { useNavigate } from 'react-router-dom';
import styles from './css/Modal.module.css';
import { useAppDispatch, useAppSelector } from '../utils/hooks';
import { EditUserProfile } from '../types';
import { editProfileSchema } from '../utils/validators';
import { editUserProfile } from '../reducers/editUserProfileReducer';

export interface EditProfileProps {
    show: boolean;
    setShow: React.Dispatch<React.SetStateAction<boolean>>;
}

const EditProfile: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const modalRef = useRef(null);

  const userProfile = useAppSelector((state) => state.userProfile.user);

  const [show, setShow] = useState(false);

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

  if (!userProfile) {
    return;
  }

  const handleClose = () => {
    setShow(false);
    navigate(-1);
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
                            onClose={handleClose}
                            onOpen={() => setShow(true)}
                            open={show}
                            ref={modalRef}
                            closeIcon
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
                                                                    First Name
                                                                </label>
                                                                <input
                                                                    type="text"
                                                                    placeholder="Enter First Name"
                                                                    name="firstName"
                                                                    id="firstName"
                                                                    onChange={props.handleChange}
                                                                    onBlur={props.handleBlur}
                                                                    className={`${styles.input_field} ${(props.errors.firstName && props.touched.firstName) && styles.text_is_invalid}`}
                                                                    defaultValue={props.values.firstName}
                                                                />
                                                            </Form.Field>

                                                            <Form.Field className={styles.view_item} required>
                                                                <label htmlFor="lastName" className={styles.input_label}>
                                                                    Last Name
                                                                </label>
                                                                <input
                                                                    type="text"
                                                                    placeholder="Enter Last Name"
                                                                    name="lastName"
                                                                    onChange={props.handleChange}
                                                                    onBlur={props.handleBlur}
                                                                    className={`${styles.input_field} ${(props.errors.lastName && props.touched.lastName) && styles.text_is_invalid}`}
                                                                    defaultValue={props.values.lastName}
                                                                />
                                                            </Form.Field>
                                                        </Form.Group>

                                                        <Form.Group>
                                                            <Form.Field className={styles.view_item} required>
                                                                <label htmlFor="email" className={styles.input_label}>
                                                                    Email
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
                                                            </Form.Field>

                                                            <Form.Field className={styles.view_item} required>
                                                                <label htmlFor="emailConfirmation" className={styles.input_label}>
                                                                    Repeat Email
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
                                                            </Form.Field>
                                                        </Form.Group>
                                                        <br />
                                                        <br />
                                                    </div>

                                                    <div className={styles.form_section}>
                                                        <Form.Group className={styles.checkbox_wrapper}>
                                                            <Form.Field
                                                                control={Checkbox}
                                                                label="Use a different email for email notifications"
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
                                                                            Alternative
                                                                            Email
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
                                                                    </Form.Field>

                                                                    <Form.Field className={styles.view_item}>
                                                                        <label className={styles.input_label}>
                                                                            Repeat Alternative Email
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
                                            Cancel
                                          </Button>
                                          <Button
                                              type="submit"
                                              positive
                                              className={styles.modal_button}
                                              // @ts-ignore
                                              onClick={props.handleSubmit}
                                          >
                                            Save
                                          </Button>
                                      </Modal.Actions>

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
