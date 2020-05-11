package droid.game.core.keep;

import droid.game.core.parameter.Parameter;

public interface IEventReceiver {
    interface IEvent{
        Parameter getRequest();
        Parameter getResponse();
        String getError();
    }
}
