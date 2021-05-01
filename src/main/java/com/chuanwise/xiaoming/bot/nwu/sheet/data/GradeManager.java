package com.chuanwise.xiaoming.bot.nwu.sheet.data;

import com.taixue.xiaoming.bot.core.data.JsonFileSavedData;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class GradeManager extends JsonFileSavedData {
    Set<Grade> data = new HashSet<>();

    public Set<Grade> getData() {
        return data;
    }

    @Nullable
    public Grade forCode(final long code) {
        for (Grade datum : data) {
            if (datum.getCode() == code) {
                return datum;
            }
        }
        return null;
    }
}
