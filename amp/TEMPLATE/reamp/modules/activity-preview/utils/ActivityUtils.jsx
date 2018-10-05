import * as AC from './ActivityConstants'
/**
 *    
 */

export default class ActivityUtils {
  
  static getTitle(field, settings) {
    let ret = '';
    let lang = settings ? settings[AC.LANGUAGE] : 'en';
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