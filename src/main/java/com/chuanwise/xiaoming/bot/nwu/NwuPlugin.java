package com.chuanwise.xiaoming.bot.nwu;

import com.chuanwise.xiaoming.bot.nwu.command.executor.NwuCommandExecutor;
import com.chuanwise.xiaoming.bot.nwu.interactor.SearchScoreInteractor;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.GradeManager;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.StudentManager;
import com.taixue.xiaoming.bot.core.plugin.XiaomingPluginImpl;

import java.io.File;

public class NwuPlugin extends XiaomingPluginImpl {
    public static NwuPlugin INSTANCE;

    // 学生信息管理器
    StudentManager studentManager;

    public StudentManager getStudentManager() {
        return studentManager;
    }

    // 成绩管理器
    GradeManager gradeManager;

    public GradeManager getGradeManager() {
        return gradeManager;
    }

    NwuCommandExecutor nwuCommandExecutor = new NwuCommandExecutor();
    SearchScoreInteractor searchScoreInteractor = new SearchScoreInteractor();

    @Override
    public void onEnable() {
        INSTANCE = this;
        getDataFolder().mkdirs();
        loadData();

        getXiaomingBot().getCommandManager().register(nwuCommandExecutor, this);
        getXiaomingBot().getInteractorManager().register(searchScoreInteractor, this);
    }

    public void loadData() {
        studentManager = getXiaomingBot().getFileSavedDataFactory()
                .forFileOrProduce(new File(getDataFolder(), "students.json"),
                        StudentManager.class,
                        StudentManager::new);
        gradeManager = getXiaomingBot().getFileSavedDataFactory()
                .forFileOrProduce(new File(getDataFolder(), "grades.json"),
                        GradeManager.class,
                        GradeManager::new);
    }
}
