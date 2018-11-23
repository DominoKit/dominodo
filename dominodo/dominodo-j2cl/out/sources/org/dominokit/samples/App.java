package org.dominokit.samples;

import com.google.gwt.core.client.EntryPoint;

import java.util.Map;
import java.util.TreeMap;

public class App implements EntryPoint {

    private Map<String, String> map = new TreeMap<>();

    public void onModuleLoad() {
        map.put("x", "y");
        map.remove("x");
        map.values().remove("x");
        new DominoDo().run();
    }
}
