package com.bom.zcloudbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bom.zcloudbackend.common.constant.FileConstant;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.mapper.UserFileMapper;
import com.bom.zcloudbackend.service.UserFileService;
import com.bom.zcloudbackend.vo.UserFileListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements UserFileService {

    @Resource
    private UserFileMapper userFileMapper;

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
}
