package com.chuanwise.xiaoming.bot.nwu.sheet.data;

import com.taixue.xiaoming.bot.core.data.JsonFileSavedData;

import java.util.HashSet;
import java.util.Set;

public class StudentManager extends JsonFileSavedData {
    Set<Student> data = new HashSet<>();

    public Set<Student> getData() {
        return data;
    }

    public Student forCode(final long code) {
        for (Student datum : data) {
            if (datum.getCode() == code) {
                return datum;
            }
        }
        return null;
    }

    public Student forQQ(final long qq) {
        for (Student datum : data) {
            if (datum.getQq() == qq) {
                return datum;
            }
        }
        return null;
    }

    public Set<Student> forName(final String name) {
        Set<Student> result = new HashSet<>();
        for (Student datum : data) {
            if (datum.getName().equals(name)) {
                result.add(datum);
            }
        }
        return result;
    }
}
