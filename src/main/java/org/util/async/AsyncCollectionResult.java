package org.util.async;

import java.util.Collection;

public interface AsyncCollectionResult<T, C extends Collection<T>> {

    void process(C completed, T value, int max);

    interface Finish<T, C extends Collection<T>> extends AsyncCollectionResult<T, C> {
        void complete(C collection);
    }
}
