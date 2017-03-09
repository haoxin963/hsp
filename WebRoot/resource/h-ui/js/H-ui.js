
/* -----------H-ui前端框架-------------*/
/*responsive-nav.min.js*/
!function(a,b,c){"use strict";var d=function(d,e){var f=!!b.getComputedStyle;f||(b.getComputedStyle=function(a){return this.el=a,this.getPropertyValue=function(b){var c=/(\-([a-z]){1})/g;return"float"===b&&(b="styleFloat"),c.test(b)&&(b=b.replace(c,function(){return arguments[2].toUpperCase()})),a.currentStyle[b]?a.currentStyle[b]:null},this});var g,h,i,j,k,l,m=function(a,b,c,d){if("addEventListener"in a)try{a.addEventListener(b,c,d)}catch(e){if("object"!=typeof c||!c.handleEvent)throw e;a.addEventListener(b,function(a){c.handleEvent.call(c,a)},d)}else"attachEvent"in a&&("object"==typeof c&&c.handleEvent?a.attachEvent("on"+b,function(){c.handleEvent.call(c)}):a.attachEvent("on"+b,c))},n=function(a,b,c,d){if("removeEventListener"in a)try{a.removeEventListener(b,c,d)}catch(e){if("object"!=typeof c||!c.handleEvent)throw e;a.removeEventListener(b,function(a){c.handleEvent.call(c,a)},d)}else"detachEvent"in a&&("object"==typeof c&&c.handleEvent?a.detachEvent("on"+b,function(){c.handleEvent.call(c)}):a.detachEvent("on"+b,c))},o=function(a){if(a.children.length<1)throw new Error("The Nav container has no containing elements");for(var b=[],c=0;c<a.children.length;c++)1===a.children[c].nodeType&&b.push(a.children[c]);return b},p=function(a,b){for(var c in b)a.setAttribute(c,b[c])},q=function(a,b){0!==a.className.indexOf(b)&&(a.className+=" "+b,a.className=a.className.replace(/(^\s*)|(\s*$)/g,""))},r=function(a,b){var c=new RegExp("(\\s|^)"+b+"(\\s|$)");a.className=a.className.replace(c," ").replace(/(^\s*)|(\s*$)/g,"")},s=function(a,b,c){for(var d=0;d<a.length;d++)b.call(c,d,a[d])},t=a.createElement("style"),u=a.documentElement,v=function(b,c){var d;this.options={animate:!0,transition:284,label:"Menu",insert:"before",customToggle:"",closeOnNavClick:!1,openPos:"relative",navClass:"nav-collapse",navActiveClass:"js-nav-active",jsClass:"js",init:function(){},open:function(){},close:function(){}};for(d in c)this.options[d]=c[d];if(q(u,this.options.jsClass),this.wrapperEl=b.replace("#",""),a.getElementById(this.wrapperEl))this.wrapper=a.getElementById(this.wrapperEl);else{if(!a.querySelector(this.wrapperEl))throw new Error("The nav element you are trying to select doesn't exist");this.wrapper=a.querySelector(this.wrapperEl)}this.wrapper.inner=o(this.wrapper),h=this.options,g=this.wrapper,this._init(this)};return v.prototype={destroy:function(){this._removeStyles(),r(g,"closed"),r(g,"opened"),r(g,h.navClass),r(g,h.navClass+"-"+this.index),r(u,h.navActiveClass),g.removeAttribute("style"),g.removeAttribute("aria-hidden"),n(b,"resize",this,!1),n(b,"focus",this,!1),n(a.body,"touchmove",this,!1),n(i,"touchstart",this,!1),n(i,"touchend",this,!1),n(i,"mouseup",this,!1),n(i,"keyup",this,!1),n(i,"click",this,!1),h.customToggle?i.removeAttribute("aria-hidden"):i.parentNode.removeChild(i)},toggle:function(){j===!0&&(l?this.close():this.open())},open:function(){l||(r(g,"closed"),q(g,"opened"),q(u,h.navActiveClass),q(i,"active"),g.style.position=h.openPos,p(g,{"aria-hidden":"false"}),l=!0,h.open())},close:function(){l&&(q(g,"closed"),r(g,"opened"),r(u,h.navActiveClass),r(i,"active"),p(g,{"aria-hidden":"true"}),h.animate?(j=!1,setTimeout(function(){g.style.position="absolute",j=!0},h.transition+10)):g.style.position="absolute",l=!1,h.close())},resize:function(){"none"!==b.getComputedStyle(i,null).getPropertyValue("display")?(k=!0,p(i,{"aria-hidden":"false"}),g.className.match(/(^|\s)closed(\s|$)/)&&(p(g,{"aria-hidden":"true"}),g.style.position="absolute"),this._createStyles(),this._calcHeight()):(k=!1,p(i,{"aria-hidden":"true"}),p(g,{"aria-hidden":"false"}),g.style.position=h.openPos,this._removeStyles())},handleEvent:function(a){var c=a||b.event;switch(c.type){case"touchstart":this._onTouchStart(c);break;case"touchmove":this._onTouchMove(c);break;case"touchend":case"mouseup":this._onTouchEnd(c);break;case"click":this._preventDefault(c);break;case"keyup":this._onKeyUp(c);break;case"focus":case"resize":this.resize(c)}},_init:function(){this.index=c++,q(g,h.navClass),q(g,h.navClass+"-"+this.index),q(g,"closed"),j=!0,l=!1,this._closeOnNavClick(),this._createToggle(),this._transitions(),this.resize();var d=this;setTimeout(function(){d.resize()},20),m(b,"resize",this,!1),m(b,"focus",this,!1),m(a.body,"touchmove",this,!1),m(i,"touchstart",this,!1),m(i,"touchend",this,!1),m(i,"mouseup",this,!1),m(i,"keyup",this,!1),m(i,"click",this,!1),h.init()},_createStyles:function(){t.parentNode||(t.type="text/css",a.getElementsByTagName("head")[0].appendChild(t))},_removeStyles:function(){t.parentNode&&t.parentNode.removeChild(t)},_createToggle:function(){if(h.customToggle){var b=h.customToggle.replace("#","");if(a.getElementById(b))i=a.getElementById(b);else{if(!a.querySelector(b))throw new Error("The custom nav toggle you are trying to select doesn't exist");i=a.querySelector(b)}}else{var c=a.createElement("a");c.innerHTML=h.label,p(c,{href:"#","class":"nav-toggle"}),"after"===h.insert?g.parentNode.insertBefore(c,g.nextSibling):g.parentNode.insertBefore(c,g),i=c}},_closeOnNavClick:function(){if(h.closeOnNavClick){var a=g.getElementsByTagName("a"),b=this;s(a,function(c){m(a[c],"click",function(){k&&b.toggle()},!1)})}},_preventDefault:function(a){return a.preventDefault?(a.stopImmediatePropagation&&a.stopImmediatePropagation(),a.preventDefault(),a.stopPropagation(),!1):void(a.returnValue=!1)},_onTouchStart:function(a){Event.prototype.stopImmediatePropagation||this._preventDefault(a),this.startX=a.touches[0].clientX,this.startY=a.touches[0].clientY,this.touchHasMoved=!1,n(i,"mouseup",this,!1)},_onTouchMove:function(a){(Math.abs(a.touches[0].clientX-this.startX)>10||Math.abs(a.touches[0].clientY-this.startY)>10)&&(this.touchHasMoved=!0)},_onTouchEnd:function(a){if(this._preventDefault(a),k&&!this.touchHasMoved){if("touchend"===a.type)return void this.toggle();var c=a||b.event;3!==c.which&&2!==c.button&&this.toggle()}},_onKeyUp:function(a){var c=a||b.event;13===c.keyCode&&this.toggle()},_transitions:function(){if(h.animate){var a=g.style,b="max-height "+h.transition+"ms";a.WebkitTransition=a.MozTransition=a.OTransition=a.transition=b}},_calcHeight:function(){for(var a=0,b=0;b<g.inner.length;b++)a+=g.inner[b].offsetHeight;var c="."+h.jsClass+" ."+h.navClass+"-"+this.index+".opened{max-height:"+a+"px !important} ."+h.jsClass+" ."+h.navClass+"-"+this.index+".opened.dropdown-active {max-height:9999px !important}";t.styleSheet?t.styleSheet.cssText=c:t.innerHTML=c,c=""}},new v(d,e)};"undefined"!=typeof module&&module.exports?module.exports=d:b.responsiveNav=d}(document,window,0);
if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
	  var msViewportStyle = document.createElement("style")
	  msViewportStyle.appendChild(
	    document.createTextNode(
	      "@-ms-viewport{width:auto!important}"
	    )
	  )
	  document.getElementsByTagName("head")[0].appendChild(msViewportStyle);
	}
	/*placeholder兼容性处理*/
	(function(window, document, $) {
		var isInputSupported = 'placeholder' in document.createElement('input');
		var isTextareaSupported = 'placeholder' in document.createElement('textarea');
		var prototype = $.fn;
		var valHooks = $.valHooks;
		var propHooks = $.propHooks;
		var hooks;
		var placeholder;

		if (isInputSupported && isTextareaSupported) {
			placeholder = prototype.placeholder = function() {
				return this;
			};
			placeholder.input = placeholder.textarea = true;
		} else {
			placeholder = prototype.placeholder = function() {
				var $this = this;
				$this
					.filter((isInputSupported ? 'textarea' : ':input') + '[placeholder]')
					.not('.placeholder')
					.bind({
						'focus.placeholder': clearPlaceholder,
						'blur.placeholder': setPlaceholder
					})
					.data('placeholder-enabled', true)
					.trigger('blur.placeholder');
				return $this;
			};
			placeholder.input = isInputSupported;
			placeholder.textarea = isTextareaSupported;
			hooks = {
				'get': function(element) {
					var $element = $(element);
					var $passwordInput = $element.data('placeholder-password');
					if ($passwordInput) {
						return $passwordInput[0].value;
					}
					return $element.data('placeholder-enabled') && $element.hasClass('placeholder') ? '' : element.value;
				},
				'set': function(element, value) {
					var $element = $(element);
					var $passwordInput = $element.data('placeholder-password');
					if ($passwordInput) {
						return $passwordInput[0].value = value;
					}
					if (!$element.data('placeholder-enabled')) {
						return element.value = value;
					}
					if (value == '') {
						element.value = value;
						if (element != safeActiveElement()) {
							setPlaceholder.call(element);
						}
					} else if ($element.hasClass('placeholder')) {
						clearPlaceholder.call(element, true, value) || (element.value = value);
					} else {
						element.value = value;
					}
					return $element;
				}
			};

			if (!isInputSupported) {
				valHooks.input = hooks;
				propHooks.value = hooks;
			}
			if (!isTextareaSupported) {
				valHooks.textarea = hooks;
				propHooks.value = hooks;
			}

			$(function() {
				$(document).delegate('form', 'submit.placeholder', function() {
					var $inputs = $('.placeholder', this).each(clearPlaceholder);
					setTimeout(function() {
						$inputs.each(setPlaceholder);
					}, 10);
				});
			});

			$(window).bind('beforeunload.placeholder', function() {
				$('.placeholder').each(function() {
					this.value = '';
				});
			});
		}

		function args(elem) {
			var newAttrs = {};
			var rinlinejQuery = /^jQuery\d+$/;
			$.each(elem.attributes, function(i, attr) {
				if (attr.specified && !rinlinejQuery.test(attr.name)) {
					newAttrs[attr.name] = attr.value;
				}
			});
			return newAttrs;
		}

		function clearPlaceholder(event, value) {
			var input = this;
			var $input = $(input);
			if (input.value == $input.attr('placeholder') && $input.hasClass('placeholder')) {
				if ($input.data('placeholder-password')) {
					$input = $input.hide().next().show().attr('id', $input.removeAttr('id').data('placeholder-id'));
					if (event === true) {
						return $input[0].value = value;
					}
					$input.focus();
				} else {
					input.value = '';
					$input.removeClass('placeholder');
					input == safeActiveElement() && input.select();
				}
			}
		}

		function setPlaceholder() {
			var $replacement;
			var input = this;
			var $input = $(input);
			var id = this.id;
			if (input.value == '') {
				if (input.type == 'password') {
					if (!$input.data('placeholder-textinput')) {
						try {
							$replacement = $input.clone().prop('type','text');
						} catch(e) {
							$replacement = $('<input>').prop($.extend(args(this), { 'type': 'text' }));
						}
						$replacement
							.removeAttr('name')
							.data({
								'placeholder-password': $input,
								'placeholder-id': id
							})
							.bind('focus.placeholder', clearPlaceholder);
						$input
							.data({
								'placeholder-textinput': $replacement,
								'placeholder-id': id
							})
							.before($replacement);
					}
					$input = $input.removeAttr('id').hide().prev().attr('id', id).show();
				}
				$input.addClass('placeholder');
				$input[0].value = $input.attr('placeholder');
			} else {
				$input.removeClass('placeholder');
			}
		}
		function safeActiveElement() {
			try {
				return document.activeElement;
			} catch (exception) {}
		}
	}(this, document, jQuery));
	/**/
	;(function($) {
	    $.extend({
	        format : function(str, step, splitor) {
	            str = str.toString();
	            var len = str.length;
	  
	            if(len > step) {
	                 var l1 = len%step, 
	                     l2 = parseInt(len/step),
	                     arr = [],
	                     first = str.substr(0, l1);
	                 if(first != '') {
	                     arr.push(first);
	                 };
	                 for(var i=0; i<l2 ; i++) {
	                     arr.push(str.substr(l1 + i*step, step));                                    
	                 };
	                 str = arr.join(splitor);
	             };
	             return str;
	        }
	    });
	})(jQuery);
	/*Dropdown*/
	+function ($) {
		'use strict';
		var backdrop = '.dropdown-backdrop';
		var toggle   = '[data-toggle="dropdown"]';
		var Dropdown = function (element) {
			$(element).on('click.bs.dropdown', this.toggle)
		}
		Dropdown.VERSION = '3.3.5';
		function getParent($this) {
			var selector = $this.attr('data-target');
			if (!selector) {
				selector = $this.attr('href');
				selector = selector && /#[A-Za-z]/.test(selector) && selector.replace(/.*(?=#[^\s]*$)/, ''); // strip for ie7
			}
			var $parent = selector && $(selector);
			return $parent && $parent.length ? $parent : $this.parent();
		}
		function clearMenus(e) {
			if (e && e.which === 3) return
			$(backdrop).remove();
			$(toggle).each(function () {
				var $this = $(this)
				var $parent = getParent($this)
				var relatedTarget = { relatedTarget: this }
				if (!$parent.hasClass('open')) return
				if (e && e.type == 'click' && /input|textarea/i.test(e.target.tagName) && $.contains($parent[0], e.target)) return
				$parent.trigger(e = $.Event('hide.bs.dropdown', relatedTarget));
				if (e.isDefaultPrevented()) return
				$this.attr('aria-expanded', 'false');
				$parent.removeClass('open').trigger('hidden.bs.dropdown', relatedTarget);
			});
		}
		Dropdown.prototype.toggle = function (e) {
			var $this = $(this)
			if ($this.is('.disabled, :disabled')) return
			var $parent  = getParent($this);
			var isActive = $parent.hasClass('open');
			clearMenus();
			if (!isActive) {
			if ('ontouchstart' in document.documentElement && !$parent.closest('.navbar-nav').length) {
				// if mobile we use a backdrop because click events don't delegate
				$(document.createElement('div')).addClass('dropdown-backdrop').insertAfter($(this)).on('click', clearMenus);
			}
			var relatedTarget = { relatedTarget: this }
			$parent.trigger(e = $.Event('show.bs.dropdown', relatedTarget));
			if (e.isDefaultPrevented()) return
				$this.trigger('focus').attr('aria-expanded', 'true');
				$parent.toggleClass('open').trigger('shown.bs.dropdown', relatedTarget);
			}
			return false
		}
		Dropdown.prototype.keydown = function (e) {
			if (!/(38|40|27|32)/.test(e.which) || /input|textarea/i.test(e.target.tagName)) return
			var $this = $(this)
			e.preventDefault()
			e.stopPropagation()
			if ($this.is('.disabled, :disabled')) return
			var $parent  = getParent($this);
			var isActive = $parent.hasClass('open');
			if (!isActive && e.which != 27 || isActive && e.which == 27) {
				if (e.which == 27) $parent.find(toggle).trigger('focus')
				return $this.trigger('click')
			}
			var desc = ' li:not(.disabled):visible a'
			var $items = $parent.find('.dropdown-menu' + desc)
			if (!$items.length) return
			var index = $items.index(e.target);
			if (e.which == 38 && index > 0)                 index--         // up
			if (e.which == 40 && index < $items.length - 1) index++         // down
			if (!~index)                                    index = 0
			$items.eq(index).trigger('focus');
		}
		function Plugin(option) {
			return this.each(function () {
				var $this = $(this);
				var data  = $this.data('bs.dropdown');
				if (!data){
					$this.data('bs.dropdown', (data = new Dropdown(this)));
				}
				if (typeof option == 'string'){
					data[option].call($this);
				}
			});
		}
		var old = $.fn.dropdown;
		$.fn.dropdown             = Plugin;
		$.fn.dropdown.Constructor = Dropdown;
		$.fn.dropdown.noConflict = function () {
			$.fn.dropdown = old;
			return this;
		}
		$(document).on('click.bs.dropdown.data-api', clearMenus).on('click.bs.dropdown.data-api', '.dropdown form', function (e) { e.stopPropagation() }).on('click.bs.dropdown.data-api', toggle, Dropdown.prototype.toggle).on('keydown.bs.dropdown.data-api', toggle, Dropdown.prototype.keydown).on('keydown.bs.dropdown.data-api', '.dropdown-menu', Dropdown.prototype.keydown);
	}(jQuery);
	/*transition*/
	!function ($) {
		"use strict";
		$(function () {
			$.support.transition = (function () {
				var transitionEnd = (function () {
					var el = document.createElement('bootstrap'),
					transEndEventNames = {
						'WebkitTransition' : 'webkitTransitionEnd',
						'MozTransition'    : 'transitionend',
						'OTransition'      : 'oTransitionEnd otransitionend',
						'transition'       : 'transitionend'
					},
					name
					for (name in transEndEventNames){
						if (el.style[name] !== undefined) {
							return transEndEventNames[name]
						}
					}
				}())
				return transitionEnd && {
					end: transitionEnd
				}
			})()
		});
	}(window.jQuery);
	/*左侧菜单-隐藏显示*/
	function displaynavbar(obj){
		if($(obj).hasClass("open")){
			$(obj).removeClass("open");
			$("body").removeClass("big-page");
		}else{
			$(obj).addClass("open");
			$("body").addClass("big-page");
						
		}
	}

	/*模拟下拉菜单*/
	jQuery.Huiselect = function(divselectid,inputselectid) {
		var inputselect = $(inputselectid);
		$(divselectid+" cite").click(function(){
			var ul = $(divselectid+" ul");
			ul.slideToggle();
		});
		$(divselectid+" ul li a").click(function(){
			var txt = $(this).text();
			$(divselectid+" cite").html(txt);
			var value = $(this).attr("selectid");
			inputselect.val(value);
			$(divselectid+" ul").hide();
		});
		$(document).click(function(){$(divselectid+" ul").hide();});
	};

	/*hover*/
	jQuery.Huihover =function(obj) {
		$(obj).hover(function(){$(this).addClass("hover");},function(){$(this).removeClass("hover");});
	};
	/*得到失去焦点*/
	jQuery.Huifocusblur = function(obj) {
		$(obj).focus(function() {$(this).addClass("focus").removeClass("inputError");});
		$(obj).blur(function() {$(this).removeClass("focus");});
	};
	/*tab选项卡*/
	jQuery.Huitab =function(tabBar,tabCon,class_name,tabEvent,i){
	  	var $tab_menu=$(tabBar);
		// 初始化操作
		$tab_menu.removeClass(class_name);
		$(tabBar).eq(i).addClass(class_name);
		$(tabCon).hide();
		$(tabCon).eq(i).show();
		
		$tab_menu.on(tabEvent,function(){
			$tab_menu.removeClass(class_name);
			$(this).addClass(class_name);
			var index=$tab_menu.index(this);
			$(tabCon).hide();
			$(tabCon).eq(index).show();
		});
	}

	/*折叠*/
	jQuery.Huifold = function(obj,obj_c,speed,obj_type,Event){
		if(obj_type == 2){
			$(obj+":first").find("b").html("-");
			$(obj_c+":first").show();
		}			
		$(obj).on(Event,function(){
			if($(this).next().is(":visible")){
				if(obj_type == 2){
					return false;
				}else{
					$(this).next().slideUp(speed).end().removeClass("selected");
					if($(this).find("b")){
						$(this).find("b").html("+");
					}
				}
			}
			else{
				if(obj_type == 3){
					$(this).next().slideDown(speed).end().addClass("selected");
					if($(this).find("b")){
						$(this).find("b").html("-");
					}
				}else{
					$(obj_c).slideUp(speed);
					$(obj).removeClass("selected");
					if($(this).find("b")){
						$(obj).find("b").html("+");
					}
					$(this).next().slideDown(speed).end().addClass("selected");
					if($(this).find("b")){
						$(this).find("b").html("-");
					}
				}
			}
		});
	}
	/*Huimodalalert*/
	function Huimodal_alert(info,speed){
		if(speed==0||typeof(speed) == "undefined"){
			$(document.body).append(
			'<div id="modal-alert" class="modal hide modal-alert">'+
		  		'<div class="modal-alert-info">'+info+'</div>'+
		  		'<div class="modal-footer"> <button class="btn btn-primary radius" onClick="modal_alert_hide()">确定</button></div>'+
			'</div>'
			);
			$("#modal-alert").fadeIn();
		}else{
			$(document.body).append(
			'<div id="modal-alert" class="modal hide modal-alert">'+
		  		'<div class="modal-alert-info">'+info+'</div>'+
			'</div>'
			);
			$("#modal-alert").fadeIn();
			setTimeout("Huimodal_alert_hide()",speed);
		}	
	}
	function Huimodal_alert_hide() {
		$("#modal-alert").fadeOut("normal",function(){
			$("#modal-alert").remove();
		});
	}
	/*设置cookie*/
	function setCookie(name, value, Days){
		if(Days == null || Days == ''){
			Days = 300;
		}
		var exp  = new Date();
		exp.setTime(exp.getTime() + Days*24*60*60*1000);
		document.cookie = name + "="+ escape (value) + "; path=/;expires=" + exp.toGMTString();
	}
	/*获取cookie*/
	function getCookie(name) {
	    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	    if(arr=document.cookie.match(reg))
	        return unescape(arr[2]); 
	    else 
	        return null; 
	}
	$(function(){
		/*****表单*****/
	    $.Huifocusblur(".input-text,.textarea");
		/*按钮loading*/
		$('.btn-loading').click(function () {
			var $btn = $(this);
			var btnval = $btn.val();
			$btn.addClass("disabled").val("loading").attr("disabled","disabled");
			setTimeout(function(){
				$btn.removeClass("disabled").val(btnval).removeAttr("disabled");
			}, 3000);
		});	
		/**/
		$.Huiselect("#divselect","#inputselect");
		
		/*下拉菜单*/
		$(document).on("mouseenter",".dropDown",function(){
			$(this).addClass("hover");
		});
		$(document).on("mouseleave",".dropDown",function(){
			$(this).removeClass("hover");
		});
		$(document).on("mouseenter",".dropDown_hover",function(){
			$(this).addClass("open");
		});
		$(document).on("mouseleave",".dropDown_hover",function(){
			$(this).removeClass("open");
		});
		$(document).on("click",".dropDown-menu li a",function(){
			$(".dropDown").removeClass('open');
		});
//		$(document).on("mouseenter",".menu > li",function(){
//			$(this).addClass("open");
//		});
//		$(document).on("mouseleave",".menu > li",function(){
//			$(this).removeClass("open");
//		});
		$(function(){
//				$(".menu > li").each(function(index){
//					$(this).click(function(){
//						if ($(this).attr("class")=="open"){
//							$(this).removeClass("open").siblings().removeClass("open");
//							$(this).children("ul.menu:first").slideUp(300);
//							$(this).siblings().children("ul>menu").slideUp(300);
//							alert("全部收起来");
//						}else{
//							$(this).addClass("open").siblings().removeClass("open");
//							$(this).children("ul.menu:first").slideDown(300);
//							$(this).siblings().children("ul.menu").slideUp(300);
//							alert("当前展开来，其他收起来");
//						}
//					});
//				})
//			$(".menu > li").click(function(){
//				$(this).toggleClass("open").siblings().removeClass("open");
//				$(this).children("ul.menu:first").slideDown(300);
//				$(this).siblings().children("ul.menu").slideUp(300);
//			})
			$('.inactive').click(function(){
				if($(this).siblings('ul').css('display')=='none'){
					$(this).parent('li').addClass('inactives_li');
					$(this).parent('li').siblings('li').removeClass('inactives_li');
					$(this).addClass('inactives');
					$(this).siblings('ul').slideDown(300).children('li');
					if($(this).parents('li').siblings('li').children('ul').css('display')=='block'){
						$(this).parents('li').siblings('li').children('ul').parent('li').children('a').removeClass('inactives');
						$(this).parents('li').siblings('li').children('ul').slideUp(300);
					}else{
						$(this).parents('li').siblings('li').children('ul').parent('li').children('a').removeClass('inactives');
						$(this).parents('li').siblings('li').children('ul').slideUp(300);
					}
				}else{
					//控制自身变成+号
					$(this).removeClass('inactives');
					//控制自身菜单下子菜单隐藏
					$(this).siblings('ul').slideUp(300);
					//控制自身子菜单变成+号
					$(this).siblings('ul').children('li').children('ul').parent('li').children('a').addClass('inactives');
					//控制自身菜单下子菜单隐藏
					$(this).siblings('ul').children('li').children('ul').slideUp(300);
		
					//控制同级菜单只保持一个是展开的
					$(this).siblings('ul').children('li').children('a').removeClass('inactives');
					
				}
			})
		})
		
		/*tag标签*/
		var tags_a = $(".tags a");
		tags_a.each(function(){
			var x = 9;
			var y = 0;
			var rand = parseInt(Math.random() * (x - y + 1) + y);
			$(this).addClass("tags"+rand);
		});
	});

