package com.bom.zcloudbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bom.zcloudbackend.common.RespResult;
import com.bom.zcloudbackend.common.util.DateUtil;
import com.bom.zcloudbackend.dto.CreateFileDTO;
import com.bom.zcloudbackend.dto.UserFileListDTO;
import com.bom.zcloudbackend.entity.User;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.service.FileService;
import com.bom.zcloudbackend.service.UserFileService;
import com.bom.zcloudbackend.service.UserService;
import com.bom.zcloudbackend.vo.UserFileListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "文件")
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;

    @Resource
    private UserService userService;

    @Resource
    private UserFileService userFileService;

    @ApiOperation(value = "创建文件", notes = "认证当前token获取用户，创建目录/文件夹")
    @PostMapping("/createFile")
    public RespResult<String> createFile(@RequestBody CreateFileDTO createFileDTO,
        @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RespResult.fail().message("token认证失败");
        }
        LambdaQueryWrapper<UserFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFile::getFileName, "")
            .eq(UserFile::getFilePath, "")
            .eq(UserFile::getUserId, 0);
        List<UserFile> userFiles = userFileService.list(queryWrapper);
        if (!userFiles.isEmpty()) {
            return RespResult.fail().message("同一目录下存在相同文件名");
        }

        UserFile userFile = new UserFile();
        userFile.setUserId(sessionUser.getUserId());
        userFile.setFileName(createFileDTO.getFileName());
        userFile.setFilePath(createFileDTO.getFilePath());
        userFile.setIsDir(1);
        userFile.setUploadTime(DateUtil.getCurrentTime());

        userFileService.save(userFile);
        return RespResult.success();

    }

    @ApiOperation(value = "获取文件列表", notes = "获取文件列表，用于前端文件列表展示")
    @GetMapping("/getFileList")
    public RespResult<UserFileListVO> getUserFileList(UserFileListDTO userFileListDTO,
        @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RespResult.fail().message("token验证失败");
        }

        List<UserFileListVO> fileList = userFileService.getUserFileByFilePath(userFileListDTO.getFilePath(),
            sessionUser.getUserId(), userFileListDTO.getCurrentPage(),
            userFileListDTO.getPageCount());

        LambdaQueryWrapper<UserFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFile::getUserId, sessionUser.getUserId())
            .eq(UserFile::getFilePath, userFileListDTO.getFilePath());
        int total = userFileService.count(queryWrapper);

        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", fileList);

        return RespResult.success().data(map);
    }

    @ApiOperation(value = "分类获取文件",notes = "根据文件类型查看文件")
    @GetMapping("/selectFileByType")
    public RespResult<List<Map<String, Object>>> selectFileByType(int fileType, Long currentPage, Long pageCount,
        @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RespResult.fail().message("token校验失败");
        }

        Long userId = sessionUser.getUserId();
        Map<String, Object> map = userFileService.getUserFileByType(fileType, currentPage, pageCount,
            userId);
        return RespResult.success().data(map);
    }
}
