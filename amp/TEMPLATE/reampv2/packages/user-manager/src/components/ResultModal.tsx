import React from 'react';
import { Modal } from 'semantic-ui-react';

interface ResultModalProps {
    content: string;
    header?: any;
    onClose?: () => void;
    open: boolean;
}

const ResultModal: React.FC<ResultModalProps> = (props) => {
  const {
    open, onClose, content, header
  } = props;

  return (
      <Modal
          header={header ?? 'Result'}
          content={content}
          open={open}
          onClose={onClose}
          size="tiny"
          dimmer="blurring"
          actions={[{ key: 'ok', content: 'OK', positive: true }]}
      />
  );
};
export default ResultModal;
