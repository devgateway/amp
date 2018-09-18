/**
 * @author Daniel Oliva
 */

export default class ActivityUtils {
  
  static getTitle(field, settings) {
    let ret = '';
    let lang = settings ? settings.language : 'en';
    if (field && field.field_label) {
      if (field.field_label[lang.toLowerCase()]) {
        ret = field.field_label[lang.toLowerCase()];
      } else if (field.field_label[lang.toUpperCase()]) {        
        ret = field.field_label[lang.toUpperCase()];
      }
    }
    return ret;
  }
}