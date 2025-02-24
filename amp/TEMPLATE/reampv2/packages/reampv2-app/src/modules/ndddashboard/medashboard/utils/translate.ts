import { useIntl, MessageDescriptor } from 'react-intl';

const useTranslate = () => {
    const intl = useIntl();

    return (id: string | MessageDescriptor, values?: Record<string, any>) =>
        intl.formatMessage(typeof id === 'string' ? { id } : id, values);
};

export default useTranslate;
