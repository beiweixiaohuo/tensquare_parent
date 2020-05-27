package com.tensquare.qa.controller;
import java.util.List;
import java.util.Map;

import com.tensquare.qa.client.LabelClient;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

	@Autowired
	private ProblemService problemService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private LabelClient labelClient;

	@GetMapping(value = "/label/{id}")
	public Result findByLabelid(@PathVariable("id")String id){
		return labelClient.findById(id);
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findAll());
	}

	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findById(id));
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
		Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestParam Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",problemService.findSearch(searchMap));
    }

	/**
	 * 根据标签ID查询最新问题列表
	 * @param labelId
	 * @param page
	 * @param size
	 * @return
	 */
    @GetMapping("/newlist/{labelId}/{page}/{size}")
	public Result findNewListByLabelId(@PathVariable("labelId")String labelId,
									   @PathVariable("page")int page,@PathVariable("size") int size){
		Page<Problem> pageList = problemService.findNewListByLabelId(labelId, page, size);
		PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(),pageList.getContent());
		return new Result(true,StatusCode.OK,"查询成功",pageResult);
	}

	/**
	 * 根据标签ID查询热门问题列表
	 * @param labelId
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/hotlist/{labelId}/{page}/{size}")
	public Result findHotListByLabelId(@PathVariable("labelId")String labelId,
									   @PathVariable("page")int page,@PathVariable("size") int size){
		Page<Problem> pageList = problemService.findHotListByLabelId(labelId, page, size);
		PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(),pageList.getContent());
		return new Result(true,StatusCode.OK,"查询成功",pageResult);
	}

	/**
	 * 根据标签ID查询等待回答列表
	 * @param labelId
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/waitlist/{labelId}/{page}/{size}")
	public Result findWaitListByLabelId(@PathVariable("labelId")String labelId,
									   @PathVariable("page")int page,@PathVariable("size") int size){
		Page<Problem> pageList = problemService.findWaitListByLabelId(labelId, page, size);
		PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(),pageList.getContent());
		return new Result(true,StatusCode.OK,"查询成功",pageResult);
	}

	/**
	 * 增加
	 * @param problem
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add( Problem problem  ){
		Claims claims = (Claims) request.getAttribute("user_claims");
		if (claims == null){
			return new Result(false,StatusCode.ACCESSERROR,"无权访问，请先登录");
		}
		problem.setUserid(claims.getId());
		problemService.add(problem);
		return new Result(true,StatusCode.OK,"增加成功");
	}

	/**
	 * 修改
	 * @param problem
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update( Problem problem, @PathVariable String id ){
		problem.setId(id);
		problemService.update(problem);
		return new Result(true,StatusCode.OK,"修改成功");
	}

	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		problemService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

}
