package deepstream.ttrack.exception;

public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SysError getSysError() {
        return sysError;
    }

    public void setSysError(SysError sysError) {
        this.sysError = sysError;
    }

    public AuthenticationException(SysError sysError) {
        this.sysError = sysError;
    }

    public AuthenticationException(String message, SysError sysError) {
        super(message);
        this.sysError = sysError;
    }

    public AuthenticationException(String message, Throwable cause, SysError sysError) {
        super(message, cause);
        this.sysError = sysError;
    }

    public AuthenticationException(Throwable cause, SysError sysError) {
        super(cause);
        this.sysError = sysError;
    }

    public AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, SysError sysError) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.sysError = sysError;
    }

    private SysError sysError;

}
