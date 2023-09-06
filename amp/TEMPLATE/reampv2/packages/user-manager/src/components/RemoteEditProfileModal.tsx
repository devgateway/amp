import React from 'react';
import { Provider } from 'react-redux';
import { Store } from 'redux';
import EditProfile, { EditProfileProps } from './EditProfile';

interface RemoteEditProfileModalProps extends EditProfileProps {
    store: Store;
}
const RemoteEditProfileModal: React.FC<RemoteEditProfileModalProps> = (props) => {
  const { store, show, setShow } = props;

  return (
      <Provider store={store}>
          <EditProfile show={show} setShow={setShow} />
      </Provider>
  );
};

export default RemoteEditProfileModal;
