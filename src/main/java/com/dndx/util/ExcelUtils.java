package com.dndx.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dndx.entity.TimestampResult;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelUtils {

    private static int sheetIndex = 0;
    private static final String filename = "./"+(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_").format(new Date()))+"计算结果.xls";
    /**
     * 如果创建多个sheet，只能用相同的workbook
     */
    private static HSSFWorkbook workbook = new HSSFWorkbook();
    /**
     * POI 创建Excel文件
     * @author yangtingting
     * @date 2019/07/29
     */
    public static void writeExcel(JSONArray jsonArray,String sheetName) {
        //创建工作表sheeet
        HSSFSheet sheet=workbook.createSheet();
        workbook.setSheetName(sheetIndex,sheetName);
        //创建第一行标题
        HSSFRow row=sheet.createRow(0);
        String[] title={"timestamp","value"};
        HSSFCell cell=null;
        for (int i=0;i<title.length;i++){
            cell=row.createCell(i);
            cell.setCellValue(title[i]);
        }
        //追加数据（从第二行开始写）
        for (int i=0;i<jsonArray.size();i++){
            JSONObject tt = (JSONObject) jsonArray.get(i);
            String name = tt.getString("timestamp")+"";
            String paperNum = tt.getString("value")+"";
            HSSFRow nextrow=sheet.createRow(i+1);
            HSSFCell cell2=nextrow.createCell(0);
            cell2.setCellValue(name);
            cell2=nextrow.createCell(1);
            cell2.setCellValue(paperNum);
        }
        //创建一个文件
        File file=null;
        FileOutputStream stream = null;
        try{
            file = new File(filename);
            if(file == null){
                // 不存在文件就创建
                file.createNewFile();
            }
            stream = FileUtils.openOutputStream(file);
            workbook.write(stream);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sheetIndex++;
    }

    public static void writeExcel(List<TimestampResult> timestampResults, String sheetName) {
        //创建工作表sheeet
        HSSFSheet sheet=workbook.createSheet();
        workbook.setSheetName(sheetIndex,sheetName);
        //创建第一行标题
        HSSFRow row=sheet.createRow(0);
        String[] title={"timestamp","result"};
        HSSFCell cell=null;
        for (int i=0;i<title.length;i++){
            cell=row.createCell(i);
            cell.setCellValue(title[i]);
        }
        //追加数据（从第二行开始写）
        for (int i=0;i<timestampResults.size();i++){
            TimestampResult tt = timestampResults.get(i);
            int name = tt.getTimestamp();
            String paperNum = tt.getResult();
            HSSFRow nextrow=sheet.createRow(i+1);
            HSSFCell cell2=nextrow.createCell(0);
            cell2.setCellValue(name);
            cell2=nextrow.createCell(1);
            cell2.setCellValue(paperNum);
        }
        //创建一个文件
        File file=null;
        FileOutputStream stream = null;
        try{
            file = new File(filename);
            if(file == null){
                // 不存在文件就创建
                file.createNewFile();
            }
            stream = FileUtils.openOutputStream(file);
            workbook.write(stream);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sheetIndex++;
    }

    public static void main(String[] args) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp",123);
        jsonObject.put("value",222);
        jsonArray.add(jsonObject);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("timestamp",1123);
        jsonObject1.put("value",2122);
        jsonArray.add(jsonObject1);
        writeExcel(jsonArray,"zhangwenjie");
    }
}


