package com.example.amit.cube26.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "Item 1","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Weekly",1));
        addItem(new DummyItem("2", "Item 2","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Daily",0));
        addItem(new DummyItem("3", "Item 3","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Weekly",1));
        addItem(new DummyItem("4", "Item 4","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Weekly",1));
        addItem(new DummyItem("5", "Item 5","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Daily",0));
        addItem(new DummyItem("6", "Item 6","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Weekly",1));
        addItem(new DummyItem("7", "Item 7","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Daily",0));
        addItem(new DummyItem("8", "Item 8","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Weekly",1));
        addItem(new DummyItem("9", "Item 9","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Weekly",1));
        addItem(new DummyItem("10", "Item 10","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Weekly",0));
        addItem(new DummyItem("11", "Item 11","12/10/2015 08:00:00","12/10/2015 08:05:00","45","Weekly",1));

    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String Jobname;
        public String Startdate;
        public String Enddate;
        public String  Runtime;
        public String RunningCycle;
        public int status;

        public DummyItem(String id, String Jobname, String Startdate,String Enddate, String Runtime,String RunningCycle, int status) {
            this.id = id;
            this.Jobname = Jobname;
            this.Startdate = Startdate;
            this.Enddate = Enddate;
            this.Runtime = Runtime;
            this.RunningCycle = RunningCycle;
            this.status = status;
        }

        @Override
        public String toString() {
            return Jobname;
        }
    }
}
