package com.ztesoft.upp.modules.msg.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.ztesoft.SysMgrKeyValues;

import util.MapUtil;
import util.SeqUtil;
import appfrm.app.util.StrUtil;
import appfrm.app.vo.PageModel;
import appfrm.resource.dao.impl.DAO;

/**
 * 消息管理
 * @author zeng.jian
 * @date 2016-10-18
 * */

public class MsgDAO {
	
	/**
	 * 使用log4j记录日志
	 * */
	private Logger logger = Logger.getLogger(MsgDAO.class);
	
	/**
	 * 2. 删除给定id的消息
	 * 
	 * @param
	 * @return
	 * @author zeng.jian
	 */
	public void deleteMsg(int id){
		
	}
	
	
	/**
	 * 3. 改
	 * 
	 * @param
	 * @return
	 */
	public void editMsg(Map params){
		
	}
	
	/**
	 * 查找消息列表
	 * @param param
	 * @return
	 * @author zeng.jian
	 * @date 2016-10-18
	 */
	@SuppressWarnings("rawtypes")
	public PageModel getMsgList(Map params){
		logger.log(Level.INFO,"getMsgList---------------");
		//可以通过标题模糊匹配查找到想要的消息
		String id= MapUtil.getStrValue(params, "ID");
		String summary = MapUtil.getStrValue(params, "msg_summary");
		int pageSize=Integer.parseInt(MapUtil.getStrValue(params, "rows"));
		int pageIndex=Integer.parseInt(MapUtil.getStrValue(params, "page"));
		StringBuffer SQL = new StringBuffer();
		SQL.append("select l.id,l.cerate_staff_name,l.publish_time,l.msg_summary from PUSH_MSG l join MSG_OPT c on l.id = c.msg_id ");
		List<String> sqlParams = new ArrayList<String>();
		if(!StrUtil.isEmpty(summary)){
			logger.info("对summary进行模糊查找！");
			SQL.append(" where msg_summary like ?");
			sqlParams.add("%"+summary+"%");
			
		}
		SQL.append(" order by ID");
		PageModel list = DAO.queryForPageModel(SQL.toString(), pageSize,
				pageIndex, sqlParams.toArray(new String[] {}));
		return list;
		
	}






/**
 * 消息分类列表
 * @author zeng.jian	
 * @date 2016-10-18
 * */

@SuppressWarnings({ "rawtypes", "unchecked" })
public List<Map> getMsgClasses(Map params) {

	logger.info("getMsgClasses-----------------");
	String id = MapUtil.getStrValue(params, "id");
	Map temp = new HashMap();
	temp.put("id", "-1");// -1为根结点的父结点ID
	List<Map> roots = getSubClass(temp);
	List<Map> result = new ArrayList<Map>(roots.size());
	for (Map item : roots) {
		result.add(getClass(item));
	}
	JSONArray array = JSONArray.fromObject(result);
	logger.info("array: " + array);
	return result;
}
/**
 * 取某个目录的有效的下级目录
 * 
 * @param menu_id
 * @return
 * @author zeng.jian
 * @date 2016-10-18
 */
@SuppressWarnings("rawtypes")
private List<Map> getSubClass(Map map) {
	String id = (String) map.get("id");
	String sql = "select id,msg_summary from push_msg t where t.id'"
			+ id + "'";
	sql += " and state=1 ";// 1为有效的目录
	List<Map> list = DAO.queryForMap(sql);
	return list;
}

/**
 * 将一个结点及它的子树全部转化为一个map类型的对象
 * 
 * @param map
 *            原始的结点，为map类型 : {id:XX,parent_id:XX,class_name:XX}
 * @return 改变后的结点，为带2个属性的map: {text:XXX,children:[]}
 *         (text的值为原结点的class_name值,[]中仍然保持这种结构,叶节点没有children属性)
 * @author zeng.jian
 * @date 2016-10-18
 */
private Map getClass(Map map) {
	Map result = new HashMap();
	result.put("text", map.get("class_name"));// 先将本节点的内容放入text的值中
	logger.info("id:" + map.get("id"));
	result.put("id", map.get("id"));
	List<Map> children = getSubClass(map);
	if (children != null && children.size() > 0) {
		List<Map> newChildren = new ArrayList<Map>();
		// 递归进行,把所有子节点变为对应的格式
		for (Map item : children) {
			newChildren.add(getClass(item));
		}
		result.put("children", newChildren);// 将改变后的子节点放入children的值中
	}
	return result;
}

/**
 * 添加消息内容
 * @author zeng.jian
 * @date 2016-10-22
 * */
@SuppressWarnings({ "unchecked", "rawtypes" })
public Map addMssage(Map params) {
	// 获取主键，大写
	String id = SeqUtil.getPrimaryKey("PUSH_MSG","ID");
	String msg_title = MapUtil.getStrValue(params, "MSG_TITLE");
	String msg_summary = MapUtil.getStrValue(params, "MSG_SUMMARY");
	String msg_content = MapUtil.getStrValue(params, "MSG_CONTENT");
	String create_staff_id = MapUtil.getStrValue(params, "CREATE_STAFF_ID");
	String create_staff_name = MapUtil.getStrValue(params, "CERATE_STAFF_NAME");

	// 执行插入
	Map result = new HashMap();
	try {
		String sql = "insert into PUSH_MSG(ID,MSG_TITLE,MSG_SUMMARY,MSG_CONTENT,CREATE_STAFF_ID,CREATE_STAFF_NAME) "
				+ "values(?,?,?,?,?,?)";
		DAO.update(sql, new String[] { id,msg_title,msg_summary,msg_content,create_staff_id,create_staff_name });
		result.put(SysMgrKeyValues.STATUSSIGN, SysMgrKeyValues.SUCCESS);
		result.put(SysMgrKeyValues.MSGSIGN, "新增成功!");
	} catch (Exception e) {
		e.printStackTrace();
		result.put(SysMgrKeyValues.STATUSSIGN, SysMgrKeyValues.FAIL);
		result.put(SysMgrKeyValues.MSGSIGN, "新增失败!");
	}
	return result;
}


}