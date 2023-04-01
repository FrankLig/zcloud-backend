package com.bom.zcloudbackend.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bom.zcloudbackend.common.RespResult;
import com.bom.zcloudbackend.common.util.DateUtil;
import com.bom.zcloudbackend.dto.*;
import com.bom.zcloudbackend.entity.File;
import com.bom.zcloudbackend.entity.User;
import com.bom.zcloudbackend.entity.UserFile;
import com.bom.zcloudbackend.service.FileService;
import com.bom.zcloudbackend.service.UserFileService;
import com.bom.zcloudbackend.service.UserService;
import com.bom.zcloudbackend.vo.TreeNodeVO;
import com.bom.zcloudbackend.vo.UserFileListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

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
        queryWrapper.eq(UserFile::getFileName, createFileDTO.getFileName())
            .eq(UserFile::getFilePath, createFileDTO.getFilePath())
            .eq(UserFile::getUserId, sessionUser.getUserId());
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
        userFile.setDeleteTag(0);
        userFile.setExtendName("文件夹");

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
            .eq(UserFile::getFilePath, userFileListDTO.getFilePath())
            .eq(UserFile::getDeleteTag, 0);
        int total = userFileService.count(queryWrapper);

        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", fileList);

        return RespResult.success().data(map);
    }

    @ApiOperation(value = "分类获取文件", notes = "根据文件类型查看文件")
    @GetMapping("/getFileListByType")
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

    @ApiOperation(value = "删除文件", notes = "删除文件或目录")
    @PostMapping("/deleteFile")
    public RespResult deleteFile(@RequestBody DeleteFileDTO deleteFileDTO, @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        userFileService.deleteUserFile(deleteFileDTO.getUserFileId(), sessionUser.getUserId());
        return RespResult.success();
    }

    @ApiOperation("批量删除文件")
    @PostMapping("/batchDeleteFile")
    public RespResult deleteImageByIds(@RequestBody BatchDeleteFileDTO batchDeleteFileDTO,
        @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        List<UserFile> userFiles = JSON.parseArray(batchDeleteFileDTO.getFiles(), UserFile.class);
        for (UserFile userFile : userFiles) {
            userFileService.deleteUserFile(userFile.getUserFileId(), sessionUser.getUserId());
        }
        return RespResult.success().message("批量删除文件成功");
    }


    @ApiOperation(value = "获取文件树", notes = "移动文件时需要用来展示目录")
    @GetMapping("/getFileTree")
    public RespResult<TreeNodeVO> getFileTree(@RequestHeader("token") String token){
        RespResult<TreeNodeVO> result = new RespResult<TreeNodeVO>();
        UserFile userFile = new UserFile();
        User sessionUser = userService.getUserByToken(token);
        userFile.setUserId(sessionUser.getUserId());

        List<UserFile> filePathList = userFileService.selectFilePathTreeByUserId(sessionUser.getUserId());
        TreeNodeVO resultTreeNode = new TreeNodeVO();
        resultTreeNode.setLabel("/");

        for (int i = 0; i < filePathList.size(); i++){
            String filePath = filePathList.get(i).getFilePath() + filePathList.get(i).getFileName() + "/";

            Queue<String> queue = new LinkedList<>();

            String[] strArr = filePath.split("/");
            for (int j = 0; j < strArr.length; j++){
                if (!"".equals(strArr[j]) && strArr[j] != null){
                    queue.add(strArr[j]);
                }

            }
            if (queue.size() == 0){
                continue;
            }
            resultTreeNode = insertTreeNode(resultTreeNode,"/", queue);


        }
        result.setSuccess(true);
        result.setData(resultTreeNode);
        return result;

    }

    @ApiOperation(value = "移动文件", notes = "可以移动文件或目录")
    @PostMapping("/moveFile")
    public RespResult<String> moveFile(@RequestBody MoveFileDTO moveFileDTO, @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        String oldFilePath = moveFileDTO.getOldFilePath();
        String newFilePath = moveFileDTO.getFilePath();
        String fileName = moveFileDTO.getFileName();
        String extendName = moveFileDTO.getExtendName();

        userFileService.updateFilepathByFilepath(oldFilePath, newFilePath, fileName, extendName,
            sessionUser.getUserId());
        return RespResult.success();
    }

    @ApiOperation(value = "批量移动文件", notes = "可以移动多个文件或者目录")
    @PostMapping("/batchmovefile")
    public RespResult<String> batchMoveFile(@RequestBody BatchMoveFileDTO batchMoveFileDto,
        @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        String files = batchMoveFileDto.getFiles();
        String newfilePath = batchMoveFileDto.getFilePath();
        List<UserFile> userFiles = JSON.parseArray(files, UserFile.class);

        for (UserFile userFile : userFiles) {
            userFileService.updateFilepathByFilepath(userFile.getFilePath(), newfilePath, userFile.getFileName(),
                userFile.getExtendName(), sessionUser.getUserId());
        }

        return RespResult.success().data("批量移动文件成功");

    }

    public RespResult<String> renameFile(@RequestBody RenameFileDTO renameFileDTO,
        @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        UserFile userFile = userFileService.getById(renameFileDTO.getUserFileId());
        List<UserFile> userFiles = userFileService.selectUserFileByNameAndPath(renameFileDTO.getFileName(),
            userFile.getFilePath(),
            sessionUser.getUserId());
        if (userFiles != null && !userFiles.isEmpty()) {
            return RespResult.fail().message("已存在同名文件");
        }
        if (1 == userFile.getIsDir()) {
            LambdaUpdateWrapper<UserFile> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(UserFile::getFileName, renameFileDTO.getFileName())
                .set(UserFile::getUploadTime, DateUtil.getCurrentTime())
                .eq(UserFile::getUserFileId, renameFileDTO.getUserFileId());
            userFileService.update(updateWrapper);
            userFileService.replaceUserFilePath(userFile.getFilePath() + renameFileDTO.getFileName() + "/",
                userFile.getFilePath() + userFile.getFileName() + "/", sessionUser.getUserId());

        } else {
            File file = fileService.getById(userFile.getFileId());
            LambdaUpdateWrapper<UserFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(UserFile::getFileName, renameFileDTO.getFileName())
                .set(UserFile::getUploadTime, DateUtil.getCurrentTime())
                .eq(UserFile::getUserFileId, renameFileDTO.getUserFileId());
            userFileService.update(lambdaUpdateWrapper);
        }
        return RespResult.success();
    }

    public TreeNodeVO insertTreeNode(TreeNodeVO treeNode, String filePath, Queue<String> nodeNameQueue){

        List<TreeNodeVO> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null){
            return treeNode;
        }

        Map<String, String> map = new HashMap<>();
        filePath = filePath + currentNodeName + "/";
        map.put("filePath", filePath);

        if (!isExistPath(childrenTreeNodes, currentNodeName)){  //1、判断有没有该子节点，如果没有则插入
            //插入
            TreeNodeVO resultTreeNode = new TreeNodeVO();


            resultTreeNode.setAttributes(map);
            resultTreeNode.setLabel(nodeNameQueue.poll());
            // resultTreeNode.setId(treeid++);

            childrenTreeNodes.add(resultTreeNode);

        }else{  //2、如果有，则跳过
            nodeNameQueue.poll();
        }

        if (nodeNameQueue.size() != 0) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {

                TreeNodeVO childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())){
                    childrenTreeNode = insertTreeNode(childrenTreeNode, filePath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildren(childrenTreeNodes);
                }

            }
        }else{
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;

    }

    public boolean isExistPath(List<TreeNodeVO> childrenTreeNodes, String path){
        boolean isExistPath = false;

        try {
            for (int i = 0; i < childrenTreeNodes.size(); i++){
                if (path.equals(childrenTreeNodes.get(i).getLabel())){
                    isExistPath = true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return isExistPath;
    }


}
