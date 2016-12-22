package com.jaedeuk.notepad.util;

import com.squareup.otto.Bus;

public final class BusProvider {
    private static final Bus BUS = new Bus();

    //이벤트 버스 싱글턴 객체
    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}