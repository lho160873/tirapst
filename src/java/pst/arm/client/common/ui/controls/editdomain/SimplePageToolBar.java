package pst.arm.client.common.ui.controls.editdomain;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import java.util.List;
import pst.arm.client.modules.images.ArmImages;

/**
 *
 * @author vorontsov
 */
public class SimplePageToolBar extends ToolBar {

    protected int buttonWidth = 25;
    protected int textWidth = 30;
    protected Button toFirstButton;
    protected Button toLastButton;
    protected Button toNextButton;
    protected Button toPrevButton;
    protected NumberField currentPageField;
    protected Label totalPagesLabel;
    protected KeyListener currentPageKeyListner;
    protected SelectionListener<ButtonEvent> toFirstListener;
    protected SelectionListener<ButtonEvent> toLastListener;
    protected SelectionListener<ButtonEvent> toNextListener;
    protected SelectionListener<ButtonEvent> toPrevListener;
    protected ArmImages images;
    protected int totalPageCount;
    protected int currentPage;
    protected int initRow;
    protected int currentRow;
    protected Grid<BeanModel> grid;
    protected List<BeanModel> models;

    public SimplePageToolBar() {
        images = (ArmImages) GWT.create(ArmImages.class);

        initListners();

        initControls();

        currentPage = -1;

        currentRow = -1;

        initRow = -1;
    }

    public SimplePageToolBar(Grid<BeanModel> grid) {
        this(grid, Boolean.FALSE);
    }

    public SimplePageToolBar(Grid<BeanModel> grid, Boolean withoutPageChanged) {
        this();
        if (grid != null) {
            this.grid = grid;

            List<BeanModel> selItems = grid.getSelectionModel().getSelectedItems();
            if ((selItems == null) || (selItems.isEmpty()) || (selItems.size() == 1)) {
                models = grid.getStore().getModels();
            } else {
                models = selItems;
            }

            setTotalPageCount(models.size());

            int gridCursorPos = getGridCursorPos();
            if (gridCursorPos != -1) {
                BeanModel at = grid.getStore().getAt(gridCursorPos);
                int index = models.indexOf(at);
                setCurrentPage(index, withoutPageChanged);
            }
        }
    }

    public int getGridCursorPos() {
        if (grid != null) {
            String cursor = null;
            for (int i = 0; i < grid.getStore().getCount(); i++) {
                cursor = grid.getView().getCell(i, 0).getInnerHTML();
                if ((cursor != null) && (!cursor.isEmpty()) && (cursor.contains("selected-row"))) 
                {
                    return i;
                }
            }
        }

        return -1;
    }

