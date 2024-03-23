package com.githib.bufferedchannelinputstream;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BufferedChannelInputStreamTest {

    @Test
    void testRead() throws IOException {
        // Prepare test data
        byte[] data = {65, 66, 67}; // "ABC"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        FileChannel mockChannel = mock(FileChannel.class);
        when(mockChannel.read(any(ByteBuffer.class))).thenAnswer(invocation -> {
            ByteBuffer buffer = invocation.getArgument(0);
            byte[] tempData = new byte[buffer.remaining()];
            int bytesRead = inputStream.read(tempData);
            if (bytesRead != -1) {
                buffer.put(tempData, 0, bytesRead);
            }
            return bytesRead;
        });

        // Test BufferedChannelInputStream
        try (BufferedChannelInputStream stream = new BufferedChannelInputStream(mockChannel, 1024)) {
            assertEquals('A', stream.read());
            assertEquals('B', stream.read());
            assertEquals('C', stream.read());
            assertEquals(-1, stream.read()); // End of stream
        }
    }

    @Test
    void testReadByteArray() throws IOException {
        // Prepare test data
        byte[] data = {65, 66, 67}; // "ABC"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        FileChannel mockChannel = mock(FileChannel.class);
        when(mockChannel.read(any(ByteBuffer.class))).thenAnswer(invocation -> {
            ByteBuffer buffer = invocation.getArgument(0);
            byte[] tempData = new byte[buffer.remaining()];
            int bytesRead = inputStream.read(tempData);
            if (bytesRead != -1) {
                buffer.put(tempData, 0, bytesRead);
            }
            return bytesRead;
        });

        // Test BufferedChannelInputStream
        byte[] buffer = new byte[10];
        try (BufferedChannelInputStream stream = new BufferedChannelInputStream(mockChannel, 1024)) {
            assertEquals(3, stream.read(buffer, 0, buffer.length));
            assertEquals('A', buffer[0]);
            assertEquals('B', buffer[1]);
            assertEquals('C', buffer[2]);
        }
    }

    @Test
    void testReadEmptyStream() throws IOException {
        // Prepare test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        FileChannel mockChannel = mock(FileChannel.class);
        when(mockChannel.read(any(ByteBuffer.class))).thenAnswer(invocation -> {
            ByteBuffer buffer = invocation.getArgument(0);
            byte[] tempData = new byte[buffer.remaining()];
            int bytesRead = inputStream.read(tempData);
            if (bytesRead != -1) {
                buffer.put(tempData, 0, bytesRead);
            }
            return bytesRead;
        });

        // Test BufferedChannelInputStream
        try (BufferedChannelInputStream stream = new BufferedChannelInputStream(mockChannel, 1024)) {
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testReadByteArrayEmptyStream() throws IOException {
        // Prepare test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        FileChannel mockChannel = mock(FileChannel.class);
        when(mockChannel.read(any(ByteBuffer.class))).thenAnswer(invocation -> {
            ByteBuffer buffer = invocation.getArgument(0);
            byte[] tempData = new byte[buffer.remaining()];
            int bytesRead = inputStream.read(tempData);
            if (bytesRead != -1) {
                buffer.put(tempData, 0, bytesRead);
            }
            return bytesRead;
        });

        // Test BufferedChannelInputStream
        byte[] buffer = new byte[10];
        try (BufferedChannelInputStream stream = new BufferedChannelInputStream(mockChannel, 1024)) {
            assertEquals(-1, stream.read(buffer, 0, buffer.length));
        }
    }

    @Test
    void testClose() throws IOException {
        // Prepare mock file channel
        File tempFile = File.createTempFile("test", ".txt");
        try (FileChannel mockChannel = new FileInputStream(tempFile).getChannel()) {

            // Test BufferedChannelInputStream close method
            BufferedChannelInputStream stream = new BufferedChannelInputStream(mockChannel, 1024);
            stream.close();
            assertThrows(IOException.class, mockChannel::position);
        }
    }
}
