package com.jaedeuk.notepad.util;

import com.jaedeuk.notepad.model.Memo;

/**
 * 이벤트 버스 객체
 */

public class PushEvent {
    Memo memo;
    int position;

    public PushEvent(Memo memo, int position) {
        this.memo = memo;
        this.position = position;
    }

    public Memo getMemo() {
        return memo;
    }

    public int getPosition() {
        return position;
    }
}
