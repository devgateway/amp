package org.digijava.kernel.web.gzip;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A simple wrapper that converts InputStream to ServletInputStream.
 * @author Octavian Ciubotaru
 */
public abstract class ServletInputStreamWrapper extends ServletInputStream {

    private final InputStream is;

    public ServletInputStreamWrapper(InputStream is) {
        this.is = is;
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }
}
