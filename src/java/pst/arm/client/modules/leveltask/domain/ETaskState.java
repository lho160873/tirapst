package pst.arm.client.modules.leveltask.domain;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public enum ETaskState {

    NONE(null),
    CREATE(0), //поставлена
    IN_WORK(1), //в работе
    EXECUTED(2),//выполнена
    CLOSE(3),//закрыта
    CANCEL(4),//отменена
    IN_CREATE(5);//в разработке
    private Integer taskState;

    ETaskState(Integer t) {
        this.taskState = t;
    }

    public Integer getTaskState() {
        return taskState;
    }
    
    public static String getTaskStateName(ETaskState e) {
        String name = "";
        switch (e) {
            case CREATE:
                name = "поставлена";
                break;
            case IN_WORK:
                name = "в работе";
                break;
            case EXECUTED:
                name = "выполнена";
                break;
            case CLOSE:
                name = "закрыта";
                break;
            case CANCEL:
                name = "отменена";
                break;
            case IN_CREATE:
                name = "в разработке";
                break;
            default:
                name = "не определено";
                break;
        }
        return name;
    }
}
