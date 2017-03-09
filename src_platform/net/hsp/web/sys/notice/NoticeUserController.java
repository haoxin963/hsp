package net.hsp.web.sys.notice;

import net.hsp.common.WebLogUtil;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("noticeUserController")
@WebLogUtil(name = "推送")
@RequestMapping("/sys/notice/noticeUser")
@Lazy(true)
public class NoticeUserController
{
	
}
