package com.easyapp.util;

import android.app.Application;
import android.content.Context;

import com.easyapp.core.TypeValidator;
import com.easyapp.util.FileUtils;
import com.easyapp.util.StreamUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DebugAssistant {

    private final static String EXTENSION = ".log";

    public static Log lastLog(Context context) {
        try {
            return lastLogOrThrows(context);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Log> logs(Context context) {
        try {
            return logsOrThrows(context);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean deleteLogs(Context context) {
        return FileUtils.delete(
                getFile(context, getDefaultFolder(context))
        );
    }

    public static List<Log> filter(Context context, Class<? extends Throwable>... types) {
        try {
            return filterOrThrows(context, types);
        } catch (Exception e) {
            return null;
        }
    }

    public static Log lastLogOrThrows(Context context) throws Exception {
        List<Log> list = logsOrThrows(context);
        Collections.sort(list);
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    public static List<Log> logsOrThrows(Context context) throws Exception {
        return filesToListLog(getFileList(context, getDefaultFolder(context), FileUtils.FILTER_FILES));
    }

    public static List<Log> filterOrThrows(Context context, final Class<? extends Throwable>... types) throws Exception {
        return filesToListLog(getFileList(context, getDefaultFolder(context), new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        try {
                            Log log = deserialize(file);
                            for (Class<? extends Throwable> type : types) {
                                if (log.getTitle().equals(type.getSimpleName())) return true;
                            }
                        } catch (Exception ignore) {}
                        return false;
                    }
                }
        ));
    }

    public final static class Log implements Serializable, Comparable<Log> {

        private static final long serialVersionUID = 1L;

        private final String header;
        private final String title;
        private final String message;
        private final String cause;
        private final long timeStamp;
        private final File file;
        private final Throwable throwable;

        public Log(Throwable th, String header, File file) {
            this.header = header;
            this.title = th.getClass().getSimpleName();
            this.message = th.getMessage();
            this.cause = getCause(th);
            this.timeStamp = System.currentTimeMillis();
            this.file = file;
            this.throwable = th;
        }

        public String getHeader() {
            return header;
        }

        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }

        public String getCause() {
            return cause;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public long getSize() {
            return file.length();
        }

        public String getName() {
            return file.getName();
        }

        public Throwable getThrowable() {
            return throwable;
        }

        @Override
        public String toString() {
            return "Title:" + title + "\n" +
                    "Message:" + message + "\n\n" +
                    "Cause:" + cause;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Log) {
                Log log = (Log) object;
                return log.getThrowable().equals(throwable);
            }
            return false;
        }

        @Override
        public int compareTo(Log log) {
            return (int) (getTimeStamp() - log.getTimeStamp());
        }


        private static String getCause(Throwable throwable) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            for (Throwable th = throwable; th != null; th = th.getCause()) {
                th.printStackTrace(pw);
            }
            String allCauses = sw.toString();
            pw.close();
            return allCauses;
        }

    }


    public static abstract class LogApplication extends Application {

        private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

        @Override
        public final void onCreate() {
            this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(final Thread thread, final Throwable ex) {
                    try {
                        final File file = new File(
                                getFile(LogApplication.this, defaultFolder()),
                                resolveFileName(ex)
                        );
                        if (FileUtils.notExists(file)) {
                            FileUtils.createFile(file);
                        }
                        serialize(new Log(ex, defaultHeader(), file), file);

                    } catch (Exception e) {
                        uncaughtExceptionHandler.uncaughtException(thread, e);
                    } finally {
                        uncaughtExceptionHandler.uncaughtException(thread, ex);
                    }
                }
            });

            super.onCreate();
            onCreateApplication();
        }

        public void onCreateApplication() {
        }

        protected abstract String defaultFolder();

        protected String defaultHeader() {
            return null;
        }

        protected abstract Name defaultName();

        public enum Name {
            TIMESTAMP, CLASS
        }


        private String resolveFileName(Throwable th) {
            if (defaultName() == Name.CLASS) {
                return th.getClass().getSimpleName() + EXTENSION;
            }
            return System.currentTimeMillis() + EXTENSION;
        }

    }

    private static File getFile(Context context, String defaultFolder) {
        return new File(context.getFilesDir().getParent(), defaultFolder);
    }

    private static File[] getFileList(Context context, String defaultFolder, FileFilter filter) {
        if (filter == null) {
            return getFile(context, defaultFolder).listFiles();
        }
        return getFile(context, defaultFolder).listFiles(filter);
    }

    private static List<Log> filesToListLog(File[] files) throws Exception {
        List<Log> list = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(EXTENSION)) {
                    list.add(deserialize(file));
                }
            }
        }
        return list;
    }

    private static String getDefaultFolder(Context context) {
        TypeValidator.argumentNonNull(context, "The context cannot be null");
        context = context.getApplicationContext();
        TypeValidator.argumentCondition(
                context instanceof LogApplication,
                "The context must be extends LogApplication"
        );
        return ((LogApplication) context).defaultFolder();
    }


    private static void serialize(Log log, File file) throws Exception {
        final ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file)
        );
        oos.writeObject(log);
        oos.flush();
        StreamUtils.close(oos);
    }

    private static Log deserialize(File file) throws Exception {
        final ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file)
        );
        Log log = (Log) ois.readObject();
        StreamUtils.close(ois);
        return log;
    }
}
