package pst.arm.client.common.domain.reports;

import java.io.Serializable;

/**
 *
 * @author vorontsov
 */
public class FileObjectDescriptor implements Serializable {
    protected String fileName;
    protected String fileContentType;
    protected String fileExt;
    protected byte[] fileContent;

    public FileObjectDescriptor() {
        fileName = "Report";
        fileExt = "doc";
        fileContentType = "application/msword";
    }

    public FileObjectDescriptor(byte[] fileContent) {
        this();

        this.fileContent = fileContent;
    }

    public FileObjectDescriptor(String fileName, String fileContentType, String fileExt, byte[] fileContent) {
        this();

        this.fileName = fileName;
        this.fileContentType = fileContentType;
        this.fileExt = fileExt;
        this.fileContent = fileContent;
    }

    
    public FileObjectDescriptor(String fileName, String fileContentType, byte[] fileContent) {
        this();
        
        this.fileName = fileName;
        this.fileContentType = fileContentType;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }
}
