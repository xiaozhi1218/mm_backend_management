package com.itheima.mm.controller;

import com.itheima.framework.Controller;
import com.itheima.framework.RequestMapping;
import com.itheima.mm.entity.Result;
import com.itheima.mm.utils.JsonUtils;
import com.itheima.mm.utils.UploadUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 包名:com.itheima.mm.controller
 *
 * @author Leevi
 * 日期2020-11-04  14:41
 */
@Controller
public class CommonsController {
    @RequestMapping("/commons/uploadFile")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1. 获取客户端上传的文件: 其实就是获取一个字节输入流
        InputStream is = null;
        FileOutputStream os = null;
        try {
            //1. 获取客户端上传的图片
            //创建磁盘工厂对象
            DiskFileItemFactory itemFactory = new DiskFileItemFactory();
            //创建Servlet的上传解析对象,构造方法中,传递磁盘工厂对象
            ServletFileUpload fileUpload = new ServletFileUpload(itemFactory);
            /*
             * fileUpload调用方法 parseRequest,解析request对象
             * 页面可能提交很多内容 文本框,文件,菜单,复选框 是为FileItem对象
             * 返回集合,存储的文件项对象
             */
            List<FileItem> list = fileUpload.parseRequest(request);
            //图片存储的路径
            String imgUrl = null;
            for (FileItem fileItem : list) {
                //这就是获取到的客户端上传文件的字节输入流
                is = fileItem.getInputStream();
                //获取客户端上传文件的那个文件名字
                String fileName = fileItem.getName();
                //解决图片重名问题: 使用UUID作为图片的名字
                fileName = UploadUtils.getUUIDName(fileName);

                //一个目录中不能存放过多文件: 我们选择创建多级目录
                String randomDir = UploadUtils.getDir();
                //第二步: 准备一个文件夹，用于存储客户端上传的文件
                String realPath = request.getServletContext().getRealPath("img/upload"+randomDir);
                File file = new File(realPath);

                //如果硬盘中并没有该文件夹，则将其在硬盘中创建出来
                if (!file.exists()) {
                    file.mkdirs();
                }

                //第三步: 使用字节输出流将客户端上传的文件输出到指定文件夹中
                os = new FileOutputStream(new File(file,fileName));

                imgUrl = "img/upload"+randomDir+"/"+fileName;
                //循环读写，将is中的字节通过os写入到磁盘
                IOUtils.copy(is,os);
            }
            JsonUtils.printResult(response,new Result(true,"上传图片成功",imgUrl));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"上传图片失败"));
        }finally {
            os.close();
            is.close();
        }
    }
}
