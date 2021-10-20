package io.cuki.global.util;

import lombok.Getter;

import java.util.List;

@Getter
public class SliceCustom<T> {

    private final List<T> content;
    private final boolean hasNext;

    public SliceCustom(List<T> content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
}
