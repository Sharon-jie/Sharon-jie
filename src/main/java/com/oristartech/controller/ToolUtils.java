package com.oristartech.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ToolUtils {
	public static HttpServletResponse getResponseType(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		return response;
	}

	public static void returnJson(String result, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=gbk");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}

	public static void setSuccess(JSONObject json, String jsonp, HttpServletResponse rs) {
		json.put("status", "success");
		String jsonpResult = jsonp + "(" + json.toString() + ")";
		try {
			ToolUtils.returnJson(jsonpResult, rs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setUnsuccess(JSONObject json, String jsonp, HttpServletResponse rs) {
		json.put("status", "fail");
		String jsonpResult = jsonp + "(" + json.toString() + ")";
		try {
			ToolUtils.returnJson(jsonpResult, rs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setUnData(JSONObject json, String jsonp, HttpServletResponse rs) {
		json.put("status", "2");
		json.put("msg", "没有相关数据");
		String jsonpResult = jsonp + "(" + json.toString() + ")";
		try {
			ToolUtils.returnJson(jsonpResult, rs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setLoginError(JSONObject json, String jsonp, HttpServletResponse rs) {
		json.put("status", "3");
		json.put("msg", "用户名或密码错误");
		String jsonpResult = jsonp + "(" + json.toString() + ")";
		try {
			ToolUtils.returnJson(jsonpResult, rs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setCustomReturn(JSONObject json, String jsonp, HttpServletResponse rs, String status,
			String msg) {
		json.put("status", status);
		json.put("msg", msg);
		String jsonpResult = jsonp + "(" + json.toString() + ")";
		try {
			ToolUtils.returnJson(jsonpResult, rs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}