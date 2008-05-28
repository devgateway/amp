
package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.Identifiable;


@Deprecated
public class AmpPerspective implements Identifiable {
        private Long ampPerspectiveId;
        private String name;
        private String code;
        /**
         * @return Returns the ampPerspectiveId.
         */
        public Long getAmpPerspectiveId() {
                return ampPerspectiveId;
        }
        /**
         * @param ampPerspectiveId The ampPerspectiveId to set.
         */
        public void setAmpPerspectiveId(Long ampPerspectiveId) {
                this.ampPerspectiveId = ampPerspectiveId;
        }
        /**
         * @return Returns the name.
         */
        public String getName() {
                return name;
        }

        public String getCode() {
                return code;
        }
        /**
         * @param name The name to set.
         */
        public void setName(String name) {
                this.name = name;
        }

        public void setCode(String code) {
                this.code = code;
        }
        public String getNameTrimmedForTrn(){
                String s=DbUtil.getTrnMessage("aim:"+this.getName().replaceAll(" ",""));
                if(s!=null) return s;
                return this.getName();
        }
        public String getTrnkey(){
                String s="aim:comp:persp:"+this.getName().replaceAll(" ","");
                return s;
        }
        public String toString() {
                return name;
        }
        public int getId() {
                // TODO Auto-generated method stub
                return 0;
        }
        public Object getIdentifier() {
                return ampPerspectiveId;
        }


}
