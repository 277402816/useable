package com.roiland.crm.sm.utils;

import java.util.List;

public interface ReleasableList<E> extends List<E> {
    void acquire();
    void release();
    boolean isExpired();
    int getSemaphore();

    boolean isDirty();
    void setDirty(boolean newValue);
}
