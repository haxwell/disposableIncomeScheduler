package com.haxwell.disposableIncomeScheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minidev.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders.AbstractMenuItemHandlerProvider;
import com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders.MenuItemHandlerProviderCollection;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class Controller {

	private static Controller instance;
	private static MenuItemHandlerProviderCollection mihpc;
	
	private Controller() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/application-context.xml");
		
		mihpc = (MenuItemHandlerProviderCollection)ctx.getBean("MenuItemHandlerProviderCollection");
	}
	
	public static Controller getInstance() {
		if (instance == null)
			instance = new Controller();
		
		return instance;
	}
	
	public static List<MenuItemHandlerBean> getMenuItemHandlers() {
		List<MenuItemHandlerBean> rtn = new ArrayList<>();
		List<AbstractMenuItemHandlerProvider> providerList = mihpc.getList();

		Iterator<AbstractMenuItemHandlerProvider> iterator = providerList.iterator();
		
		while (iterator.hasNext()) {
			MenuItemHandlerBean bean = iterator.next().getMenuItemHandler();
			
			if (bean != null) {
				rtn.add(bean);
			}
		}
		
		return rtn;
	}
}
