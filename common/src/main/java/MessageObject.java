import java.io.Serializable;

abstract class MessageObject implements Serializable {

//    auth_request
//    auth_accept
//    auth_error
//    msg_format_error
//    add_file
//    copy_file
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

    static FileAddObject getFileAddObject(String fileName, long fileSize, byte... file) {
        return new FileAddObject(fileName, fileSize, file);
    }

    static FilesListObject getFilesListObject(String[] filesList) {
        return new FilesListObject(filesList);
    }

    static FileDeleteObject getFileDeleteObject (String fileName){
        return new FileDeleteObject(fileName);
    }

    static  FileCopyObject getFileCopyObject (String fileName){
        return new  FileCopyObject(fileName);
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

class FileAddObject extends MessageObject {

    private String fileName;
    private long fileSize;
    private byte[] file;

    FileAddObject(String fileName, long fileSize, byte... file) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.file = file;
    }

    String getFileName() {
        return fileName;
    }

    long getFileSize() {
        return fileSize;
    }

    byte[] getFile() {
        return file;
    }
}

class FilesListObject extends MessageObject {

    private String[] filesList;

    FilesListObject(String[] filesList){
        this.filesList = filesList;
    }

    String[] getFilesList() {
        return filesList;
    }
}

class FileDeleteObject extends MessageObject{

    private  String fileName;

    FileDeleteObject(String fileName){
        this.fileName = fileName;
    }

    String getFileName() {
        return fileName;
    }
}

class FileCopyObject extends  MessageObject{

    private  String fileName;

    FileCopyObject(String fileName){
        this.fileName = fileName;
    }

    String getFileName() {
        return fileName;
    }
}