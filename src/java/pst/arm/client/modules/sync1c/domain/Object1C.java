package pst.arm.client.modules.sync1c.domain;

import java.io.Serializable;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public abstract class Object1C implements Serializable {

    public enum ObjctType {

        Post, Department, Worker
    }

    public abstract Integer getId();

    public abstract String getText();
}
