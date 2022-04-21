package pst.arm.client.common.events;

import org.atmosphere.gwt.client.AtmosphereGWTSerializer;
import org.atmosphere.gwt.client.SerialTypes;

@SerialTypes(MessageEvent.class)
public abstract class MessageEventSerializer extends AtmosphereGWTSerializer {
}