package com.evanschambers.botboard.datamodels;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Row extends BaseDataModel {
    private static final String TAG = Row.class.getSimpleName();
    private static final String NODE_NAME = "row";

    private Hashtable<Integer, Widget> widgets = new Hashtable<Integer, Widget>();

    public Row() {
        super();
    }

    public static Row createDefaultRow() {
        Row newDefaultRow = new Row();

        Widget newDefaultWidget = Widget.createDefaultWidget();
        newDefaultRow.widgets.put(Integer.parseInt(newDefaultWidget.getUuid()), newDefaultWidget);

        return newDefaultRow;
    }

    public static Row createDefaultDashboardRow() {
        Row newDefaultRow = new Row();

        Widget newDefaultWidget = Widget.createDefaultDashboardWidget();
        newDefaultRow.widgets.put(Integer.parseInt(newDefaultWidget.getUuid()), newDefaultWidget);

        return newDefaultRow;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        String widgetsJSON = getWidgetsJSONValue();
        String myJSONValue = getThisJSONValue(widgetsJSON);

        return NODE_NAME + ":" + myJSONValue;
    }

    public String toJSONValueString() {
        //without the node title - i.e. NODE_NAME + ":" + myJSONValue
        String widgetsJSON = getWidgetsJSONValue();
        String myJSONValue = getThisJSONValue(widgetsJSON);

        return myJSONValue;
    }

    private String getWidgetsJSONValue() {
        String rowJSON = "[";

        if (widgets != null && widgets.size() > 0) {
            Collection<Widget> theWidgets = widgets.values();
            Iterator<Widget> iterator = theWidgets.iterator();

            while (iterator.hasNext()) {
                Widget aWidget = iterator.next();
                String aWidgetJSON = aWidget.toJSONValueString();
                rowJSON += aWidgetJSON;
                if (iterator.hasNext()) {
                    rowJSON += ", ";
                }
            }
        }
        rowJSON += "]";

        return rowJSON;
    }

    private String getThisJSONValue(String theWidgetsJSONValue) {
        String myJSONValue =
                "{" +
                        "\"uuid\":\"" + uuid + "\", " +
                        "\"widgets\":" + theWidgetsJSONValue + ", " +
                        "}";

        return myJSONValue;
    }

    public Hashtable<Integer, Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(Hashtable<Integer, Widget> widgets) {
        this.widgets = widgets;
    }
}
