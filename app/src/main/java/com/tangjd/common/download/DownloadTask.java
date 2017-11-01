package com.tangjd.common.download;

import com.tangjd.common.utils.IOUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask implements Runnable {
    private String mUrl;
    private File mFile;
    private DownloadListener mDownloadListener;

    private long mTotalSize = 0;

    public DownloadTask(String url, File file) {
        mUrl = url;
        mFile = file;
    }

    public DownloadTask(String url, File file, DownloadListener listener) {
        mUrl = url;
        mFile = file;
        mDownloadListener = listener;
    }

    @Override
    public void run() {
        if (mFile.exists()) {
            onFileExists(mFile);
            return;
        }
        try {
            loadFromNetwork(mUrl);
        } catch (IOException e) {
            onFailure(e);
            e.printStackTrace();
        }
    }

    /**
     * Initiates the fetch operation.
     */
    private void loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        try {
            stream = downloadUrl(urlString);
            if (stream != null) {
                readIt(stream);
            }
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets an
     * input stream.
     *
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        /**
         * By default this implementation of HttpURLConnection requests that servers use gzip compression.
         * Since getContentLength() returns the number of bytes transmitted,
         * you cannot use that method to predict how many bytes can be read from getInputStream().
         * Instead, read that stream until it is exhausted: when read() returns -1.
         * Gzip compression can be disabled by setting the acceptable encodings in the request header。

         * 在默认情况下，HttpURLConnection 使用 gzip方式获取，文件 getContentLength() 这个方法，
         * 每次read完成后可以获得，当前已经传送了多少数据，而不能用这个方法获取 需要传送多少字节的内容，
         * 当read() 返回 -1时，读取完成，由于这个压缩后的总长度我无法获取，那么进度条就没法计算值了。
         * 要取得长度则，要求http请求不要gzip压缩。
         */
        conn.setRequestProperty("Accept-Encoding", "identity");
        /**
         * 若setRequestProperty("Accept-Encoding", "identity")失败，加上User-agent伪装成浏览器，一般就行了
         * */
        conn.setRequestProperty("User-Agent",
                " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");

        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        if (conn.getResponseCode() != 200) {
            onFailure(new IOException(conn.getResponseMessage() + " " + conn.getResponseCode()));
            return null;
        }
        mTotalSize = conn.getContentLength();
        return stream;
        // END_INCLUDE(get_inputstream)
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param is InputStream containing HTML from targeted site.
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private void readIt(InputStream is) throws IOException {
        File tempFile = new File(mFile.getAbsolutePath() + ".temp");

        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }

        if (tempFile.exists()) {
            tempFile.delete();
        }

        tempFile.createNewFile();

        int bufferSize = 8 * 1024;

        FileOutputStream fos = new FileOutputStream(tempFile);

        BufferedInputStream bis = new BufferedInputStream(is, bufferSize);
        BufferedOutputStream bos = new BufferedOutputStream(fos, bufferSize);

        byte[] buffer = new byte[bufferSize];
        int length;
        long currSize = 0;
        try {
            while ((length = is.read(buffer, 0, bufferSize)) != -1) {
                bos.write(buffer, 0, length);
                currSize += length;
                onUpdate(mTotalSize, currSize, (int) (((double) currSize / (double) mTotalSize) * 100));
            }
            tempFile.renameTo(mFile);
            onComplete(mFile);
        } finally {
            IOUtil.close(bos, bis, fos, is);
        }
    }

    private void onUpdate(long totalSize, long currentSize, int percent) {
        if (mDownloadListener != null) {
            mDownloadListener.onUpdate(totalSize, currentSize, percent);
        }
    }

    private void onComplete(File file) {
        if (mDownloadListener != null) {
            mDownloadListener.onComplete(file);
        }
    }

    private void onFailure(Throwable error) {
        if (mDownloadListener != null) {
            mDownloadListener.onFailure(error);
        }
    }

    private void onFileExists(File file) {
        if (mDownloadListener != null) {
            mDownloadListener.onFileExists(file);
        }
    }

    public interface DownloadListener {
        void onUpdate(long totalSize, long currentSize, int percent);

        void onComplete(File file);

        void onFailure(Throwable error);

        void onFileExists(File file);
    }
}
