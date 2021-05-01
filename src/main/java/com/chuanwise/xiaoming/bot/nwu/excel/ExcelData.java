package com.chuanwise.xiaoming.bot.nwu.excel;

import com.chuanwise.xiaoming.bot.nwu.sheet.data.Grade;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.GradeManager;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.Student;
import com.chuanwise.xiaoming.bot.nwu.sheet.data.StudentManager;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表示 Excel 表格中的数据
 */
public class ExcelData {
    private XSSFSheet sheet;

    /**
     * 构造函数，初始化excel数据
     * @param filePath  excel路径
     * @param sheetName sheet表名
     */
    public ExcelData(String filePath,String sheetName)
            throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            XSSFWorkbook sheets = new XSSFWorkbook(inputStream);
            sheet = sheets.getSheet(sheetName);
        }
    }

    /**
     * 根据行和列的索引获取单元格的数据
     * @param row
     * @param column
     * @return
     */
    public String getExcelDateByIndex(int row,int column){
        XSSFRow row1 = sheet.getRow(row);
        String cell = row1.getCell(column).toString();
        return cell;
    }

    /**
     * 根据某一列值为“******”的这一行，来获取该行第x列的值
     * @param caseName
     * @param currentColumn 当前单元格列的索引
     * @param targetColumn 目标单元格列的索引
     * @return
     */
    public String getCellByCaseName(String caseName,int currentColumn,int targetColumn){
        String operateSteps="";
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for(int i=0;i<rows;i++){
            XSSFRow row = sheet.getRow(i);
            String cell = row.getCell(currentColumn).toString();
            if(cell.equals(caseName)){
                operateSteps = row.getCell(targetColumn).toString();
                break;
            }
        }
        return operateSteps;
    }

    //打印excel数据
    public void readExcelData(){
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for(int i=0;i<rows;i++){
            //获取列数
            XSSFRow row = sheet.getRow(i);
            int columns = row.getPhysicalNumberOfCells();
            for(int j=0;j<columns;j++){
                String cell = row.getCell(j).toString();
                System.out.println(cell);
            }
        }
    }

    //测试导入的方法
    public static void main(String[] args) {
        final File sheetFile = new File("【含QQ】成绩、排名.xlsx");
        final XSSFSheet sheet;
        try {
            try (InputStream inputStream = new FileInputStream(sheetFile)) {
                sheet = new XSSFWorkbook(inputStream).getSheet("Sheet1");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }



        // 去掉第一行
        //QQ
        //学号
        //姓名
        //班级
        //学分加权平均分
        //年级排名
        //不及格门次
        // QQ	学号	姓名	班级	学分加权平均分	年级排名	不及格门次
        Pattern pattern = Pattern.compile("([\\deE\\.]+)");
        if (true) {
            StudentManager studentManager = new StudentManager();
            try {
                for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    final XSSFRow row = sheet.getRow(rowIndex);
                    Student student = new Student();
                    // qq
                    final String qqString = row.getCell(0).toString().trim();
                    final Matcher qqMatcher = pattern.matcher(qqString);
                    if (qqMatcher.find()) {
                        student.setQq(new BigDecimal(qqMatcher.group(1)).longValue());
                    } else {
                        System.err.println("qq doesn't matches!");
                        break;
                    }

                    // code
                    final String codeString = row.getCell(1).toString().trim();
                    final Matcher codeMatcher = pattern.matcher(codeString);
                    if (codeMatcher.find()) {
                        student.setCode(new BigDecimal(codeMatcher.group(1)).longValue());
                    } else {
                        System.err.println("code doesn't matches!");
                        break;
                    }

                    // name
                    student.setName(row.getCell(2).toString().trim());
                    // clazz
                    student.setClazz(row.getCell(3).toString().trim());

                    studentManager.getData().add(student);
                    System.out.println(student);
                }
            } catch (Exception ignored) {
            }
            System.out.println(studentManager.getData().size() + " students added");

            studentManager.setFile(new File("students.json"));
            studentManager.save();
        } else {
            GradeManager gradeManager = new GradeManager();
            try {
                for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    final XSSFRow row = sheet.getRow(rowIndex);
                    Grade grade = new Grade();
                    // code
                    final String codeString = row.getCell(1).toString().trim();
                    final Matcher codeMatcher = pattern.matcher(codeString);
                    if (codeMatcher.find()) {
                        grade.setCode(new BigDecimal(codeMatcher.group(1)).longValue());
                    } else {
                        System.err.println("code doesn't matches!");
                        break;
                    }

                    // name
                    grade.setWeightedAverageCreadit(Double.parseDouble(row.getCell(4).toString().trim()));
                    // clazz
                    grade.setRanking(Integer.parseInt(row.getCell(5).toString().trim()));

                    grade.setFailCourseNumber(Integer.parseInt(row.getCell(6).toString().trim()));

                    gradeManager.getData().add(grade);
                    System.out.println(grade);
                }
            } catch (Exception ignored) {
            }
            System.out.println(gradeManager.getData().size() + " grades added");

            gradeManager.setFile(new File("grades.json"));
            gradeManager.save();
        }
    }
}