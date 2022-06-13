package com.example.catalog;

import java.io.*;
import java.util.ArrayList;

public class Item implements Serializable {

    private Type type;
    private String name;
    private final ArrayList<Tag> tags;
    private final ArrayList<Property> properties;

    public Item(Type type, String name) {
        this.type = type;
        this.name = name;
        tags = new ArrayList<>() {
            @Override
            public String toString() {
                return super.toString().replace("[", "").replace("]", "");
            }
        };
        properties = new ArrayList<>() {
            @Override
            public String toString() {
                return super.toString().replace("[", "").replace("]", "");
            }
        };
    }

    public void createProperty(Property property) {
        properties.add(property);
        type.addFieldLabel(property.getLabel());
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void deleteTag(Tag tag) {
        tags.remove(tag);
    }

    public void deleteProperty(Property property) {
        properties.remove(property);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }


    public ArrayList<Property> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return name;
    }

    public String exportItem() {

        ArrayList<String> tempProperty = new ArrayList<>() {
            @Override
            public String toString() {
                return super.toString().replace("[", "").replace("]", "").replace(",", "\n");
            }
        };
        for (Property property : properties)
            tempProperty.add("          <tr>\n" +
                    "               <td>" + property.getLabel() + "</td>\n" +
                    "               <td>" + property.getContent() + "</td>\n" +
                    "          </tr>");

        return "<!DOCTYPE html>\n" +
                "<html lang=en><html>\n" +
                "<head>\n" +
                "   <title>" + name + "</title>\n" +
                "   <style>\n" +
                "        body{\n" +
                "            margin-top: 0;\n" +
                "            width: 500px;\n" +
                "            text-align: center;\n" +
                "            font-family: 'Times New Roman', Times, serif;\n" +
                "            font-size: large;" +
                "        }\n" +
                "        h1{\n" +
                "            color: #66BFBF;\n" +
                "            margin: 0px auto 10px auto;\n" +
                "            display: block;\n" +
                "            font-size: 3em;\n" +
                "        }\n" +
                "        .propertyTable {\n" +
                "            margin-left: auto;\n" +
                "            margin-right: auto;\n" +
                "            border-width: 1px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "   <h1>" + name + "</h1>\n" +
                "   <p>Item's type is: " + type + "</p>\n" +
                "   <p>Item's tags are: " + tags.toString() + "</p>\n" +
                "   <div>" +
                "       <h2>Properties</h2>\n" +
                "       <table class = \"propertyTable\" cellspacing = \"10\">\n" +
                "           <tr>\n" +
                "               <th>Label</th>\n" +
                "               <th>Content</th>\n" +
                "           </tr>\n" + tempProperty + "\n" +
                "       </table>\n" +
                "   </div>\n" +
                "</body>\n" +
                "</html>";
    }

    public File printFile() {
        File file = null;
        try {
            file = new File("template.html");
            if (file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(exportItem());
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}