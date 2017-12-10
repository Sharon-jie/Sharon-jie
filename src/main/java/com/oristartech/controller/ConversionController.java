//package com.oristartech.controller;
//
//import java.text.ParseException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.oristartech.pojo.Conversion;
//import com.oristartech.pojo.Film;
//import com.oristartech.pojo.Order;
//import com.oristartech.pojo.Session;
//import com.oristartech.service.ConversionMessageProvide;
//import com.oristartech.service.FilmMessageProvide;
//import com.oristartech.service.OrderProvide;
//import com.oristartech.service.SessionMessageProvide;
//
//@RestController
//public class ConversionController {
//	
//	@Reference(timeout=15000)
//	private SessionMessageProvide provide;
//	
//	@Reference(timeout=15000)
//	private FilmMessageProvide fmp;
//	
//	@Reference(timeout=15000)
//	private ConversionMessageProvide cmp;
//	
//	@Reference(timeout=15000)
//	private OrderProvide op;
//	
//	private ObjectMapper mapper = new ObjectMapper();
//	
//	@RequestMapping(value= "/convert/{convertCode}",method = RequestMethod.GET)
//	@ResponseBody
//		//首先根据兑换码查询兑换券信息
//	public ResponseEntity<String> convertMessage(@PathVariable String convertCode,HttpServletRequest request) throws ParseException {
//		Conversion convert = this.cmp.checkByConvertCode(convertCode);
//		Map<String,Object> map = new HashMap<>();
//		try {
//		//判断兑换券是否为空
//		if(convert == null){
//			//如果为空 说明兑换码为假
//			map.put("status", "1");
//			map.put("msg", "兑换券为假");
//		}else if(convert.getStatus() == 5){
//			//异常 状态码为5
//			map.put("status", "1");
//			map.put("msg", "网络异常，请稍后重试");
//		}else if(convert.getStatus() != 0){
//			//如果不为空 判断状态码 状态码为1  可使用 
//			if(convert.getStatus() == 1){
//				//如果状态码为1 判断兑换券类型
//				if(convert.getTicket_type() != null && "Q".equals(convert.getTicket_type())){
//					//如果为Q 为取票码 获取订单数据
//					map.put("status", "2");
//					//根据兑换码查询订单
//					 List<Order> order = this.op.queryOrderByCode(convertCode);
//					map.put("order", order);
//				}
//				//根据兑换码查询
//				if(convert.getTicket_type() != null && "D".equals(convert.getTicket_type())){
//					//如果状态码为D 判断film_code是否为空
//					if(convert.getFilm_code() == null){
//						//返回状态码为3 通兑
//						map.put("status", "3");
//					}else if(convert.getFilm_code() != null){
//						//film_code 不为空 根据影片编码和日期查询场次和影片信息
//						Film film = this.fmp.queryByFilmCode(convert.getFilm_code());
//						List<Session> sessions = this.provide.queryByFilmCodeAndDate(convert.getFilm_code(), new Date());
//						//返回状态码为4 根据指定影片选取场次
//						map.put("status", "4");
//						map.put("film", film);
//						map.put("session", sessions);
//					}
//				}
//			}else if(convert.getStatus() == 2){
//				map.put("status", "1");
//				map.put("msg", "已使用");
//			}else if(convert.getStatus() == 3){
//				map.put("status", "1");
//				map.put("msg", "已过期");
//			}else{
//				map.put("status", "1");
//				map.put("msg", "系统异常，请稍等！！！");
//			}
//		}
//					String json = mapper.writeValueAsString(map);
//					// 1 获取callback参数
//					String callback = request.getParameter("callback");
//	
//					// 2. 判断callback是否为空，
//					if (callback != null) {
//						// 如果不为空，表示请示使用的jsonp进行，我们就需要把返回的json数据进行包裹，使用callback
//						String result = callback + "(" + json + ")";
//						return ResponseEntity.ok(result);
//	
//					} else {
//						// 如果为空，表示请求不是使用的jsonp，和原来一样，直接返回
//						return ResponseEntity.ok(json);
//					}
//				} catch (JsonProcessingException e) {
//					map.put("status", "1");
//					map.put("msg", "网络异常，请稍后重试");
//					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map.toString());
//				}
//	}
//}
