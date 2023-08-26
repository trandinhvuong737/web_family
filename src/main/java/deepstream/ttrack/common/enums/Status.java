package deepstream.ttrack.common.enums;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("pending"),
    COMPLETED("completed"),
    CANCELED("canceled");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
