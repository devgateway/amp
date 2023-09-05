import React, { useRef, useState } from 'react';
import {
  Button, Col, Form, Modal, Row,
} from 'react-bootstrap';
import { Formik, FormikProps } from 'formik';
import styles from './css/Modal.module.css';
import { useAppDispatch, useAppSelector } from '../utils/hooks';
import { EditUserProfile } from '../types';
import { editProfileSchema } from '../utils/validators';
import { editUserProfile } from '../reducers/editUserProfileReducer';

interface EditProfileProps {
    show: boolean;
    setShow: React.Dispatch<React.SetStateAction<boolean>>;
}

const EditProfile: React.FC<EditProfileProps> = (props) => {
  const dispatch = useAppDispatch();
  const modalRef = useRef(null);

  const userProfile = useAppSelector((state) => state.userProfile.user);

  const { show, setShow } = props;
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

  const handleClose = () => setShow(false);
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

  window.addEventListener('openUserModal', () => {
    setShow(true);
    console.log('openUserModal inside user manager');
  });

  return (
    <>
      {userProfile
            && (
            <div>
              <Modal
                size="lg"
                show={show}
                onHide={handleClose}
                centered
                animation={false}
                backdropClassName={styles.modal_backdrop}
                backdrop="static"
                keyboard={false}
                ref={modalRef}
              >
                <Modal.Header closeButton>
                  <Modal.Title>Edit Profile</Modal.Title>
                </Modal.Header>
                <Formik
                  validationSchema={editProfileSchema}
                  initialValues={initialValues}
                  innerRef={formikRef}
                  onSubmit={(values) => handleSubmit(values)}
                >
                  {(props) => (
                    <>
                      <Form noValidate onSubmit={props.handleSubmit}>
                        <Modal.Body>
                          <div>

                <div className={styles.modal_form_group}>
                              <Row className={styles.view_row}>
                                <Form.Group as={Col} controlId="firstName">
                                  <Form.Label className={styles.input_label}>
                                    First
                                    Name
                                    </Form.Label>
                                  <Form.Control
                                    type="text"
                                    placeholder="Enter First Name"
                                    name="firstName"
                                    onChange={props.handleChange}
                                    onBlur={props.handleBlur}
                                    defaultValue={props.values.firstName}
                                  />
                                </Form.Group>

                                <Form.Group as={Col} controlId="lastName">
                                  <Form.Label className={styles.input_label}>
                                    Last
                                    Name
                                    </Form.Label>
                                  <Form.Control
                                    type="text"
                                    placeholder="Enter Last Name"
                                    name="lastName"
                                    onChange={props.handleChange}
                                    onBlur={props.handleBlur}
                                    defaultValue={props.values.lastName}
                                  />
                                </Form.Group>
                              </Row>

                              <Row className={styles.view_row}>
                                <Form.Group as={Col} controlId="email">
                                  <Form.Label
                                    className={styles.input_label}
                                  >
                                      Email
                                  </Form.Label>
                                  <Form.Control
                                    type="email"
                                    placeholder="john@doe.com"
                                    name="email"
                                    onChange={props.handleChange}
                                    onBlur={props.handleBlur}
                                    defaultValue={props.values.email}
                                  />
                                </Form.Group>

                                <Form.Group as={Col} controlId="emailConfirmation">
                                  <Form.Label className={styles.input_label}>
                                    Repeat
                                    Email
                                    </Form.Label>
                                  <Form.Control
                                    type="email"
                                    placeholder="john@doe.com"
                                    name="emailConfirmation"
                                    onChange={props.handleChange}
                                    onBlur={props.handleBlur}
                                    defaultValue={props.values.email}
                                  />
                                </Form.Group>
                              </Row>
                            </div>

                <div className={styles.modal_form_group}>
                              <Form.Check
                                type="checkbox"
                                label="Use a different email for email notifications"
                                defaultChecked={props.values.notificationEmailEnabled ?? false}
                                name="notificationEmailEnabled"
                                onChange={props.handleChange}
                                onBlur={props.handleBlur}
                              />
                              {
                                    props.values.notificationEmailEnabled && (
                                    <Row className={styles.view_row}>
                                      <Form.Group as={Col} controlId="notificationEmail">
                                        <Form.Label className={styles.input_label}>
                                          Alternative
                                          Email
                                        </Form.Label>
                                        <Form.Control
                                          type="email"
                                          placeholder="john@doe.com"
                                          name="notificationEmail"
                                          onChange={props.handleChange}
                                          onBlur={props.handleBlur}
                                          defaultValue={props.values.notificationEmail}
                                        />
                                      </Form.Group>

                                      <Form.Group as={Col} controlId="repeatNotificationEmail">
                                        <Form.Label className={styles.input_label}>
                                          Repeat
                                          Alternative
                                          Email
                                        </Form.Label>
                                        <Form.Control
                                          type="email"
                                          placeholder="john@doe.com"
                                          name="repeatNotificationEmail"
                                          onChange={props.handleChange}
                                          onBlur={props.handleBlur}
                                          defaultValue={props.values.notificationEmail}
                                        />
                                      </Form.Group>
                                    </Row>
                                    )
                                }

                            </div>

              </div>

                        </Modal.Body>
                        <Modal.Footer className={styles.modal_footer}>
                          <Button
                className={styles.modal_button}
                onClick={handleClose}
                variant="danger"
              >
                            Cancel
              </Button>
                          <Button
                type="submit"
                className={styles.modal_button}
                variant="success"
              >
                            Save
              </Button>
                        </Modal.Footer>
                      </Form>
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