    protected void initListners() {
        currentPageKeyListner = new KeyListener() {

            @Override
            public void componentKeyUp(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    int tempValue = currentPageField.getValue().intValue() - 1;
                    onGoToPage(tempValue);
                }

                if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    setCurrentPage(currentPage);
                }
            }
        };

        setToFirstListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                onSelectFirst();
            }
        });

        setToLastListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                onSelectLast();
            }
        });

        setToNextListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                onSelectNext();
            }
        });

        setToPrevListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                onSelectPrev();
            }
        });

    }

    protected void initControls() {
        setBorders(false);
        
        toFirstButton = new Button();
        toFirstButton.setIcon(images.nav_first());
        toFirstButton.addSelectionListener(getToFirstListener());
        toFirstButton.setWidth(buttonWidth);
        toFirstButton.setToolTip("Первая страница");

        toPrevButton = new Button();
        toPrevButton.setIcon(images.nav_previous());
        toPrevButton.addSelectionListener(getToPrevListener());
        toPrevButton.setWidth(buttonWidth);
        toPrevButton.setToolTip("Предыдущая страница");

        currentPageField = new NumberField();
        currentPageField.setWidth(textWidth);
        currentPageField.setAllowDecimals(false);
        currentPageField.setAllowNegative(false);
        currentPageField.setSelectOnFocus(true);
        currentPageField.addKeyListener(currentPageKeyListner);
        currentPageField.setToolTip("Текущяя страница");

        totalPagesLabel = new Label();
        totalPagesLabel.setAutoWidth(true);
        totalPagesLabel.setText("из ");

        toNextButton = new Button();
        toNextButton.setIcon(images.nav_next());
        toNextButton.addSelectionListener(getToNextListener());
        toNextButton.setWidth(buttonWidth);
        toNextButton.setToolTip("Следующая страница");

        toLastButton = new Button();
        toLastButton.setIcon(images.nav_last());
        toLastButton.addSelectionListener(getToLastListener());
        toLastButton.setWidth(buttonWidth);
        toLastButton.setToolTip("Последняя страница");

        add(toFirstButton);
        add(toPrevButton);
        add(currentPageField);
        add(new Text("&nbsp&nbsp&nbsp"));
        add(totalPagesLabel);
        add(new Text("&nbsp&nbsp&nbsp"));
        add(toNextButton);
        add(toLastButton);
    }

    protected void changeNaviButtonsState() {
        if (currentPage > 0) {
            toFirstButton.setEnabled(true);
            toPrevButton.setEnabled(true);
        } else {
            toFirstButton.setEnabled(false);
            toPrevButton.setEnabled(false);
        }

        if (currentPage < (totalPageCount - 1)) {
            toLastButton.setEnabled(true);
            toNextButton.setEnabled(true);
        } else {
            toLastButton.setEnabled(false);
            toNextButton.setEnabled(false);
        }
    }

    public boolean isReadyForPageChange(int currentPage) {
        return true;
    }

    public void beforePageChanged() {
    
    }
    public void onPageChanged() {
    }

    public void onSelectFirst() {
        if (currentPage != 0 && totalPageCount > 0) {
            if (!isReadyForPageChange(0)) {
                return;
            }
            setCurrentPage(0);
        }
    }

    public void onSelectLast() {
        if (currentPage < (totalPageCount - 1)) {
            if (!isReadyForPageChange(totalPageCount - 1)) {
                return;
            }
            setCurrentPage(totalPageCount - 1);
        }
    }

    public void onSelectNext() {
        if (currentPage < (totalPageCount - 1)) {
            if (currentPage != -1) { // only if this is not first init
                if (!isReadyForPageChange(currentPage + 1)) {
                    return;
                }
            }

            setCurrentPage(++currentPage);
        }
    }

    public void onSelectPrev() {
        if (currentPage >= 1) {
            if (currentPage != -1) { // only if this is not first init
                if (!isReadyForPageChange(currentPage - 1)) {
                    return;
                }
            }

            setCurrentPage(--currentPage);
        }
    }

    public void onGoToPage(int toPage) {
        if ((toPage >= 0) && (toPage < totalPageCount) && toPage != currentPage) {
            if (!isReadyForPageChange(toPage)) {
                currentPageField.setValue(currentPage+1);
                return;
            }
            setCurrentPage(toPage);
        }
    }

    public SelectionListener<ButtonEvent> getToFirstListener() {
        return toFirstListener;
    }

    public void setToFirstListener(SelectionListener<ButtonEvent> toFirstListener) {
        this.toFirstListener = toFirstListener;
    }

    public SelectionListener<ButtonEvent> getToLastListener() {
        return toLastListener;
    }

    public void setToLastListener(SelectionListener<ButtonEvent> toLastListener) {
        this.toLastListener = toLastListener;
    }

    public SelectionListener<ButtonEvent> getToNextListener() {
        return toNextListener;
    }

    public void setToNextListener(SelectionListener<ButtonEvent> toNextListener) {
        this.toNextListener = toNextListener;
    }

    public SelectionListener<ButtonEvent> getToPrevListener() {
        return toPrevListener;
    }

    public void setToPrevListener(SelectionListener<ButtonEvent> toPrevListener) {
        this.toPrevListener = toPrevListener;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;

        totalPagesLabel.setText("из " + (totalPageCount));
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCursorToDefaultPos() {
        if ((grid != null) && (initRow != -1)) {
            grid.getView().getCell(currentRow, 0).setInnerHTML("");
            grid.getView().getCell(initRow, 0).setInnerHTML("<div class='selected-row'/>");
            grid.getView().ensureVisible(initRow, 0, false);
            grid.getView().layout();
        }
    }

    public BeanModel getCurrentModel() {
        if ((currentPage < 0) || (currentPage > (models.size() - 1))) {
            return null;
        }

        return models.get(currentPage);
    }

    public void setCurrentModel(BeanModel model) {
        if ((currentPage < 0) || (currentPage > (models.size() - 1))) {
            return;
        }

        models.set(currentPage, model);
    }

    public void setCurrentPage(int page) {
        setCurrentPage(page, Boolean.FALSE);
    }

    public void setCurrentPage(int page, Boolean withPageChanged) {
        beforePageChanged();
        this.currentPage = page;
        currentPageField.setValue(page + 1); // +1 - difference beetween prgrammer's & user's number manipulation
        changeNaviButtonsState();

        if (!withPageChanged) {
            onPageChanged();
        }
        syncGridCursor();
    }

    protected void syncGridCursor() {
        BeanModel model = getCurrentModel();
        if (model == null) {
            return;
        }

        if (grid == null) {
            return;
        }

        int index = grid.getStore().indexOf(model);
        if (index == -1) {
            return;
        }

        if (currentRow != -1) {
            grid.getView().getCell(currentRow, 0).setInnerHTML("");
        }

        grid.getView().ensureVisible(index, 0, false);
        grid.getView().getCell(index, 0).setInnerHTML("<div class='selected-row'/>");

        currentRow = index;

        if (initRow == -1) {
            initRow = currentRow;
        }
    }

    /**
     * @return the grid
     */
    public Grid<BeanModel> getGrid() {
        return grid;
    }

    /**
     * @param grid the grid to set
     */
    public void setGrid(Grid<BeanModel> grid) {
        this.grid = grid;
    }
    
    
    
    
    
}
