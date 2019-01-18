package com.trans.controller;

import com.trans.enty.TreeElement;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import utils.TransUtil;
import utils.UnZipUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JIAYU_WANG
 *  1. 上传文件
 *  2. 转存为zip
 *  3. 解压
 *  4. 生成Excel
 *  5. 下载
 */
@WebServlet(name = "TransServlet")
public class TransServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = this.getServletContext().getRealPath("upload");
        String fileName = null;
        // 1.上传
        try {
            // 创建文件项工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 创建文件上传核心组件
            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
            // 解决中文乱码问题
            servletFileUpload.setHeaderEncoding("UTF-8");
            // 解析Multipart
            List<FileItem> items = servletFileUpload.parseRequest(request);
            // 解析items
            for (FileItem item: items){
                fileName = item.getName().split("\\.")[0];
                // 输入流
                InputStream inputStream = item.getInputStream();
                // 2.转存zip
                File desFile = new File(path, "temp.zip");
                // 输出流
                FileOutputStream fileOutputStream = new FileOutputStream(desFile);
                // 完成文件复制
                byte[] bytes = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(bytes)) != -1){
                    fileOutputStream.write(bytes, 0 ,len);
                }
                // 关闭流
                fileOutputStream.close();
                inputStream.close();
            }
            // 3.解压
            File zipFile = new File(path, "temp.zip");
            UnZipUtil.unZip(zipFile,path);
            // 4.生成Excel
            File xmlFile = new File(path,"content.xml");
            // 创建SAXReader
            SAXReader reader = new SAXReader();
            // 获取Document对象
            Document doc = reader.read(xmlFile);
            // 获取根节点
            Element root = doc.getRootElement();

            // 获取所有节点
            List<Element> elements = new ArrayList<>();
            TransUtil.getAllChildren(root,elements);

            // 去掉画布
            elements.remove(elements.size()-1);
            // 去掉extension
            elements.remove(elements.size()-1);

            // pID
            List<String> pID = new ArrayList<>();
            // ID
            List<String> ID = new ArrayList<>();
            // 节点转化成TreeElement
            List<TreeElement> treeElements = new ArrayList<>();
            for (Element element : elements
                    ) {
                TreeElement treeElement = new TreeElement();
                treeElement.setId(element.getParent().attributeValue("id"));
                treeElement.setContent(element.getTextTrim());
                treeElement.setPid(TransUtil.getPID(element.getParent()));
                treeElements.add(treeElement);
                pID.add(treeElement.getPid());
                ID.add(treeElement.getId());
                System.out.println(treeElement.toString());
            }

            // 叶子节点
            List<TreeElement> leafElements = TransUtil.getLeafNode(treeElements,pID,ID);

            List<List<TreeElement>> strList = new ArrayList<>();
            for (TreeElement treeElement:leafElements
                    ) {
                List<TreeElement> list = new LinkedList();
                list.add(treeElement);
                TransUtil.getList(treeElement,list,treeElements);
                strList.add(list);
            }

            // 声明一个工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet();
            // 第一行
            HSSFRow row = sheet.createRow(0);
            // 第一列
            HSSFCell cell1 = row.createCell(0);
            HSSFCell cell2 = row.createCell(1);
            HSSFCell cell3 = row.createCell(2);
            HSSFCell cell4 = row.createCell(3);
            HSSFCell cell5 = row.createCell(4);
            HSSFCell cell6 = row.createCell(5);
            HSSFCell cell7 = row.createCell(6);
            HSSFCell cell8 = row.createCell(7);
            cell1.setCellValue("项目名称");
            cell2.setCellValue("模块");
            cell3.setCellValue("功能");
            cell4.setCellValue("子功能");
            cell5.setCellValue("确认点");
            cell6.setCellValue("操作步骤");
            cell7.setCellValue("预期结果");
            cell8.setCellValue("实际结果");

            HSSFCellStyle style = workbook.createCellStyle();
            // 背景色
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
            // 水平居中
            style.setAlignment(HorizontalAlignment.CENTER);
            // 垂直居中
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            sheet.setColumnWidth(0, 20 * 256);
            sheet.setColumnWidth(1, 20 * 256);
            sheet.setColumnWidth(2, 20 * 256);
            sheet.setColumnWidth(3, 20 * 256);
            sheet.setColumnWidth(4, 20 * 256);
            sheet.setColumnWidth(5, 20 * 256);
            sheet.setColumnWidth(6, 20 * 256);
            sheet.setColumnWidth(7, 20 * 256);
            // 单元格绑定样式
            cell1.setCellStyle(style);
            cell2.setCellStyle(style);
            cell3.setCellStyle(style);
            cell4.setCellStyle(style);
            cell5.setCellStyle(style);
            cell6.setCellStyle(style);
            cell7.setCellStyle(style);
            cell8.setCellStyle(style);
            int rowNum = 1;
            // 写入Excel
            for (List<TreeElement> list:strList
                    ) {
                // 创建行
                HSSFRow row1 = sheet.createRow(rowNum);
                rowNum++;
                int cellNum = 0;
                Collections.reverse(list);

                for ( TreeElement cellValue:list){
                    String treeValue = cellValue.getContent();
                    if( (!"确认点".equals(treeValue)) && (cellNum <= 4)){
                        HSSFCell cell = row1.createCell(cellNum);
                        cell.setCellValue(cellValue.getContent());
                        cellNum ++;
                    }else if("确认点".equals(treeValue)){
                        cellNum = 4;
                    }else if(cellValue == list.get(list.size() - 1)){
                        // 最后一列  结果列
                        HSSFCell cell = row1.createCell(6);
                        cell.setCellValue(cellValue.getContent());
                    }else{
                        HSSFCell cell = row1.getCell(5);
                        if (cell == null){
                            // cell为空的情况
                            cell = row1.createCell(5);
                            cell.setCellValue(cellValue.getContent());
                        }else {
                            // cell不为空的情况
                            String temp = cell.getStringCellValue() + "\r\n" + cellValue.getContent();
                            cell.setCellValue(temp);
                        }
                    }
                }
            }
            // 导出到Excel
            FileOutputStream output = new FileOutputStream(path + "\\" + fileName  + ".xls");
            workbook.write(output);
            // 关闭流
            output.close();
            // 下载
            String downName = fileName + "测试用例.xls";
            //设置响应头，控制浏览器下载该文件
            response.setHeader("content-disposition", "attachment;filename=" + new String(downName.getBytes("UTF-8"),"ISO8859-1"));
            //读取要下载的文件，保存到文件输入流
            FileInputStream fileInputStream = new FileInputStream(path + "\\" + fileName + ".xls");
            //创建输出流
            OutputStream outputStream = response.getOutputStream();
            // 创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            while((len = fileInputStream.read(buffer))>0){
                outputStream.write(buffer, 0, len);
            }
            // 关闭流
            fileInputStream.close();
            outputStream.close();
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