/* -----------H-ui前端框架-------------  */
var num=0,oUl=$("#min_title_list"),hide_nav=$("#Hui-tabNav");

/*获取顶部选项卡总长度*/
function tabNavallwidth(){
	var taballwidth=0,
		$tabNav = hide_nav.find(".acrossTab"),
		$tabNavWp = hide_nav.find(".Hui-tabNav-wp"),
		$tabNavitem = hide_nav.find(".acrossTab li"),
		$tabNavmore =hide_nav.find(".Hui-tabNav-more");
	if (!$tabNav[0]){return}
	$tabNavitem.each(function(index, element) {
        taballwidth += Number(parseFloat($(this).width()+60))
    });
	$tabNav.width(taballwidth+25);
	var w = $tabNavWp.width();
	if(taballwidth+25>w){
		$tabNavmore.show()}
	else{
		$tabNavmore.hide();
		$tabNav.css({left:0})
	}
}

/*左侧菜单响应式*/
function Huiasidedisplay(){
	if($(window).width()>=768){
		$(".Hui-aside").show()
	} 
}
/*获取皮肤cookie*/
function getskincookie(){
	var v = getCookie("Huiskin");
	var hrefStr=$("#skin").attr("href");
	if(v==null||v==""){
		v="default";
	}
	if(hrefStr!=undefined){
		var hrefRes=hrefStr.substring(0,hrefStr.lastIndexOf('skin/'))+'skin/'+v+'/skin.css';
		$("#skin").attr("href",hrefRes);
	}
}
/*菜单导航*/
var hrefonload;
function Hui_admin_tab(obj){
	if($(obj).attr('data-href')){
		var bStop = false,
			bStopIndex = 0,
			href = $(obj).attr('data-href'),
			title = $(obj).attr("data-title"),
			topWindow = $(window.parent.document),
			show_navLi = topWindow.find("#min_title_list li"),
			iframe_box = topWindow.find("#iframe_box");
			
		show_navLi.each(function() {
			if($(this).find('span').attr("data-href")==href){
				bStop=true;
				bStopIndex=show_navLi.index($(this));
				return false;
			}
		});
		if(!bStop){
			creatIframe(href,title);
			min_titleList();
		}
		else{
			show_navLi.removeClass("active").eq(bStopIndex).addClass("active");			
			iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe").attr("src",href);
		}
	}
}

