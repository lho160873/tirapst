package pst.arm.client.modules.ganttchart.lang;

/**
 *
 * @author nikita
 */
public interface GanttChartConstants extends com.google.gwt.i18n.client.Constants {

    @Key("ganttchart.heading")
    String heading();

    @Key("ganttchart.mbp.btn.addtask")
    String btnAddTask();

    @Key("ganttchart.mbp.btn.remtask")
    String btnRemTask();

    @Key("ganttchart.mbp.btn.shiftlefttask")
    String btnShiftLeftTask();

    @Key("ganttchart.mbp.btn.shiftrighttask")
    String btnShiftRightTask();

    @Key("ganttchart.mbp.btn.moveuptask")
    String btnMoveUpTask();

    @Key("ganttchart.mbp.btn.movedowntask")
    String btnMoveDownTask();

    @Key("ganttchart.mbp.btn.detailstask")
    String btnDetailsTask();

    @Key("ganttchart.mbp.btn.resourcestask")
    String btnResourcesTask();

    @Key("ganttchart.mbp.btn.zoominchart")
    String btnZoomInChart();

    @Key("ganttchart.mbp.btn.zoomoutchart")
    String btnZoomOutChart();

    @Key("ganttchart.mbp.btn.build")
    String btnBuild();

    @Key("ganttchart.dp.btn.savedetails")
    String btnSaveDetails();

    @Key("ganttchart.dp.btn.backdetails")
    String btnBackDetails();

    @Key("ganttchart.dp.lbl.details")
    String lblDetails();

    @Key("ganttchart.dp.lbl.taskname")
    String lblTaskName();

    @Key("ganttchart.dp.lbl.taskduration")
    String lblTaskDuration();

    @Key("ganttchart.dp.lbl.taskcomplete")
    String lblTaskComplete();

    @Key("ganttchart.dp.lbl.taskstart")
    String lblTaskStart();

    @Key("ganttchart.dp.lbl.taskfinish")
    String lblTaskFinish();

    @Key("ganttchart.dp.lbl.taskpredecessors")
    String lblTaskPredecessors();

    @Key("ganttchart.dp.btn.addpredecessor")
    String btnAddPredecessor();

    @Key("ganttchart.dp.btn.rempredecessor")
    String btnRemPredecessor();

    @Key("ganttchart.dp.col.predecessorname")
    String colPredecessorName();

    @Key("ganttchart.dp.col.predecessortype")
    String colPredecessorType();

    @Key("ganttchart.dp.lbl.taskresourcesname")
    String lblTaskResourcesName();

    @Key("ganttchart.dp.btn.taskaddresource")
    String btnTaskAddResource();

    @Key("ganttchart.dp.btn.taskremresource")
    String btnTaskRemResource();

    @Key("ganttchart.rp.lbl.resourcesname")
    String lblResourcesName();

    @Key("ganttchart.rp.btn.backresource")
    String btnBackResource();

    @Key("ganttchart.rp.btn.addresource")
    String btnAddResource();

    @Key("ganttchart.rp.btn.remresource")
    String btnRemResource();
}
