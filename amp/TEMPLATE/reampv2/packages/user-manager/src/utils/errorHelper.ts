export const errorHelper = <T>(error: T | any[]) => {
  if (error === null) {
    return 'No error';
  }

  if (typeof error === 'string') {
    return error;
  }

  const tempError = error as any;

  if (tempError) {
    const detectKeys = Object.keys(tempError.error)[0];
    const extractedError = tempError.error[detectKeys][0];

    if (!extractedError) {
      return 'An error occurred.';
    }

    return extractedError;
  }
};