function min_titleList(){
	var topWindow = $(window.parent.document),
		show_nav = topWindow.find("#min_title_list"),
		aLi = show_nav.find("li");
};
function creatIframe(href,titleName){
	var topWindow=$(window.parent.document),
		show_nav=topWindow.find('#min_title_list'),
		iframe_box=topWindow.find('#iframe_box'),
		iframeBox=iframe_box.find('.show_iframe'),
		$tabNav = topWindow.find(".acrossTab"),
		$tabNavWp = topWindow.find(".Hui-tabNav-wp"),
		$tabNavmore =topWindow.find(".Hui-tabNav-more");
	var taballwidth=0;
		
	show_nav.find('li').removeClass("active");	
	show_nav.append('<li class="active"><span data-href="'+href+'">'+titleName+'</span><i></i><em></em></li>');	
	var $tabNavitem = topWindow.find(".acrossTab li");
	if (!$tabNav[0]){return}
	$tabNavitem.each(function(index, element) {
        taballwidth+=Number(parseFloat($(this).width()+60))
    });
	$tabNav.width(taballwidth+25);
	var w = $tabNavWp.width();
	if(taballwidth+25>w){
		$tabNavmore.show()}
	else{
		$tabNavmore.hide();
		$tabNav.css({left:0})
	}
	
	iframeBox.hide();
	iframe_box.append('<div class="show_iframe"><div class="loading"></div><iframe frameborder="0" src='+href+'></iframe></div>');
	var showBox=iframe_box.find('.show_iframe:visible');
	showBox.find('iframe').load(function(){
		showBox.find('.loading').hide();
	});
}
function removeIframe(){
	var topWindow = $(window.parent.document),
		iframe = topWindow.find('#iframe_box .show_iframe'),
		tab = topWindow.find(".acrossTab li"),
		showTab = topWindow.find(".acrossTab li.active"),
		showBox=topWindow.find('.show_iframe:visible'),
		i = showTab.index();
	tab.eq(i-1).addClass("active");
	tab.eq(i).remove();
	iframe.eq(i-1).show();	
	iframe.eq(i).remove();
}

