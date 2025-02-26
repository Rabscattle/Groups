package com.github.dschreid.groups;

import com.github.dschreid.groups.placeholders.PlaceholderPair;

public class PrefixedExceptionBuilder {
    private String message;
    private PlaceholderPair[] placeholder;

    public PrefixedExceptionBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public PrefixedExceptionBuilder setPlaceholder(PlaceholderPair... placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public PrefixedException createPrefixedException() {
        return new PrefixedException(message, placeholder);
    }
}
