package com.ztesoft.upp.modules.msg.service;

import java.util.Map;

import net.buffalo.service.invoker.ContextMeta;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import appfrm.app.vo.PageModel;

import com.ztesoft.ioc.LogicInvokerFactory;
import com.ztesoft.upp.modules.msg.dao.MsgDAO;
import com.ztesoft.upp.modules.msg.dao.SensitiveWordDAO;
import comx.order.inf.IContextLocal;

@Controller
@RemoteProxy
public class SensitiveWordService {
	/**
	 * 敏感词管理
	 * @author zeng.jian
	 * @date 2016-08-23
	 * 
	 * */
	
	
	private SensitiveWordDAO getSensitiveWordDAO(){
		LogicInvokerFactory logic = LogicInvokerFactory.getInstance();
		SensitiveWordDAO dao = logic.getBO(SensitiveWordDAO.class);
		return dao;
		
	}
	
	/**
	 * 查找敏感词列表
	 * @author zeng.jian
	 * @return
	 * @param
	 * @date 2016-08-23
	 * */
	@SuppressWarnings("rawtypes")
	@RemoteMethod
	@Transactional
	@ContextMeta(cls = IContextLocal.DefaultWebContext)
	public PageModel getSensitiveList(Map param){
		return this.getSensitiveWordDAO().getSensitiveList(param);
	}
	
	/**
	 * 添加关键词
	 * @author zeng.jian
	 * @return
	 * @param
	 * @date 2016-08-23
	 * */
	@SuppressWarnings("rawtypes")
	@RemoteMethod
	@Transactional
	@ContextMeta(cls = IContextLocal.DefaultWebContext)
	public Map addSensitive(Map param){
		return this.getSensitiveWordDAO().addSensitive(param);
	}
	
	/**
	 * 删除关键词
	 * @author zeng.jian
	 * @return
	 * @param
	 * @date 2016-08-23
	 * */
	@SuppressWarnings("rawtypes")
	@RemoteMethod
	@Transactional
	@ContextMeta(cls = IContextLocal.DefaultWebContext)
	public Map deleteSensitive(Map param){
		return this.getSensitiveWordDAO().deleteSensitive(param);
	}
}
