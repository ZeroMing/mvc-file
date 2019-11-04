package com.example.demo;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    /**
     *
     */
    @GetMapping(value = "/download")
    public void downLoad(HttpServletResponse response) throws UnsupportedEncodingException {
        String originalFileName = "1.pdf";
        String path = "F:\\BaiduYunDownload\\傻瓜都读得懂的理财书.pdf";
        File file = new File(path);
        //相应头的处理
        //清空response中的输出流
        response.reset();
        //设置文件大小
        response.setContentLength((int) file.length());
        //设置Content-Type头
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置Content-Disposition头 以附件形式解析
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(path, "utf-8"));

        //将来文件流写入response中
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //获取文件输入流
            fileInputStream = new FileInputStream(file);
            //创建数据缓冲区
            byte[] buffers = new byte[1024];
            //通过response中获取ServletOutputStream输出流
            outputStream = response.getOutputStream();
            int length;
            while ((length = fileInputStream.read(buffers)) > 0) {
                //写入到输出流中
                outputStream.write(buffers, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //流的关闭
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    @GetMapping("/download1")
    public ResponseEntity<byte[]> export(String fileName, String filePath) throws IOException {
        String path = "F:\\BaiduYunDownload\\傻瓜都读得懂的理财书.pdf";
        HttpHeaders headers = new HttpHeaders();
        File file = new File(path);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

}
