package pst.arm.client.common.exception;

import java.io.Serializable;

/**
 *
 * @author vorontsov
 */
public class RpcServiceException extends Exception implements Serializable {

    protected String hint;
    protected String info;
    private Integer errCode = null;

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public RpcServiceException() {
    }

    public RpcServiceException(String message) {
        super(message);
    }

    public RpcServiceException(String message, String hint) {
        super(message);
        this.hint = hint;
    }

    public RpcServiceException(String message, String hint, Integer errCode) {
        super(message);
        this.hint = hint;
        this.errCode = errCode;
    }

    public RpcServiceException(String message, String hint, String info) {
        super(message);
        this.hint = hint;
        this.info = info;
    }

    public RpcServiceException(String message, String hint, String info, Integer errCode) {
        super(message);
        this.hint = hint;
        this.info = info;
        this.errCode = errCode;
    }

    public RpcServiceException(Exception e) {
        super(e.getMessage());
    }

    /**
     * @return the hint
     */
    public String getHint() {
        return hint;
    }

    /**
     * @param hint the hint to set
     */
    public void setHint(String hint) {
        this.hint = hint;
    }
  /**
     * @return the hint
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param hint the hint to set
     */
    public void setInfo(String info) {
        this.info = info;
    }
}
