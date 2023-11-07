package deepstream.ttrack.common.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum Status {
    PENDING("pending"),
    COMPLETED("completed"),
    CANCELED("canceled");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public static List<String> getDisplayNames() {
        List<String> displayNames = new ArrayList<>();
        for (Status status : Status.values()) {
            displayNames.add(status.getDisplayName());
        }
        return displayNames;
    }
}
