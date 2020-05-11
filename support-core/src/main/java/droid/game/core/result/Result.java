package droid.game.core.result;

import droid.game.core.keep.IEventReceiver;
import droid.game.core.parameter.Parameter;

public class Result implements IEventReceiver.IEvent {
    private Parameter mRequest;
    private Parameter mResponse;
    private String mError;
    private boolean mSuccess;

    public Result(boolean success,Parameter request,Parameter response,String error) {
        super();
        mRequest = request;
        mResponse = response;
        mError = error;
        mSuccess = success;
    }

    @Override
    public Parameter getRequest() {
        return mRequest;
    }

    @Override
    public Parameter getResponse() {
        return mResponse;
    }

    @Override
    public String getError() {
        return mError;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public static final class Init extends Result{
        public Init(boolean success,Parameter request, Parameter response, String error) {
            super(success,request, response, error);
        }
    }

    public static final class Login extends Result{

        public Login(boolean success,Parameter request, Parameter response, String error) {
            super(success,request, response, error);
        }
    }

    public static final class Logout extends Result{

        public Logout(boolean success,Parameter request, Parameter response, String error) {
            super(success,request, response, error);
        }
    }

    public static final class Role extends Result{
        public Role(boolean success,Parameter request, Parameter response, String error) {
            super(success,request, response, error);
        }
    }

    public static final class Pay extends Result{
        public Pay(boolean success,Parameter request, Parameter response, String error) {
            super(success,request, response, error);
        }
    }

    public static final class Exit extends Result{
        public Exit(boolean success,Parameter request, Parameter response, String error) {
            super(success,request, response, error);
        }
    }

    public static final class Exec extends Result{
        public Exec(boolean success,Parameter request, Parameter response, String error) {
            super(success,request, response, error);
        }
    }
}
