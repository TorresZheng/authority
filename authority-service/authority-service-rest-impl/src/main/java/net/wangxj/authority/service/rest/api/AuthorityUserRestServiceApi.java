package net.wangxj.authority.service.rest.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import net.wangxj.util.constant.RegexConstant;
import net.wangxj.util.string.TimeUtil;
import net.wangxj.util.string.UuidUtil;
import net.wangxj.util.validate.ValidationResult;
import net.wangxj.util.validate.groups.AddValidate;
import net.wangxj.util.validate.groups.DeleteValidate;
import net.wangxj.util.validate.groups.EditValidate;
import net.wangxj.authority.po.AuthorityUserPO;
import net.wangxj.authority.po.Page;
import net.wangxj.authority.service.AuthorityUserService;

/**
 * created by	: wangxj
 * created time	: 2016-12-26 18:06:42
 */
@Path("/users")
@Produces({"application/json"})
@Consumes({"application/json"})
public class AuthorityUserRestServiceApi extends AbstractAuthrotiyRestService{

	private static Logger logger = Logger.getLogger(AuthorityUserRestServiceApi.class);
	
	@Autowired
	private AuthorityUserService authorityUserService;
	
	/**
	 * @apiDefine 200 成功 200
	 */
	/**
	 * @apiDefine 400 错误 400
	 */
	/**
	 * @apiDefine 500 错误 500
	 */
	/**
	 * @apiDefine userSuccessResponse
	 * @apiSuccess (200) {String} success 是否操作成功
	 * @apiSuccessExample {json}　请求成功响应 : 
	 * 									{
	 *									  "success": true
	 *									} 
	 */
	/**
	 * @apiDefine user400Response
	 *@apiError (400) {String} error_message 错误说明
	 *@apiError (400) {Boolean} is_pass　　格式是否正确
	 *@apiErrorExample {json} 错误400响应 : 
	 *									{
	 *									   "error_message": "该user_login_name已被注册",
	 *									   "is_pass": false 
	 *									}
	 */
	/**
	 *@apiDefine user500Response
	 *@apiError (500) {String} error 错误说明
	 *@apiErrorExample {json} 错误500响应 :
	 *									{
	 *										"error": "服务器内部发生错误
	 *									}
	 */
	
	
	
	/**
	 * 添加用户
	 * @param userPo
	 * @return
	 * @throws Exception
	 * 
	 * apidoc------------>
	 * @api {POST} /users 添加用户
	 * @apiExample {curl} curl请求示例:
	 * curl -H "Content-Type:application/json" -H "Accept:application/json" -i -X POST -d '
	 * {"user_login_name":"CE_SHI",
	 *  "user_login_password":"123456",
	 *  "user_email":"ceshi@qq.com",
	 *  "user_phone":"13211563421",
	 *  "user_status":1,
	 *  "user_type" : 1,
	 *  "user_add_type" : 1,
	 *  "user_add_by" : "0cf700bfd72142c498ff7508aa2603c3"}' http://localhost:9000/api/users 
	 *  @apiGroup users
	 *  @apiParam {String{2..64}}　[user_login_name]　用户名
	 *  @apiParam {String} user_email 用户登录邮箱
	 *  @apiParam {String{6..20}} user_login_password 用户登录密码
	 *  @apiParam {String} [user_phone] 用户电话
	 *  @apiParam {number=1(已注册未激活),2(已注册并激活),3(已锁定)} user_status 用户状态
	 *  @apiParam {number=1(内部用户),2(外部用户)} user_type 用户类型
	 *  @apiParam {number=1(被内部用户添加),2(自己注册)} user_add_type 用户添加类型
	 *  @apiParam {String} user_add_by 添加人
	 *  @apiParamExample {json} 请求参数示例:
	 * {"user_login_name":"CE_SHI",
	 *  "user_login_password":"123456",
	 *  "user_email":"ceshi@qq.com",
	 *  "user_phone":"13211563421",
	 *  "user_status":1,
	 *  "user_type" : 1,
	 *  "user_add_type" : 1,
	 *  "user_add_by" : "0cf700bfd72142c498ff7508aa2603c3"} 
	 *  @apiUse userSuccessResponse
	 *  @apiUse user400Response
	 *  @apiUse user500Response
	 */
	@POST
	public Response add(AuthorityUserPO userPo) throws Exception{
		logger.debug("验证---->");
		ValidationResult addValidateResult = authorityUserService.validatePoAndNotRepeadField(userPo, AddValidate.class);
		logger.debug("验证结果:--->" + addValidateResult);
		if(addValidateResult != null){
			return failValidate(addValidateResult);
		}else{
			logger.debug("添加操作开始---->");
			Map<String , Boolean> addResMap = new HashMap<>();
			addResMap.put("success", authorityUserService.add(userPo) == 1 ? true : false);
			return success(addResMap);
		}
	}
	
