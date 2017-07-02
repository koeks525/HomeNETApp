package Utilities;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by Okuhle on 2017/06/14.
 */

public class ProgressRequestBody extends RequestBody {

    private File file;
    private String path;
    private UploadCallbacks listener;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    private String contentType;

    public ProgressRequestBody(final File file, String contentType, UploadCallbacks listener) {
        this.file = file;
        this.listener = listener;
        this.contentType = contentType;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = file.length();
        byte [] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(file);
        long uploaded = 0;
        try {
         int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {
                handler.post(new ProgressUpdater(uploaded, fileLength, listener));
                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }
}
