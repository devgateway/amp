package org.digijava.kernel.web.gzip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.WebUtils;

/**
 * A wrapper for HttpServletRequest that decompresses underlying input stream.
 * @author Octavian Ciubotaru
 */
public class GzippedServletRequest extends HttpServletRequestWrapper {

    /**
     * Constructs a request object wrapping the given request.
     */
    public GzippedServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamWrapper(new GZIPInputStream((super.getInputStream())));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        String characterEncoding = getCharacterEncoding();
        if (characterEncoding == null) {
            characterEncoding = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        return new BufferedReader(new InputStreamReader(getInputStream(), characterEncoding));
    }
}
