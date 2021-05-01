# 西北大学插件：xiaoming-bot-nwu
为西大群员开发的小明插件，基于 [小明机器人框架](https://github.com/Chuanwise/xiaoming-bot) 编写。

## 使用方法
下载最新 `release` 的插件本体 `jar` 文件，放在`小明根目录/plugins`，启动小明即可。

## 导入数据
暂时没写录入成绩的方式，但可通过修改 `小明根目录/plugins/nwu/students.json` 以录入学生学号、QQ、院系等信息，
通过修改 `小明根目录/plugins/nwu/grades.json`  以录入成绩。例如：

### 学生信息表：students.json
```json
{
    "@class": "com.chuanwise.xiaoming.bot.nwu.sheet.data.StudentManager",
    "data": [
        "java.util.HashSet",
        [
            {
                "@class": "com.chuanwise.xiaoming.bot.nwu.sheet.data.Student",
                "code": 123456789,
                "qq": 987654321,
                "name": "学生姓名",
                "clazz": "班级名"
            }
        ]
    ]
}
```
其中学生的 `code` 属性是学号。

### 学生成绩表：grades.json
```json
{
    "@class": "com.chuanwise.xiaoming.bot.nwu.sheet.data.GradeManager",
    "data": [
        "java.util.HashSet",
        [
            {
                "@class": "com.chuanwise.xiaoming.bot.nwu.sheet.data.Grade",
                "code": 2019118082,
                "weightedAverageCreadit": 71.7,
                "ranking": 122,
                "failCourseNumber": 0
            }
        ]
    ]
}
```
其中：
* `weightedAverageCreadit`：加权平均学分
* `ranking`：年级排名
* `failCourseNumber`：不及格科目数