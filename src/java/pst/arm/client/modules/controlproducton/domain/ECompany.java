package pst.arm.client.modules.controlproducton.domain;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public enum ECompany {

    RIMR(1, "НТО ОАО «РИМР»"), MART(3, "НТО ОАО «МАРТ»");
    private Integer id;
    private String name;

    private ECompany(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
