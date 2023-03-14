package com.bom.zcloudbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bom.zcloudbackend.entity.File;
import com.bom.zcloudbackend.mapper.FileMapper;
import com.bom.zcloudbackend.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

}
