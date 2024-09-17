package com.github.teachingai.ollama;


import com.google.zxing.spring.boot.ZxingBarCodeTemplate;

public class BarCodeTest {

    static ZxingBarCodeTemplate barCodeTemplate = new ZxingBarCodeTemplate();

    // 条形码
    static String imgPath = "D:\\zxing_EAN13.png";

    public static void main(String[] args) {

        String contents = "6923450657713";
        int width = 105, height = 50;

        barCodeTemplate.encode(contents, width, height, imgPath);
        System.out.println("finished zxing EAN-13 encode.");


        String decodeContent = barCodeTemplate.decode(imgPath);
        System.out.println("解码内容如下：" + decodeContent);
        System.out.println("finished zxing EAN-13 decode.");
    }

}

