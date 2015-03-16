package net.coderodde.datamining.loader;

import java.io.File;
import net.coderodde.datamining.model.AppDataStorage;
import static net.coderodde.datamining.utils.ValidationUtilities.checkFileExists;
import static net.coderodde.datamining.utils.ValidationUtilities.checkFileIsRegular;
import static net.coderodde.datamining.utils.ValidationUtilities.checkNotNull;

/**
 * This abstract class defines the API for a data loader implementation.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public abstract class DataLoader {
    
    public abstract AppDataStorage load(final File file);
    
    protected void checkFile(final File file) {
        checkNotNull(file, "The file is null.");
        checkFileExists(file);
        checkFileIsRegular(file);
    }
}
