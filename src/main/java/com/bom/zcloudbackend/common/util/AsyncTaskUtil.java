package com.bom.zcloudbackend.common.util;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bom.zcloudbackend.entity.File;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.mapper.FileMapper;
import com.bom.zcloudbackend.mapper.UserFileMapper;
import com.bom.zcloudbackend.service.FileTransferService;
import com.bom.zcloudbackend.service.UserFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 异步任务工具类
 *
 * @author Frank Liang
 */
@Slf4j
@Component
@Async("asyncTaskExecutor")
public class AsyncTaskUtil {

    @Resource
    private UserFileService userFileService;

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileTransferService fileTransferService;

    /**
     * 获取文件引用数量
     *
     * @param fileId
     * @return
     */
    public Long getFilePointCount(Long fileId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getFileId, fileId);
        long count = userFileMapper.selectCount(lambdaQueryWrapper);
        return count;
    }

    /**
     * 删除本地文件
     *
     * @param userFileId
     * @return
     */
    public Future<String> deleteUserFile(Long userFileId) {
        UserFile userFile = userFileService.getById(userFileId);
        if (userFile.getIsDir() == 1) {
            LambdaQueryWrapper<UserFile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userFileLambdaQueryWrapper.eq(UserFile::getDeleteBatchNum, userFile.getDeleteBatchNum());
            List<UserFile> list = userFileService.list(userFileLambdaQueryWrapper);
            for (UserFile file : list) {
                Long filePointCount = getFilePointCount(file.getFileId());
                if (filePointCount != null && file.getIsDir() == 0) {
                    File fileBean = fileMapper.selectById(file.getFileId());
                    try {
                        fileTransferService.deleteFile(fileBean);
                        fileMapper.deleteById(fileBean.getFileId());
                    } catch (Exception e) {
                        log.error("删除本地文件失败" + JSON.toJSONString(fileBean));
                    }
                }
            }
        } else {
            System.out.println("删除本地1");
            Long filePointCount = getFilePointCount(userFile.getFileId());

            if (filePointCount != null && userFile.getIsDir() == 0) {
                System.out.println("删除本地2");
                File fileBean = fileMapper.selectById(userFile.getFileId());
                try {
                    fileTransferService.deleteFile(fileBean);
                    fileMapper.deleteById(fileBean.getFileId());
                } catch (Exception e) {
                    log.error("删除本地文件失败：" + JSON.toJSONString(fileBean));
                }
            }
        }
        return new AsyncResult<>("deleteUserFile");
    }

}
