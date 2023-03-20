package com.bom.zcloudbackend.controller;

import com.bom.zcloudbackend.common.RespResult;
import com.bom.zcloudbackend.common.util.DateUtil;
import com.bom.zcloudbackend.common.util.FileUtil;
import com.bom.zcloudbackend.dto.DownloadFileDTO;
import com.bom.zcloudbackend.dto.UploadFileDTO;
import com.bom.zcloudbackend.entity.File;
import com.bom.zcloudbackend.entity.User;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.service.FileService;
import com.bom.zcloudbackend.service.FileTransferService;
import com.bom.zcloudbackend.service.UserFileService;
import com.bom.zcloudbackend.service.UserService;
import com.bom.zcloudbackend.vo.UploadFileVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/fileTransfer")
public class FileTransferController {

    @Resource
    private UserService userService;

    @Resource
    private FileService fileService;

    @Resource
    private UserFileService userFileService;

    @Resource
    private FileTransferService fileTransferService;

    @ApiOperation(value = "极速上传", notes = "检验md5判断文件是否存在，存在则直接上传返回skip-true,不存在则返回skip-false早调用该接口post方法")
    @GetMapping("/uploadFile")
    public RespResult<UploadFileVO> uploadFileSpeed(UploadFileDTO uploadFileDTO, @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RespResult.fail().message("未登录");
        }

        UploadFileVO uploadFileVO = new UploadFileVO();
        HashMap<String, Object> param = new HashMap<>();
        param.put("identifier", uploadFileDTO.getIdentifier());
        synchronized (FileTransferController.class) {
            List<File> list = fileService.listByMap(param);
            if (list != null && !list.isEmpty()) {
                File file = list.get(0);
                UserFile userFile = new UserFile();
                userFile.setFileId(file.getFileId());
                userFile.setUserId(sessionUser.getUserId());
                userFile.setFilePath(uploadFileDTO.getFilePath());
                String fileName = uploadFileDTO.getFileName();
                userFile.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
                userFile.setExtendName(FileUtil.getFileExtendName(fileName));
                userFile.setIsDir(0);
                userFile.setUploadTime(DateUtil.getCurrentTime());
                userFileService.save(userFile);
                uploadFileVO.setSkipUpload(true);
            } else {
                uploadFileVO.setSkipUpload(false);
            }
        }
        return RespResult.success().data(uploadFileVO);
    }

    @ApiOperation(value = "上传文件夹", notes = "真正的文件上传接口")
    @PostMapping("/uploadFile")
    public RespResult<UploadFileVO> uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto,
        @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RespResult.fail().message("未登录");
        }

        fileTransferService.uploadFile(request, uploadFileDto, sessionUser.getUserId());
        UploadFileVO uploadFileVo = new UploadFileVO();
        return RespResult.success().data(uploadFileVo);

    }

    @ApiOperation(value = "下载文件")
    @RequestMapping("/downloadfile")
    public void downloadFile(HttpServletResponse response, DownloadFileDTO downloadFileDTO) {
        fileTransferService.downloadFile(response, downloadFileDTO);
    }
}
