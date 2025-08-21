import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';

interface Outcome {
  id: number;
  name: string;
}

interface AddNewOutputModalProps {
  show: boolean;
  setShow: (show: boolean) => void;
  outcomes: Outcome[];
  onSubmit?: (output: { name: string; description?: string; outcomeIds: number[] }) => void;
  initialName?: string;
  initialDescription?: string;
  initialOutcomeIds?: number[];
}

const OutputModal: React.FC<AddNewOutputModalProps> = ({ show, setShow, outcomes, onSubmit, initialName = '', initialDescription = '', initialOutcomeIds = [] }) => {
  const [name, setName] = useState(initialName);
  const [description, setDescription] = useState(initialDescription);
  const [selectedOutcomes, setSelectedOutcomes] = useState<number[]>(initialOutcomeIds);

  React.useEffect(() => {
    setName(initialName);
    setDescription(initialDescription);
    setSelectedOutcomes(initialOutcomeIds);
  }, [initialName, initialDescription, initialOutcomeIds, show]);

  const handleCheckboxChange = (id: number) => {
    setSelectedOutcomes(prev =>
      prev.includes(id) ? prev.filter(oid => oid !== id) : [...prev, id]
    );
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (onSubmit) {
      onSubmit({ name, description, outcomeIds: selectedOutcomes });
    }
    setShow(false);
    setName('');
    setDescription('');
    setSelectedOutcomes([]);
  };

  return (
    <Modal show={show} onHide={() => setShow(false)}>
      <Modal.Header closeButton>
        <Modal.Title>Add New Output</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group controlId="outputName">
            <Form.Label>Output Name</Form.Label>
            <Form.Control
              type="text"
              value={name}
              onChange={e => setName(e.target.value)}
              required
              placeholder="Enter output name"
            />
          </Form.Group>
          <Form.Group controlId="outputDescription" className="mt-3">
            <Form.Label>Output Description (Optional)</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={description}
              onChange={e => setDescription(e.target.value)}
              placeholder="Enter output description"
            />
          </Form.Group>
          <Form.Group controlId="linkOutcomes" className="mt-3">
            <Form.Label>Link to Parent Outcome(s)</Form.Label>
            <div>
              {outcomes.length === 0 ? (
                <div>No outcomes available</div>
              ) : (
                outcomes.map(outcome => (
                  <Form.Check
                    key={outcome.id}
                    type="checkbox"
                    label={outcome.name}
                    checked={selectedOutcomes.includes(outcome.id)}
                    onChange={() => handleCheckboxChange(outcome.id)}
                  />
                ))
              )}
            </div>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShow(false)}>
            Cancel
          </Button>
          <Button variant="primary" type="submit">
            Save Output
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default OutputModal;
