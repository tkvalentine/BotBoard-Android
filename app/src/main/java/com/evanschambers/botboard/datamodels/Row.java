package com.evanschambers.botboard.datamodels;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Row {
    private static final String TAG = Row.class.getSimpleName();
    private static final String NODE_NAME = "row";
    private String uuid;

    private Hashtable<Integer, Widget> widgets = new Hashtable<Integer, Widget>();

    public Row() {
    }

    public static Row createDefaultRow() {
        Row newDefaultRow = new Row();
        int i = (int) (UUID.randomUUID().getLeastSignificantBits());
        i = i < 0 ? i * -1 : i;
        newDefaultRow.uuid = i + "";

        Widget newDefaultWidget = Widget.createDefaultWidget();
        newDefaultRow.widgets.put(Integer.parseInt(newDefaultWidget.getUuid()), newDefaultWidget);

        return newDefaultRow;
    }

    public static Row createDefaultDashboardRow() {
        Row newDefaultRow = new Row();
        int i = (int) (UUID.randomUUID().getLeastSignificantBits());
        i = i < 0 ? i * -1 : i;
        newDefaultRow.uuid = i + "";

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid1) {
        this.uuid = uuid1;
    }

    public Hashtable<Integer, Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(Hashtable<Integer, Widget> widgets) {
        this.widgets = widgets;
    }
}
