/**
 * @author Daniel Oliva
 */
export default class ActivityUtils {
    
    static getTitle(field) {
        let ret = '';
        if (field && field.field_label) {
          if (field.field_label['en']) {
            ret = field.field_label['en'];
          } else if (field.field_label['EN']) {        
            ret = field.field_label['EN'];
          }
        }
        return ret;
    }
  }