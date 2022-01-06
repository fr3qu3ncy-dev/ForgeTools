package de.fr3qu3ncy.forgetools.V1_17.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;

public class TextUtils {

    /**
     * Convert a String with "&" Color Codes to a TextComponent
     * @param text - The text to convert
     * @return - A TextComponent object
     */
    public static TextComponent createText(String text) {
        //Initialize a new reference
        TextComponent comp = new TextComponent("");

        //The current ChatFormatting (default white)
        ChatFormatting current = ChatFormatting.WHITE;

        //Set start and end index defaults
        int startIndex = 0;
        int endIndex = 0;

        //Loop every character in string
        for (int i = 0 ; i < text.length() ; i++) {

            //Search the next ChatFormatting in the string
            ChatFormatting found = findColor(text, i);

            //Check if there is another Color Code in the string
            if (found != null) {

                //Set the current index to the endIndex
                endIndex = i;

                //Append the current substring with the color from before
                comp.append(new TextComponent(
                        text.substring(startIndex, endIndex))
                        .withStyle(current));

                //Set the new color
                current = found;

                //Skip 2 indices to after the Color Code
                startIndex = endIndex + 2;

                //Check if Color Code was on last 2 indices
                if (startIndex >= text.length()) {
                    break;
                }
            } else if (i == text.length() - 1) {
                //Last character, must append now
                comp.append(
                        new TextComponent(
                                text.substring(startIndex, i + 1))
                                .withStyle(current));
            }
        }
        return comp;
    }

    private static ChatFormatting findColor(String text, int index) {
        char atIndex = text.charAt(index);
        if (atIndex == '&') {
            if (index == text.length() - 1) {
                return null;
            }
            char atNextIndex = text.charAt(index + 1);
            return getByCode(atNextIndex);
        }
        return null;
    }

    private static ChatFormatting getByCode(char c) {
        return ChatFormatting.getByCode(c);
    }

}
