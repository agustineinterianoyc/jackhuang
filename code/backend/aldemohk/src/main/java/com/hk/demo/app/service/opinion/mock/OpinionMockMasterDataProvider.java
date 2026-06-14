package com.hk.demo.app.service.opinion.mock;

import com.hk.demo.api.enums.opinion.OpinionUnitType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 主数据 mock 提供者。
 *
 * 当前需求包按 Q-A1 默认假设：先 mock 字典，外部主数据接口就绪后再切换。
 * 此处仅提供征集对象勾选所需的"单位列表"与"当前用户"上下文，方便 P01 跑通。
 */
@Component
public class OpinionMockMasterDataProvider {

    /**
     * 单位字典（与原型 P01 进度数据示例对齐）。
     */
    private static final List<UnitInfo> UNITS = List.of(
        new UnitInfo(101L, "市区公司", OpinionUnitType.POWER),
        new UnitInfo(102L, "市北公司", OpinionUnitType.POWER),
        new UnitInfo(103L, "市南公司", OpinionUnitType.POWER),
        new UnitInfo(104L, "市东公司", OpinionUnitType.POWER),
        new UnitInfo(105L, "市西公司", OpinionUnitType.POWER),
        new UnitInfo(106L, "县区公司一", OpinionUnitType.POWER),
        new UnitInfo(107L, "县区公司二", OpinionUnitType.POWER),
        new UnitInfo(108L, "县区公司三", OpinionUnitType.POWER),
        new UnitInfo(109L, "县区公司四", OpinionUnitType.POWER),
        new UnitInfo(110L, "县区公司五", OpinionUnitType.POWER),

        new UnitInfo(201L, "电科院", OpinionUnitType.SUPPORT),
        new UnitInfo(202L, "经研院", OpinionUnitType.SUPPORT),
        new UnitInfo(203L, "信通公司", OpinionUnitType.SUPPORT),
        new UnitInfo(204L, "物资公司", OpinionUnitType.SUPPORT),
        new UnitInfo(205L, "培训中心", OpinionUnitType.SUPPORT),
        new UnitInfo(206L, "服务中心", OpinionUnitType.SUPPORT),

        new UnitInfo(301L, "营销公司", OpinionUnitType.MARKET),
        new UnitInfo(302L, "招标公司", OpinionUnitType.MARKET),
        new UnitInfo(303L, "客服中心", OpinionUnitType.MARKET),
        new UnitInfo(304L, "新能源公司", OpinionUnitType.MARKET),
        new UnitInfo(305L, "其他单位", OpinionUnitType.OTHER)
    );

    private static final Map<Long, UnitInfo> UNIT_INDEX;

    static {
        UNIT_INDEX = new java.util.HashMap<>();
        for (UnitInfo u : UNITS) {
            UNIT_INDEX.put(u.id(), u);
        }
    }

    /**
     * 专业部门字典。
     */
    private static final List<DeptInfo> DEPTS = List.of(
        new DeptInfo(1L, "财务部"),
        new DeptInfo(2L, "发展部"),
        new DeptInfo(3L, "党委宣传部"),
        new DeptInfo(4L, "安监部"),
        new DeptInfo(5L, "设备部")
    );

    private static final Map<Long, DeptInfo> DEPT_INDEX;

    static {
        DEPT_INDEX = new java.util.HashMap<>();
        for (DeptInfo d : DEPTS) {
            DEPT_INDEX.put(d.id(), d);
        }
    }

    /**
     * Mock 当前用户 ID。
     *
     * 实际项目中应从安全上下文读取；此处固定 1L 用于本期开发与联调。
     */
    public Long currentUserId() {
        return 1L;
    }

    /**
     * 列出所有单位。
     */
    public List<UnitInfo> listAllUnits() {
        return Collections.unmodifiableList(new ArrayList<>(UNITS));
    }

    /**
     * 按 ID 查找单位；找不到返回 null。
     */
    public UnitInfo findUnit(Long id) {
        return id == null ? null : UNIT_INDEX.get(id);
    }

    /**
     * 列出所有专业部门。
     */
    public List<DeptInfo> listAllDepts() {
        return Collections.unmodifiableList(new ArrayList<>(DEPTS));
    }

    /**
     * 按 ID 查找专业部门；找不到返回 null。
     */
    public DeptInfo findDept(Long id) {
        return id == null ? null : DEPT_INDEX.get(id);
    }

    /**
     * 单位信息。
     */
    public record UnitInfo(Long id, String name, OpinionUnitType type) {
    }

    /**
     * 专业部门信息。
     */
    public record DeptInfo(Long id, String name) {
    }
}
