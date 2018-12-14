package cn.deepmax.easyquery.exception;




public class EasyQueryException extends RuntimeException {


    public EasyQueryException() {
    }

    public EasyQueryException(String message) {
        super(message);
    }


    public EasyQueryException(String message, Throwable cause) {
        super(message, cause);
    }


    public EasyQueryException(Throwable cause) {
        super(cause);
    }


    public EasyQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
