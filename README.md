# buffered-channel-input-stream

[![Java CI](https://github.com/javadev/buffered-channel-input-stream/actions/workflows/maven.yml/badge.svg)](https://github.com/javadev/buffered-channel-input-stream/actions/workflows/maven.yml)

## BufferedChannelInputStream

The `BufferedChannelInputStream` class is an implementation of `java.io.InputStream` that reads data from a `FileChannel` with buffering capabilities.

### Constructors

- `BufferedChannelInputStream(FileChannel channel, int bufferSize)`: Constructs a `BufferedChannelInputStream` with the specified `FileChannel` and buffer size.

### Methods

- `int read() throws IOException`: Reads the next byte of data from the input stream. Returns `-1` if the end of the stream is reached.
- `int read(byte[] b, int off, int len) throws IOException`: Reads up to `len` bytes of data into an array of bytes. Returns the total number of bytes read into the buffer or `-1` if the end of the stream is reached.
- `void close() throws IOException`: Closes the input stream and releases any system resources associated with it.

### Usage

```java
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Main {
    public static void main(String[] args) {
        try {
            File file = new File("example.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel channel = fileInputStream.getChannel();
            BufferedChannelInputStream inputStream = new BufferedChannelInputStream(channel, 1024);

            // Read data from inputStream
            // ...

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### Note

- The `BufferedChannelInputStream` class provides buffered reading capabilities to improve performance when reading data from a `FileChannel`.
- It is recommended to close the input stream after use to release system resources.
