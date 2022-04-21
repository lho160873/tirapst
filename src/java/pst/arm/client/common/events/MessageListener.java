package pst.arm.client.common.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.StatusCodeException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.atmosphere.gwt.client.AtmosphereListener;
import pst.arm.client.common.lang.BaseMessages;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class MessageListener implements AtmosphereListener {

    Logger logger = Logger.getLogger(getClass().getName());
    private final static BaseMessages BASE_MESSAGES = GWT.create(BaseMessages.class);

    @Override
    public void onConnected(int heartbeat, int connectionID) {
        logger.info("comet.connected [" + heartbeat + ", " + connectionID + "]");
//            displayCookies();
//            toggleStartStop(true);
    }

    @Override
    public void onBeforeDisconnected() {
        //logger.log(Level.INFO, "comet.beforeDisconnected");
    }

    @Override
    public void onDisconnected() {
        logger.info("comet.disconnected");
//            toggleStartStop(false);
    }

    @Override
    public void onError(Throwable exception, boolean connected) {
        int statuscode = -1;
        if (exception instanceof StatusCodeException) {
            statuscode = ((StatusCodeException) exception).getStatusCode();
        }
        //MessageBox.info("MessageListener", "comet.error [connected=" + connected + "] (" + statuscode + ") ExceptionMessage:" + exception.getMessage(), null);
        logger.log(Level.SEVERE, "comet.error [connected=" + connected + "] (" + statuscode + ")", exception);
    }

    @Override
    public void onHeartbeat() {
        //logger.info("comet.heartbeat [" + client.getConnectionID() + "]");
    }

    @Override
    public void onRefresh() {
        //logger.info("comet.refresh [" + client.getConnectionID() + "]");
    }

    @Override
    public void onAfterRefresh() {
        //logger.info("comet.afterRefresh [" + client.getConnectionID() + "]");
    }

    @Override
    public void onMessage(List<?> messages) {
       /* StringBuilder result = new StringBuilder();
        for (Object obj : messages) {
            result.append(obj.toString()).append("<br/>");
        }
        //logger.log(Level.INFO, "comet.message [" + client.getConnectionID() + "] " + result.toString());
        Info.display(BASE_MESSAGES.msgInfoIn(), result.toString());*/
    }
}