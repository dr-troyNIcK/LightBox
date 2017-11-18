import java.io.Serializable;

abstract class MessageObject implements Serializable {

//    auth_request
//    auth_accept
//    auth_error
//    msg_format_error
//    file
//    delete_file
//    file_list

    static AuthRequestObject getAuthRequest(String login, String password) {
        return new AuthRequestObject(login, password);
    }

    static AuthAcceptObject getAuthAccept(String login) {
        return new AuthAcceptObject(login);
    }

    static AuthErrorObject getAuthError(String login) {
        return new AuthErrorObject(login);
    }

    static MsgFormatErrorObject getMsgFormatError() {
        return new MsgFormatErrorObject();
    }
}

class AuthRequestObject extends MessageObject {

    private String login;
    private String password;

    AuthRequestObject(String login, String password) {
        this.login = login;
        this.password = password;
    }

    String getLogin() {
        return login;
    }

    String getPassword() {
        return password;
    }
}

class AuthAcceptObject extends MessageObject {

    private String login;

    AuthAcceptObject(String login){
        this.login = login;
    }

    @Override
    public String toString() {
        return "Client " + login + " connected";
    }

}

class AuthErrorObject extends MessageObject {

    private String login;

    AuthErrorObject(String login){
        this.login = login;
    }

    @Override
    public String toString() {
        return "Client " + login + " is not registered";
    }
}

class MsgFormatErrorObject extends MessageObject {

    @Override
    public String toString() {
        return "Error object message format";
    }
}