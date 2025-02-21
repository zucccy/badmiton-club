package com.yun.springbootinit.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.springbootinit.annotation.AuthCheck;
import com.yun.springbootinit.common.BaseResponse;
import com.yun.springbootinit.common.DeleteRequest;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.common.ResultUtils;
import com.yun.springbootinit.constant.CommonConstant;
import com.yun.springbootinit.constant.UserConstant;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.exception.ThrowUtils;
import com.yun.springbootinit.model.dto.chart.*;
import com.yun.springbootinit.model.entity.Chart;
import com.yun.springbootinit.model.entity.User;
import com.yun.springbootinit.service.IChartService;
import com.yun.springbootinit.service.IUserService;
import com.yun.springbootinit.utils.ExcelUtils;
import com.yun.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 图表接口
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private IChartService chartService;

    @Resource
    private IUserService userService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    private final static Long ONE_MB = 1 * 1024 * 1024L;
    // 文件后缀名校验白名单，只支持上传excel表格
    private final static List<String> ValidSuffixList = Arrays.asList("xlsx", "xls");

    // region 增删改查

    /**
     * 创建
     *
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = chart.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chartService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param chartUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        // 参数校验
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Chart> getChartById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                     HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                       HttpServletRequest request) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    // endregion

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
//    @PostMapping("/search/page/vo")
//    public BaseResponse<Page<Chart>> searchChartVOByPage(@RequestBody ChartQueryRequest chartQueryRequest,
//                                                          HttpServletRequest request) {
//        long size = chartQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Chart> chartPage = getQueryWrapper(chartQueryRequest);
//        return ResultUtils.success(chartPage);
//    }

    /**
     * 编辑（用户）
     *
     * @param chartEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);
        User loginUser = userService.getLoginUser(request);
        long id = chartEditRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 智能分析接口（同步）
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                                 GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) throws FileNotFoundException {
        // 获取genChartByAiRequest对象中三个字段
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 获取当前登录用户信息
        User currentUser = userService.getLoginUser(request);

        // 校验字段（goal和name）
        // goal为空的校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "分析目标为空");
        // name不为空且name长度超过100的校验
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "图表名称过长");

        // 校验文件
        // 校验文件大小
        long size = multipartFile.getSize();
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件大小超过1MB");

        // 校验文件后缀
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        ThrowUtils.throwIf(!ValidSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");

        // 将multipartFile转换为csv
        String fileData = ExcelUtils.excelToCsv(multipartFile);
        // 校验原始数据
        ThrowUtils.throwIf(StringUtils.isBlank(fileData), ErrorCode.PARAMS_ERROR, "原始数据为空");

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        // 拼接分析需求
        userInput.append("分析需求：").append("\n");
        String userGoal = goal;
        // 拼接图表类型
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        // 拼接原始数据
        userInput.append("原始数据：").append("\n");
        userInput.append(fileData).append("\n");

        // 调用鱼聪明AI API获取生成对话数据
        // 设置模型id
        String result = "";
        // 对result进行拆分
        String[] splits = result.split("【【【【【");
        // 校验splits长度
        ThrowUtils.throwIf(splits.length < 3, ErrorCode.SYSTEM_ERROR, "AI 生成错误");
        // 第1部分是生成的ECharts option代码
        String genChart = splits[1].trim();
        // 第2部分是生成的结论
        String genResult = splits[2].trim();
        // 入库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(fileData);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setUserId(currentUser.getId());
        boolean saveResult = chartService.save(chart);
        // 校验saveResult
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        // 包装BiResponse
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
        biResponse.setChartId(chart.getId());

        return ResultUtils.success(biResponse);

//        User loginUser = userService.getLoginUser(request);
//        // 文件目录：根据业务、用户来划分
//        String uuid = RandomStringUtils.randomAlphanumeric(8);
//        String filename = uuid + "-" + multipartFile.getOriginalFilename();
//        File file = null;
//        try {
//            // 返回可访问地址
//            return ResultUtils.success("");
//        } catch (Exception e) {
////            log.error("file upload error, filepath = " + filepath, e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
//        } finally {
//            if (file != null) {
//                // 删除临时文件
//                boolean delete = file.delete();
//                if (!delete) {
////                    log.error("file delete error, filepath = {}", filepath);
//                }
//            }
//        }
    }

    /**
     * 智能分析接口（异步）
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen/async")
    public BaseResponse<BiResponse> genChartByAiAsync(@RequestPart("file") MultipartFile multipartFile,
                                                      GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) throws FileNotFoundException {
        // 获取genChartByAiRequest对象中三个字段
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 获取当前登录用户信息
        User currentUser = userService.getLoginUser(request);

        // 校验字段（goal和name）
        // goal为空的校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "分析目标为空");
        // name不为空且name长度超过100的校验
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "图表名称过长");

        // 校验文件
        // 校验文件大小
        long size = multipartFile.getSize();
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件大小超过1MB");

        // 校验文件后缀
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        ThrowUtils.throwIf(!ValidSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");

        // 将multipartFile转换为csv
        String fileData = ExcelUtils.excelToCsv(multipartFile);
        // 校验原始数据
        ThrowUtils.throwIf(StringUtils.isBlank(fileData), ErrorCode.PARAMS_ERROR, "原始数据为空");

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        // 拼接分析需求
        userInput.append("分析需求：").append("\n");
        String userGoal = goal;
        // 拼接图表类型
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        // 拼接原始数据
        userInput.append("原始数据：").append("\n");
        userInput.append(fileData).append("\n");

        // 先将用户提交的信息入库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(fileData);
        chart.setChartType(chartType);
        chart.setUserId(currentUser.getId());
        boolean saveResult = chartService.save(chart);
        // 校验saveResult
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        // 返回结果前提交异步任务（调用AI）
        // TODO: 2023/7/23 建议处理一下任务队列满了的话，抛异常可以怎么处理
        CompletableFuture.runAsync(() -> {
            // 打印日志
            log.info("用户名：" + currentUser.getUsername() + "的任务执行中：" + name + "执行线程：" + Thread.currentThread().getName());
            // 修改图表状态为执行中
            Chart updateChart = new Chart();
            updateChart.setId(chart.getId());
            updateChart.setStatus("running");
            boolean res = chartService.updateById(updateChart);
            // 一般情况下是数据库出错了
            if (!res) {
                // 调用工具方法修改执行状态为失败和信息
                handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
            }
            // 调用鱼聪明AI API获取生成对话数据
            // 设置模型id
            String result = "";
            // 对result进行拆分
            String[] splits = result.split("【【【【【");
            // 校验splits长度
            if (splits.length < 3) {
                handleChartUpdateError(chart.getId(), "AI 生成错误");
            }
            // 第1部分是生成的ECharts option代码
            String genChart = splits[1].trim();
            // 第2部分是生成的结论
            String genResult = splits[2].trim();
            // 得到结果后再更新一次任务状态和信息
            Chart updateChartResult = new Chart();
            updateChartResult.setId(chart.getId());
            updateChartResult.setGenChart(genChart);
            updateChartResult.setGenResult(genResult);
            updateChartResult.setStatus("succeed");
            boolean res2 = chartService.updateById(updateChartResult);
            if (!res2) {
                handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
            }

        }, threadPoolExecutor);

        BiResponse biResponse = new BiResponse();
        // 前端只需要一个图表id即可
        biResponse.setChartId(chart.getId());
        return ResultUtils.success(biResponse);
    }

    /**
     * 智能分析接口（异步mq）
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen/async/mq")
    public BaseResponse<BiResponse> genChartByAiAsyncMq(@RequestPart("file") MultipartFile multipartFile,
                                                        GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) throws FileNotFoundException {
        // 获取genChartByAiRequest对象中三个字段
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 获取当前登录用户信息
        User currentUser = userService.getLoginUser(request);

        // 校验字段（goal和name）
        // goal为空的校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "分析目标为空");
        // name不为空且name长度超过100的校验
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "图表名称过长");

        // 校验文件
        // 校验文件大小
        long size = multipartFile.getSize();
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件大小超过1MB");

        // 校验文件后缀
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        ThrowUtils.throwIf(!ValidSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");

        // 将multipartFile转换为csv
        String fileData = ExcelUtils.excelToCsv(multipartFile);
        // 校验原始数据
        ThrowUtils.throwIf(StringUtils.isBlank(fileData), ErrorCode.PARAMS_ERROR, "原始数据为空");
        // 将用户提交的信息入库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(fileData);
        chart.setChartType(chartType);
        chart.setUserId(currentUser.getId());
        boolean saveResult = chartService.save(chart);
        // 校验saveResult
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        // 返回结果前提交任务到消息队列中（调用AI）
//        CompletableFuture.runAsync(() -> {
//
//        }, threadPoolExecutor);

        BiResponse biResponse = new BiResponse();
        // 前端只需要一个图表id即可
        biResponse.setChartId(chart.getId());
        return ResultUtils.success(biResponse);
    }


    /**
     * 工具方法，用于处理修改图表时发生的异常，再修改一遍图表状态和执行信息
     *
     * @param chartId
     * @param execMessage
     */
    private void handleChartUpdateError(Long chartId, String execMessage) {
        log.info("更新图表状态为failed，执行信息为" + execMessage + "，图表id为" + chartId);
        Chart chart = new Chart();
        chart.setId(chartId);
        chart.setStatus("failed");
        chart.setExecMessage(execMessage);
        boolean res = chartService.updateById(chart);
        if (!res) {
            log.error("图表信息失败状态更新失败" + chartId + "," + execMessage);
        }
        log.info("图表信息失败状态更新成功" + chartId + "," + execMessage);
    }


    /**
     * 获取查询包装类
     *
     * @param chartQueryRequest
     * @return
     */
    private QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }
        String chartType = chartQueryRequest.getChartType();
        String name = chartQueryRequest.getName();
        String goal = chartQueryRequest.getGoal();
        Long id = chartQueryRequest.getId();
        Long userId = chartQueryRequest.getUserId();
        String sortOrder = chartQueryRequest.getSortOrder();
        String sortField = chartQueryRequest.getSortField();
        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}
