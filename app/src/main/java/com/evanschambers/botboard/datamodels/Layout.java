package com.evanschambers.botboard.datamodels;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Layout extends BaseDataModel {
    private static final String TAG = Layout.class.getSimpleName();
    private static final String NODE_NAME = "layout";

    private Hashtable<Integer, Row> rows = new Hashtable<Integer, Row>();

    public Layout() {
        super();
    }

    public static Layout createDefaultLayout() {
        Layout newDefaultLayout = new Layout();

        Row newDefaultRow = Row.createDefaultRow();
        newDefaultLayout.rows.put(Integer.parseInt(newDefaultRow.getUuid()), newDefaultRow);

        return newDefaultLayout;
    }

    public static Layout createDefaultDashboardLayout() {
        Layout newDefaultLayout = new Layout();

        Row newDefaultRow = Row.createDefaultDashboardRow();
        newDefaultLayout.rows.put(Integer.parseInt(newDefaultRow.getUuid()), newDefaultRow);

        return newDefaultLayout;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        String rowsJSON = getRowsJSONValue();
        String myJSONValue = getThisJSONValue(rowsJSON);

        return NODE_NAME + ":" + myJSONValue;
    }

    public String toJSONValueString() {
        //without the node title - i.e. NODE_NAME + ":" + myJSONValue
        String rowsJSON = getRowsJSONValue();
        String myJSONValue = getThisJSONValue(rowsJSON);

        return myJSONValue;
    }

    private String getRowsJSONValue() {
        String rowJSON = "[";

        if (rows != null && rows.size() > 0) {
            Collection<Row> theRows = rows.values();
            Iterator<Row> iterator = theRows.iterator();

            while (iterator.hasNext()) {
                Row aRow = iterator.next();
                String aRowJSON = aRow.toJSONValueString();
                rowJSON += aRowJSON;
                if (iterator.hasNext()) {
                    rowJSON += ", ";
                }
            }
        }
        rowJSON += "]";

        return rowJSON;
    }

    private String getThisJSONValue(String theRowsJSONValue) {
        String myJSONValue =
                "{" +
                        "\"uuid\":\"" + uuid + "\", " +
                        "\"rows\":" + theRowsJSONValue + ", " +
                        "}";

        return myJSONValue;
    }

    public Hashtable<Integer, Row> getRows() {
        return rows;
    }

    public void setRows(Hashtable<Integer, Row> rows) {
        this.rows = rows;
    }
}
