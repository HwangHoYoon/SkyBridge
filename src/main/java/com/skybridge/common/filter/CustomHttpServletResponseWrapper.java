package com.skybridge.common.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream capture;
    private ServletOutputStream output;
    private PrintWriter writer;

    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        capture = new ByteArrayOutputStream(response.getBufferSize());
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (output == null) {
            output = new ServletOutputStream() {
                @Override
                public void write(int b) {
                    capture.write(b);
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener listener) {
                }
            };
        }

        return output;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (output != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writer == null) {
            writer = new PrintWriter(capture);
        }

        return writer;
    }

    public byte[] getCaptureAsBytes() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (output != null) {
            output.flush();
        }
        return capture.toByteArray();
    }

    public String getCaptureAsString() throws IOException {
        return new String(getCaptureAsBytes(), getCharacterEncoding());
    }
}
