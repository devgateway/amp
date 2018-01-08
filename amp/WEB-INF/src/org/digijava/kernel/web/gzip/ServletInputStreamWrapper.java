package org.digijava.kernel.web.gzip;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

/**
 * A simple wrapper that converts InputStream to ServletInputStream.
 * @author Octavian Ciubotaru
 */
public class ServletInputStreamWrapper extends ServletInputStream {

    private InputStream is;

    public ServletInputStreamWrapper(InputStream is) {
        this.is = is;
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }
}
