package com.tensquare.user.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private JwtUtil jwtUtil;

	//更新粉丝数
	@PostMapping("/incfans/{userid}/{x}")
	public void incFanscount(@PathVariable String userid,@PathVariable int x){
		userService.incFanscount(userid,x);
	}
	//更新关注数
	@PostMapping("/incfollow/{userid}/{x}")
	public void incFollowcount(@PathVariable String userid,@PathVariable int x){
		userService.incFollowcount(userid,x);
	}

	/**
	 * 登录
	 * @param loginname
	 * @param password
	 * @return
	 */
	@PostMapping("/login")
	public Result login(String loginname,String password){
		User user = userService.findByLoginnameAndPassword(loginname, password);
		if (user != null){
			String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
			Map<String,String> map = new HashMap<>();
			map.put("token",token);
			map.put("name",user.getNickname());
			map.put("avatar",user.getAvatar());
			return new Result(true,StatusCode.OK,"登录成功",map);
		}
		return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
	}
	/**
	 * 注册
	 * @param user
	 * @param code
	 * @return
	 */
	@PostMapping("/register/{code}")
	public Result register(User user,@PathVariable("code")String code){
		userService.add(user,code);
		return new Result(true,StatusCode.OK,"注册成功");
	}

	/**
	 * 获取短信验证码
	 * @param mobile
	 * @return
	 */
	@PostMapping("/sendsms/{mobile}")
	public Result sendsms(@PathVariable("mobile")String mobile){
		userService.sendSms(mobile);
		return new Result(true,StatusCode.OK,"发送成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}

	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestParam Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestParam Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }

	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);
		return new Result(true,StatusCode.OK,"修改成功");
	}

	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		Claims claims = (Claims) request.getAttribute("admin_claims");
		if (claims == null){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

}
