package com.company.project.utils.poi;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;

public class PDFUtils{
    // 必须要有
    private static final String PATH_FONT_COUR = "C:\\Windows\\Fonts\\STFANGSO.TTF";

    public static void addPageNum(String pdfPath, String outFilePath) {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            // 创建一个pdf读入流
            reader = new PdfReader(pdfPath);
            // 根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
            stamper = new PdfStamper(reader, new FileOutputStream(outFilePath));
            // 这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
            BaseFont bf = BaseFont.createFont(PATH_FONT_COUR, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            // baseFont不支持字体样式设定.但是font字体要求操作系统支持此字体会带来移植问题.
            Font font = new Font(bf, 10);
            font.setStyle(Font.BOLD);
            font.getBaseFont();
            // 获得宽
            Rectangle pageSize = reader.getPageSize(1);
            float width = pageSize.getWidth();
            // 获取页码
            int num = reader.getNumberOfPages();
            for (int i = 1; i <= num; i++) {
                PdfContentByte over = stamper.getOverContent(i);

                over.beginText();
                over.setFontAndSize(font.getBaseFont(), 13);
                over.setColorFill(BaseColor.BLACK);
                // 设置页码在页面中的坐标
                over.setTextMatrix((int) width/2, 30);
//				over.setTextRenderingMode(1); // 设置字体加粗
                over.showText( i +" / "+num);
                over.endText();
                over.stroke();
            }
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static void main(String[] args) {
        addPageNum("C:/1.pdf","C:/2.pdf");

    }
}
