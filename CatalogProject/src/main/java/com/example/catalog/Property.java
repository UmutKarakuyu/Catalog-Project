package com.example.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Property implements Serializable {


    private final String label;
    private final ArrayList<String> content;

    public Property(String label, String content) {
        this.label = label;
        this.content = new ArrayList<>(){
            @Override
            public String toString() {
                return super.toString().replace("[", "").replace(",","\n").replace("]", "");
            }
        };
        setContent(content);
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content.contains(",")) {
            String[] s = content.split(",");
            for (int i=0;i<s.length;i++)
                s[i] = s[i].trim();
            Collections.addAll(this.content, s);
        }
        else
            this.content.add(content);
    }

    @Override
    public String toString() {
        return label + ": " + content;
    }
}