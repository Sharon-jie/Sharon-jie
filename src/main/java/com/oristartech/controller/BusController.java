//package com.oristartech.controller;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
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
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.oristartech.pojo.Catalogue;
//import com.oristartech.pojo.Order;
//import com.oristartech.pojo.OrderDetail;
//import com.oristartech.service.CatalogueProvide;
//import com.oristartech.service.OrderProvide;
//import com.oristartech.service.TicketMessageProvider;
//
//
//@RestController
//@RequestMapping(value = "/busController")
//public class BusController {
//
//	@Reference(timeout = 15000)
//	private CatalogueProvide catalogueProvide;
//
//	@Reference(timeout = 15000)
//	private OrderProvide orderProvide;
//
//	@Reference(timeout = 15000)
//	private TicketMessageProvider ticketMsgProvider;
//
//	private ObjectMapper mapper = new ObjectMapper();
//
//	@RequestMapping(value = "/queryCatalogue/{cinema_code}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<String> queryCatalogueInfo(@PathVariable(value = "cinema_code") String cinema_code,
//			HttpServletRequest request) {
//		String json = "";
//		String callback = "";
//		System.out.println("进入了controller");
//		try {
//			List<Catalogue> catalogueList = catalogueProvide.queryCatalogue(cinema_code);
//			json = mapper.writeValueAsString(catalogueList);
//			callback = request.getParameter("callback");
//			if (callback != null) {
//				// 如果不为空，表示请示使用的jsonp进行，我们就需要把返回的json数据进行包裹，使用callback
//				String result = callback + "(" + json + ")";
//				return ResponseEntity.ok(result);
//			} else {
//				// 如果为空，表示请求不是使用的jsonp，和原来一样，直接返回
//				return ResponseEntity.ok(json);
//			}
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//	}
//
//	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<String> createOrder(HttpServletRequest request, HttpServletResponse reponse)
//			throws Exception {
//		System.out.println("进入了生成订单的controller");
//		String json = null;
//		String callback = null;
//		int order_code = 0;
//		String list = request.getParameter("orderArr");
//		if (list == null || list.length() <= 0) {
//			return ResponseEntity.ok(json);
//		}
//		System.out.println("list=" + list);
//		List<OrderDetail> orderDetailList = JSON.parseArray(list, OrderDetail.class);
//		order_code = orderProvide.createOrder(orderDetailList);
//		System.out.println("order_code="+order_code);
//		if (order_code == 500) {
//			return ResponseEntity.ok(json);
//		}
//		json = mapper.writeValueAsString(order_code);
//		callback = request.getParameter("callback");
//		if (callback != null) {
//			// 如果不为空，表示请示使用的jsonp进行，我们就需要把返回的json数据进行包裹，使用callback
//			String result = callback + "(" + json + ")";
//			return ResponseEntity.ok(result);
//		} else {
//			// 如果为空，表示请求不是使用的jsonp，和原来一样，直接返回
//			return ResponseEntity.ok(json);
//		}
//	}
//
//	@RequestMapping(value = "/queryOrderByCode/{convert_code}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<String> queryOrderByCode(@PathVariable("convert_code") String convert_code,
//			HttpServletRequest request) {
//		String json = "";
//		String result = "";
//		try {
//			System.out.println("进入查询订单的controller");
//			System.out.println("convert_code="+convert_code);
//			List<Order> orderList = orderProvide.queryOrderByCode(convert_code);
//			String callback = request.getParameter("callback");
//			json = mapper.writeValueAsString(orderList);
//			if (callback != null) {
//				// 如果不为空，表示请示使用的jsonp进行，我们就需要把返回的json数据进行包裹，使用callback
//				result = callback + "(" + json + ")";
//				return ResponseEntity.ok(result);
//			} else {
//				// 如果为空，表示请求不是使用的jsonp，和原来一样，直接返回
//				return ResponseEntity.ok(json);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//	}
//
//	@RequestMapping(value = "/updateOrderStatus/{order_code}/{order_status}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<String> updateOrderStatus(@PathVariable("order_code") String order_code,
//			@PathVariable("order_status") int order_status, HttpServletRequest request) {
//
//		String json = "";
//		String result = "";
//		try {
//			int status = orderProvide.updateOrderStatus(order_code, order_status);
//			String callback = request.getParameter("callback");
//			json = mapper.writeValueAsString(status);
//			if (callback != null) {
//				// 如果不为空，表示请示使用的jsonp进行，我们就需要把返回的json数据进行包裹，使用callback
//				result = callback + "(" + json + ")";
//				return ResponseEntity.ok(result);
//			} else {
//				// 如果为空，表示请求不是使用的jsonp，和原来一样，直接返回
//				return ResponseEntity.ok(json);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//	}
//
//	@RequestMapping(value = "/balance", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<String> orderBalance(HttpServletRequest request, HttpServletResponse response) {
//		System.out.println("进入了结算的controller");
//		String json = "";
//		Map<String, Object> forSaleTicketMap = null;
//		List<Map<String, Object>> forSaleTicketList = new ArrayList<>();
//		try {
//			String data = request.getParameter("data");
//			if (data == null || data.length() <= 0) {
//				return ResponseEntity.ok(json);
//			}
//			System.out.println("data=" + data);
//			String[] arr = data.split(",");
//			int length = arr.length;
//			for (int i = 0; i < length; i++) {
//				if (arr[i] != null || arr[i].length() <= 0) {
//					forSaleTicketMap = new HashMap<String, Object>();
//					forSaleTicketMap.put("forSale_ticket_code", arr[i]);
//					forSaleTicketList.add(forSaleTicketMap);
//				}
//			}
//			System.out.println("forSaleTicketList=" + forSaleTicketList);
//			String status = ticketMsgProvider.forSaleTicketLock(forSaleTicketList);
//			System.out.println("status=" + status);
//			String callback = request.getParameter("callback");
//			json = mapper.writeValueAsString(status);
//			if (callback != null) {
//				// 如果不为空，表示请示使用的jsonp进行，我们就需要把返回的json数据进行包裹，使用callback
//				String result = callback + "(" + json + ")";
//				return ResponseEntity.ok(result);
//			} else {
//				// 如果为空，表示请求不是使用的jsonp，和原来一样，直接返回
//				return ResponseEntity.ok(json);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//	}
//
//	@RequestMapping(value = "/clear", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<String> forSaleTicketRelease(HttpServletRequest request, HttpServletResponse response) {
//		System.out.println("进入了清零的controller");
//		String json = "";
//		Map<String, Object> forSaleTicketMap = null;
//		List<Map<String, Object>> forSaleTicketList = new ArrayList<>();
//		try {
//			String data = request.getParameter("data");
//			if (data == null || data.length() <= 0) {
//				return ResponseEntity.ok(json);
//			}
//			System.out.println("data=" + data);
//			String[] arr = data.split(",");
//			int length = arr.length;
//			for (int i = 0; i < length; i++) {
//				if (arr[i] != null) {
//					forSaleTicketMap = new HashMap<String, Object>();
//					forSaleTicketMap.put("forSale_ticket_code", arr[i]);
//					forSaleTicketList.add(forSaleTicketMap);
//				}
//			}
//			System.out.println("forSaleTicketList=" + forSaleTicketList);
//			String status = ticketMsgProvider.forSaleTicketRelease(forSaleTicketList);
//			System.out.println("status=" + status);
//			String callback = request.getParameter("callback");
//			json = mapper.writeValueAsString(status);
//			if (callback != null) {
//				// 如果不为空，表示请示使用的jsonp进行，我们就需要把返回的json数据进行包裹，使用callback
//				String result = callback + "(" + json + ")";
//				return ResponseEntity.ok(result);
//			} else {
//				// 如果为空，表示请求不是使用的jsonp，和原来一样，直接返回
//				return ResponseEntity.ok(json);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//	}
//}
