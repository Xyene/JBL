package tk.jblib.bytecode.introspection.metadata;

import java.util.Collection;

public interface Metadatable<T> {
    public Collection<T> getMetadataInstances(String meta);

    public void removeMetadata(String meta);

    public Object getMetadata(String meta);

    public boolean hasMetadata(String meta);

    public <V> void addMetadata(String meta, V value);
}
