package org.eazytg.lib;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ExtendedInlineKeyboardButton extends InlineKeyboardButton {
    @JsonProperty("icon_custom_emoji_id")
    private String iconCustomEmojiId;

    @JsonProperty("style")
    private String style;

    public String getIconCustomEmojiId() {
        return iconCustomEmojiId;
    }

    public void setIconCustomEmojiId(String iconCustomEmojiId) {
        this.iconCustomEmojiId = iconCustomEmojiId;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
