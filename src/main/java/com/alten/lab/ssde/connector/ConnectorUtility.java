package com.alten.lab.ssde.connector;

import java.util.ArrayList;
import java.util.List;

import com.alten.lab.ssde.data.table.Header;

public class ConnectorUtility {

    /**
     * Turn a {@code List<Header>} into a {@code List<String>} by calling the method {@link Header#getName()}.
     * 
     * @param headers a {@code List<Header>}.
     * @return a {@code List<String>}.
     */
    public static List<String> headerAsStringList(List<Header> headers) {
        if (headers == null) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (Header head : headers) {
            list.add(head.getName());
        }
        return list;
    }

    /**
     * Transform a {@code List<List<Object>>} into a {@code List<String[]>}. We use the .toString of the Object. The toString of the object
     * of the {@code List<List<Object>>} must return a parsable value.
     * 
     * @param objects a {@code List<List<Object>>}.
     * @return a {@code List<String[]>}.
     */
    public static List<String[]> asStringsList(List<List<Object>> objects) {
        List<String[]> list = new ArrayList<>();
        for (List<Object> objectList : objects) {
            String[] stringList = new String[objectList.size()];
            int counter = 0;
            for (Object object : objectList) {
                stringList[counter] = object.toString();
                counter++;
            }
            list.add(stringList);
        }
        return list;
    }
}
