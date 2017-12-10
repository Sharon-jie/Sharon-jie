package com.oristartech.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oristartech.common.JsonUtil;
import com.oristartech.pojo.Element;
import com.oristartech.pojo.Film;
import com.oristartech.pojo.ForSale_ticket;
import com.oristartech.pojo.ImageMessageVo;
import com.oristartech.pojo.SalesStatusImage;
import com.oristartech.pojo.Screen;
import com.oristartech.pojo.Seat;
import com.oristartech.pojo.SeatMessageVo;
import com.oristartech.pojo.SeatTypeImage;
import com.oristartech.pojo.Session;
import com.oristartech.pojo.SessionMessageVo;
import com.oristartech.pojo.TicketNum;
import com.oristartech.service.ConversionMessageProvide;
import com.oristartech.service.FilmMessageProvide;
import com.oristartech.service.FindScreenProvide;
import com.oristartech.service.FindSeatProvide;
import com.oristartech.service.FindSeatTypeProvide;
import com.oristartech.service.OrderProvide;
import com.oristartech.service.PrintTemplateProvider;
import com.oristartech.service.SessionMessageProvide;
import com.oristartech.service.TicketMessageProvider;

/**
 * @author xxj
 *
 */
@RestController
public class FilmController {

	@Reference(timeout=15000,loadbalance="roundrobin")
	private SessionMessageProvide sessionMessageProvide;

	@Reference(timeout=15000,loadbalance="roundrobin")
	private FilmMessageProvide filmMessageProvide;

	@Reference(timeout=15000,loadbalance="roundrobin")
	private TicketMessageProvider ticketMessageProvider;

	@Reference(timeout=15000,loadbalance="roundrobin")
	private FindScreenProvide screenProvider;

	@Reference(timeout=15000,loadbalance="roundrobin")
	private FindSeatProvide seatProvider;

	@Reference(timeout=15000,loadbalance="roundrobin")
	private FindSeatTypeProvide seatTypeProvider;

	@Reference(timeout=15000,loadbalance="roundrobin")
	private OrderProvide orderProvide;

	@Reference(timeout=15000,loadbalance="roundrobin")
	private PrintTemplateProvider printTemplateProvider;

