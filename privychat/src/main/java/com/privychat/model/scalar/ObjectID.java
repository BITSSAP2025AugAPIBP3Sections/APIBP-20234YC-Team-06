package com.privychat.model.scalar;

import org.bson.types.ObjectId;
import java.util.Objects;

/**
 * Wrapper for MongoDB ObjectId to represent GraphQL custom scalar ObjectID.
 */
public final class ObjectID {
    private final ObjectId value;

    private ObjectID(ObjectId value) {
        this.value = value;
    }

    public static ObjectID from(ObjectId value) {
        return new ObjectID(value);
    }

    public static ObjectID parse(String hexString) {
        return new ObjectID(new ObjectId(hexString));
    }

    public ObjectId getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toHexString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectID)) return false;
        ObjectID objectID = (ObjectID) o;
        return Objects.equals(value, objectID.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

