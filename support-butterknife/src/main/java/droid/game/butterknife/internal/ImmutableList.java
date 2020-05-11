package droid.game.butterknife.internal;

import java.util.AbstractList;
import java.util.RandomAccess;

final class ImmutableList<T> extends AbstractList<T> implements RandomAccess {
    private final T[] views;

    ImmutableList(T[] views) {
        this.views = views;
    }

    @Override public T get(int index) {
        return views[index];
    }

    @Override public int size() {
        return views.length;
    }

    @Override public boolean contains(Object o) {
        for (T view : views) {
            if (view == o) {
                return true;
            }
        }
        return false;
    }
}
