package com.ztesoft.upp.modules.msg.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.ztesoft.SysMgrKeyValues;

import util.MapUtil;
import util.SeqUtil;
import appfrm.app.util.StrUtil;
import appfrm.app.vo.PageModel;
import appfrm.resource.dao.impl.DAO;

/**
 * 敏感词管理
 * @author zeng.jian
 * @date 2016-08-23
 * */
public class SensitiveWordDAO {
	/**
	 * 使用log4j记录日志
	 * */
	private Logger logger = Logger.getLogger(SensitiveWordDAO.class);
	
	
	/**
	 * 获取敏感词列表
	 *@author zeng.jian	
	 * @date 2016-08-23
	 * */
	public PageModel getSensitiveList(Map params){
		logger.log(Level.INFO,"getSensitiveList---------------");
		//可以通过内容模糊匹配查找到想要的敏感词
		String id= MapUtil.getStrValue(params, "ID");
		String words_content = MapUtil.getStrValue(params, "words_content");
		int pageSize=Integer.parseInt(MapUtil.getStrValue(params, "rows"));
		int pageIndex=Integer.parseInt(MapUtil.getStrValue(params, "page"));
		StringBuffer SQL = new StringBuffer();
		SQL.append("select id,words_content,use_range,update_time from SENSITIVE_WORDS ");
		List<String> sqlParams = new ArrayList<String>();
		if(!StrUtil.isEmpty(words_content)){
			logger.info("对敏感词进行模糊查找！");
			SQL.append(" where words_content like ?");
			sqlParams.add("%"+words_content+"%");
			
		}
		SQL.append(" order by ID");
		PageModel list = DAO.queryForPageModel(SQL.toString(), pageSize,
				pageIndex, sqlParams.toArray(new String[] {}));
		return list;
		
	}
	
	/**
	 * 添加敏感词
	 * @author zeng.jian	
	 * @date 2016-08-23
	 * 
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map addSensitive(Map params){
		logger.log(Level.INFO,"addSensitive---------------");
		String id = SeqUtil.getPrimaryKey("SENSITIVE_WORDS","ID");
		String words_content = MapUtil.getStrValue(params, "words_content");
		String words_level = MapUtil.getStrValue(params, "words_level");
		String state = MapUtil.getStrValue(params, "state");
		String use_range = MapUtil.getStrValue(params, "use_range");
		
		//执行插入
		Map result = new HashMap();
		try {
			String sql = "insert into SENSITIVE_WORDS(id,words_content,words_level,update_time,state,use_range,create_time)"+
		"  values (?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
			Date date=new Date();
			  DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  String time=format.format(date);
			  //检查是否已有待加入的关键词
			  String checkSql = "select words_content from SENSITIVE_WORDS";
			  //将已存在的关键词保存在current_word_list
			  List<Map> current_word_list =DAO.queryForMap(checkSql);
			  //遍历查看新增敏感词是否和已存在数据库中的敏感词相同;
			  //如果存在一致的敏感词则抛出SameException；添加敏感词失败
			  for (Map map : current_word_list) {
				  if(map.get("words_content").equals(words_content)){
					  throw new SameException();
				  }
				 
				//System.out.println(map.get("words_content"));
			}
			  //System.out.println(current_word_list);
			DAO.update(sql, new String[]{
					id,words_content,words_level,time,state,use_range,time});	
			result.put(SysMgrKeyValues.STATUSSIGN, SysMgrKeyValues.SUCCESS);
			result.put(SysMgrKeyValues.MSGSIGN, "新增成功!");
		}catch(SameException ee){
			ee.printStackTrace();
			result.put(SysMgrKeyValues.STATUSSIGN, SysMgrKeyValues.FAIL);
			result.put(SysMgrKeyValues.MSGSIGN, "新增失败!（请核对是否已有想要添加的敏感词）");
		}catch (Exception e) {
			e.printStackTrace();
			result.put(SysMgrKeyValues.STATUSSIGN, SysMgrKeyValues.FAIL);
			result.put(SysMgrKeyValues.MSGSIGN, "新增失败!");
		}
		return result;
	}
	
	/**
	 * 删除敏感词
	 * @author zeng.jian
	 * @date 2016-08-23
	 * */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map deleteSensitive(Map params){
		//获取主键
		logger.log(Level.INFO,"deleteSensitive---------------");
		String id = MapUtil.getStrValue(params, "id");
		String words_content = MapUtil.getStrValue(params, "words_content");
		Map result = new HashMap();
		try{
			//System.out.println(id);
			//根据获取到的id删除掉对应的敏感词
			String sql = "delete from SENSITIVE_WORDS where ID = ?";
			DAO.update(sql, new String[]{ id});
			result.put(SysMgrKeyValues.STATUSSIGN, SysMgrKeyValues.SUCCESS);
			result.put(SysMgrKeyValues.MSGSIGN, "删除成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put(SysMgrKeyValues.STATUSSIGN, SysMgrKeyValues.FAIL);
			result.put(SysMgrKeyValues.MSGSIGN, "删除失败!");
		}
		return result;
	}
	/**
	 *@author zeng.jian
	 *@date 2016-08-24
	 *存在相同的敏感词的异常类
	 */
	public class SameException extends Exception{
		public SameException(){
			super();
		}
	}
	
	
}