	@Reference(timeout=15000,loadbalance="roundrobin")
	private ConversionMessageProvide conversionMessageProvide;

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 前端首页页面,返回当天影片信息
	 * 
	 * @param request
	 * @return 影片信息
	 */
//	@RequestMapping(value = "/filmTicket/filmCurrentMessage", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<String> queryFilm(HttpServletRequest request) {
//		System.out.println("进入当天影片信息查询。。。。");
//		String remoteHost = RpcContext.getContext().getRemoteHost();
//		int remotePort = RpcContext.getContext().getRemotePort();
//		System.out.println("服务地址 "+remoteHost+":"+remotePort);
//		Date date = new Date();
//		ResponseEntity<String> filmMessage = findFilm(date, request);
//		return filmMessage;
//	}
//
//	/**
//	 * 前端首页页面,返回指定日期影片信息
//	 * 
//	 * @param date
//	 * @param request
//	 * @return 影片信息
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/filmTicket/filmMessage/{time}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<String> queryFilms(@PathVariable String time, HttpServletRequest request) throws Exception {
//		System.out.println("进入指定日期影片信息查询。。。。");
//		String remoteHost = RpcContext.getContext().getRemoteHost();
//		int remotePort = RpcContext.getContext().getRemotePort();
//		System.out.println("服务地址 "+remoteHost+":"+remotePort);
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = formatter.parse(time);
//		ResponseEntity<String> filmMessage = findFilm(date, request);
//		return filmMessage;
//	}
//
//	/**
//	 * 首页刷新页面,显示场次信息
//	 * 
//	 * @param film_code
//	 * @param date
//	 * @param request
//	 * @return 场次信息
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/filmTicket/findSession/{film_code}/{date}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<String> findSession(@PathVariable String film_code, @PathVariable String date,
//			HttpServletRequest request) throws Exception {
//		System.out.println("进入场次信息查询。。。。");
//		String remoteHost = RpcContext.getContext().getRemoteHost();
//		int remotePort = RpcContext.getContext().getRemotePort();
//		System.out.println("服务地址 "+remoteHost+":"+remotePort);
//		Date date1 = null;
//		if ("1".equals(date)) {
//			date1 = new Date();
//		} else {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//			date1 = formatter.parse(date);
//		}
//		List<Session> session = sessionMessageProvide.queryByFilmCodeAndDate(film_code, date1);
//		List<String> sessionCode = new ArrayList<String>();
//		List<String> screenCode = new ArrayList<String>();
//		if (!(session.isEmpty())) {
//			for (Session session2 : session) {
//				sessionCode.add(session2.getSession_code());
//				screenCode.add(session2.getScreen_code());
//			}
//			List<SessionMessageVo> list = new ArrayList<SessionMessageVo>();
//			List<Screen> screenList = screenProvider.findScreenByScreenIdList(screenCode);
//			for (Session session2 : session) {
//				SessionMessageVo sessionMessage = new SessionMessageVo();
//				List<TicketNum> ticketNum = ticketMessageProvider.findForSaleTicketLeft(sessionCode);
//				if (!(screenList.isEmpty())) {
//					for (Screen screen2 : screenList) {
//						if (screen2 != null & screen2.getScreen_code().equals(session2.getScreen_code())) {
//							sessionMessage.setScreen_code(screen2.getScreen_code());
//							sessionMessage.setScreen_name(screen2.getScreen_name());
//						}
//					}
//				}
//				sessionMessage.setSession_code(session2.getSession_code());
//				sessionMessage.setFilm_version(session2.getVersion_code());
//				sessionMessage.setLanguage(session2.getLanguage_code());
//				sessionMessage.setPrice(session2.getPrice());
//				SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
//				String format1 = formatter1.format(session2.getBegin_time());
//				sessionMessage.setBegin_time(format1);
//				String format2 = formatter1.format(session2.getEnd_time());
//				sessionMessage.setEnd_time(format2);
//				if (!(ticketNum.isEmpty())) {
//					for (TicketNum ticketNum2 : ticketNum) {
//						if (ticketNum2.getSession_code().equals(session2.getSession_code())) {
//							sessionMessage.setTicket_left_num(ticketNum2.getTicket_left_num());
//							sessionMessage.setTicket_num(ticketNum2.getTicket_num());
//						}
//					}
//				}
//				list.add(sessionMessage);
//			}
//			ResponseEntity<String> entity = switchToJson(list, request);
//			return entity;
//		} else {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//		}
//	}
//
//	/**
//	 * 根据影院编码查找所有座位类型
//	 * 
//	 * @param request
//	 * @return 座位类型集合
//	 */
//	@RequestMapping(value = "/filmTicket/findAllSeatType/{cinema_code}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<String> findSeatType(@PathVariable String cinema_code, HttpServletRequest request) {
//		System.out.println("进入座位类型信息查询。。。。");
//		String remoteHost = RpcContext.getContext().getRemoteHost();
//		int remotePort = RpcContext.getContext().getRemotePort();
//		System.out.println("服务地址 "+remoteHost+":"+remotePort);
//		List<SeatTypeImage> seatType = seatTypeProvider.findSeatTypeByCinemaId(cinema_code);
//		ResponseEntity<String> entity = switchToJson(seatType, request);
//		return entity;
//
//	}
//
//	/**
//	 * 根据座位类型以及日期查询所有的影片信息
//	 * 
//	 * @param seat_type
//	 * @param date
//	 * @param request
//	 * @return 影片信息
//	 * @throws ParseException
//	 */
//	@RequestMapping(value = "/filmTicket/findFilmBySeat/{seat_type}/{date}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<String> findFilmBySeatType(@PathVariable String seat_type, @PathVariable String date,
//			HttpServletRequest request) throws ParseException {
//		System.out.println("进入根据座位类型以及日期查询所有的影片信息。。。。");
//		String remoteHost = RpcContext.getContext().getRemoteHost();
//		int remotePort = RpcContext.getContext().getRemotePort();
//		System.out.println("服务地址 "+remoteHost+":"+remotePort);
//		Date date1 = null;
//		if ("1".equals(date)) {
//			date1 = new Date();
//		} else {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//			date1 = formatter.parse(date);
//		}
//		List<String> filmCodeBySeatType = ticketMessageProvider.findFilmCodeBySeatType(seat_type, date1);
//		List<Film> film = filmMessageProvide.queryByFilmCodes(filmCodeBySeatType);
//		ResponseEntity<String> entity = switchToJson(film, request);
//		return entity;
//
//	}
//
//	/**
//	 * 根据影厅编码和场次编码查询所有的座位信息
//	 * 
//	 * @param screen_code
//	 * @param session_code
//	 * @param request
//	 * @return 座位信息
//	 */
//	@RequestMapping(value = "/filmTicket/findFilmBySeatType/{screen_code}/{session_code}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<String> findSeatMessage(@PathVariable String screen_code, @PathVariable String session_code,
//			HttpServletRequest request) {
//		System.out.println("进入根据影厅编码和场次编码查询所有的座位信息。。。。");
//		String remoteHost = RpcContext.getContext().getRemoteHost();
//		int remotePort = RpcContext.getContext().getRemotePort();
//		System.out.println("服务地址 "+remoteHost+":"+remotePort);
//		List<Seat> seat = seatProvider.findSeatByScreenId(screen_code);
//		List<ForSale_ticket> forSaleTicket = ticketMessageProvider.findForSaleTicket(session_code);
//		List<SeatMessageVo> seatMessage = new ArrayList<SeatMessageVo>();
//		if (!(forSaleTicket.isEmpty()) && !(seat.isEmpty())) {
//			for (Seat seat2 : seat) {
//				// 判断座位状态信息
//				// 是好坐
//				if ("1".equals(seat2.getSeat_state())) {
//					for (ForSale_ticket forSale_ticket : forSaleTicket) {
//						// 用来确定是该座位对应的票
//						if (forSale_ticket.getSeat_code().equals(seat2.getSeat_code())) {
//							SeatMessageVo seatMessageVo = new SeatMessageVo();
//							// 已售或者锁定
//							if (forSale_ticket.getSales_status().equals("s1")
//									|| forSale_ticket.getSales_status().equals("s2")) {
//								seatMessageVo.setSeat_code(forSale_ticket.getSeat_code());
//								seatMessageVo.setForSale_ticket_code(forSale_ticket.getForSale_ticket_code());
//								seatMessageVo.setSeat_x_axis(seat2.getSeat_x_axis());
//								seatMessageVo.setSeat_y_axis(seat2.getSeat_y_axis());
//								seatMessageVo.setStatusCode("s1");
//								seatMessageVo.setSeat_state(seat2.getSeat_state());
//								seatMessageVo.setSeat_group(seat2.getSeat_group_sign());
//								seatMessageVo.setSeat_name(seat2.getSeat_row() + "排" + seat2.getSeat_column() + "座");
//								seatMessage.add(seatMessageVo);
//								// 未售
//							} else {
//								seatMessageVo.setSeat_code(forSale_ticket.getSeat_code());
//								seatMessageVo.setForSale_ticket_code(forSale_ticket.getForSale_ticket_code());
//								seatMessageVo.setSeat_x_axis(seat2.getSeat_x_axis());
//								seatMessageVo.setSeat_y_axis(seat2.getSeat_y_axis());
//								seatMessageVo.setStatusCode(seat2.getSeat_type());
//								seatMessageVo.setSeat_state(seat2.getSeat_state());
//								seatMessageVo.setSeat_group(seat2.getSeat_group_sign());
//								seatMessageVo.setSeat_name(seat2.getSeat_row() + "排" + seat2.getSeat_column() + "座");
//								seatMessage.add(seatMessageVo);
//							}
//						}
//					}
//					// 坏座
//				} else if (seat2.getSeat_state().equals("0")) {
//					SeatMessageVo seatMessageVo = new SeatMessageVo();
//					seatMessageVo.setSeat_code(seat2.getSeat_code());
//					seatMessageVo.setForSale_ticket_code(null);
//					seatMessageVo.setSeat_x_axis(seat2.getSeat_x_axis());
//					seatMessageVo.setSeat_y_axis(seat2.getSeat_y_axis());
//					seatMessageVo.setStatusCode("s1");
//					seatMessageVo.setSeat_state(seat2.getSeat_state());
//					seatMessageVo.setSeat_group(seat2.getSeat_group_sign());
//					seatMessageVo.setSeat_name(null);
//					seatMessage.add(seatMessageVo);
//					// 空座
//				} else {
//					SeatMessageVo seatMessageVo = new SeatMessageVo();
//					seatMessageVo.setSeat_code(seat2.getSeat_code());
//					seatMessageVo.setForSale_ticket_code(null);
//					seatMessageVo.setSeat_x_axis(seat2.getSeat_x_axis());
//					seatMessageVo.setSeat_y_axis(seat2.getSeat_y_axis());
//					seatMessageVo.setStatusCode(seat2.getSeat_type());
//					seatMessageVo.setSeat_state(seat2.getSeat_state());
//					seatMessageVo.setSeat_group(seat2.getSeat_group_sign());
//					seatMessageVo.setSeat_name(null);
//					seatMessage.add(seatMessageVo);
//				}
//			}
//		} else {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//		}
//		ResponseEntity<String> entity = switchToJson(seatMessage, request);
//		return entity;
//	}

	/**
	 * 查询所有状态图片集合
	 * 
	 * @param request
	 * @return 状态图片集合
	 */
	@RequestMapping(value = "/filmTicket/findImage", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> findTicketStatus(HttpServletRequest request) {
		System.out.println("进入查询所有状态图片集合。。。。");
		String remoteHost = RpcContext.getContext().getRemoteHost();
		int remotePort = RpcContext.getContext().getRemotePort();
		System.out.println("服务地址 "+remoteHost+":"+remotePort);
		List<SalesStatusImage> statusImage = ticketMessageProvider.findSeatStatusImage();
		List<SeatTypeImage> seatType = seatTypeProvider.findSeatType();
		List<ImageMessageVo> imageList = new ArrayList<ImageMessageVo>();
		if (!(statusImage.isEmpty()) && !(seatType.isEmpty())) {
			for (SalesStatusImage salesStatusImage : statusImage) {
				ImageMessageVo imageMessageVo = new ImageMessageVo();
				imageMessageVo.setSales_status(salesStatusImage.getSales_status());
				imageMessageVo.setImage_url(salesStatusImage.getImage_url());
				imageList.add(imageMessageVo);
			}
			for (SeatTypeImage seatTypeImage : seatType) {
				ImageMessageVo imageMessageVo = new ImageMessageVo();
				imageMessageVo.setSales_status(seatTypeImage.getSeat_type());
				imageMessageVo.setImage_url(seatTypeImage.getImage_url());
				imageList.add(imageMessageVo);
			}
		}
		ResponseEntity<String> entity = switchToJson(imageList, request);
		return entity;

	}

	/**
	 * 找零时改变订单,待售影票状态.同时将待售影票数据存储到已售影票库中,并进行打印操作.
	 * 
	 * @param order_code
	 * @param forSale_ticket_code
	 * @param cinema_code
	 * @param convert_code
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
//	@RequestMapping(value = "/filmTicket/changeStatus",method=RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<String> changeStatus(HttpServletRequest request) throws JsonParseException,
//			JsonMappingException, InstantiationException, IllegalAccessException, IOException {
//		System.out.println("进入找零！！");
//		String remoteHost = RpcContext.getContext().getRemoteHost();
//		int remotePort = RpcContext.getContext().getRemotePort();
//		System.out.println("服务地址 "+remoteHost+":"+remotePort);
//		String parameter = request.getParameter("data");
//		System.out.println(parameter);
//		JSONArray jsonArray = JSON.parseArray(parameter);
//		Map<String, Object> map = new HashMap<>();
//		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
//			JSONObject job = (JSONObject) iterator.next();
//			map.put("ordercode", job.get("ordercode"));
//			map.put("forSale_ticket_code", job.get("forSale_ticket_code"));
//			map.put("cinema_code", job.get("cinema_code"));
//			map.put("convertcode", job.get("convertcode"));
//		}
//		String order_code =map.get("ordercode").toString();
//		System.out.println(order_code);
//		String forSale_ticket_code = (String) map.get("forSale_ticket_code");
//		List<String> forSaleTicketList = new ArrayList<>();
//		String[] arr = forSale_ticket_code.split(",");
//		for (int i = 0; i < arr.length; i++) {
//			if (arr[i] != null || arr[i].length() <= 0) {
//				forSaleTicketList.add(arr[i]);
//			}
//		}
//		System.out.println("forSaleTicketList=" + forSaleTicketList);
//		System.out.println(forSale_ticket_code);
//		try {
//			String cinema_code = (String) map.get("cinema_code");
//			System.out.println(cinema_code);
//			String convert_code = (String) map.get("convertcode");
//			System.out.println(convert_code);
//			int order_status = 1;
//			int orderStatus = orderProvide.updateOrderStatus(order_code, order_status);
//			if (200==orderStatus) {
//				String forSaleTicketSold = ticketMessageProvider.forSaleTicketSold(forSaleTicketList);
//				System.out.println("forSaleTicketSold="+forSaleTicketSold);
//				if ("success".equals(forSaleTicketSold)) {
//					if (convert_code!=null) {
//						int status = conversionMessageProvide.updateStatus(convert_code);
//					}
//					ResponseEntity<String> entity = switchToJson("success", request);
//					System.out.println("entity="+entity);
//					return entity;
//				} else {
//					return null;
//				}
//			} else {
//				return null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//	/**
//	 * 撤销操作时,订单状态改变,释放票
//	 * 
//	 * @param order_code
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/filmTicket/revocation",method=RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<String> revocation(HttpServletRequest request) {
//		System.out.println("进入撤销操作。。。。");
//		String remoteHost = RpcContext.getContext().getRemoteHost();
//		int remotePort = RpcContext.getContext().getRemotePort();
//		System.out.println("服务地址 "+remoteHost+":"+remotePort);
//			String data = request.getParameter("data");
//			String json = null;
//			String order_code = null;
//			String forSale_ticket_code = null;
//			Map<String, Object> forSaleTicketMap = null;
//			List<Map<String, Object>> forSaleTicketList = new ArrayList<>();
//			if (data == null || data.length() <= 0) {
//				return ResponseEntity.ok(json);
//			}
//			System.out.println("data=" + data);
//			JSONArray jsonArray = JSON.parseArray(data);
//			int length = jsonArray.size();
//			for (int i = 0; i < 1; i++) {
//				forSaleTicketMap = new HashMap<String, Object>(); 
//				order_code = jsonArray.getJSONObject(i).get("ordercode").toString();
//				forSale_ticket_code = jsonArray.getJSONObject(i).get("forSale_ticket_code").toString();
//				String[] arr = forSale_ticket_code.split(",");
//				for (int j = 0; j < arr.length; j++) {
//					forSaleTicketMap = new HashMap<>();
//					forSaleTicketMap.put("forSale_ticket_code",arr[j]);
//					forSaleTicketList.add(forSaleTicketMap);
//				}
//			}
//			System.out.println("forSaleTicketList=" + forSaleTicketList);
//			int order_status = 2;
//			int status = orderProvide.updateOrderStatus(order_code, order_status);
//			if(500==status){
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//			}
//			String statu = ticketMessageProvider.forSaleTicketRelease(forSaleTicketList);
//			System.out.println("statu="+statu);
//			ResponseEntity<String> entity = switchToJson("success", request);
//			System.out.println("entity="+entity);
//			return entity;
//	}

	/**
	 * 根据日期查询影片
	 * 
	 * @param date
	 * @param request
	 * @return 影片信息
	 */
	public ResponseEntity<String> findFilm(Date date, HttpServletRequest request) {
		if ("".equals(date)) {
			date = new Date();
		}
		List<String> filmCode = sessionMessageProvide.queryByDate(date);
		List<Film> film = filmMessageProvide.queryByFilmCodes(filmCode);
		ResponseEntity<String> entity = switchToJson(film, request);
		return entity;
	}

	/**
	 * 转jsonp/json
	 * 
	 * @param object
	 * @param request
	 * @return jsonp/json信息
	 */
	public ResponseEntity<String> switchToJson(Object object, HttpServletRequest request) {
		String string;
		try {
			string = mapper.writeValueAsString(object);
			// 1 获取callback参数
			String callback = request.getParameter("callback");
			// 2. 判断callback是否为空,
			if (callback != null) {
				// 如果不为空，表示请示使用的jsonp进行，我们就需要把返回的json数据进行包裹，使用callback
				String result = callback + "(" + string + ")";
				return ResponseEntity.ok(result);

			} else {
				// 如果为空，表示请求不是使用的jsonp，和原来一样，直接返回
				return ResponseEntity.ok(string);
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

	}
}
