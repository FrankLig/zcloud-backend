package com.bom.zcloudbackend.operation.download.product;

import com.bom.zcloudbackend.common.util.EncryptUserUtil;
import com.bom.zcloudbackend.common.util.PathUtil;
import com.bom.zcloudbackend.operation.download.BaseDownloader;
import com.bom.zcloudbackend.operation.download.domain.DownloadFile;
import com.bom.zcloudbackend.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Frank Liang
 */
@Component
public class LocalStorageDownloader extends BaseDownloader {

    @Resource
    private UserService userService;

    @Override
    public void download(HttpServletResponse response, DownloadFile downloadFile) {
        BufferedInputStream bis = null;
        byte[] buffer = new byte[1024];
        //设置文件路径
        File file = new File(PathUtil.getStaticPath() + downloadFile.getFileUrl());
        if (file.exists()) {

            FileInputStream fis = null;

            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void downloadEncFile(HttpServletResponse response, DownloadFile downloadFile, Long userId) {
        FileInputStream fis=null;
        OutputStream os=null;
        File file = new File(PathUtil.getStaticPath() + downloadFile.getFileUrl());
        if (file.exists()) {
            try {
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                String secretKey = EncryptUserUtil.decrypt(userService.getById(userId).getEncryptKey());
                SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                fis = new FileInputStream(file);
                os = response.getOutputStream();
                byte[] buffer=new byte[1024];
                int bytesRead;
                while ((bytesRead=fis.read(buffer))!=-1){
                    byte[] output = cipher.update(buffer, 0, bytesRead);
                    if(output!=null){
                        os.write(output);
                    }
                }
                byte[] outputBytes = cipher.doFinal();
                if(outputBytes!=null){
                    os.write(outputBytes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(os!=null){
                    try {
                        os.close();
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }
}
