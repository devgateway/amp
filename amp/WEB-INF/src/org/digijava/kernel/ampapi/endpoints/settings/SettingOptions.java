package org.digijava.kernel.ampapi.endpoints.settings;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * Stores a generic setting configuration  
 * @author Nadejda Mandrescu
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingOptions {
    
    /**
     * A setting option
     */
    public static class Option {
        /** option id */
        public final String id;
        /** option name to display */
        public final String name;
        /** option value, to change */
        public final String value;
        
        /**
         * Configures an option by 'id' and 'name'
         * 
         * @param id - option id
         * @param name - option name
         */
        public Option(String id, String name) {
            this(id, name, id, false);
        }
        
        public Option(String id, String name, String value) {
            this(id, name, value, false);
        }
        
        public Option(String id, String name, boolean translate) {
            this(id, name, id, translate);
        }
        
        public Option(String id, String name, String value, boolean translate) {
            this.id = id;
            this.name = translate ? TranslatorWorker.translateText(name) : name;
            this.value = value;
        }
        
        public Option(String id) {
            // The setting does not have a "name", only id.
            this(id, id);
        }
        
        @Override public String toString() {
            return String.format("option(id=<%s>, name=<%s>, value=<%s>", this.id, this.name, this.value);
        }
    }
    
    /** Specifies if multiple options can be selected */
    public final Boolean multi;
    /** Default setting option id */
    public final String defaultId;
    /** List of available setting options */
    public final List<Option> options;
    
    /**
     * Simple list of options
     * 
     * @param defaultId default setting option id
     * @param options list of available setting options
     */
    public SettingOptions(String defaultId, List<Option> options) {
        this(false, defaultId, options);
    }

    /**
     * Simple list of options
     *
     * @param multi allow multiple selections
     * @param defaultId default setting option id
     * @param options list of available setting options
     */
    public SettingOptions(boolean multi, String defaultId, List<Option> options) {
        this.multi = multi;
        this.defaultId = defaultId;
        this.options = options;
    }

    @Override public String toString() {
        return String.format("(multi: %s, defaultId: %s, options: %s)", multi, defaultId, options);
    }
}
