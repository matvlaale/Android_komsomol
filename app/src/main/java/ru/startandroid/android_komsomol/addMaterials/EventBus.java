package ru.startandroid.android_komsomol.addMaterials;

import com.squareup.otto.Bus;

public class EventBus {
    private static Bus bus;

    public static Bus getBus() {
        if (bus == null) bus = new Bus();
        return bus;
    }
}
