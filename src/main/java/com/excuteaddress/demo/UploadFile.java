package com.excuteaddress.demo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UploadFile {

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        if (!file.isEmpty()) {
            String saveFileName = file.getOriginalFilename();
            File saveFile = new File("D:/aaa/upload/" + saveFileName);
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
                out.write(file.getBytes());
                out.flush();
                out.close();

                File saveFile2 = new File("D:/aaa/upload/");
                for (File file2 : saveFile2.listFiles()) {
                    List<AddressExcel> addressExcels = importData(file2);

                    File outfile = new File("D:/aaa/outload/");
                    if (!outfile.exists()) {
                        outfile.mkdirs();
                    }

                    exportAddressInfo(addressExcels, "D:/aaa/upload/sample.xlsx", outfile.getAbsolutePath() +File.separator+ file2.getName());
                }
                return "处理完成";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败";
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败";
            }
        } else {
            return "上传失败，因为文件为空.";
        }
    }



    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }


    public List<AddressExcel> importData(File file) {
        Workbook wb = null;
        List<AddressExcel> HeroList = new ArrayList();
        try {
            if (UploadFile.isExcel2007(file.getPath())) {
                wb = new XSSFWorkbook(new FileInputStream(file));
            } else {
                wb = new HSSFWorkbook(new FileInputStream(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Sheet sheet = wb.getSheetAt(0);
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);//获取索引为i的行，以0开始
            String code = row.getCell(0).getStringCellValue();
            String excutedata = row.getCell(1).getStringCellValue();
            String requirement = row.getCell(2).getStringCellValue();
            String suggestion = row.getCell(3).getStringCellValue();

            AddressExcel hero = new AddressExcel();
            hero.setCode(code);
            hero.setExcutedata(excutedata);
            hero.setRequirement(requirement);
            hero.setSuggestion(suggestion);
            HeroList.add(hero);
        }
        try {
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return HeroList;
    }


    public static void exportAddressInfo(List<AddressExcel> heroList, String templetFilePath, String exportFilePath) {
        try {
            File exportFile = new File(exportFilePath);
            File templetFile = new File(templetFilePath);
            Workbook workBook;

            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(exportFile);
            FileInputStream fis = new FileInputStream(templetFile);
            if (isExcel2007(templetFile.getPath())) {
                workBook = new XSSFWorkbook(fis);
            } else {
                workBook = new HSSFWorkbook(fis);
            }

            Sheet sheet = workBook.getSheetAt(0);

            int rowIndex = 1;
            for (AddressExcel item : heroList) {
                OutAddress outAddress = new OutAddress();
                //处理逻辑
                String searchStr = item.getRequirement() + "," + item.getSuggestion();
                Pattern abs = Pattern.compile("湛江市.{1,4}(市|区|县)(.{1,5}镇)?(.{1,5}街道)?(.{1,5}片区)?(.{1,10}大道)?(.{1,10}村)?(.{1,10}社区)?(.{1,10}小区)?(.{1,10}街)?(.{1,10}路)?");
                Matcher matcher = abs.matcher(searchStr);
                while(matcher.find()){
                    Row row = sheet.createRow(rowIndex);
                    row.createCell(0).setCellValue(matcher.group());
                    rowIndex++;
                }
            }

            workBook.write(out);
            out.flush();
            out.close();

            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
