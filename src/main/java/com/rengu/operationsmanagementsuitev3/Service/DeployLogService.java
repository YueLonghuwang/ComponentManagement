package com.rengu.operationsmanagementsuitev3.Service;

import com.rengu.operationsmanagementsuitev3.Entity.DeployLogEntity;
import com.rengu.operationsmanagementsuitev3.Entity.ProjectEntity;
import com.rengu.operationsmanagementsuitev3.Repository.DeployLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: operations-management-suite-v3
 * @author: hanch
 * @create: 2018-09-06 18:19
 **/

@Slf4j
@Service
@Transactional
public class DeployLogService {

    private final DeployLogRepository deployLogRepository;

    @Autowired
    public DeployLogService(DeployLogRepository deployLogRepository) {
        this.deployLogRepository = deployLogRepository;
    }

    public DeployLogEntity saveDeployLog(DeployLogEntity deployLogEntity, boolean complete, String message, long sendSize) {
        deployLogEntity.setFinishTime(new Date());
        deployLogEntity.setTotalSendSize(sendSize);
        deployLogEntity.setComplete(complete);
        deployLogEntity.setMessage(message);
        deployLogEntity.setSpeed((deployLogEntity.getTotalSendSize() == 0 ? 1 : deployLogEntity.getTotalSendSize() / 1024.0) / ((deployLogEntity.getFinishTime().getTime() - deployLogEntity.getStartTime().getTime()) == 0 ? 1 : (double) (deployLogEntity.getFinishTime().getTime() - deployLogEntity.getStartTime().getTime()) / 1000));
        deployLogEntity.setProgress(((double) deployLogEntity.getTotalSendSize() / deployLogEntity.getTotalFileSize()) * 100);
        return deployLogRepository.save(deployLogEntity);
    }

    public Page<DeployLogEntity> getDeployLogsByProject(Pageable pageable, ProjectEntity projectEntity) {
        return deployLogRepository.findAllByProjectEntity(pageable, projectEntity);
    }

    public void deleteDeployLogByProject(ProjectEntity projectEntity) {
        deployLogRepository.deleteAllByProjectEntity(projectEntity);
    }
}
