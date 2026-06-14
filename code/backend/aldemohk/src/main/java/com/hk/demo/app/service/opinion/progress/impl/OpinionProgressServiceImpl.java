package com.hk.demo.app.service.opinion.progress.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.api.enums.opinion.OpinionDeptAuditStatus;
import com.hk.demo.api.enums.opinion.OpinionDeptSubmitStatus;
import com.hk.demo.api.enums.opinion.OpinionUnitAuditStatus;
import com.hk.demo.api.enums.opinion.OpinionUnitFillStatus;
import com.hk.demo.api.enums.opinion.OpinionUnitType;
import com.hk.demo.app.mapper.opinion.OpinionDeptTaskMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptTaskDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitTaskDO;
import com.hk.demo.app.model.request.opinion.progress.ProgressListRequest;
import com.hk.demo.app.model.response.opinion.progress.DeptProgressVO;
import com.hk.demo.app.model.response.opinion.progress.UnitProgressVO;
import com.hk.demo.app.service.opinion.progress.OpinionProgressService;
import com.hk.demo.data.pagination.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 进度查询服务实现。
 */
@Service
public class OpinionProgressServiceImpl implements OpinionProgressService {

    private final OpinionUnitTaskMapper unitTaskMapper;
    private final OpinionDeptTaskMapper deptTaskMapper;

    @Autowired
    public OpinionProgressServiceImpl(OpinionUnitTaskMapper unitTaskMapper,
                                      OpinionDeptTaskMapper deptTaskMapper) {
        this.unitTaskMapper = unitTaskMapper;
        this.deptTaskMapper = deptTaskMapper;
    }

