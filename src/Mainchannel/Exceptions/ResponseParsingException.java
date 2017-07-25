//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.Exceptions;

public class ResponseParsingException extends Exception {
    public ResponseParsingException() {
    }

    public ResponseParsingException(String message) {
        super("Response foramt is invalid: " + message);
    }

    public ResponseParsingException(Throwable cause) {
        super(cause);
    }

    public ResponseParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
