package com.bom.zcloudbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.vo.UserFileListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Frank Liang
 */
@Mapper
public interface UserFileMapper extends BaseMapper<UserFile> {

    /**
     * 查找用户文件列表
     * @param userfile
     * @param beginCount
     * @param pageCount
     * @return
     */
    List<UserFileListVO> userfileList(UserFile userfile, Long beginCount, Long pageCount);

    /**
     * 根据类型查找文件
     * @param fileNameList
     * @param beginCount
     * @param pageCount
     * @param userId
     * @return
     */
    List<UserFileListVO> selectFileByExtendName(List<String> fileNameList, Long beginCount, Long pageCount,
        long userId);

    /**
     * 根据类型查询文件数量
     * @param fileNameList
     * @param beginCount
     * @param pageCount
     * @param userId
     * @return
     */
    Long selectCountByExtendName(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    /**
     * 根据类型限定查询文件
     * @param fileNameList
     * @param beginCount
     * @param pageCount
     * @param userId
     * @return
     */
    List<UserFileListVO> selectFileNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount,
        long userId);

    /**
     * 根据类型限定查询文件数量
     * @param fileNameList
     * @param beginCount
     * @param pageCount
     * @param userId
     * @return
     */
    Long selectCountNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    /**
     * 移动文件
     * @param oldfilePath
     * @param newfilePath
     * @param userId
     */
    void updateFilepathByFilepath(String oldfilePath, String newfilePath, Long userId);

    /**
     * 修改文件存放路径
     * @param filePath
     * @param oldFilePath
     * @param userId
     */
    void replaceFilePath(@Param("filePath") String filePath, @Param("oldFilePath") String oldFilePath, @Param("userId") Long userId);

    /**
     * 查询用户空间
     * @param userId
     * @return
     */
    Long selectStorageSizeByUserId(Long userId);
}
