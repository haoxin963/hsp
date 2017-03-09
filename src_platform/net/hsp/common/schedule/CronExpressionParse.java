package net.hsp.common.schedule;

import java.util.StringTokenizer;

import net.hsp.entity.sys.schedule.Schedule;

import org.apache.commons.lang.StringUtils;

/**
 * quartz 表达式构建&解析
 * 
 * @author caocy
 */
public class CronExpressionParse {
	private String cronExpression;

	private Schedule schedule;

	public CronExpressionParse(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public CronExpressionParse(Schedule schedule) {
		this.schedule = schedule;
	}

	public String getCronExpression() {
		if (null == cronExpression) {
			cronExpression = buildCronExpression();
		}

		return cronExpression;
	}

	/**
	 * 获取表达式概述（转化中文）
	 * 
	 * @return String
	 */
	public String getSummary() {
		// 判断是否合法表达式
		// if (CronExpression.isValidExpression(cronExpression)) {
		// return "";
		// }
		// 分割表达式
		String summary = "";
		StringTokenizer exprsTok = new StringTokenizer(cronExpression, " \t", false);
		String secondsExp = exprsTok.nextToken().trim();
		String minutesExp = exprsTok.nextToken().trim();
		String hoursExp = exprsTok.nextToken().trim();
		String daysOfMonthExp = exprsTok.nextToken().trim();
		String monthsExp = exprsTok.nextToken().trim();
		String daysOfWeekExp = exprsTok.nextToken().trim();
		if (exprsTok.hasMoreTokens()) {
			String year = exprsTok.nextToken().trim();
			summary += replaceNormalExpression(year, "年");
		}
		summary += replaceMonthExpression(monthsExp, "月");
		summary += replaceWeekExpression(daysOfWeekExp, "周");
		summary += replaceNormalExpression(daysOfMonthExp, "日");
		summary += replaceNormalExpression(hoursExp, "时");
		summary += replaceNormalExpression(minutesExp, "分");
		summary += replaceNormalExpression(secondsExp, "秒");
		summary += "执行";
		System.out.println(monthsExp);
		return summary;
	}

	/**
	 * 替换标准表达式
	 * 
	 * @return String
	 */
	public String replaceNormalExpression(String expression, String unit) {
		if ("?".equalsIgnoreCase(expression)) {
			return "";
		} else {
			return expression.replaceAll("(\\d+)-(\\d+)", "$1到$2之间").replaceAll("(\\d+)/", "$1开始/").replaceAll("/(\\d+)", "每隔$1").replaceAll("\\*", "每" + unit).replaceAll("(\\d+)", "$1" + unit) + " ";
		}
	}

	/**
	 * 替换月表达式
	 * 
	 * @return String
	 */
	public String replaceMonthExpression(String expression, String unit) {
		System.out.println("Month = " + expression);
		expression = expression.replaceAll("(1|JAN)", "一月").replaceAll("(2|FEB)", "二月").replaceAll("(3|MAR)", "三月").replaceAll("(4|APR)", "四月").replaceAll("(5|MAY)", "五月").replaceAll("(6|JUN)", "六月")
				.replaceAll("(7|JUL)", "七月").replaceAll("(8|AUG)", "八月").replaceAll("(9|SEP)", "九月").replaceAll("(10|OCT)", "十月").replaceAll("(11|NOV)", "十一月").replaceAll("(12|DEC)", "十二月");
		return replaceNormalExpression(expression, unit);
	}

	/**
	 * 替换日表达式
	 * 
	 * @return String
	 */
	public String replaceDayExpression(String expression, String unit) {
		System.out.println("Day = " + expression);
		expression = expression.replaceAll("L", "当月最后一天");
		return replaceNormalExpression(expression, unit);
	}

	/**
	 * 替换周表达式
	 * 
	 * @return String
	 */
	public String replaceWeekExpression(String expression, String unit) {
		System.out.println("Week = " + expression);
		expression = expression.replaceAll("([0-7])L", "当月最后一周的周$1").replaceAll("([1-7])#([1-5])", "当月第$2周的周$1").replaceAll("(^[1-7])", "周$1").replaceAll(",([1-7])", ",周$1").replaceAll("\\-([1-7])",
				",周$1").replaceAll("(周1|SUN)", "周日").replaceAll("(周2|MON)", "周一").replaceAll("(周3|TUES)", "周二").replaceAll("(周4|WED)", "周三").replaceAll("(周5|THUR)", "周四").replaceAll("(周6|FRI)", "周五")
				.replaceAll("(周7|SAT)", "周六");
		return replaceNormalExpression(expression, "");
	}

	/**
	 * 构建表达式
	 * 
	 * @return String
	 */
	private String buildCronExpression() {
		String expression = "";
		String secondExpression = bulidSMHCronExpression(schedule.getFromSecond(), schedule.getToSecond(), schedule.getIntervalSecond(), schedule.getSeconds());
		String minuteExpression = bulidSMHCronExpression(schedule.getFromMinute(), schedule.getToMinute(), schedule.getIntervalMinute(), schedule.getMinutes());
		String hourExpression = bulidSMHCronExpression(schedule.getFromHour(), schedule.getToHour(), schedule.getIntervalHour(), schedule.getHours());
		String dayExpression = bulidDayCronExpression(schedule.getFromDay(), schedule.getToDay(), schedule.getIntervalDay(), schedule.getDays(), schedule.getWeeks());
		String MonthExpression = bulidMonthCronExpression(schedule.getMonths());
		String weekExpression = bulidWeekCronExpression(schedule.getCheckedWeeks());
		String yearExpression = bulidYearCronExpression(schedule.getYears());

		if (!"0".equals(secondExpression) && "0".equals(minuteExpression)) {
			minuteExpression = "*";
		}
		if (!"0".equals(minuteExpression) && "0".equals(hourExpression)) {
			hourExpression = "*";
		}

		expression = secondExpression + " " + minuteExpression + " " + hourExpression + " " + dayExpression + " " + MonthExpression + " " + weekExpression + " " + yearExpression;
		return expression;
	}

	/**
	 * 构建时分秒表达式
	 * 
	 * @return String
	 */
	private String bulidSMHCronExpression(Integer from, Integer to, Integer interval, String checkeds) {
		String expression = "";
		if (null == from && null == to && null == interval) {
			if (null == checkeds) {
				return "0";
			} else {
				return checkeds;
			}
		}
		if (null == from && null == to && null != interval) {
			expression = "*/" + interval;
		} else {

			if (null == from)
				from = 0;
			expression += from;
			if (null != to) {
				expression += "-" + to;
			}
			if (null != interval) {
				expression += "/" + interval;
			}
		}
		if (null != checkeds) {
			expression += "," + checkeds;
		}
		return expression;
	}

	/**
	 * 构建日表达式
	 * 
	 * @return String
	 */
	public String bulidDayCronExpression(Integer from, Integer to, Integer interval, String checkeds, String weekCheckeds) {
		String expression = "";
		if (null == from && null == to && null == interval) {
			if (null == checkeds) {
				if (null == weekCheckeds) {
					return "*";
				} else {
					return "?";
				}
			} else {
				return checkeds;
			}
		}
		if (null == from)
			from = 0;
		expression += from;
		if (null != to) {
			expression += "-" + to;
		}
		if (null != interval) {
			expression += "/" + interval;
		}
		if (null != checkeds) {
			expression += "," + checkeds;
		}
		return expression;
	}

	/**
	 * 构建月表达式
	 * 
	 * @return String
	 */
	public String bulidMonthCronExpression(String checkeds) {
		String expression = "";
		if (null != checkeds) {
			expression += checkeds;
		} else {
			expression = "*";
		}
		return expression;
	}

	/**
	 * 构建周表达式
	 * 
	 * @return String
	 */
	public String bulidWeekCronExpression(String[] checkeds) {
		String expression = "";
		if (null != checkeds) {
			expression = "";
			for (int i = 0; i < checkeds.length; i++) {
				String checked = checkeds[i];
				if (StringUtils.isNumeric(checked)) {
					int numChecked = Integer.parseInt(checked);
					numChecked = numChecked == 7 ? 1 : numChecked + 1;
					if (i != 0) {
						expression += ",";
					}
					expression += numChecked;
				}
			}
		} else {
			expression = "?";
		}
		return expression;
	}

	/**
	 * 构建年表达式
	 * 
	 * @return String
	 */
	public String bulidYearCronExpression(String checkeds) {
		String expression = "";
		if (null != checkeds) {
			expression += checkeds;
		} else {
			expression = "*";
		}
		return expression;
	}
}
