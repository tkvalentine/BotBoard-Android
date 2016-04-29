package com.evanschambers.botboard.datamodels;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Widget extends BaseDataModel {
    private static final String TAG = Widget.class.getSimpleName();
    private static final String NODE_NAME = "widget";

    private String header;
    private String type;
    private Integer columns;

    private String labels;
    private String colors;
    private String imagePath;
    private String sources;
    private String event;

    private Hashtable<Integer, Widget> widgets = new Hashtable<Integer, Widget>();

    public Widget() {
        super();
    }

    public static Widget createDefaultWidget() {
        Widget newDefaultWidget = new Widget();

        newDefaultWidget.header = "Default Widget";

        newDefaultWidget.type = "picture";
        newDefaultWidget.columns = 1;
//        newDefaultWidget.widgets.put(Integer.parseInt(newDefaultWidget.getUuid()), newDefaultWidget);

        return newDefaultWidget;
    }

    public static Widget createDefaultDashboardWidget() {
        Widget newDefaultWidget = new Widget();

        newDefaultWidget.header = "Documents Ingested / Source";

        newDefaultWidget.type = "barChart";
        newDefaultWidget.columns = 4;
        newDefaultWidget.labels = "twitter,reddit";
        newDefaultWidget.colors = "#48bcfa,#ff4605";
        Widget subWidget = Widget.createDefaultWidget();
        newDefaultWidget.widgets.put(Integer.parseInt(subWidget.getUuid()), subWidget);

        return newDefaultWidget;
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
        //without the node header - i.e. NODE_NAME + ":" + myJSONValue
        String widgetsJSON = getWidgetsJSONValue();
        String myJSONValue = getThisJSONValue(widgetsJSON);

        return myJSONValue;
    }

    private String getWidgetsJSONValue() {
        String widgetsJSON = "[";

        if (widgets != null && widgets.size() > 0) {
            Collection<Widget> theWidgets = widgets.values();
            Iterator<Widget> iterator = theWidgets.iterator();

            while (iterator.hasNext()) {
                Widget aWidget = iterator.next();
                String aWidgetJSON = aWidget.toJSONValueString();
                widgetsJSON += aWidgetJSON;
                if (iterator.hasNext()) {
                    widgetsJSON += ", ";
                }
            }
        }
        widgetsJSON += "]";

        return widgetsJSON;
    }

    private String getThisJSONValue(String theContentJSONValue) {
        String myJSONValue =
                "{" +
                        "\"uuid\":\"" + uuid + "\", " +
                        "\"header\":\"" + header + "\", " +
                        "\"columns\":\"" + columns + "\", " +
                        "\"type\":\"" + type + "\", " +
                        "\"labels\":\"" + labels + "\", " +
                        "\"colors\":\"" + colors + "\", " +
                        "\"sources\":\"" + sources + "\", " +
                        "\"imagePath\":\"" + imagePath + "\", " +
                        "\"event\":\"" + event + "\", " +

                        "\"widgets\":" + theContentJSONValue + ", " +
                        "}";

        return myJSONValue;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String name1) {
        this.header = name1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type1) {
        this.type = type1;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer showFooter1) {
        this.columns = showFooter1;
    }

    public Hashtable<Integer, Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(Hashtable<Integer, Widget> widgets) {
        this.widgets = widgets;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String active1) {
        this.labels = active1;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String logo1) {
        this.colors = logo1;
    }


    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
