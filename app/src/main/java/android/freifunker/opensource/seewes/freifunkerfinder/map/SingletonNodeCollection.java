package android.freifunker.opensource.seewes.freifunkerfinder.map;

import android.freifunker.opensource.seewes.freifunkerfinder.components.NodeCollection;

/**
 * Created by Henning von See on 22.08.2015.
 */

public class SingletonNodeCollection {

    private NodeCollection collection;

    private static final SingletonNodeCollection single = new SingletonNodeCollection();
    public static SingletonNodeCollection getInstance(){
        return single;
    }

    /**
     * @return the collection
     */
    public NodeCollection getCollection() {
        return collection;
    }

    /**
     * @param collection the collection to set
     */
    public void setCollection(NodeCollection collection) {
        this.collection = collection;
    }

}