    // ===== API-601 基层进度列表 =====
    @Override
    public PageResult<UnitProgressVO> listUnits(ProgressListRequest request) {
        int page = Math.max(1, request.getPage());
        int size = Math.max(1, Math.min(200, request.getPageSize()));

        LambdaQueryWrapper<OpinionUnitTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpinionUnitTaskDO::getSurveyId, request.getSurveyId());

        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.like(OpinionUnitTaskDO::getUnitName, request.getKeyword().trim());
        }
        if (!CollectionUtils.isEmpty(request.getSubmitStatus())) {
            wrapper.in(OpinionUnitTaskDO::getFillStatus, request.getSubmitStatus());
        }

        List<OpinionUnitTaskDO> all = unitTaskMapper.selectList(wrapper);
        long total = all.size();
        int from = (page - 1) * size;
        int to = Math.min(from + size, all.size());
        List<OpinionUnitTaskDO> tasks = from < all.size() ? all.subList(from, to) : List.of();

        List<UnitProgressVO> rows = new ArrayList<>();
        for (OpinionUnitTaskDO t : tasks) {
            UnitProgressVO vo = new UnitProgressVO();
            vo.setTaskId(t.getId());
            vo.setUnitId(t.getUnitId());
            vo.setUnitName(t.getUnitName());
            vo.setUnitType(t.getUnitType());
            vo.setUnitTypeText(unitTypeText(t.getUnitType()));
            vo.setFillStatus(t.getFillStatus());
            vo.setFillStatusText(fillStatusText(t.getFillStatus()));
            vo.setAuditStatus(t.getAuditStatus());
            vo.setAuditStatusText(unitAuditStatusText(t.getAuditStatus()));
            vo.setSubmittedAt(t.getSubmittedAt());
            vo.setSubmittedBy(t.getSubmittedBy());
            rows.add(vo);
        }
        return new PageResult<>(total, rows);
    }

    // ===== API-602 专业进度列表 =====
    @Override
    public PageResult<DeptProgressVO> listDepts(ProgressListRequest request) {
        int page = Math.max(1, request.getPage());
        int size = Math.max(1, Math.min(200, request.getPageSize()));

        LambdaQueryWrapper<OpinionDeptTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpinionDeptTaskDO::getSurveyId, request.getSurveyId());

        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.like(OpinionDeptTaskDO::getDepartmentName, request.getKeyword().trim());
        }
        if (!CollectionUtils.isEmpty(request.getSubmitStatus())) {
            wrapper.in(OpinionDeptTaskDO::getSubmitStatus, request.getSubmitStatus());
        }

        List<OpinionDeptTaskDO> allDepts = deptTaskMapper.selectList(wrapper);
        long totalDepts = allDepts.size();
        int fromD = (page - 1) * size;
        int toD = Math.min(fromD + size, allDepts.size());
        List<OpinionDeptTaskDO> deptTasks = fromD < allDepts.size() ? allDepts.subList(fromD, toD) : List.of();

        List<DeptProgressVO> rows = new ArrayList<>();
        for (OpinionDeptTaskDO t : deptTasks) {
            DeptProgressVO vo = new DeptProgressVO();
            vo.setTaskId(t.getId());
            vo.setDepartmentId(t.getDepartmentId());
            vo.setDepartmentName(t.getDepartmentName());
            vo.setModuleCode(t.getModuleCode());
            vo.setModuleName(deptModuleName(t.getModuleCode()));
            vo.setSubmitStatus(t.getSubmitStatus());
            vo.setSubmitStatusText(deptSubmitStatusText(t.getSubmitStatus()));
            vo.setAuditStatus(t.getAuditStatus());
            vo.setAuditStatusText(deptAuditStatusText(t.getAuditStatus()));
            vo.setSubmittedAt(t.getSubmittedAt());
            vo.setSubmittedBy(t.getSubmittedBy());
            rows.add(vo);
        }
        return new PageResult<>(totalDepts, rows);
    }

    // ===== 状态文本转换 =====

    private String unitTypeText(String unitType) {
        if (unitType == null) {
            return null;
        }
        OpinionUnitType type = safeEnum(OpinionUnitType.class, unitType);
        return type != null ? type.getText() : unitType;
    }

    private String fillStatusText(String fillStatus) {
        if (OpinionUnitFillStatus.PENDING.name().equals(fillStatus)) {
            return OpinionUnitFillStatus.PENDING.getText();
        }
        if (OpinionUnitFillStatus.SUBMITTED.name().equals(fillStatus)) {
            return OpinionUnitFillStatus.SUBMITTED.getText();
        }
        return fillStatus;
    }

    private String unitAuditStatusText(String auditStatus) {
        if (OpinionUnitAuditStatus.NONE.name().equals(auditStatus)) {
            return OpinionUnitAuditStatus.NONE.getText();
        }
        if (OpinionUnitAuditStatus.PENDING.name().equals(auditStatus)) {
            return OpinionUnitAuditStatus.PENDING.getText();
        }
        if (OpinionUnitAuditStatus.PASS.name().equals(auditStatus)) {
            return OpinionUnitAuditStatus.PASS.getText();
        }
        if (OpinionUnitAuditStatus.REJECTED.name().equals(auditStatus)) {
            return OpinionUnitAuditStatus.REJECTED.getText();
        }
        return auditStatus;
    }

    private String deptSubmitStatusText(String submitStatus) {
        if (OpinionDeptSubmitStatus.PENDING.name().equals(submitStatus)) {
            return OpinionDeptSubmitStatus.PENDING.getText();
        }
        if (OpinionDeptSubmitStatus.SUBMITTED.name().equals(submitStatus)) {
            return OpinionDeptSubmitStatus.SUBMITTED.getText();
        }
        return submitStatus;
    }

    private String deptAuditStatusText(String auditStatus) {
        if (OpinionDeptAuditStatus.NONE.name().equals(auditStatus)) {
            return OpinionDeptAuditStatus.NONE.getText();
        }
        if (OpinionDeptAuditStatus.PENDING.name().equals(auditStatus)) {
            return OpinionDeptAuditStatus.PENDING.getText();
        }
        if (OpinionDeptAuditStatus.PASS.name().equals(auditStatus)) {
            return OpinionDeptAuditStatus.PASS.getText();
        }
        if (OpinionDeptAuditStatus.REJECTED.name().equals(auditStatus)) {
            return OpinionDeptAuditStatus.REJECTED.getText();
        }
        return auditStatus;
    }

    /** Mock：模块编码 -> 模块名称映射。 */
    private String deptModuleName(String moduleCode) {
        if (moduleCode == null) {
            return null;
        }
        // 模块名称由 OpinionSurveyModuleMapper 实时查询，此处以编码兜底
        switch (moduleCode) {
            case "ASSESSMENT":
                return "考核建议";
            case "DISCIPLINE":
                return "纪律审查";
            case "OPERATION":
                return "运营管理";
            case "SATISFACTION":
                return "客户满意度";
            case "SAFETY":
                return "安全生产";
            default:
                return moduleCode;
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> E safeEnum(Class<E> enumType, String name) {
        try {
            return Enum.valueOf(enumType, name);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
