package pst.arm.client.common.ui.controls.editdomain;

import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.ui.form.Editable.EditState;

/**
 * конфигурация для базового окна DomainEditWindow
 * 
 * Устанавливает:
 * три возможных режима в котором м.б. окно
 * текущее состояние
 * и дополнительные настройки (кнопка копия и пр.)
 * 
 * @author maslov
 */
public final class DomainEditWindowConfig {

    private List<EditState> modes = new ArrayList<EditState>();
    private EditState state = EditState.VIEW;
    private Boolean copyEnabled = Boolean.FALSE;
    private Boolean saveAddEnabled = Boolean.FALSE;
    /**
     * 
     * @param modeAndState
     */
    public DomainEditWindowConfig(EditState modeAndState) {
        modes.clear();
        modes.add(modeAndState);
        if (EditState.EDIT.compareTo(modeAndState) == 0){
            enabledViewMode(true);
        }
        if (EditState.NEW.compareTo(modeAndState) == 0){
            enabledEditMode(true);
            enabledViewMode(true);
        }
        state = modeAndState;
    }

    public void enabledViewMode(Boolean enabled) {
        enabledMode(enabled, EditState.VIEW);
    }

    public void enabledEditMode(Boolean enabled) {
        enabledMode(enabled, EditState.EDIT);
    }

    public void enabledNewMode(Boolean enabled) {
        enabledMode(enabled, EditState.NEW);
    }

    private void enabledMode(Boolean enabled, EditState mode) {
        if (modes == null) {
            modes = new ArrayList<EditState>();
        }
        if (enabled && !modes.contains(mode)) {
            modes.add(mode);
        } else if (!enabled && modes.contains(mode)) {
            modes.remove(mode);
        }
    }

    public void setState(EditState state) {
        this.state = state;
    }

    public EditState getState() {
        return state;
    }

    public Boolean standByViewMode() {
        return modes.contains(EditState.VIEW);
    }

    public Boolean standByEditMode() {
        return modes.contains(EditState.EDIT);
    }

    public Boolean standByNewMode() {
        return modes.contains(EditState.NEW);
    }

    public Boolean standByMode(EditState mode) {
        return modes.contains(mode);
    }

    /**
     * @return the copyEnabled
     */
    public Boolean isCopyEnabled() {
        return copyEnabled;
    }

    /**
     * @param copyEnabled the copyEnabled to set
     */
    public void setCopyEnabled(Boolean copyEnabled) {
        this.copyEnabled = copyEnabled;
    }

    /**
     * @return the saveAddEnabled
     */
    public Boolean isSaveAddEnabled() {
        return saveAddEnabled;
    }

    /**
     * @param saveAddEnabled the saveAddEnabled to set
     */
    public void setSaveAddEnabled(Boolean saveAddEnabled) {
        this.saveAddEnabled = saveAddEnabled;
    }
}
