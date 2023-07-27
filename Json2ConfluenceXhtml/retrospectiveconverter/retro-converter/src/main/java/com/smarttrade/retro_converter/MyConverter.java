
package com.smarttrade.retro_converter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Project Name : RetroSpective Converter for Confluence<br>
 * Author : @lsauvaire<br>
 * Author Name : Lino Sauvaire
 */
public class MyConverter {

    private final String jsonFile;
    private final String xhtmlFile;
    private static final int LENGTH_CLOSING_AC_TASK_LIST = 17;
    private static final int LENGTH_CLOSING_UL = 5;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date dateTitle;

    public MyConverter(String jsonFile, String xhtmlFile) {
        this.jsonFile = jsonFile;
        this.xhtmlFile = xhtmlFile;
    }

    /**
     * @param type
     * @return All Content of the Json File
     * @throws Exception
     */

    private List<String> getJsonContent(String type) throws Exception {
        final List<String> getJsonContent = new ArrayList<>();
        final JsonNode jsonTree = new ObjectMapper().readTree(new File(jsonFile));
        jsonTree.get(type).elements().forEachRemaining((jn) -> {
            if (jn.get("content") != null) getJsonContent.add(jn.get("content").asText());
        });
        return getJsonContent;
    }

    /**
     * @param fileName
     * @return
     * @throws IOException
     * @throws Exception
     */
    public String readFileAsString(String fileName) throws IOException {
        final Path path = Paths.get(new File(MyConverter.class.getClassLoader().getResource("template_confluence/template.xhtml").getFile()).getPath());
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            return lines.reduce((s1, s2) -> s1 + s2).orElse(null);
        }
    }

    private String resultType(String type) throws Exception {
        String resultType = "<ul>\n";
        for (final String tempGladAndSad : getJsonContent(type)) {
            final String replace = tempGladAndSad.replace("\n", "");
            final String escapeXml = StringEscapeUtils.escapeXml10(replace);
            resultType = resultType + "\t<li>" + escapeXml + "</li>\n";
        }
        resultType = resultType + "\t</ul>\n";
        return resultType;
    }

    private String getDateTime() throws Exception {
        dateTitle = new Date();
        final String strDate = dateFormat.format(dateTitle);
        final String date = "datetime=\"" + strDate + "\"";
        return date;
    }

    public String insertDate(String data) throws Exception {
        return data.replaceFirst("datetime=\"\"", getDateTime());
    }

    public Date getDateTitle() {
        return dateTitle;
    }

    /**
     * @return good text for action
     * @throws Exception
     */
    private String resultAction() throws Exception {
        String resultaction = "<ac:task-list> \n";
        int id = 1;
        for (final String temp : getJsonContent("Actions")) {
            resultaction = resultaction + "\t<ac:task>\n";
            resultaction = resultaction + "\t<ac:task-id>" + id + "</ac:task-id>\n";
            id++;
            resultaction = resultaction + "\t<ac:task-status>incomplete</ac:task-status>\n";
            final String replace = temp.replace("\n", "");
            final String escapeXml = StringEscapeUtils.escapeXml10(replace);
            resultaction = resultaction + "\t<ac:task-body>" + escapeXml + "</ac:task-body>\n";
            resultaction = resultaction + "\t</ac:task>\n";
        }
        resultaction = resultaction + "</ac:task-list>\n";

        return resultaction;
    }

    /**
     * @return The finnaly xhtml text completed
     * @throws Exception
     */
    public String generateXhtml() throws Exception {
        final String rawData = readFileAsString(xhtmlFile);
        final String data = insertDate(rawData);
        return data.substring(0, data.indexOf("<ul>")) + resultType("Glad") + // Beginning To the first <ul> then replaced List Glad
                data.substring(data.indexOf("</ul>") + LENGTH_CLOSING_UL, data.lastIndexOf("<ul>")) + resultType("Sad") + // First closing <ul> to last opening <ul> then replaced List Sad
                data.substring(data.lastIndexOf("</ul>") + LENGTH_CLOSING_UL, data.indexOf("<ac:task-list>")) + resultAction() + // Last closing <ul> to opening acListTask then replaced List Action
                data.substring(data.lastIndexOf("</ac:task-list>") + LENGTH_CLOSING_AC_TASK_LIST, data.length());
    }

}
