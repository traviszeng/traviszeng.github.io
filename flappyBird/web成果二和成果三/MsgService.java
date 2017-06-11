package com.ztesoft.upp.modules.msg.service;

import java.util.List;
import java.util.Map;

import net.buffalo.service.invoker.ContextMeta;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import appfrm.app.vo.PageModel;

import com.ztesoft.ioc.LogicInvokerFactory;
import com.ztesoft.upp.modules.msg.dao.MsgDAO;

import comx.order.inf.IContextLocal;

@Controller
@RemoteProxy
public class MsgService {
	
	/**
	 * 消息管理
	 * @author zeng.jian
	 * @date 2016-08-18
	 * */
	private MsgDAO getMsgDAO(){
		LogicInvokerFactory logic = LogicInvokerFactory.getInstance();
		MsgDAO dao = logic.getBO(MsgDAO.class);
		return dao;
	}
	
	/**
	 * 查找消息列表
	 * @author zeng.jian
	 * @return
	 * @param
	 * @date 2016-08-18
	 * */
	@SuppressWarnings("rawtypes")
	@RemoteMethod
	@Transactional
	@ContextMeta(cls = IContextLocal.DefaultWebContext)
	public PageModel getMsgList(Map param){
		return this.getMsgDAO().getMsgList(param);
	}
	
	/**
	 * 添加消息
	 * @author zeng.jian
	 * @param
	 * @return 
	 * @date 2016-08-22
	 * */
	@SuppressWarnings("rawtypes")
	@RemoteMethod
	@Transactional
	@ContextMeta(cls = IContextLocal.DefaultWebContext)
	public Map addMessage(Map param){
		return this.getMsgDAO().addMssage(param);
	}
	
	
	/**
	 * 查找消息分类列表
	 * */
	@SuppressWarnings("rawtypes")
	@RemoteMethod
	@Transactional
	@ContextMeta(cls = IContextLocal.DefaultWebContext)
	public List<Map> getMsgClasses(Map param){
		return this.getMsgDAO().getMsgClasses(param);
	}
}

