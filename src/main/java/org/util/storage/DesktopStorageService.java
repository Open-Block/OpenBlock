package org.util.storage;

import com.gluonhq.attach.storage.StorageService;

import java.io.File;
import java.util.Optional;

public class DesktopStorageService implements StorageService {
    @Override
    public Optional<File> getPrivateStorage() {
        return Optional.of(new File(System.getProperty("user.home")));
    }

    @Override
    public Optional<File> getPublicStorage(String s) {
        return getPrivateStorage();
    }

    @Override
    public boolean isExternalStorageWritable() {
        if (getPrivateStorage().isEmpty()) {
            return false;
        }
        return getPrivateStorage().get().canWrite();
    }

    @Override
    public boolean isExternalStorageReadable() {
        if (getPrivateStorage().isEmpty()) {
            return false;
        }
        return getPrivateStorage().get().canRead();
    }
}
