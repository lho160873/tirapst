package pst.arm.client.common.ui.controls.store;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTFileServiceAsync;

/**
 *
 * @author maslov
 */
public class FilePanel extends FormPanel {

    protected CommonImages commonImages = Registry.get("commonImages");
    protected CommonConstants commonConstants = Registry.get("commonConstants");
    protected GWTFileServiceAsync service = Registry.get("fileService");
    private String name;
    private Integer labelWidth;
    private Boolean isEditable;
    private LayoutContainer parent;
    private FileUploadField fileUpload;
    private AdapterField afFileDownload;
    private LabelField lfFileEmpty;
    private LabelField lfFileName;
    private Integer fileId; // id текущего загруженного в БД файла
    private String fileName; //  имя текущего загруженного в БД файла
    private boolean isAddedFile = false; // пометка о том что файл отображаемый на форме, был добавлен в "этой сессии"
    private boolean isOldFileMustBeRemoves = false; // пометка для удаления старого файла при закрытии окна
    private Integer oldFileId; // id старого файла который надо удалить при закрытии окна.

    /**
     * панель загрузки/выгрузки файла в режиме редактирования доступна загрузка
     * и скачивание файла в режиме просмотра только скачивание файла
     * <p/>
     * при закрытии окна использующий данный контрол, надо позоботиться об
     * удалении мусора (старого файла или нового) т.е. по нажатию OK надо
     * удалить старый файл если таковой имеется, по CANCEL удалить новый
     * <p/>
     * @param editable - режим работы панели, true - редактирование, fasle -
     * посмотр
     * @param labelWidth
     */
    public FilePanel(String name, Integer labelWidth, LayoutContainer parent, Boolean editable) {

        this.name = name;
        this.labelWidth = labelWidth;
        this.parent = parent;
        this.isEditable = editable;
        initGUI();
        changeFilePanelView();
    }

    public Integer getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileIdAndName(Integer fileId, String fileName) {
        this.fileId = fileId;
        this.fileName = fileName;
        lfFileName.setValue(fileName);
        changeFilePanelView();
    }

    public Boolean isAddedFile() {
        return isAddedFile;
    }

    public Boolean isOldFileMustBeRemoves() {
        return isOldFileMustBeRemoves;
    }

    public Integer getOldFileId() {
        return oldFileId;
    }

    public FileUploadField getFileUploadField() {
        return fileUpload;
    }

    public void deleteOldFile(Listener listener) {
        deletingFile(oldFileId, listener);
    }

    public void deleteFile(Listener listener) {
        deletingFile(fileId, listener);
    }

    private void initGUI() {
        setHeaderVisible(Boolean.FALSE);
        setPadding(0);
        setBodyBorder(Boolean.FALSE);
        setLabelAlign(FormPanel.LabelAlign.RIGHT);
        setLabelWidth(labelWidth);
        setAction(GWT.getHostPageBaseURL() + "fileupload.htm");
        setMethod(FormPanel.Method.POST);
        setEncoding(FormPanel.Encoding.MULTIPART);
        addListener(Events.Submit, new Listener<FormEvent>() {
            @Override
            public void handleEvent(FormEvent be) {
                windowUnmask();
                String val = be.getResultHtml().toLowerCase();
                if (val.equalsIgnoreCase("null")) {
                    MessageBox.alert(commonConstants.error(), "errr", null);
                } else {
                    try {
                        onFileUpload(Integer.parseInt(val), fileUpload.getValue());
                    } catch (NumberFormatException e) {
                        MessageBox.alert(commonConstants.error(), "errr", null);
                    } catch (Exception e) {
                        MessageBox.alert(commonConstants.error(), "errr"
                                + "\r\nexception:" + e.getStackTrace(), null);
                    }
                }
            }
        });
        fileUpload = new FileUploadField() {
            @Override
            public void setValue(String value) {
                super.setValue(value);
                if (value != null && fileUpload.getRawValue() != null && !fileUpload.getRawValue().isEmpty()) {
                    submit();
                    windowMask();
                }
            }
        };
        fileUpload.setWidth(300);
        fileUpload.getMessages().setBrowseText("открыть");
        fileUpload.setName("file");
        fileUpload.setId("file");
        fileUpload.setFieldLabel(name);

        // init GUI for file download

        HBoxLayout lclayout = new HBoxLayout();
        lclayout.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.MIDDLE);
        lclayout.setPack(BoxLayout.BoxLayoutPack.START);
        lclayout.setPadding(new Padding(0));
        LayoutContainer fileDownload = new LayoutContainer(lclayout);
        fileDownload.setHeight(22);
        HBoxLayoutData hBoxLayoutData = new HBoxLayoutData(0, 4, 0, 0);

        Button btnOpen = new Button(commonConstants.open(), AbstractImagePrototype.create(commonImages.save()),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        onFileDownload();
                    }
                });
        fileDownload.add(btnOpen, hBoxLayoutData);

        if (isEditable) {
            Button btnDelete = new Button(commonConstants.delete(), AbstractImagePrototype.create(commonImages.delete()),
                    new SelectionListener<ButtonEvent>() {
                        @Override
                        public void componentSelected(ButtonEvent ce) {
                            onFileDelete();
                        }
                    });
            fileDownload.add(btnDelete, hBoxLayoutData);
        }
        lfFileName = new LabelField();
        fileDownload.add(lfFileName, hBoxLayoutData);
        afFileDownload = new AdapterField(fileDownload);
        afFileDownload.setFieldLabel(name);

        lfFileEmpty = new LabelField();
        lfFileEmpty.setFieldLabel(name);
        lfFileEmpty.setLabelSeparator(":");
    }

    private void changeFilePanelView() {
        removeAll();
        if (fileId != null) {
            add(afFileDownload, new FormData("100%"));
        } else if (isEditable) {
            add(fileUpload, new FormData("100%"));
        } else {
            add(lfFileEmpty, new FormData("100%"));
        }
        parent.layout();
    }

    private void onFileDownload() {
        if (fileId != null) {
            com.google.gwt.user.client.Window.Location.assign(GWT.getHostPageBaseURL() + "filedownload.htm?id=" + fileId);
        }
    }

    private void onFileUpload(Integer fileId, String fileName) {
        isAddedFile = true;

        this.fileId = fileId;
        this.fileName = fileName;
        lfFileName.setValue(fileName);
        changeFilePanelView();
    }

    private void onFileDelete() {
        if (!isAddedFile) { // если файл ещё небыл загружен в этой сессии
            // запоминаем файл который надо будет удалить при успешном закрытии окна
            isOldFileMustBeRemoves = true;
            oldFileId = fileId;

            fileId = null;
            fileName = null;
            fileUpload.setValue(null);
            lfFileName.setValue(fileName);
            changeFilePanelView();
        } else {
            deletingFile(fileId, new Listener() {
                @Override
                public void handleEvent(BaseEvent be) {
                    fileId = null;
                    fileName = null;
                    fileUpload.setValue(null);
                    lfFileName.setValue(fileName);
                    changeFilePanelView();
                }
            });
        }
    }

    private void deletingFile(Integer fileId, final Listener actionListener) {
        if (fileId != null) {
            windowMask();
            service.deleteFile(fileId, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    windowUnmask();
                }

                @Override
                public void onSuccess(Void result) {
                    windowUnmask();
                    if (actionListener != null) {
                        actionListener.handleEvent(new BaseEvent(null));
                    }
                }
            });
        }
    }

    private void windowMask() {
        parent.mask(commonConstants.loading());
    }

    private void windowUnmask() {
        parent.unmask();
    }
}
