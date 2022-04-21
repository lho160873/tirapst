package pst.arm.client.modules.aiscontracts.domain;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public enum SyncOperationType {

    create("+"), update("*"), skipped("~"), delete("-"), equals("=");
    private String sign;

    SyncOperationType(String sign) {
        this.sign = sign;
    }

    public String sign() {
        return sign;
    }
}
