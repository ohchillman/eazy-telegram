package org.eazytg.lib;

public class Button {
    private String text;
    private String data;
    private boolean isUrl = false;

    public Button(String text, String data) {
        this.text = text;
        this.data = data;
        if (data.startsWith("http")) {
            isUrl = true;
        }
    }

    public String getText() {
        return text;
    }

    public String getData() {
        return data;
    }

    public boolean isUrl() {
        return isUrl;
    }
}