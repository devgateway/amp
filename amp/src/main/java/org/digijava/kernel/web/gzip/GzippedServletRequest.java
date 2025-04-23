package org.digijava.kernel.web.gzip;

import org.springframework.web.util.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

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
        ServletInputStream is = super.getInputStream();
        return new ServletInputStreamWrapper(new GZIPInputStream(is)) {

            @Override
            public boolean isFinished() {
                return is.isFinished();
            }

            @Override
            public boolean isReady() {
                return is.isReady();
            }

            @Override
            public void setReadListener(ReadListener listener) {
                is.setReadListener(listener);
            }
        };
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
