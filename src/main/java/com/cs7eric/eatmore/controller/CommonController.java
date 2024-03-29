package com.cs7eric.eatmore.controller;

import com.cs7eric.eatmore.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 *
 * @author cs7eric
 * @date 2023/01/15
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${eatmore.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return {@link R}<{@link String}>
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        // file 是一个 tmp 文件，需要转存到其他位置，否则本次请求完成过后，tmp 文件会删除
        log.info(file.toString());

        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 使用 UUID 重新生成文件名
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前 目录是否存在
        if(!dir.exists()){

            // 目录不存在 需要重新创建
            dir.mkdirs();
        }

        // 将 临时文件转存到 指定位置
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return R.success(fileName);
    }


    /**
     * 文件下载
     *
     * @param name     名字
     * @param response 响应
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {

            // 输入流，通过 输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // 输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){

                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            // 关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
