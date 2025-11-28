package com.privychat.model.input;

import com.privychat.model.enums.AttachmentType;

public class AttachmentInput {
    private AttachmentType type;
    private String url;

    public AttachmentInput() {}

    public AttachmentInput(AttachmentType type, String url) {
        this.type = type;
        this.url = url;
    }

    public AttachmentType getType() { return type; }
    public void setType(AttachmentType type) { this.type = type; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}

