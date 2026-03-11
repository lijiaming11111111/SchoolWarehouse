package com.basketball.context;

import java.util.ArrayList;
import java.util.List;


public class FileUploadContext {

    private static final ThreadLocal<List<Long>> UPLOADED_FILE_IDS = ThreadLocal.withInitial(ArrayList::new);

    public static void addFileId(Long fileId) {
        UPLOADED_FILE_IDS.get().add(fileId);
    }

    public static List<Long> getFileIds() {
        return UPLOADED_FILE_IDS.get();
    }

    public static void setFileIds(List<Long> fileIds) {
        UPLOADED_FILE_IDS.set(fileIds);
    }

    public static void clear() {
        UPLOADED_FILE_IDS.remove();
    }

}
