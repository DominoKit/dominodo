package org.dominokit.samples;

import com.google.gwt.core.client.EntryPoint;
import org.gwtproject.safehtml.shared.annotations.GwtIncompatible;

public class App implements EntryPoint {

    public void onModuleLoad() {
        new DominoDo().run("puppa", "subpuppa");
    }


    @GwtIncompatible()
    public void emptyPuppa() {
        System.out.println("PIPPO IS SUPER STRONG");
    }
}
