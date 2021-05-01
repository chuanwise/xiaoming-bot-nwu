package com.chuanwise.xiaoming.bot.nwu.sheet.data;

public class Grade {
    long code;

    // 学分加权平均分
    double weightedAverageCreadit;

    // 年级排名
    int ranking;

    // 不及格门次
    int failCourseNumber;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public double getWeightedAverageCreadit() {
        return weightedAverageCreadit;
    }

    public void setWeightedAverageCreadit(double weightedAverageCreadit) {
        this.weightedAverageCreadit = weightedAverageCreadit;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getFailCourseNumber() {
        return failCourseNumber;
    }

    public void setFailCourseNumber(int failCourseNumber) {
        this.failCourseNumber = failCourseNumber;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "code=" + code +
                ", weightedAverageCreadit=" + weightedAverageCreadit +
                ", ranking=" + ranking +
                ", failCourseNumber=" + failCourseNumber +
                '}';
    }
}
