package com.haxwell.disposableIncomeScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.textDisplayedBeforeAndAfterMenuBeans.AbstractBeforeAndAfterMenuBean;
import com.haxwell.disposableIncomeScheduler.beans.textDisplayedBeforeAndAfterMenuBeans.TextDisplayedBeforeAndAfterMenuBeansCollection;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class Processor {

	private Println println;
	private InputGetter inputGetter;
	
	private ApplicationContext ctx;
	private Map<String, AbstractBeforeAndAfterMenuBean> beforeAndAfterMenuBeanMap;
	
	public Processor() {
		beforeAndAfterMenuBeanMap = initBeforeAndAfterMenuBeanMap();		
	}

	public boolean process() {
		JSONObject data = DataAndStateSingleton.getInstance().getData();
		JSONObject state = DataAndStateSingleton.getInstance().getState();

		Map<Integer, MenuItemHandlerBean> map = new HashMap<>();
		boolean changesMade = false;
		
		MenuItemHandlerBean selection;
		
		do {
			List<MenuItemHandlerBean> list = Controller.getInstance().getMenuItemHandlers();
			
			map.clear();
			
			int index = 0;
			for (MenuItemHandlerBean child : list) {
				map.put(++index, child);
			}

			displayMenu(map);
			
			selection = getMenuSelection(map);
			
			if (selection != null) {
				if (selection.doIt(data, state))
					changesMade = true;
			}
		
		} while (selection != null);
		
		return changesMade;		
	}
	
	private void displayMenu(Map<Integer, MenuItemHandlerBean> map) {
		JSONObject state = DataAndStateSingleton.getInstance().getState();
		
		int index = 0;

		getPrintln().println();

		AbstractBeforeAndAfterMenuBean bean = getBeforeAndAfterMenuBean();

		getPrintln().println(bean.getBeforeText());
		
		while (map.containsKey(++index)) {
			getPrintln().println(index + ". " + ((MenuItemHandlerBean)map.get(index)).getMenuText());
		}
		
		getPrintln().println();
		getPrintln().println(bean.getAfterText());
	}
	
	private MenuItemHandlerBean getMenuSelection(Map<Integer, MenuItemHandlerBean> map) {
		MenuItemHandlerBean rtn = null;
		String input = "-";
		
		InputGetter inputGetter = getInputGetter();
		
		while (rtn == null && !input.equals("quit")) {
			try {
				input = inputGetter.readInput();
				int i = Integer.parseInt(input);
				rtn = map.get(i);

				if (rtn == null)
					getPrintln().println("\nDidn't find a menu item handler " + i + ".");
			}
			catch (NumberFormatException nfe) {
				if (!input.equals("quit"))
					getPrintln().println("\nNot a number. Try again. Type 'quit' to exit.\n");
			}
		}
		
		return rtn;
	}
	
	private Map<String, AbstractBeforeAndAfterMenuBean> initBeforeAndAfterMenuBeanMap() {
		ctx = new ClassPathXmlApplicationContext("/application-context.xml");
		TextDisplayedBeforeAndAfterMenuBeansCollection coll = (TextDisplayedBeforeAndAfterMenuBeansCollection)ctx.getBean("TextDisplayedBeforeAndAfterMenuBean");
		
		Map<String, AbstractBeforeAndAfterMenuBean> map = new HashMap<>();
		
		for (AbstractBeforeAndAfterMenuBean bean : coll.getList()) {
			map.put(bean.getAssociatedMenuFocusState(), bean);
		}
		
		return map;
	}
	
	private AbstractBeforeAndAfterMenuBean getBeforeAndAfterMenuBean() {
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		
		JSONObject state = dass.getState();
		
		Object obj = state.get(Constants.MENU_FOCUS);
		
		if (obj == null)
			obj = Constants.MAIN_LEVEL_MENU_FOCUS;
		
		return beforeAndAfterMenuBeanMap.get(obj.toString());
	}
	
	private InputGetter getInputGetter() {
		if (inputGetter == null)
			inputGetter = new InputGetter();
		
		return inputGetter;
	}
	
	public void setInputGetter(InputGetter ig) {
		inputGetter = ig;
	}
	
	private Println getPrintln() {
		if (println == null)
			println = new Println();
		
		return println;
	}
	
	public void setPrintln(Println p) {
		println = p;
	}
}
