package eu.pb4.placeholders.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public final class PlaceholderResult {
    private final Component text;
    private final String string;
    private final boolean valid;

    private PlaceholderResult(Component text, String reason) {
        if (text != null) {
            this.text = text;
            this.valid = true;
        } else {
            this.text = Component.literal("[" + (reason != null ? reason : "Invalid placeholder!") + "]").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true));
            this.valid = false;
        }
        this.string = this.text.getString();
    }

    /**
     * Returns text component from placeholder
     *
     * @return Text
     */
    public Component text() {
        return this.text;
    }

    /**
     * Returns text component as String (without formatting) from placeholder
     *
     * @return String
     */
    public String string() {
        return this.string;
    }

    /**
     * Checks if placeholder was valid
     *
     * @return boolean
     */
    public boolean isValid() {
        return this.valid;
    }

    /**
     * Create result for invalid placeholder
     *
     * @return PlaceholderResult
     */
    public static PlaceholderResult invalid(String reason) {
        return new PlaceholderResult(null, reason);
    }

    /**
     * Create result for invalid placeholder
     *
     * @return PlaceholderResult
     */
    public static PlaceholderResult invalid() {
        return new PlaceholderResult(null, null);
    }

    /**
     * Create result for placeholder with formatting
     *
     * @return PlaceholderResult
     */
    public static PlaceholderResult value(Component text) {
        return new PlaceholderResult(text, null);
    }

    /**
     * Create result for placeholder
     *
     * @return PlaceholderResult
     */
    public static PlaceholderResult value(String text) {
        return new PlaceholderResult(TextParserUtils.formatText(text), null);
    }
}


