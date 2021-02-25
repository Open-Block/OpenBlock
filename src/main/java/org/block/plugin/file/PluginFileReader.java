package org.block.plugin.file;

import org.block.jsf.data.jsfclass.JSFClass;
import org.block.plugin.Plugin;
import org.block.serialization.ConfigImplementation;
import org.util.async.AsyncCollectionResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginFileReader {

    private final InputStream stream;

    public PluginFileReader(InputStream stream) {
        this.stream = stream;
    }

    public Plugin readPlugin(AsyncCollectionResult<CollectionEventStore, Set<CollectionEventStore>> event) throws IOException {
        var json = new BufferedReader(new InputStreamReader(this.stream)).lines().collect(Collectors.joining("\n"));
        var rootNode = ConfigImplementation.JSON.load(json);
        var id = rootNode.getString(Plugin.ID_NODE).orElseThrow(() -> new IOException("Unknown Id"));
        var name = rootNode.getString(Plugin.NAME_NODE).orElseThrow(() -> new IOException("Unknown Name"));
        var version = rootNode.getString(Plugin.VERSION_NODE).orElseThrow(() -> new IOException("Unknown Version"));
        var plugin = new Plugin(id, name, version);
        var dependents = rootNode.getCollection(Plugin.CLASSES_NODE, new DependencyNodeParser());
        var eventStoreSet = new HashSet<DependentEvent>();
        for (var dependent : dependents) {
            eventStoreSet.add(new DependentEvent(new CollectionEventStore(dependent)) {
                @Override
                public void onProcess(CollectionEventStore store) {
                    event.process(eventStoreSet.parallelStream().map(e -> e.store).filter(s -> s.finished).collect(Collectors.toSet()), store, eventStoreSet.size());
                }

                @Override
                public void onComplete(CollectionEventStore store) {
                    plugin.getClasses().addAll(store.classes);
                    if (!(event instanceof AsyncCollectionResult.Finish)) {
                        return;
                    }
                    if (eventStoreSet.parallelStream().allMatch(e -> e.store.finished)) {
                        ((Finish<CollectionEventStore, Set<CollectionEventStore>>) event).complete(eventStoreSet.parallelStream().map(e -> e.store).collect(Collectors.toSet()));
                    }
                }
            });
        }
        new Thread(() -> eventStoreSet.parallelStream().forEach(e -> e.store.node.getClasses(e))).start();
        return plugin;
    }

    public static class CollectionEventStore {

        private final DependencyNode node;
        private Set<JSFClass> classes = new HashSet<>();
        private JSFClass lastComplete;
        private int max;
        private boolean finished;

        private CollectionEventStore(DependencyNode node) {
            this.node = node;
        }

        public DependencyNode getTarget() {
            return this.node;
        }

        public int getMax() {
            return this.max;
        }

        public Optional<JSFClass> getLastComplete() {
            return Optional.ofNullable(this.lastComplete);
        }

        public Set<JSFClass> getComplete() {
            return this.classes;
        }

    }

    private abstract static class DependentEvent implements AsyncCollectionResult.Finish<JSFClass, Set<JSFClass>> {

        private final CollectionEventStore store;

        public DependentEvent(CollectionEventStore store) {
            this.store = store;
        }

        public abstract void onProcess(CollectionEventStore store);

        public abstract void onComplete(CollectionEventStore store);

        @Override
        public void process(Set<JSFClass> completed, JSFClass value, int max) {
            this.store.classes = completed;
            this.store.max = max;
            this.store.lastComplete = value;
            this.onProcess(this.store);
        }

        @Override
        public void complete(Set<JSFClass> collection) {
            this.store.finished = true;
            this.store.classes = collection;
            this.onComplete(this.store);
        }
    }


}
