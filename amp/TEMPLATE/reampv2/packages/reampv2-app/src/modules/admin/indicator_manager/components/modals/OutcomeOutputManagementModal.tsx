import React from 'react';
import { Modal, Button } from 'react-bootstrap';

interface Outcome {
  id: number;
  name: string;
  outputs: Output[];
}

interface Output {
  id: number;
  name: string;
}

interface OutcomeOutputManagementModalProps {
  show: boolean;
  setShow: (show: boolean) => void;
  outcomes?: Outcome[];
  translations: any;
}

const OutcomeOutputManagementModal: React.FC<OutcomeOutputManagementModalProps> = ({ show, setShow, outcomes = [], translations }) => {
  return (
    <Modal show={show} onHide={() => setShow(false)} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>{translations['amp.dashboard:outcome-output-management'] || 'Outcome and Output Management'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {outcomes.length === 0 ? (
          <div>{translations['amp.indicatormanager:no-data'] || 'No data available'}</div>
        ) : (
          <ul>
            {outcomes.map(outcome => (
              <li key={outcome.id}>
                <strong>{outcome.name}</strong>
                {outcome.outputs.length > 0 && (
                  <ul>
                    {outcome.outputs.map(output => (
                      <li key={output.id}>{output.name}</li>
                    ))}
                  </ul>
                )}
              </li>
            ))}
          </ul>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={() => setShow(false)}>
          {translations['amp.indicatormanager:close'] || 'Close'}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default OutcomeOutputManagementModal;