	/**
	 * 更新用户
	 * @param uuid
	 * @param userPo
	 * @return
	 * @throws Exception 
	 * apidoc------------------->
	 * @api {PUT} /users/{user_uuid} 修改用户
	 * @apiExample {curl} curl请求示例:
	 * curl -H "Content-Type:application/json" -H "Accept:application/json" -i -X PUT -d '
	 * {"user_login_name":"CE_SHI",
	 *  "user_login_password":"123456",
	 *  "user_email":"ceshi@qq.com",
	 *  "user_phone":"13211563421",
	 *  "user_status":1,
	 *  "user_type" : 1,
	 *  "user_add_type" : 1,
	 *  "user_edit_by" : "0cf700bfd72142c498ff7508aa2603c3"}' http://localhost:9000/api/users/db1d225261cf4a1293e7eb8d4371b667 
	 *  @apiGroup users
	 *  @apiParam {String} user_uuid 用户uuid
	 *  @apiParam {String{2..64}}　[user_login_name]　用户名
	 *  @apiParam {String} [user_email] 用户登录邮箱
	 *  @apiParam {String{6..20}} [user_login_password] 用户登录密码
	 *  @apiParam {String} [user_phone] 用户电话
	 *  @apiParam {number=1(已注册未激活),2(已注册并激活),3(已锁定)} [user_status] 用户状态
	 *  @apiParam {number=1(内部用户),2(外部用户)} [user_type] 用户类型
	 *  @apiParam {number=1(被内部用户添加),2(自己注册)} [user_add_type] 用户添加类型
	 *  @apiParam {String} user_add_by 添加人
	 *  @apiParamExample {json} 请求参数示例:
	 * {"user_uuid" : "db1d225261cf4a1293e7eb8d4371b667",
	 *  "user_login_name":"CE_SHI",
	 *  "user_login_password":"123456",
	 *  "user_email":"ceshi@qq.com",
	 *  "user_phone":"13211563421",
	 *  "user_status":1,
	 *  "user_type" : 1,
	 *  "user_add_type" : 1,
	 *  "user_edit_by" : "0cf700bfd72142c498ff7508aa2603c3"}  
	 *  @apiUse userSuccessResponse
	 *  @apiUse user400Response
	 *  @apiUse user500Response
	 *  
	 */
	@PUT
	@Path("/{uuid}")
	public Response update(@PathParam(value = "uuid")String uuid , AuthorityUserPO userPo) throws Exception{
		userPo.setUserUuid(uuid);
		ValidationResult editValidateResult = authorityUserService.validatePoAndNotRepeadField(userPo, EditValidate.class);
		logger.debug("验证结果:--->" + editValidateResult);
		if(editValidateResult != null){
			return failValidate(editValidateResult);
		}else{
			//更新
			Map<String,Object> updateResultMap = new HashMap<>();
			updateResultMap.put("success", authorityUserService.update(userPo) == 1 ? true : false);
			logger.debug("更新结果:--->" + updateResultMap);
			return success(updateResultMap);
		}
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @return
	 * apidoc----------------------->
	 * @api {GET} /users 分页查询
	 * @apiExample {curl} curl请求示例:
	 * curl -X GET 'http://localhost:9000/api/users?page_number=2&limit=3&order=asc&sort=user_uuid'
	 * @apiGroup users
	 * @apiParam {number} page_number 页码
	 * @apiParam {number}  limit 每页条数
	 * @apiParam {String="desc","asc"} order 排序(正序/反序)
	 * @apiParam {String} sort 排序字段(按该字段排序)
	 * @apiParamExample {json} 请求参数示例:
	 * {
	 *   "page_number":2,
	 *   "limit": 3,
	 *   "order": "asc",
	 *   "sort": "platform_uuid"
	 * } 
	 * @apiSuccess (200) {String} data 数据
	 * @apiSuccess (200) {number} count 数据总条数
	 * @apiSuccessExample {json}　请求成功响应 :
	 * {
	　*	  "data": [
	　*	    {
	　*	      "user_add_by": "0cf700bfd72142c498ff7508aa2603c3",
	　*	      "user_add_time": "2017-05-19 18:24:06",
	　*	      "user_add_type": 1,
	　*	      "user_edit_by": "0cf700bfd72142c498ff7508aa2603c3",
	　*	      "user_edit_time": "2017-05-20 09:28:12",
	　*	      "user_email": "ceshixiugaiaa@qq.com",
	　*	      "user_login_name": "CE_SHI_XIUGAI_qq",
	　*	      "user_login_password": "625:5b42403664356232346137:f48d80df8ecad60df6ad9129e6cfb82e4a057025ce9006f647bf5c700dc92943df1732c843136232a6aa1888532b989b22cabce031f530c43c5ea8ab2894e901",
	　*	      "user_phone": "13322563421",
	　*	      "user_status": 1,
	　*	      "user_type": 1,
	　*	      "user_uuid": "db1d225261cf4a1293e7eb8d4371b667"
	　*	    },
	　*	    {
	　*	      "user_add_by": "de0c7b2480494fda98db82f7a4707649",
	　*	      "user_add_time": "2017-02-21 16:53:23",
	　*	      "user_add_type": 1,
	　*	      "user_email": "1416236046@qq.com",
	　*	      "user_login_name": "admin",
	　*	      "user_login_password": "",
	　*	      "user_phone": "13811255489",
	　*	      "user_status": 2,
	　*	      "user_type": 1,
	　*	      "user_uuid": "de0c7b2480494fda98db82f7a4707649"
	　*	    }
	 *	  ],
	 *	  "count": 2
	 *	}
	 *@apiError (400) {String} error_message 错误说明
	 *@apiError (400) {Boolean} is_pass　　格式是否正确
	 *@apiErrorExample {json} 错误400响应 : 
	 *									{
	 *									   "error_message": "page_number非法",
	 *									   "is_pass": false
	 *									}
	 *@apiUse user500Response
	 *
	 */
	@GET
	public Response pageQuery(@BeanParam Page page){
		ValidationResult pageValidateResult = authorityUserService.validatePo(page, Default.class);
		ValidationResult validatleSortResult = authorityUserService.validateSort(AuthorityUserPO.class, page.getSort());
		if(pageValidateResult != null){
			return failValidate(pageValidateResult);
		}else if(validatleSortResult != null){
			return failValidate(validatleSortResult);
		}else{
			Map<String, Object> pageResultMap = authorityUserService.pageQuery(new AuthorityUserPO(), page);
			return success(pageResultMap);
		}
	}
	
	/**
	 * 批量删除
	 * @param user
	 * @param uuids
	 * @return
	 * apidoc------------------>
	 * @api {DELETE} /users 删除用户(批量)
	 * @apiExample {curl} curl请求示例:
	 * curl -X DELETE 'http://localhost:9000/api/users?delete_user=de0c7b2480494fda98db82f7a4707649&uuids=db1d225261cf4a1293e7eb8d4371b667,de0c7b2480494fda98db82f7a4707649'
	 * @apiGroup users
	 * @apiParam {String} delete_user 删除人
	 * @apiParam {String} uuids 用户uuid集合(以,分割)
	 * @apiParamExample {json} 请求参数示例:
	 * {
	 *  "delete_user" : "de0c7b2480494fda98db82f7a4707649",
	 *  "uuids" : "db1d225261cf4a1293e7eb8d4371b667,de0c7b2480494fda98db82f7a4707649"
	 * }
	 * @apiUse userSuccessResponse
	 * @apiUse user400Response
	 * @apiUse user500Response
	 */
	@DELETE
	public Response delete(@QueryParam(value="delete_user")String user , @QueryParam(value="uuids")String uuids){
		logger.debug("delete_user" + user + "---uuids" + uuids);
		List<AuthorityUserPO> userListPo = new ArrayList<>();
		for(String uuid : uuids.split(",")){
			AuthorityUserPO userPo = new AuthorityUserPO();
			userPo.setUserUuid(uuid);
			userPo.setUserDelBy(user);
			ValidationResult deleteValidateResult = authorityUserService.validatePo(userPo, DeleteValidate.class);
			if(deleteValidateResult != null){
				return failValidate(deleteValidateResult);
			}
			userListPo.add(userPo);
		}
		Map<String,Object> deleteResultMap = new HashMap<>();
		deleteResultMap.put("success", authorityUserService.deleteBatch(userListPo) > 0 ? true : false);
		return success(deleteResultMap);
	}
	
	/**
	 * 为用户授予角色
	 * @param addUser
	 * @param roleUuids
	 * @return
	 * apidoc------------------>
	 * 
	 */
	@Path("{uuid}/roles")
	@PUT
	public Response grantRoles(@PathParam("uuid")String userUuid ,@QueryParam("platform_uuid")String platformUuid,
								@QueryParam("add_user")String addUser , @QueryParam("role_uuids")String roleUuids){
		//校验addUser与roleUuids,uuid
		Map<String,Object> grantRoleResultMap = new HashMap<>();
		ValidationResult validateResult = new ValidationResult();
		List<String> roleUuidList = new ArrayList<>();
		if(!Pattern.matches(RegexConstant.UUID_32, userUuid)){
			 validateResult.setErrorMsg("user_uuid非法");
			return failValidate(validateResult);
		}else if(!Pattern.matches(RegexConstant.UUID_32, addUser)){
			validateResult.setErrorMsg("add_user非法");
			return failValidate(validateResult);
		}else if(!Pattern.matches(RegexConstant.UUID_32, platformUuid)){
			validateResult.setErrorMsg("platform_uuid非法");
			return failValidate(validateResult);
		}else{
			for (String roleUuid : roleUuids.split(",")) {
				if("".equals(roleUuid.trim())){
					continue;
				}else{
					if(!Pattern.matches(RegexConstant.UUID_32, roleUuid)){
						validateResult.setErrorMsg("role_uuid非法");
						return failValidate(validateResult);
					}
					else{
						roleUuidList.add(roleUuid);
					}
				}
			}
			//校验完毕,开始授予操作
			grantRoleResultMap.put("success", authorityUserService.grantRoles(userUuid, platformUuid, roleUuidList, addUser));
			return success(grantRoleResultMap);
		}
	}
	

}