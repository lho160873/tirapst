package pst.arm.client.common.domain;

import java.io.Serializable;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class FsFile implements Serializable{
    private Integer size;
    private String md5;
    private String relativeStoreDir;
    private java.io.File file;
    private Integer fsFileId;

    public FsFile() {
    }

    /**
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * @return the md5
     */
    public String getMd5() {
        return md5;
    }

    /**
     * @param md5 the md5 to set
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    /**
     * @return the relativeStoreDir
     */
    public String getRelativeStoreDir() {
        return relativeStoreDir;
    }

    /**
     * @param relativeStoreDir the relativeStoreDir to set
     */
    public void setRelativeStoreDir(String relativeStoreDir) {
        this.relativeStoreDir = relativeStoreDir;
    }

    /**
     * @return the file
     */
    public java.io.File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(java.io.File file) {
        this.file = file;
    }

    /**
     * @return the fsFileId
     */
    public Integer getFsFileId() {
        return fsFileId;
    }

    /**
     * @param fsFileId the fsFileId to set
     */
    public void setFsFileId(Integer fsFileId) {
        this.fsFileId = fsFileId;
    }
    
    
}
