package upload;

public class FileUpload {
    String name;
    String path;
    Byte[] bytes;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public Byte[] getBytes() {
        return bytes;
    }
    
    public void setBytes(Byte[] bytes) {
        this.bytes = bytes;
    }
    
}
