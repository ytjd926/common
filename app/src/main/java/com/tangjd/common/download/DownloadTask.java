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

//	private long mTotalSize = 0;

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
            onComplete();
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
                stream.close();
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
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        if (conn.getResponseCode() != 200) {
            onFailure(DownloadErrorCode.CONNECTION_FAIL);
            return null;
        }
//		mTotalSize = conn.getContentLength();
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
    private void readIt(InputStream is) throws IOException, UnsupportedEncodingException {
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

        int length = 0;

//		long currSize = 0;

        try {
            while ((length = is.read(buffer, 0, bufferSize)) != -1) {
//				long startTime = System.currentTimeMillis();
                bos.write(buffer, 0, length);
//				currSize += length;
//				long endTime = System.currentTimeMillis();
//				float speed = (float) ((8.0f) / ((float)(endTime - startTime) / 1000f));
//				double formatedSpeed = ((int) (speed * 10) / 10.0);
//				onUpdate(mTotalSize, currSize, formatedSpeed);
            }
            tempFile.renameTo(mFile);
            onComplete();
        } finally {
            IOUtil.close(bos, bis, fos, is);
        }
    }

    @SuppressWarnings("unused")
    private void onUpdate(long totalSize, long currentSize, double speed) {
        if (mDownloadListener != null) {
            mDownloadListener.onUpdate(totalSize, currentSize, speed);
        }
    }

    private void onComplete() {
        if (mDownloadListener != null) {
            mDownloadListener.onComplete();
        }
    }

    private void onFailure(DownloadErrorCode errorCode) {
        if (mDownloadListener != null) {
            mDownloadListener.onFailure(errorCode);
        }
    }

    private void onFailure(Throwable error) {
        if (mDownloadListener != null) {
            mDownloadListener.onFailure(error);
        }
    }

    public interface DownloadListener {
        void onUpdate(long totalSize, long currentSize, double speed);

        void onComplete();

        void onFailure(DownloadErrorCode errorCode);

        void onFailure(Throwable error);
    }

    public enum DownloadErrorCode {
        FILE_EXISTS, CONNECTION_FAIL
    }

    public static File getDownloadFile(String url, String fileName, File fileDir) {
//        if (url.endsWith("?odconv/pdf")) {
//            url = url.replace("?odconv/pdf", "");
//        }
        return new File(fileDir.getAbsolutePath() + File.separator + fileName + /*url.substring(url.lastIndexOf("."))*/ ".pdf");
    }
}
