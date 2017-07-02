package Utilities;

/**
 * Created by Okuhle on 2017/06/14.
 */

public class ProgressUpdater implements Runnable {

    private long uploaded;
    private long total;
    private UploadCallbacks listener;

    public ProgressUpdater(long uploaded, long total, UploadCallbacks listener) {
        this.uploaded = uploaded;
        this.total = total;
        this.listener = listener;
    }
    @Override
    public void run() {
        listener.onProgressUpdate((int) (100 * uploaded / total));
    }
}
