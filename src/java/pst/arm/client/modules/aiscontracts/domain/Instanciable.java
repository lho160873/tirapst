package pst.arm.client.modules.aiscontracts.domain;

/**
 * Created by akozhin on 21.12.14.
 */
public interface Instanciable<T> {
    public T newInstance();
}
