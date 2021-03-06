package com.controls;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.commons.apiresult.ApiResult;
import com.commons.apiresult.ResponseStatus;
import com.commons.utils.PageMode;
import com.commons.utils.RequestObjectUtil;
import com.interceptor.BaseCurrentWorkerAware;
import com.modules.mail.bean.EmailRecord;
import com.modules.mail.service.EmailRecordService;
import com.modules.member.bean.MemberBaseInfo;
import com.modules.member.service.MemberBaseInfoService;
import com.modules.worker.bean.Worker;
import com.modules.worker.service.WorkerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "邮件管理")
@RequestMapping("/api/email")
@RestController
public class EmailController extends BaseCurrentWorkerAware {
	private static final Logger log = LoggerFactory.getLogger(WorkerController.class);
	@Autowired
	private EmailRecordService emailRecordService;
	@Autowired
	private MemberBaseInfoService memberBaseInfoService;
	@Autowired
	private WorkerService workerService;

	@ApiOperation(value = "发送邮件给客户")
	@ApiImplicitParams({ @ApiImplicitParam(name = "to", value = "客户id ,隔开", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sub", value = "标题", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "content", value = "内容", dataType = "String", paramType = "query") })
	@ApiResponses({ @ApiResponse(code = 100000, message = "请求成功"), @ApiResponse(code = 100001, message = "请求失败"),
			@ApiResponse(code = 100008, message = "非法请求"), @ApiResponse(code = 100003, message = "参数为空"),
			@ApiResponse(code = 100004, message = "未登录"), @ApiResponse(code = 100005, message = "未设置可显字段") })
	@RequestMapping(value = "send_email", method = { RequestMethod.POST })
	@ResponseBody
	public ApiResult send_email(@RequestParam(required = false) String to,
			@RequestParam(required = false) String content, @RequestParam(required = false) String sub) {
		ApiResult apiResult = new ApiResult();
		if (StringUtils.isBlank(to)) {
			apiResult.setStatus(ResponseStatus.null_param.getStatusCode());
			apiResult.setDesc(ResponseStatus.null_param.getStatusDesc());
			return apiResult;
		}
		try {
			if (StringUtils.isNotBlank(to)) {
				List<MemberBaseInfo> ml = memberBaseInfoService.searchInfoByIds(to);
				emailRecordService.mimeMailToMember(sub, content, ml);
			}
			apiResult.setStatus(ResponseStatus.success.getStatusCode());
			apiResult.setDesc(ResponseStatus.success.getStatusDesc());
		} catch (Exception e) {
			log.error("发送邮件给客户", e);
			apiResult.setStatus(ResponseStatus.error.getStatusCode());
			apiResult.setDesc(ResponseStatus.error.getStatusDesc());
		}
		return apiResult;
	}

	@ApiOperation(value = "发送邮件给员工")
	@ApiImplicitParams({ @ApiImplicitParam(name = "to", value = "员工workerId ,隔开", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sub", value = "标题", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "content", value = "内容", dataType = "String", paramType = "query") })
	@ApiResponses({ @ApiResponse(code = 100000, message = "请求成功"), @ApiResponse(code = 100001, message = "请求失败"),
			@ApiResponse(code = 100008, message = "非法请求"), @ApiResponse(code = 100003, message = "参数为空"),
			@ApiResponse(code = 100004, message = "未登录"), @ApiResponse(code = 100005, message = "未设置可显字段") })
	@RequestMapping(value = "send_email_worker", method = { RequestMethod.POST })
	@ResponseBody
	public ApiResult send_email_worker(@RequestParam(required = false) String to,
			@RequestParam(required = false) String content, @RequestParam(required = false) String sub) {
		ApiResult apiResult = new ApiResult();
		if (StringUtils.isBlank(to)) {
			apiResult.setStatus(ResponseStatus.null_param.getStatusCode());
			apiResult.setDesc(ResponseStatus.null_param.getStatusDesc());
			return apiResult;
		}
		try {
			if (StringUtils.isNotBlank(to)) {
				StringBuffer sb = new StringBuffer();
				for (String t : to.split(",")) {
					sb.append("'").append(t).append("',");
				}
				List<Worker> ml = workerService.searchInfoWorkerIds(sb.substring(0, sb.length() - 1));
				emailRecordService.mimeMailToWorker(sub, content, ml,"EmailWorker");
			}
			apiResult.setStatus(ResponseStatus.success.getStatusCode());
			apiResult.setDesc(ResponseStatus.success.getStatusDesc());
		} catch (Exception e) {
			log.error("发送邮件给员工", e);
			apiResult.setStatus(ResponseStatus.error.getStatusCode());
			apiResult.setDesc(ResponseStatus.error.getStatusDesc());
		}
		return apiResult;
	}

	@ApiOperation(value = "查询")
	@ApiImplicitParams({ @ApiImplicitParam(name = "page", value = "页码", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "显示条数", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "id", value = "编号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "from", value = "发件地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "to", value = "收件地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "workerId", value = "工号id", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "memberId", value = "员工id", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "1发送给客户2发送给员工", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "1成功2失败", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query") })
	@GetMapping("search")
	public ApiResult search(HttpServletRequest request) {
		ApiResult apiResult = new ApiResult();
		try {
			EmailRecord emailRecord = (EmailRecord) RequestObjectUtil.mapToBean(request, EmailRecord.class);// 获取条件参数
			PageMode pageMode = (PageMode) RequestObjectUtil.mapToBean(request, PageMode.class);// 获取分页参数
			if (emailRecord == null) {
				emailRecord = new EmailRecord();
			}
			if (pageMode == null) {
				pageMode = new PageMode();
			}
			emailRecordService.search(emailRecord, apiResult, pageMode);
			apiResult.setStatus(ResponseStatus.success.getStatusCode());
			apiResult.setDesc(ResponseStatus.success.getStatusDesc());
		} catch (Exception e) {
			log.error("邮件查询", e);
			apiResult.setStatus(ResponseStatus.error.getStatusCode());
			apiResult.setDesc(ResponseStatus.error.getStatusDesc());
		}
		return apiResult;
	}

	@ApiOperation(value = "删除")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "编号", dataType = "int", paramType = "query") })
	@DeleteMapping("delete")
	public ApiResult delete(@RequestParam(required = false) Integer id) {
		ApiResult apiResult = new ApiResult();
		try {
			if (id == null) {
				apiResult.setStatus(ResponseStatus.null_param.getStatusCode());
				apiResult.setDesc(ResponseStatus.null_param.getStatusDesc());
				return apiResult;
			}
			if (emailRecordService.delete(new EmailRecord(id))) {
				apiResult.setStatus(ResponseStatus.success.getStatusCode());
				apiResult.setDesc(ResponseStatus.success.getStatusDesc());
			} else {
				apiResult.setStatus(ResponseStatus.failed.getStatusCode());
				apiResult.setDesc(ResponseStatus.failed.getStatusDesc());
			}
		} catch (Exception e) {
			log.error("删除邮件", e);
			apiResult.setStatus(ResponseStatus.error.getStatusCode());
			apiResult.setDesc(ResponseStatus.error.getStatusDesc());
		}
		return apiResult;
	}
}
