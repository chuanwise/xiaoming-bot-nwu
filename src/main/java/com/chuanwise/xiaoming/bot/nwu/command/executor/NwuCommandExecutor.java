package com.chuanwise.xiaoming.bot.nwu.command.executor;

import com.chuanwise.xiaoming.bot.nwu.NwuPlugin;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.Grade;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.GradeManager;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.Student;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.StudentManager;
import com.chuanwise.xiaoming.bot.nwu.util.NwuWords;
import com.taixue.xiaoming.bot.api.annotation.Command;
import com.taixue.xiaoming.bot.api.annotation.CommandParameter;
import com.taixue.xiaoming.bot.api.user.QQXiaomingUser;
import com.taixue.xiaoming.bot.core.command.executor.CommandExecutorImpl;
import com.taixue.xiaoming.bot.util.AtUtil;
import com.taixue.xiaoming.bot.util.CommandWordUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NwuCommandExecutor extends CommandExecutorImpl {
    public static final String SCORE = "(成绩|score)";

    @NotNull
    @Override
    public String getHelpPrefix() {
        return NwuWords.NWU_REGEX;
    }

    @Command(CommandWordUtil.SEARCH_REGEX + CommandWordUtil.PERSONAL_REGEX + SCORE)
    public void onSearchScore(final QQXiaomingUser user) {
        final NwuPlugin instance = NwuPlugin.INSTANCE;
        final StudentManager studentManager = instance.getStudentManager();
        final GradeManager gradeManager = instance.getGradeManager();

        // 寻找该学生的学号
        final Student student = studentManager.forQQ(user.getQQ());
        if (Objects.isNull(student)) {
            user.sendError("小明没有收录你的信息哦");
            return;
        }

        // 寻找该学生的成绩
        final Grade grade = gradeManager.forCode(student.getCode());
        if (Objects.isNull(grade)) {
            user.sendError("小明也想知道你的成绩哦");
            return;
        }

        // 查询结果
        final StringBuilder builder = new StringBuilder();
        builder.append("你的成绩是：").append("\n")
                .append("学分加权平均分：").append(grade.getWeightedAverageCreadit()).append("\n")
                .append("年级排名：").append(grade.getRanking()).append("\n")
                .append("不及格门次：").append(grade.getFailCourseNumber());

        user.sendMessage(builder.toString());
    }

    // 通过学号查询
    @Command(CommandWordUtil.SEARCH_REGEX + SCORE + " {who}")
    public void onCodeSearchOtherScore(final QQXiaomingUser user,
                                       @CommandParameter("who") final String who) {
        final NwuPlugin instance = NwuPlugin.INSTANCE;
        final StudentManager studentManager = instance.getStudentManager();
        final GradeManager gradeManager = instance.getGradeManager();

        // 寻找该学生的学号
        Student student = null;
        if (who.matches("\\d+")) {
            long code = Long.parseLong(who);
            student = studentManager.forCode(code);
        } else {
            // 按 @ 人查找
            final long qq = AtUtil.parseQQ(who);
            if (qq > 0) {
                student = studentManager.forQQ(qq);
            } else {
                // 按姓名查找
                final Set<Student> allSameNameStudents = studentManager.forName(who);
                if (allSameNameStudents.size() == 1) {
                    student = allSameNameStudents.iterator().next();
                } else if (allSameNameStudents.size() > 1) {
                    // 检查是否有权限查询多个
                    Set<Student> searchableSamenameStudents = new HashSet<>();
                    for (Student sameNameStudent : allSameNameStudents) {
                        final String permissionNode = "nwu.score.search." + sameNameStudent.getCode();
                        if (sameNameStudent.getQq() == user.getQQ() || user.hasPermission(permissionNode)) {
                            searchableSamenameStudents.add(sameNameStudent);
                        }
                    }

                    if (searchableSamenameStudents.size() == 1) {
                        student = searchableSamenameStudents.iterator().next();
                    } else if (searchableSamenameStudents.size() > 1) {
                        user.sendError("你可以查询多个名为{}的同学的成绩，请使用 #查成绩 以交互式查询。", who);
                        return;
                    }
                }
            }
        }
        if (Objects.isNull(student)) {
            user.sendError("小明没有收录这名同学的信息");
            return;
        }

        // 判断查询权限
        if (user.getQQ() != student.getQq() && !user.checkPermissionAndReport("nwu.score.search." + student.getCode())) {
            return;
        }

        // 寻找该学生的成绩
        final Grade grade = gradeManager.forCode(student.getCode());
        if (Objects.isNull(grade)) {
            user.sendError("小明也想知道这名同学的成绩哦");
            return;
        }

        // 如果是多此一举就提醒一下
        final boolean isMyself = student.getQq() == user.getQQ();

        // 查询结果
        final StringBuilder builder = new StringBuilder();
        if (isMyself) {
            builder.append("你的成绩是：").append("\n");
        } else {
            builder.append("姓名：").append(student.getName()).append("\n")
                    .append("学号：").append(student.getCode()).append("\n")
                    .append("班级：").append(student.getClazz()).append("\n");
        }

        builder.append("学分加权平均分：").append(grade.getWeightedAverageCreadit()).append("\n")
                .append("年级排名：").append(grade.getRanking()).append("\n")
                .append("不及格门次：").append(grade.getFailCourseNumber());

        user.sendMessage(builder.toString());
        if (isMyself) {
            user.sendMessage("查询自己的成绩可以只输入「#查我成绩」哦");
        }
    }
}