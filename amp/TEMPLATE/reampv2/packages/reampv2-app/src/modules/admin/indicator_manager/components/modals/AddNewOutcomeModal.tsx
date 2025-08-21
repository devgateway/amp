import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';

interface AddNewOutcomeModalProps {
  show: boolean;
  setShow: (show: boolean) => void;
  onSubmit?: (outcome: { name: string; description?: string }) => void;
  initialName?: string;
  initialDescription?: string;
}

const AddNewOutcomeModal: React.FC<AddNewOutcomeModalProps> = ({ show, setShow, onSubmit, initialName = '', initialDescription = '' }) => {
  const [name, setName] = useState(initialName);
  const [description, setDescription] = useState(initialDescription);

  React.useEffect(() => {
    setName(initialName);
    setDescription(initialDescription);
  }, [initialName, initialDescription, show]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (onSubmit) {
      onSubmit({ name, description });
    }
    setShow(false);
    setName('');
    setDescription('');
  };

  return (
    <Modal show={show} onHide={() => setShow(false)}>
      <Modal.Header closeButton>
        <Modal.Title>Add New Outcome</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group controlId="outcomeName">
            <Form.Label>Outcome Name</Form.Label>
            <Form.Control
              type="text"
              value={name}
              onChange={e => setName(e.target.value)}
              required
              placeholder="Enter outcome name"
            />
          </Form.Group>
          <Form.Group controlId="outcomeDescription" className="mt-3">
            <Form.Label>Outcome Description (Optional)</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={description}
              onChange={e => setDescription(e.target.value)}
              placeholder="Enter outcome description"
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShow(false)}>
            Cancel
          </Button>
          <Button variant="primary" type="submit">
            Save Outcome
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default AddNewOutcomeModal;
