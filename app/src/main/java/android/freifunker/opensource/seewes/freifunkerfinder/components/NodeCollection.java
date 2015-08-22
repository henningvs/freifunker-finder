package android.freifunker.opensource.seewes.freifunkerfinder.components;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by Henning on 22.08.2015.
 */
public class NodeCollection {


    private LinkedHashMap<String, FFNode> nodeList;
    private Long area;

    public Collection<FFNode> getNodeList() {

        return nodeList.values();

    }

    public Long getArea() {
        return this.area;
    }

    public void setArea(Long area) {
        this.area = area;
    }
}
