package org.eazytg.lib;

public class Button {
    private String text;
    private String data;
    private boolean isUrl = false;
    private String iconCustomEmojiId;
    private String style;

    public enum Style {
        DANGER("danger"),
        SUCCESS("success"),
        PRIMARY("primary");

        private final String value;

        Style(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public Button(String text, String data) {
        this.text = text;
        this.data = data;
        if (data.startsWith("http")) {
            isUrl = true;
        }
    }

    public Button(String text, String data, String iconCustomEmojiId, String style) {
        this(text, data);
        this.iconCustomEmojiId = iconCustomEmojiId;
        this.style = style;
    }

    public Button(String text, String data, String iconCustomEmojiId, Style style) {
        this(text, data, iconCustomEmojiId, style != null ? style.getValue() : null);
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

    public String getIconCustomEmojiId() {
        return iconCustomEmojiId;
    }

    public void setIconCustomEmojiId(String iconCustomEmojiId) {
        this.iconCustomEmojiId = iconCustomEmojiId;
    }

    public Button withIconCustomEmojiId(String iconCustomEmojiId) {
        this.iconCustomEmojiId = iconCustomEmojiId;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setStyle(Style style) {
        this.style = style != null ? style.getValue() : null;
    }

    public Button withStyle(String style) {
        this.style = style;
        return this;
    }

    public Button withStyle(Style style) {
        this.style = style != null ? style.getValue() : null;
        return this;
    }
}