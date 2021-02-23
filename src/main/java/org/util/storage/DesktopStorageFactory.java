package org.util.storage;

import com.gluonhq.attach.storage.StorageService;
import com.gluonhq.charm.down.ServiceFactory;

import java.util.Optional;

public class DesktopStorageFactory implements ServiceFactory<StorageService> {

    private final StorageService service;

    public DesktopStorageFactory(StorageService service) {
        this.service = service;
    }

    @Override
    public Class<StorageService> getServiceType() {
        return StorageService.class;
    }

    @Override
    public Optional<StorageService> getInstance() {
        return Optional.of(this.service);
    }
}
