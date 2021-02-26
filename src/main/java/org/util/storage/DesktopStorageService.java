package org.util.storage;

import com.gluonhq.attach.storage.StorageService;

import java.io.File;
import java.util.Optional;

public class DesktopStorageService implements StorageService {
    @Override
    public Optional<File> getPrivateStorage() {
        String appData = System.getenv("APPDATA");
        if (appData == null) {
            return Optional.empty();
        }
        return Optional.of(new File(appData, "OpenBlocks"));
    }

    @Override
    public Optional<File> getPublicStorage(String s) {
        return Optional.of(new File(System.getProperty("user.home"), "." + s));
    }

    @Override
    public boolean isExternalStorageWritable() {
        if (this.getPrivateStorage().isEmpty()) {
            return false;
        }
        return this.getPrivateStorage().get().canWrite();
    }

    @Override
    public boolean isExternalStorageReadable() {
        if (this.getPrivateStorage().isEmpty()) {
            return false;
        }
        return this.getPrivateStorage().get().canRead();
    }
}
