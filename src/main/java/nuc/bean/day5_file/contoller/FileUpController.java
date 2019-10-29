package nuc.bean.day5_file.contoller;

import org.omg.CORBA.PolicyHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


//单文件上传的页面有（upload.html,upload2.html(只上传图片)）
//其余的都是多文件上传（uploads.html(是最简单的上传)，uploads.html和upload2s.html是使用了io流）



@Controller
public class FileUpController {
    Logger logger = LoggerFactory.getLogger(getClass());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-HH-dd");
    //获取到文件保存的位置 “locahost”
    @Value("${spring.servlet.multipart.location=C://Users//123//Desktop//123456}")
    String locahost;

    //单文件上传
    @RequestMapping("/upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam("upfile") MultipartFile file) {
        //如果没有该文件夹，就自己创建一个
        File s = new File(locahost);
        if (!s.isDirectory()) {
            s.mkdirs();
        }
        //设置文件保存的路径
        String realPath = file.getOriginalFilename();
        logger.info(realPath);
        //把这个删掉就可以看了
        String format = simpleDateFormat.format(new Date());
        //logger.info(format);
        File folder = new File(format+realPath );
        System.out.println(folder.getName());
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        try {
            file.transferTo(folder);
            logger.info("上传成功");
            return  dealResultMap(true, "上传成功");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dealResultMap(false, "上传失败");
    }

    //多文件上传(通过在前端页面“input”中添加“multiple = "multiple"”)
    @RequestMapping("/uploads")
    public void uploads(@RequestParam("upfiles") List<MultipartFile> multipartFiles) {
        for (MultipartFile SS : multipartFiles) {
            String realPath = SS.getOriginalFilename();
            String format = simpleDateFormat.format(new Date());
            File folder = new File(format+realPath);
            try {
                SS.transferTo(folder);
                logger.info("上传成功");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

//多文件添加（通过在前端多几个添加口，不需要在前端页面添加 multiple = "multiple"）
@RequestMapping("/uploads12")
public void upload12(@RequestParam("file1") MultipartFile file1 ,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3 ,@RequestParam("file4") MultipartFile file4){
    List<MultipartFile> multipartFiles = new ArrayList<>();
    multipartFiles.add(file1);
    multipartFiles.add(file2);
    multipartFiles.add(file3);
    multipartFiles.add(file4);
    for (MultipartFile SS : multipartFiles) {
        String realPath = SS.getOriginalFilename();
        String format = simpleDateFormat.format(new Date());
        File folder = new File(format+realPath);
        try {
            SS.transferTo(folder);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}




    //第二种文件上传（IO流）
    //单文件上传

    @RequestMapping("/s")
    @ResponseBody
    public Map<String, Object> upload3(@RequestParam("photo") MultipartFile photo) {
        String path = "C:/Users/123/Desktop/123456/";

        String filename = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf("."));
        if (!suffix.equals(".jpg")) {
            return dealResultMap(false, "上传失败");
        }
        try {
            FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(path + filename + suffix));

        } catch (IOException e) {
            e.printStackTrace();
            return dealResultMap(false, "上传失败");
        }
        return dealResultMap(true, "上传成功");
    }

    private Map<String, Object> dealResultMap(boolean success, String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", success);
        result.put("msg", msg);
        return result;
    }

    // 多文件上传
    @RequestMapping("/ss")
    @ResponseBody
    public Map<String, Object> upload3s(@RequestParam("photos") MultipartFile[] photos) {
        String path = "C:/Users/123/Desktop/123456/";
        for (MultipartFile ph : photos) {
            String filename = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

            String suffix = ph.getOriginalFilename().substring(ph.getOriginalFilename().lastIndexOf("."));
            if (!suffix.equals(".jpg")) {
                return dealResultMap(false, "上传失败");
            }
            try {
                FileCopyUtils.copy(ph.getInputStream(), new FileOutputStream(path + filename + suffix));

            } catch (IOException e) {
                e.printStackTrace();
                return dealResultMap(false, "上传失败");
            }

        }
        return dealResultMap(true, "上传成功");
    }




    //文件下载（）



   }















