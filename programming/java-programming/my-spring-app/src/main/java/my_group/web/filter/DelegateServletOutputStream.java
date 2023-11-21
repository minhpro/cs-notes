package my_group.web.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Delegate (proxy) ServletOutputStream write, flush, and close APIs to an OutputStream - called targetStream
 */
public class DelegateServletOutputStream extends ServletOutputStream {
    OutputStream targetStream;

    public DelegateServletOutputStream(OutputStream targetStream) {
        this.targetStream = targetStream;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(int b) throws IOException {
        targetStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        this.targetStream.flush();
    }

    @Override
    public void close() throws IOException {
        this.targetStream.close();
    }
}
