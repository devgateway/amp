export function validateSaveModal(title) {
  if (title === null || title === undefined || title.toString().trim().length === 0) {
    return 'missingTitle';
  }
  return null;
}

export function javaHashCode(s) {
  let h = 0;
  for (let i = 0; i < s.length; i++) {
    // eslint-disable-next-line no-bitwise
    h = Math.imul(31, h) + s.charCodeAt(i) | 0;
  }
  return h;
}
