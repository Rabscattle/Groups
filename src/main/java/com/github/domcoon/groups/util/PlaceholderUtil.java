package com.github.domcoon.groups.util;

import com.github.domcoon.groups.placeholders.PlaceholderPair;

public class PlaceholderUtil {
    public static String replacePlaceholders(String message, PlaceholderPair[] values) {
        for (PlaceholderPair value : values) {
            message = message.replace(value.getK(), value.getV());
        }
        return message;
    }
}
