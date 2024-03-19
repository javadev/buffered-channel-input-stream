package com.githib.bufferedchannelinputstream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferedChannelInputStream extends java.io.InputStream {
    private final FileChannel channel;
    private final ByteBuffer buffer;

    public BufferedChannelInputStream(FileChannel channel, int bufferSize) {
        this.channel = channel;
        // Set buffer size as needed
        this.buffer = ByteBuffer.allocate(bufferSize);
        // Set buffer in read mode
        this.buffer.flip();
    }

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

    @Override
    public void close() throws IOException {
        // Close the underlying channel
        channel.close();
    }
}
