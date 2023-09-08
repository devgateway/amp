import React, { useEffect, useState } from 'react';
import EditProfile from './EditProfile';
import { fetchUserProfile } from '../reducers/fetchUserProfileReducer';
import { useAppDispatch } from '../utils/hooks';

function Statrup() {
  const dispatch = useAppDispatch();
  const [show, setShow] = useState(false);

  window.addEventListener('openUserModal', (e) => {
    setShow(true);
    console.log(e);
    console.log('openUserModal inside user manager');
  });

  useEffect(() => {
    dispatch(fetchUserProfile());
  }, []);
  return (
    <div className="App">
      <EditProfile show={show} setShow={setShow} />
    </div>
  );
}
export default Statrup;
