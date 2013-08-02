/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2013, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ------------------------------------------
 * DefaultMultiValueCategoryDatasetTests.java
 * ------------------------------------------
 * (C) Copyright 2007, 2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 28-Sep-2007 : Version 1 (DG);
 *
 */

package org.jfree.data.statistics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.UnknownKeyException;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;

/**
 * Tests for the {@link DefaultMultiValueCategoryDataset} class.
 */
public class DefaultMultiValueCategoryDatasetTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(DefaultMultiValueCategoryDatasetTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public DefaultMultiValueCategoryDatasetTests(String name) {
        super(name);
    }

    /**
     * Some checks for the getValue() method.
     */
    public void testGetValue() {
        DefaultMultiValueCategoryDataset d
                = new DefaultMultiValueCategoryDataset();
        List values = new ArrayList();
        values.add(new Integer(1));
        values.add(new Integer(2));
        d.add(values, "R1", "C1");
        assertEquals(new Double(1.5), d.getValue("R1", "C1"));
        boolean pass = false;
        try {
            d.getValue("XX", "C1");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);

        pass = false;
        try {
            d.getValue("R1", "XX");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * A simple check for the getValue(int, int) method.
     */
    public void testGetValue2() {
        DefaultMultiValueCategoryDataset d
                = new DefaultMultiValueCategoryDataset();
        boolean pass = false;
        try {
            /* Number n =*/ d.getValue(0, 0);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some tests for the getRowCount() method.
     */
    public void testGetRowCount() {
        DefaultMultiValueCategoryDataset d
                = new DefaultMultiValueCategoryDataset();
        assertTrue(d.getRowCount() == 0);
        List values = new ArrayList();
        d.add(values, "R1", "C1");
        assertTrue(d.getRowCount() == 1);

        d.add(values, "R2", "C1");
        assertTrue(d.getRowCount() == 2);

        d.add(values, "R2", "C1");
        assertTrue(d.getRowCount() == 2);
    }

    /**
     * Some tests for the getColumnCount() method.
     */
    public void testGetColumnCount() {
        DefaultMultiValueCategoryDataset d
                = new DefaultMultiValueCategoryDataset();
        assertTrue(d.getColumnCount() == 0);

        List values = new ArrayList();
        d.add(values, "R1", "C1");
        assertTrue(d.getColumnCount() == 1);

        d.add(values, "R1", "C2");
        assertTrue(d.getColumnCount() == 2);

        d.add(values, "R1", "C2");
        assertTrue(d.getColumnCount() == 2);

    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    public void testEquals() {
        DefaultMultiValueCategoryDataset d1
                = new DefaultMultiValueCategoryDataset();
        DefaultMultiValueCategoryDataset d2
                = new DefaultMultiValueCategoryDataset();
        assertTrue(d1.equals(d2));
        assertTrue(d2.equals(d1));

        List values = new ArrayList();
        d1.add(values, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.add(values, "R1", "C1");
        assertTrue(d1.equals(d2));

        values.add(new Integer(99));
        d1.add(values, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.add(values, "R1", "C1");
        assertTrue(d1.equals(d2));

        values.add(new Integer(99));
        d1.add(values, "R1", "C2");
        assertFalse(d1.equals(d2));
        d2.add(values, "R1", "C2");
        assertTrue(d1.equals(d2));
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() throws IOException, ClassNotFoundException {
        DefaultMultiValueCategoryDataset d1
                = new DefaultMultiValueCategoryDataset();
        DefaultMultiValueCategoryDataset d2
                = new DefaultMultiValueCategoryDataset();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(d1);
        out.close();

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                buffer.toByteArray()));
        d2 = (DefaultMultiValueCategoryDataset) in.readObject();
        in.close();
        assertEquals(d1, d2);
    }

    /**
     * Some checks for the add() method.
     */
    public void testAddValue() {
        DefaultMultiValueCategoryDataset d1
                = new DefaultMultiValueCategoryDataset();

        boolean pass = false;
        try {
            d1.add(null, "R1", "C1");
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);

        List values = new ArrayList();
        d1.add(values, "R2", "C1");
        assertEquals(values, d1.getValues("R2", "C1"));

        pass = false;
        try {
            d1.add(values, null, "C2");
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Confirm that cloning works.
     */
    public void testCloning() throws CloneNotSupportedException {
        DefaultMultiValueCategoryDataset d1
                = new DefaultMultiValueCategoryDataset();
        DefaultMultiValueCategoryDataset d2 
                = (DefaultMultiValueCategoryDataset) d1.clone();
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));

        // try a dataset with some content...
        List values = new ArrayList();
        values.add(new Integer(99));
        d1.add(values, "R1", "C1");
        d2 = (DefaultMultiValueCategoryDataset) d1.clone();
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));

        // check that the clone doesn't share the same underlying arrays.
        List values2 = new ArrayList();
        values2.add(new Integer(111));
        d1.add(values2, "R2", "C2");
        assertFalse(d1.equals(d2));
        d2.add(values2, "R2", "C2");
        assertTrue(d1.equals(d2));
    }

}