$(function(){

	getskincookie();
	
	
	/*左侧菜单树结构点击触发调用*/
	$.Huifold(".menu_dropdown dl dt",".menu_dropdown dl dd","fast",1,"click");
	
	/*选项卡tab导航*/
	$(".Hui-aside").on("click",".menu_dropdown a",function(){
		Hui_admin_tab(this);
	});
	
	$(document).on("click","#min_title_list li",function(){
		var bStopIndex=$(this).index();
		var iframe_box=$("#iframe_box");
		$("#min_title_list li").removeClass("active").eq(bStopIndex).addClass("active");
		iframe_box.find(".show_iframe").hide().eq(bStopIndex).show();
		//tab点击刷新frime
		//var href = iframe_box.find(".show_iframe").eq(bStopIndex).show().find("iframe").attr("src");
		//iframe_box.find(".show_iframe").eq(bStopIndex).find("iframe").attr("src",href);
		
	});
	$(document).on("click","#min_title_list li i",function(){
		var aCloseIndex=$(this).parents("li").index();
		$(this).parent().remove();
		$('#iframe_box').find('.show_iframe').eq(aCloseIndex).remove();	
		num==0?num=0:num--;
		tabNavallwidth();
	});
	$(document).on("dblclick","#min_title_list li",function(){
		var aCloseIndex=$(this).index();
		var iframe_box=$("#iframe_box");
		if(aCloseIndex>0){
			$(this).remove();
			$('#iframe_box').find('.show_iframe').eq(aCloseIndex).remove();	
			num==0?num=0:num--;
			$("#min_title_list li").removeClass("active").eq(aCloseIndex-1).addClass("active");
			iframe_box.find(".show_iframe").hide().eq(aCloseIndex-1).show();
			tabNavallwidth();
		}else{
			return false;
		}
	});
	tabNavallwidth();
	
	$('#js-tabNav-next').click(function(){
		num==oUl.find('li').length-1?num=oUl.find('li').length-1:num++;
		toNavPos();
	});
	$('#js-tabNav-prev').click(function(){
		num==0?num=0:num--;
		toNavPos();
	});
	
	function toNavPos(){
		oUl.stop().animate({'left':-num*100},100);
	}
	
	/*换肤*/
	//$("#Hui-skin .dropDown-menu a").click(function(){
		//var v = $(this).attr("data-val");
		//setCookie("Huiskin", v);
		//var hrefStr=$("#skin").attr("href");
		//var hrefRes=hrefStr.substring(0,hrefStr.lastIndexOf('skin/'))+'skin/'+v+'/skin.css';

		//外面的js皮肤
		//$(window.frames.document).contents().find("#skin").attr("href",hrefRes);
		//$("#skin").attr("href",hrefResd);
		//alert($(window.frames.document).contents().find("#skin").attr("href"))
		
	//});
}); 
