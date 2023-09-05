import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import EditProfile from './EditProfile';
import { fetchUserProfile } from '../reducers/fetchUserProfileReducer';
import { useAppDispatch } from '../utils/hooks';

function Statrup() {
  const dispatch = useAppDispatch();
  const [show, setShow] = useState(false);

  useEffect(() => {
    dispatch(fetchUserProfile());
  }, []);
  return (
    <div className="App">
      <Button onClick={() => setShow(true)}>Open Modal</Button>
      <EditProfile show={show} setShow={setShow} />
    </div>
  );
}
export default Statrup;
