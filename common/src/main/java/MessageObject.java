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

    static AuthAcceptObject getAuthAccept() {
        return new AuthAcceptObject();
    }

    static AuthErrorObject getAuthError(String authError) {
        return new AuthErrorObject(authError);
    }

    static MsgFormatErrorObject getMsgFormatError() {
        return new MsgFormatErrorObject();
    }

    static AddFileObject getAddFileObject(String fileName, int fileSize, byte ... file) {
        return new AddFileObject(fileName, fileSize, file);
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

    @Override
    public String toString() {
        return "Client is connected";
    }

}

class AuthErrorObject extends MessageObject {

    private String authError;

    AuthErrorObject(String authError) {
        this.authError = authError;
    }

    @Override
    public String toString() {
        return authError;
    }
}

class MsgFormatErrorObject extends MessageObject {

    @Override
    public String toString() {
        return "Error object message format";
    }
}

class AddFileObject extends MessageObject {

    private String fileName;
    private int fileSize;
    private byte[] file;

    AddFileObject(String fileName, int fileSize, byte... file){
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.file = file;
    }

    String getFileName() {
        return fileName;
    }

    int getFileSize() {
        return fileSize;
    }

    byte[] getFile() {
        return file;
    }
}