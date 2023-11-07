package deepstream.ttrack.exception;

public class UsernameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private SysError sysError;

    public UsernameNotFoundException(SysError sysError) {
        this.sysError = sysError;
    }

    public UsernameNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UsernameNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public UsernameNotFoundException(String message) {
        super(message);
    }

    public SysError getSysError() {
        return sysError;
    }

    public void setSysError(SysError sysError) {
        this.sysError = sysError;
    }
}
