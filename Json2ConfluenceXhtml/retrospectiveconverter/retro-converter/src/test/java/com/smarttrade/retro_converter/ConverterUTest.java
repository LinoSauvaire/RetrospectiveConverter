
package com.smarttrade.retro_converter;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConverterUTest {

    private static String TEMPLATE_FILE_EMPTY = new File(ConverterUTest.class.getClassLoader().getResource("template_confluence/templatetest.xhtml").getFile())
            .getPath();
    private static final String JSON_PATH_EMPTY = "";
    private static String TEMPLATE_PATH = new File(ConverterUTest.class.getClassLoader().getResource("template_confluence/template.xhtml").getFile()).getPath();
    private static String htmlTemp;
    private static String JSON_PATH = new File(ConverterUTest.class.getClassLoader().getResource("json/LesCoderRangers.json").getFile()).getPath();

    @Before
    public void setup() throws Exception {
        System.out.println(ConverterUTest.class.getClassLoader().getResource("template_confluence/template.xhtml").toURI().getPath());

    }

    @Test
    public void generateXhtml_should_throw_exception_if_json_is_missing() throws Exception {
        System.out.println();
        // Given
        final MyConverter tested = new MyConverter(JSON_PATH_EMPTY, TEMPLATE_PATH);

        try {
            // When
            tested.generateXhtml();
            Assert.fail();
        } catch (final FileNotFoundException f) {

            // Then an exception is thrown
        }
    }

    @Test
    public void generateXhtml_should_throw_exception_when_xhtml_input_is_empty() throws Exception {
        final MyConverter tested = new MyConverter(JSON_PATH, TEMPLATE_FILE_EMPTY);
        try {
            tested.generateXhtml();
            Assert.fail();
        } catch (final StringIndexOutOfBoundsException s) {

        }
    }

    @Test
    public void generateXhtml_should_not_throw_exception_when_all_is_good() throws Exception {
        final MyConverter tested = new MyConverter(JSON_PATH, TEMPLATE_PATH);
        final String xhtml = tested.generateXhtml();
        assertThat(xhtml, notNullValue());
    }

    @Test
    public void read_Json_file_with_good_path() throws Exception {
        final MyConverter tested = new MyConverter(JSON_PATH, TEMPLATE_PATH);
        final String xhtml = tested.generateXhtml();
        assertThat(xhtml, notNullValue());
    }

}
