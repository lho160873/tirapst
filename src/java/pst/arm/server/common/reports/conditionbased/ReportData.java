package pst.arm.server.common.reports.conditionbased;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Artem Vorontsov
 */
public class ReportData {
    protected List<ReportDataRow> data;

    public ReportData() {
        data = new ArrayList<ReportDataRow>();
    }

    public void addRow(ReportDataRow row) {
        data.add(row);
    }

    public void setRow(int index, ReportDataRow row) {
        data.set(index, row);
    }

    public ReportDataRow getRow(int index) {
        ReportDataRow row = null;
        try {
            row = data.get(index);
        } catch(IndexOutOfBoundsException ex) {

        }

        return row;
    }

    public String getValueAt(int rowIndex, int colIndex) {
        String ret = null;
        ReportDataRow row = getRow(rowIndex);
        if(row == null) {
            return ret; // null
        }

        ret = row.getAt(colIndex);

        return ret;
    }

    public Integer getRecordCount() {
        return data == null ? null : data.size();
    }

    public void clear() {
        data.clear();
    }
    
    /**
     * Сортирует коллекци по заданному полю
     * @param numberOfColumn - Номер колонки, по которой происходит упорядочивание
     * @param reverse - По умолчанию сортировка по возрастанию. При reverse=true - инвертирование - сортировка по убыванию
     */
    public void sort(int numberOfColumn, Boolean reverse)
    {
        sort(numberOfColumn, reverse, -1, null);
    }
    
    /**
     *Сортирует коллекци по заданным полям
     * @param numberOfColumn2 - поле для сортировки, при равенстве значений в первом поле
     */
    public void sort(int numberOfColumn, Boolean reverse, int numberOfColumn2, Boolean reverse2)
    {
        try
        {
            List<ReportDataRow> temp = new ArrayList<ReportDataRow>(data.size());
            temp.add(data.get(0));
            for (int i = 1; i < data.size(); i++)
            {
                ReportDataRow nextRow = data.get(i);
                for (int j = 0; j < temp.size(); j++)
                {
                    if (!reverse)
                    {
                        if (nextRow.getAt(numberOfColumn).compareToIgnoreCase(temp.get(j).getAt(numberOfColumn)) < 0
                                || (numberOfColumn2 != -1 && reverse2 != null &&
                                nextRow.getAt(numberOfColumn).compareToIgnoreCase(temp.get(j).getAt(numberOfColumn)) == 0 &&
                                nextRow.getAt(numberOfColumn2).compareToIgnoreCase(temp.get(j).getAt(numberOfColumn2)) < 0))
                        {
                            temp.add(j, nextRow);
                            break;
                        }
                    }
                    else
                    {
                        if (nextRow.getAt(numberOfColumn).compareToIgnoreCase(temp.get(j).getAt(numberOfColumn)) > 0
                                || (numberOfColumn2 != -1 && reverse2 != null &&
                                nextRow.getAt(numberOfColumn).compareToIgnoreCase(temp.get(j).getAt(numberOfColumn)) == 0 &&
                                nextRow.getAt(numberOfColumn2).compareToIgnoreCase(temp.get(j).getAt(numberOfColumn2)) > 0))
                        {
                            temp.add(j, nextRow);
                            break;
                        }
                    }
                }
                if (temp.size() == i)
                {
                    temp.add(nextRow);
                }
            }
            data = temp;
        }
        catch(Exception e){}
    }
}
