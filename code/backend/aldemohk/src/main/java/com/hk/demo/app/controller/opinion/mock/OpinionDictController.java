package com.hk.demo.app.controller.opinion.mock;

import com.hk.demo.api.enums.opinion.OpinionCategory;
import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.api.enums.opinion.OpinionModuleCode;
import com.hk.demo.api.enums.opinion.OpinionUnitType;
import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider;
import com.hk.demo.core.response.ApiResponseFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 主数据 / 枚举字典查询接口。
 *
 * 当前为 mock 实现（Q-A1 默认假设），用于支撑 P01 编辑页的征集对象勾选与下拉枚举。
 * 真实主数据接口就绪后可在此处替换实现，前端契约保持不变。
 */
@RestController
@RequestMapping("/api/opinion/dict")
public class OpinionDictController {

    private final OpinionMockMasterDataProvider masterData;

    public OpinionDictController(OpinionMockMasterDataProvider masterData) {
        this.masterData = masterData;
    }

    /**
     * 列出所有可勾选的征集对象（单位）。
     */
    @GetMapping("/units")
    public ApiResponse<List<UnitVO>> listUnits() {
        List<UnitVO> list = new ArrayList<>();
        for (OpinionMockMasterDataProvider.UnitInfo u : masterData.listAllUnits()) {
            UnitVO vo = new UnitVO();
            vo.setUnitId(u.id());
            vo.setUnitName(u.name());
            vo.setUnitType(u.type().name());
            vo.setUnitTypeText(u.type().getText());
            list.add(vo);
        }
        return ApiResponseFactory.success(list);
    }

    /**
     * 列出 5 个指标 Tab 模块编码。
     */
    @GetMapping("/modules")
    public ApiResponse<List<EnumVO>> listModules() {
        List<EnumVO> list = new ArrayList<>();
        for (OpinionModuleCode m : OpinionModuleCode.values()) {
            list.add(new EnumVO(m.name(), m.getText()));
        }
        return ApiResponseFactory.success(list);
    }

    /**
     * 列出主状态枚举。
     */
    @GetMapping("/main-status")
    public ApiResponse<List<EnumVO>> listMainStatus() {
        List<EnumVO> list = new ArrayList<>();
        for (OpinionMainStatus s : OpinionMainStatus.values()) {
            list.add(new EnumVO(s.name(), s.getText()));
        }
        return ApiResponseFactory.success(list);
    }

    /**
     * 列出意见分类枚举。
     */
    @GetMapping("/opinion-category")
    public ApiResponse<List<EnumVO>> listOpinionCategory() {
        List<EnumVO> list = new ArrayList<>();
        for (OpinionCategory c : OpinionCategory.values()) {
            list.add(new EnumVO(c.name(), c.getText()));
        }
        return ApiResponseFactory.success(list);
    }

    /**
     * 列出单位类型枚举。
     */
    @GetMapping("/unit-type")
    public ApiResponse<List<EnumVO>> listUnitType() {
        List<EnumVO> list = new ArrayList<>();
        for (OpinionUnitType t : OpinionUnitType.values()) {
            list.add(new EnumVO(t.name(), t.getText()));
        }
        return ApiResponseFactory.success(list);
    }

    /**
     * 一次返回所有静态枚举（开发期便于前端常量初始化）。
     */
    @GetMapping("/enums")
    public ApiResponse<Map<String, List<EnumVO>>> listAllEnums() {
        Map<String, List<EnumVO>> map = new LinkedHashMap<>();
        map.put("mainStatus", listMainStatus().getData());
        map.put("module", listModules().getData());
        map.put("opinionCategory", listOpinionCategory().getData());
        map.put("unitType", listUnitType().getData());
        return ApiResponseFactory.success(map);
    }

    /**
     * 单位 VO。
     */
    public static class UnitVO {
        private Long unitId;
        private String unitName;
        private String unitType;
        private String unitTypeText;

        public Long getUnitId() {
            return unitId;
        }

        public void setUnitId(Long unitId) {
            this.unitId = unitId;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getUnitType() {
            return unitType;
        }

        public void setUnitType(String unitType) {
            this.unitType = unitType;
        }

        public String getUnitTypeText() {
            return unitTypeText;
        }

        public void setUnitTypeText(String unitTypeText) {
            this.unitTypeText = unitTypeText;
        }
    }

    /**
     * 通用枚举 VO。
     */
    public static class EnumVO {
        private String code;
        private String text;

        public EnumVO() {
        }

        public EnumVO(String code, String text) {
            this.code = code;
            this.text = text;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
