package com.bom.zcloudbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.vo.UserFileListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserFileMapper extends BaseMapper<UserFile> {

    List<UserFileListVO> userfileList(UserFile userfile, Long beginCount, Long pageCount);

    List<UserFileListVO> selectFileByExtendName(List<String> fileNameList, Long beginCount, Long pageCount,
        long userId);

    Long selectCountByExtendName(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    List<UserFileListVO> selectFileNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount,
        long userId);

    Long selectCountNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);
}
