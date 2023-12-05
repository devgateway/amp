package org.digijava.kernel.lucene;

import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.module.help.util.HelpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This is temporary, fast solution, it should come from configurations 
 * or some mechanism where modules are registered. 
 * @author Irakli Kobiashvili
 *
 */
public class LuceneModules implements LuceneModuleRegistry{
    private final List<LucModule<?>> registeredModules;
    public LuceneModules() {
        registeredModules = new ArrayList<LucModule<?>>();
        //TRANSLATION
        registeredModules.addAll(TrnUtil.getLuceneModules());
        //HELP
        registeredModules.addAll(HelpUtil.getLuceneModules());
        //ORGANIZATION not used yet.
        //modules.add(new LucOrganisationModule());
    }

    @Override
    public LucModule<?> get(String name) {
        //TODO do nothing, this is temporary fast solution!
        return null;
    }

    @Override
    public void put(String name, LucModule<?> module) {
        //TODO do nothing, this is temporary fast solution!
    }

    @Override
    public List<LucModule<?>> getAll() {
        return registeredModules;
    }
}
