package com.privychat.model;

import com.privychat.model.enums.Role;
import com.privychat.model.scalar.ObjectID;
import java.util.Objects;

public class RoleEntry {
    private ObjectID userId;
    private Role role;

    public RoleEntry() {}

    public RoleEntry(ObjectID userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public ObjectID getUserId() { return userId; }
    public void setUserId(ObjectID userId) { this.userId = userId; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEntry)) return false;
        RoleEntry that = (RoleEntry) o;
        return Objects.equals(userId, that.userId) && role == that.role;
    }

    @Override
    public int hashCode() { return Objects.hash(userId, role); }
}

