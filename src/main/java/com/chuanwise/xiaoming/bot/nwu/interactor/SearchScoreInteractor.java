package com.chuanwise.xiaoming.bot.nwu.interactor;

import com.chuanwise.xiaoming.bot.nwu.NwuPlugin;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.Grade;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.GradeManager;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.Student;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.StudentManager;
import com.taixue.xiaoming.bot.api.annotation.InteractMethod;
import com.taixue.xiaoming.bot.api.annotation.RequirePermission;
import com.taixue.xiaoming.bot.api.listener.dispatcher.user.DispatcherUser;
import com.taixue.xiaoming.bot.api.listener.interactor.user.InteractorUser;
import com.taixue.xiaoming.bot.core.listener.interactor.InteractorImpl;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SearchScoreInteractor extends InteractorImpl {
    @Override
    public boolean willInteract(DispatcherUser dispatcherUser) {
        return Objects.equals(dispatcherUser.getMessage(), "#查成绩");
    }

    @InteractMethod
    public void onMessage(InteractorUser user) {
        final NwuPlugin plugin = NwuPlugin.INSTANCE;
        final StudentManager studentManager = plugin.getStudentManager();
        final GradeManager gradeManager = plugin.getGradeManager();

        user.sendMessage("你想查谁的成绩呢 {}", getXiaomingBot().getEmojiManager().get("happy"));
        final String targetName = getNextInput(user);

        // 确定查找的同学
        Student targetStudent = null;
        if (targetName.matches("\\d+")) {
            // 输入的是数字，当做学号处理
            targetStudent = studentManager.forCode(Long.parseLong(targetName));

            if (Objects.isNull(targetStudent)) {
                user.sendError("找不到学号为「{}」的朋友哦", targetName);
                return;
            } else {
                // 动态验证权限
                final String permissionNode = "nwu.score.search." + targetStudent.getCode();
                if (!user.hasPermission(permissionNode)) {
                    user.sendError("你不能查询这位同学的成绩哦，因为缺少权限节点：{}", permissionNode);
                    return;
                }
            }
        } else {
            // 输入的不是数字，则当做姓名处理
            final Set<Student> students = new HashSet<>();
            final Set<Student> allSameNameStudents = studentManager.forName(targetName);
            for (Student student : allSameNameStudents) {
                // 动态验证权限
                final String permissionNode = "nwu.score.search." + student.getCode();
                // 他有权限查询或者他本人就是
                if (user.hasPermission(permissionNode) || student.getCode() == user.getQQ()) {
                    students.add(student);
                }
            }

            if (allSameNameStudents.isEmpty()) {
                user.sendError("没有找到名为「{}」的学生哦", targetName);
                return;
            } else if (students.isEmpty()) {
                user.sendError("小明找到了 {} 个名为「{}」的学生，但是你都没有权限查询他们的成绩哦", allSameNameStudents.size(), targetName);
                return;
            } else if (students.size() > 1) {
                StringBuilder builder = new StringBuilder()
                        .append("小明找到了多个名为「" + targetName + "」的学生，他们分别是：");
                int index = 1;
                final Student[] studentArray = students.toArray(new Student[0]);
                for (Student student : studentArray) {
                    builder.append("\n").append(index++).append("、")
                            .append("学号：").append(student.getCode()).append("\n")
                            .append("班级：").append(student.getClazz());
                }
                user.sendMessage(builder.toString());
                user.sendMessage("想要查询哪个{}呢？输入他的序号吧", targetName);
                while (true) {
                    final String targetCode = getNextInput(user);

                    if (Objects.equals(targetCode, "退出")) {
                        user.sendMessage("下次想查成绩的时候记得告诉我「#查成绩」哦");
                        return;
                    } else if (targetCode.matches("\\d+")) {
                        index = Integer.parseInt(targetCode);
                        if (index <= 0 || index > students.size()) {
                            user.sendError("序号「{}」应该在 1 到 {} 之间哦", targetCode, students.size());
                        } else {
                            targetStudent = studentArray[index - 1];
                            break;
                        }
                    } else {
                        user.sendError("「{}」并不是一个合理的序号哦，重新说一遍吧。如果想要退出告诉我「退出」就可以哦。", targetCode);
                    }
                }
            } else {
                targetStudent = students.iterator().next();
            }
        }

        // 查找他的成绩
        final Grade grade = gradeManager.forCode(targetStudent.getCode());
        if (Objects.isNull(grade)) {
            user.sendError("小明并没有收录这位{}同学的成绩哦", targetStudent.getName());
            return;
        }

        // 如果是多此一举一会儿提醒更简单的查询方式
        boolean isMyself = targetStudent.getQq() == user.getQQ();

        // 查询结果
        final StringBuilder builder = new StringBuilder();
        if (isMyself) {
            builder.append("你的成绩是：").append("\n");
        } else {
            builder.append("姓名：").append(targetStudent.getName()).append("\n")
                    .append("学号：").append(targetStudent.getCode()).append("\n")
                    .append("班级：").append(targetStudent.getClazz()).append("\n");
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
