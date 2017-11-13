import java.io.Serializable;

public class MessageObject {

    private MessageObject() {
    }

//    auth_request
//    auth_accept
//    auth_error
//    file
//    delete_file
//    file_list
//    msg_format_error

    public static AuthRequestObject getAuthRequest(String login, String password) {
        return new AuthRequestObject(login, password);
    }

    public static AuthAcceptObject getAuthAccept(String login) {
        return new AuthAcceptObject(login);
    }

    public static AuthErrorObject getAuthError(String authErrorMsg) {
        return new AuthErrorObject(authErrorMsg);
    }

    public static MsgFormatErrorObject getMsgFormatError(String value) {
        return new MsgFormatErrorObject(value);
    }
}

class AuthRequestObject implements Serializable {

    private String login;
    private String password;

    AuthRequestObject(String login, String password) {
        this.login = login;
        this.password = password;
    }
}

class AuthAcceptObject implements Serializable {

    private String login;

    AuthAcceptObject(String login) {
        this.login = login;
    }
}

class AuthErrorObject implements Serializable {

    private String authErrorMsg;

    AuthErrorObject(String authErrorMsg) {
        this.authErrorMsg = authErrorMsg;
    }
}

class MsgFormatErrorObject implements Serializable {

    private String value;

    MsgFormatErrorObject(String value) {
        this.value = value;
    }
}