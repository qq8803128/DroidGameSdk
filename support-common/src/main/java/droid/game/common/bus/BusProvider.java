package droid.game.common.bus;

import droid.game.open.source.otto.Bus;

public final class BusProvider extends Bus {
    public static BusProvider get(){
        return Holder.holder;
    }

    private static class Holder{
        private static final BusProvider holder = new BusProvider();
    }
}
