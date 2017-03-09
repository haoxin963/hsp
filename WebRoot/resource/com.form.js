var form;
layui.use([ 'form' ], function() {
	form = layui.form();
});

function valid() {
	formVerifyExt();
	var verify = form.config.verify, stop = null, DANGER = 'layui-form-danger', field = {}, elem = $("#"
			+ formId), verifyElem = elem.find('*[lay-verify]') // 获取需要校验的元素
	, formElem = $("#" + formId) // 获取当前所在的form元素，如果存在的话
	, fieldElem = elem.find('input,select,textarea');
	// 开始校验
	layui.each(verifyElem, function(_, item) {
		var othis = $(this), ver = othis.attr('lay-verify'), tips = '';
		var value = othis.val(), isFn = typeof verify[ver] === 'function';
		othis.removeClass(DANGER);
		if (verify[ver]
				&& (isFn ? tips = verify[ver](value, item) : !verify[ver][0]
						.test(value))) {
			layer.msg(tips || verify[ver][1], {
				icon : 5,
				shift : 6
			});

			item.focus();

			othis.addClass(DANGER);
			return stop = true;
		}
	});

	if (stop)
		return false;
	return true;
}

/**
 * 自定义验证规则
 */
function formVerifyExt(){};