package com.bom.zcloudbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bom.zcloudbackend.common.constant.FileConstant;
import com.bom.zcloudbackend.common.util.DateUtil;
import com.bom.zcloudbackend.entity.File;
import com.bom.zcloudbackend.entity.RecoveryFile;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.mapper.FileMapper;
import com.bom.zcloudbackend.mapper.RecoveryFileMapper;
import com.bom.zcloudbackend.mapper.UserFileMapper;
import com.bom.zcloudbackend.service.UserFileService;
import com.bom.zcloudbackend.vo.UserFileListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements UserFileService {

    //线程池创建方式可改进
    public static final Executor executor = Executors.newFixedThreadPool(20);

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private RecoveryFileMapper recoveryFileMapper;


    @Override
    public List<UserFileListVO> getUserFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        UserFile userfile = new UserFile();
        userfile.setUserId(userId);
        userfile.setFilePath(filePath);
        List<UserFileListVO> fileList = userFileMapper.userfileList(userfile, beginCount, pageCount);
        return fileList;
    }

    @Override
    public Map<String, Object> getUserFileByType(int fileType, Long currentPage, Long pageCount, Long userId) {
        Long beginCount = (currentPage - 1) * pageCount;
        List<UserFileListVO> fileList;
        Long total;
        if (fileType == FileConstant.OTHER_TYPE) {
            List<String> typeList = new ArrayList<>();
            typeList.addAll(Arrays.asList(FileConstant.DOC_FILE));
            typeList.addAll(Arrays.asList(FileConstant.IMG_FILE));
            typeList.addAll(Arrays.asList(FileConstant.VIDEO_FILE));
            typeList.addAll(Arrays.asList(FileConstant.MUSIC_FILE));

            fileList = userFileMapper.selectFileNotInExtendNames(typeList, beginCount, pageCount, userId);
            total = userFileMapper.selectCountNotInExtendNames(typeList, beginCount, pageCount, userId);
        } else {
            List<String> typeList = new ArrayList<>();
            if (fileType == FileConstant.IMAGE_TYPE) {
                typeList = Arrays.asList(FileConstant.IMG_FILE);
            } else if (fileType == FileConstant.DOC_TYPE) {
                typeList = Arrays.asList(FileConstant.DOC_FILE);
            } else if (fileType == FileConstant.VIDEO_TYPE) {
                typeList = Arrays.asList(FileConstant.VIDEO_FILE);
            } else if (fileType == FileConstant.MUSIC_TYPE) {
                typeList = Arrays.asList(FileConstant.MUSIC_FILE);
            }
            fileList = userFileMapper.selectFileByExtendName(typeList, beginCount, pageCount, userId);
            total = userFileMapper.selectCountByExtendName(typeList, beginCount, pageCount, userId);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", fileList);
        map.put("total", total);
        return map;
    }

    @Override
    public void deleteUserFile(Long userFileId, Long sessionUserId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        String uuid = UUID.randomUUID().toString();
        if (userFile.getIsDir() == 1) {
            LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userFileLambdaUpdateWrapper.set(UserFile::getDeleteTag, 1)
                .set(UserFile::getDeleteBatchNum, uuid)
                .set(UserFile::getDeleteTime, DateUtil.getCurrentTime())
                .eq(UserFile::getUserFileId, userFileId);
            userFileMapper.update(null, userFileLambdaUpdateWrapper);

            String filePath = userFile.getFilePath() + userFile.getFileName() + "/";
            updateFileDeleteStateByFilePath(filePath, userFile.getDeleteBatchNum(), sessionUserId);

        } else {
            UserFile userFileTemp = userFileMapper.selectById(userFileId);
            File file = fileMapper.selectById(userFileTemp.getFileId());
            LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userFileLambdaUpdateWrapper.set(UserFile::getDeleteTag, 1)
                .set(UserFile::getDeleteTime, DateUtil.getCurrentTime())
                .set(UserFile::getDeleteBatchNum, uuid)
                .eq(UserFile::getUserFileId, userFileTemp.getUserFileId());
            userFileMapper.update(null, userFileLambdaUpdateWrapper);

        }

        RecoveryFile recoveryFile = new RecoveryFile();
        recoveryFile.setUserFileId(userFileId);
        recoveryFile.setDeleteTime(DateUtil.getCurrentTime());
        recoveryFile.setDeleteBatchNum(uuid);
        recoveryFileMapper.insert(recoveryFile);

    }

    @Override
    public List<UserFile> selectFileTreeListLikeFilePath(String filePath, long userId) {

        filePath = filePath.replace("\\", "\\\\\\\\");
        filePath = filePath.replace("'", "\\'");
        filePath = filePath.replace("%", "\\%");
        filePath = filePath.replace("_", "\\_");

        LambdaQueryWrapper<UserFile> queryWrapper = new LambdaQueryWrapper<>();
        log.info("查询文件路径:" + filePath);

        queryWrapper.eq(UserFile::getUserId, userId)
            .likeRight(UserFile::getFilePath, filePath);
        return userFileMapper.selectList(queryWrapper);
    }

    //删除目录时将该文件目录下的所有文件都放入回收站
    private void updateFileDeleteStateByFilePath(String filePath, String deleteBatchNum, Long userId) {
        new Thread(() -> {
            List<UserFile> fileList = selectFileTreeListLikeFilePath(filePath, userId);
            for (int i = 0; i < fileList.size(); i++) {
                UserFile userFileTemp = fileList.get(i);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        LambdaUpdateWrapper<UserFile> updateWrapper = new LambdaUpdateWrapper<>();
                        updateWrapper.set(UserFile::getDeleteTag, 1)
                            .set(UserFile::getDeleteTime, DateUtil.getCurrentTime())
                            .set(UserFile::getDeleteBatchNum, deleteBatchNum)
                            .eq(UserFile::getUserFileId, userFileTemp.getUserFileId())
                            .eq(UserFile::getDeleteTag, 0);
                        userFileMapper.update(null, updateWrapper);
                    }
                });
            }
        }).start();
    }
}
