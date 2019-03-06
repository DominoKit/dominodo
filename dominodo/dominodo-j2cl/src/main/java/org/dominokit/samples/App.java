package org.dominokit.samples;

import com.google.gwt.core.client.EntryPoint;
import org.gwtproject.safehtml.shared.annotations.GwtIncompatible;

public class App implements EntryPoint {

    public void onModuleLoad() {
        System.out.println();
        new DominoDo().run();
    }


    @GwtIncompatible()
    public void emptyPuppa() {
        System.out.println("PIPPO");
    }
}
