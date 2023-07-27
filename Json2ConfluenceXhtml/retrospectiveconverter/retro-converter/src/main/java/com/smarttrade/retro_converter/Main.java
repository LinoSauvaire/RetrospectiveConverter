
package com.smarttrade.retro_converter;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Project Name : RetroSpective Converter for Confluence<br>
 * Author : @lsauvaire<br>
 * Author Name : Lino Sauvaire
 */
public class Main {

    private static final String XHTML_PATH = new File(Main.class.getClassLoader().getResource("template_confluence/template.xhtml").getFile()).getPath();
    private static final String CONFLUENCE_PASSWORD_KEY = "confluence_password";
    private static final String CONFLUENCE_USER_KEY = "confluence_user";
    private static final String CONFLUENCE_PARENT_KEY = "confluence_parent_key";
    private static final String CONFLUENCE_BASE_URL_KEY = "confluence_base_url";
    private static final String CONFLUENCE_SPACE_KEY = "confluence_space_key";
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws Exception {
        final Properties props = new Properties();
        props.load(Main.class.getClassLoader().getResourceAsStream("cfg/converter_required.cfg"));
        final String baseUrl = props.getProperty(CONFLUENCE_BASE_URL_KEY);
        final String parentUrl = props.getProperty(CONFLUENCE_PARENT_KEY);
        final String spaceKey = props.getProperty(CONFLUENCE_SPACE_KEY);
        final String fileName = System.getenv("HomePath").replace(" ", "") + "\\.gradle\\gradle.properties";
        final File file = new File(fileName);
        final Properties Gradleprop = new Properties();
        Gradleprop.load(new FileInputStream(file));

        final Path dir = Paths.get(new File(MyConverter.class.getClassLoader().getResource("json/").getFile()).getPath());

        Path firstJsonFile = null;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*{json}")) {
            final Iterator<Path> iterator = stream.iterator();
            if (iterator.hasNext()) {
                firstJsonFile = iterator.next();

            } else {
                System.out.println("No Json File in directory : " + dir);
            }
        } catch (final Exception e) {
            System.out.println(e);
        }

        if (firstJsonFile == null) {
            System.out.println("No jsonFile Found");
            return;
        }

        final MyConverter converter = new MyConverter(firstJsonFile.toString(), XHTML_PATH);
        final String converterFinal = converter.generateXhtml();

        final ConfluenceParams param = new ConfluenceParams();
        param.setUrl(baseUrl);
        param.setLogin(getValue(CONFLUENCE_USER_KEY, props, Gradleprop));
        param.setPassword(getValue(CONFLUENCE_PASSWORD_KEY, props, Gradleprop));
        param.setSpaceKey(spaceKey);
        param.setParentUrl(parentUrl);

        final SimpleConfluenceWriter writer = new SimpleConfluenceWriter(param);
        final String dateStr = dateFormat.format(converter.getDateTitle());
        writer.pushDraft(converterFinal, dateStr + " Retrospective");

    }

    private static String getValue(String key, Properties props, Properties gradleProps) {
        final String o = (String) props.get(key);
        return !StringUtils.isBlank(o) ? o : (String) gradleProps.get(key);
    }

}
