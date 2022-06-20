package com.alten.lab.ssde.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import com.alten.lab.ssde.connector.exception.ConnectorException;
import com.alten.lab.ssde.data.flow.Flow;
import com.alten.lab.ssde.data.flow.impl.RowSupplier;
import com.alten.lab.ssde.data.table.Header;

import org.junit.Before;
import org.junit.Test;

public abstract class FlowConnectorTest {

    protected List<String> _headers;
    protected List<List<Object>> _data;

    public static final File RESOURCES_DIRECTORY = new File("src/test/resources");

    protected abstract FlowConnector newFlowConnector(List<String> headers, List<List<Object>> data, int bufferSize)
            throws ConnectorException;

    protected abstract FlowConnector newFlowConnector(int bufferSize);

    protected FlowConnector newFlowConnector() throws ConnectorException {
        return newFlowConnector(_headers, _data, 1);
    }

    /**
     * Setup data.
     */
    @Before
    public final void setup() {
        _headers = Arrays.asList("A", "B", "C");

        _data = Arrays.asList(
                Arrays.asList("aaa", "2", "2.5"),
                Arrays.asList("10.85", "bbb", "58"),
                Arrays.asList("48.5", "ccc", "d"));
    }

    /**
     * Test that the method {@link Connector#get()} return something and that no expection is throw.
     * 
     * @throws ConnectorException if trouble on {@link Connector#get()} (not expected).
     */
    @Test
    public void getTest() throws ConnectorException {
        try (FlowConnector connector = newFlowConnector(1);) {
            assertNotNull(connector.get());
        }
    }

    /**
     * Test the idempotency of the {@link Connector#get()} method.
     * 
     * @throws Exception if trouble on {@link Connector#get()} or {@link RowSupplier#take()} (not expected).
     */
    @Test
    public void getIdempotentTest() throws Exception {
        try (FlowConnector connector = newFlowConnector(1);) {
            Flow flow = connector.get();
            assertEquals(flow, connector.get());
        }
    }

    /**
     * Check that the result of {@link Flow#getHeaders()} is the expected result.
     * 
     * @throws Exception if trouble on {@link Connector#get()} (not expected).
     */
    @Test
    public void getHeadersTest() throws Exception {
        try (FlowConnector connector = newFlowConnector(1);) {
            Flow flow = connector.get();
            List<Header> headers = flow.getHeaders();
            assertEquals(_headers, ConnectorUtility.headerAsStringList(headers));
            headers.stream().map(Header::getTypeHint).allMatch("String"::equals);
        }
    }

    /**
     * Check that the result of {@link Flow#getRows()} is the expected result.
     * 
     * @throws Exception if trouble on {@link Connector#get()} or {@link RowSupplier#take()} (not expected).
     */
    @Test
    public void getRowsTest() throws Exception {
        try (FlowConnector connector = newFlowConnector(10);) {
            Flow flow = connector.get();
            try (RowSupplier content = flow.getRows();) {
                assertEquals(_data.get(0), content.take().toObjects());
                assertEquals(_data.get(1), content.take().toObjects());
                assertEquals(_data.get(2), content.take().toObjects());
            }
        }
    }

    /**
     * Check that the result of {@link Flow#getRows()} is the expected result.
     * 
     * @throws Exception if trouble on {@link Connector#get()} (not expected) or on {@link RowSupplier#take()} (expected on the last).
     */
    @Test(expected = NoSuchElementException.class)
    public void getRowsBlockPutTest() throws Exception {
        try (FlowConnector connector = newFlowConnector(1);) {
            Flow flow = connector.get();
            try (RowSupplier content = flow.getRows();) {
                content.take();
                content.take();
                content.take();
                content.take();
            }
        }
    }

    /**
     * Verify there is no exception when calling {@link Connector#close()}.
     * 
     * @throws ConnectorException if trouble on {@link Connector#close()} (not expected).
     */
    @Test
    public void closeTest() throws ConnectorException {
        FlowConnector connector = newFlowConnector(1);
        connector.close();
    }

    /**
     * Verify there is no exception when calling {@link Connector#close()} twice.
     * 
     * @throws ConnectorException if trouble on {@link Connector#close()} (not expected).
     */
    @Test
    public void closeTwiceTest() throws ConnectorException {
        FlowConnector connector = newFlowConnector(1);
        connector.close();
        connector.close();
    }
}
