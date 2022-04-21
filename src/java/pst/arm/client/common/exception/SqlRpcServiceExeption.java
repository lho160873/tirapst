package pst.arm.client.common.exception;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author root
 */
public class SqlRpcServiceExeption extends RpcServiceException {

    protected Map<Integer, String> map = new HashMap<Integer, String>();
    protected Integer errorCode = null;
    protected boolean mode_errorCode = false;
    
    public SqlRpcServiceExeption() {
        super();
    }

    public SqlRpcServiceExeption(String message) {
        super(message);
    }
    
    public SqlRpcServiceExeption(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    @Override
    public String getMessage() {
        if( mode_errorCode ) {
            return errorCode.toString();
        }
        else return findMessage(errorCode);
    }
    
    public void setModeErrorCode(boolean mode_errorCode) {
        this.mode_errorCode = mode_errorCode;
    }
    
    public boolean getModeErrorCode() {
        return mode_errorCode;
    }
    
    public Integer getErrorCode() {
        return errorCode;
    }

    public void fillMap(Map<Integer, String> map) {
        this.map.putAll(map);
    }

    String findMessage(Integer errorCode) {
        Set set = map.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            int key = (Integer) m.getKey();
            if (key == errorCode.intValue()) {
                return (String) m.getValue();
            }
        }
        return super.getMessage();
    }
}
