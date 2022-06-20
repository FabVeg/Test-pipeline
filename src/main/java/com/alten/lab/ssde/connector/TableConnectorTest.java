package com.alten.lab.ssde.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.alten.lab.ssde.connector.exception.ConnectorException;
import com.alten.lab.ssde.data.table.Header;
import com.alten.lab.ssde.data.table.Row;
import com.alten.lab.ssde.data.table.Table;

import org.junit.Before;
import org.junit.Test;

public abstract class TableConnectorTest {

    public static final File RESOURCES_DIRECTORY = new File("src/test/resources");

    protected abstract TableConnector newTableConnector(List<String> headers, List<List<Object>> data) throws ConnectorException;

    protected TableConnector newTableConnector() throws ConnectorException {
        return newTableConnector(_headers, _data);
    }

    protected List<String> _headers;
    protected List<List<Object>> _data;

    /**
     * Prepare data.
     */
    @Before
    public void setup() {
        _headers = Arrays.asList("A", "B", "C");

        _data = Arrays.asList(
                Arrays.asList("aaa", "2", "2.5"),
                Arrays.asList("10.85", "bbb", "58"),
                Arrays.asList("48.5", "ccc", "d"));
    }

    /**
     * Check that {@link Table#getHeaders()} return the values specified in the header.
     * 
     * @throws Exception if trouble on {@link Connector#get()} (not expected).
     */
    @Test
    public void getHeadersTest() throws Exception {
        try (TableConnector connector = newTableConnector(_headers, _data);) {
            Table table = connector.get();
            List<Header> headers = table.getHeaders();
            assertEquals(_headers, ConnectorUtility.headerAsStringList(headers));
            assertTrue(headers.stream().map(Header::getTypeHint).allMatch("String"::equals));
        }
    }

    /**
     * Check that {@link Table#getRows()} return the values specified in the data.
     * 
     * @throws Exception if trouble on {@link Connector#get()} (not expected).
     */
    @Test
    public void getRowsTest() throws Exception {
        try (TableConnector connector = newTableConnector(_headers, _data);) {
            Table table = connector.get();
            List<Row> content = table.getRows();
            assertEquals(_data.get(0), content.get(0).toObjects());
            assertEquals(_data.get(1), content.get(1).toObjects());
            assertEquals(_data.get(2), content.get(2).toObjects());
        }
    }

    /**
     * Verify the idempotent of the method {@link Connector#get()}.
     * 
     * @throws Exception if trouble on {@link Connector#get()} (not expected).
     */
    @Test
    public void idempotentGetTest() throws Exception {
        try (TableConnector tableConnector = newTableConnector();) {
            assertTrue(tableConnector.get() == tableConnector.get());
        }
    }

    /**
     * Verify there is no exception when calling {@link Connector#close()}.
     * 
     * @throws Exception if trouble on {@link Connector#close()} (not expected).
     */
    @Test
    public void closeTest() throws Exception {
        TableConnector tableConnector = newTableConnector();
        tableConnector.close();
    }

    /**
     * Verify there is no exception when calling {@link Connector#close()} twice.
     * 
     * @throws Exception if trouble on {@link Connector#close()} (not expected).
     */
    @Test
    public void twiceCloseTest() throws Exception {
        TableConnector tableConnector = newTableConnector();
        tableConnector.close();
        tableConnector.close();
    }
}
