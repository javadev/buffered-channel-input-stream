package com.githib.bufferedchannelinputstream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * A custom InputStream implementation that reads data from a FileChannel into a ByteBuffer.
 */
public class BufferedChannelInputStream extends java.io.InputStream {
    private final FileChannel channel;
    private final ByteBuffer buffer;

    /**
     * Constructs a new BufferedChannelInputStream with the specified FileChannel and buffer size.
     *
     * @param channel    the FileChannel from which data is read.
     * @param bufferSize the size of the buffer to be used.
     */
    public BufferedChannelInputStream(FileChannel channel, int bufferSize) {
        this.channel = channel;
        // Set buffer size as needed
        this.buffer = ByteBuffer.allocate(bufferSize);
        // Set buffer in read mode
        this.buffer.flip();
    }

     /**
     * Reads a single byte from the input stream.
     *
     * @return the byte read, or -1 if the end of the stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        if (!buffer.hasRemaining()) {
            // Clear buffer for reading
            buffer.clear();
            // Read data into the buffer
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                // End of stream
                return -1;
            }
            // Set buffer in read mode
            buffer.flip();
        }
        // Convert byte to unsigned integer
        return buffer.get() & 0xFF;
    }

    /**
     * Reads up to len bytes of data from the input stream into an array of bytes.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in the array b at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or -1 if there is no more data
     *         because the end of the stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int totalBytesRead = 0;
        while (totalBytesRead < len) {
            if (!buffer.hasRemaining()) {
                // Clear buffer for reading
                buffer.clear();
                // Read data into the buffer
                int bytesRead = channel.read(buffer);
                if (bytesRead == -1) {
                    // Return bytes read or -1 for end of stream
                    return totalBytesRead > 0 ? totalBytesRead : -1;
                }
                // Set buffer in read mode
                buffer.flip();
            }
            int bytesToRead = Math.min(len - totalBytesRead, buffer.remaining());
            // Read bytes from buffer into array
            buffer.get(b, off + totalBytesRead, bytesToRead);
            totalBytesRead += bytesToRead;
        }
        return totalBytesRead;
    }

    /**
     * Closes this input stream and releases any system resources associated with the stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        // Close the underlying channel
        channel.close();
    }
}
