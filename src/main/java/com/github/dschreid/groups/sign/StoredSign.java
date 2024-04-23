package com.github.dschreid.groups.sign;

/**
 * This class holds the currently represented
 * version of a sign that a player will see
 */
public class StoredSign {
    private final String[] content;

    public StoredSign(String[] content) {
        this.content = content;
    }

    public String[] getContent() {
        return content;
    }
}
