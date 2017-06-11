<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<!-- 菜单弹窗 -->
<div id="SensitiveInfo" class="content resource-main zte-form" style="width:800px;height:auto;overflow: hidden;">
	<input type="hidden" id="menu_id" />
	<table class='pop-table'>
		<tr>
			<td class='tr'><em class="zte-star">*</em>敏感词：</td>
			<td class='tl'><input type="text" class="zte-form-control zte-validatebox" data-options="required:true" id="words_content" /></td>
			<td class='tr'><em class="zte-star">*</em>敏感词级别：</td>
			<td class='tl'><input type="text" class="zte-form-control zte-validatebox" data-options="required:true" id="words_level" /></td>
		</tr>
		<tr>
		<td class='tr'><em class="zte-star">*</em>敏感词状态：</td>
			<td class='tl'><input type="text" class="zte-form-control" id="state" /></td>
			<td class='tr'><em class="zte-star">*</em>适用范围：</td>
			<td class='tl'><input type="text" class="zte-form-control zte-numberbox" data-options="required:true" id="use_range" /></td>
		</tr>
		
	</table>
</div>