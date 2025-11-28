package com.privychat.model;

import com.privychat.model.enums.AttachmentType;
import java.util.Objects;

public class Attachment {
    private AttachmentType type;
    private String url;

    public Attachment() {}

    public Attachment(AttachmentType type, String url) {
        this.type = type;
        this.url = url;
    }

    public AttachmentType getType() { return type; }
    public void setType(AttachmentType type) { this.type = type; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attachment)) return false;
        Attachment that = (Attachment) o;
        return type == that.type && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() { return Objects.hash(type, url); }
}

