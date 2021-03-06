/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */
package org.apache.struts2.views.freemarker.tags;

import com.jivesoftware.community.lifecycle.JiveApplication;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.URL;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Extension of Struts2 URL tag.<br>
 * Method getBean return spring bean with name 'hfURLComponent' if it's defined.<br>
 * Otherwise standard Struts2 {@link URL} component is returned.
 * <p/>
 * hfURLComponent bean must be child of {@link Component} class
 *
 * @author <a href="mailto:lkrzyzan@redhat.com">Libor Krzyzanek</a>
 * @see URL
 */
public class URLModel extends TagModel {

	private static final Log log = LogFactory.getLog(URLModel.class);

	public URLModel(ValueStack stack, HttpServletRequest req,
					HttpServletResponse res) {
		super(stack, req, res);
		log.trace("New URLModel called."
				+ " Bean will be retrieved from spring configuration."
				+ " Bean name: hfURLComponent");
	}

	protected Component getBean() {
		try {
			Object bean = null;
			ApplicationContext applicationContext = (ApplicationContext) JiveApplication.getContext();
			if (applicationContext != null) {
				bean = applicationContext.getBean("hfURLComponent", new Object[]{stack, req, res});
			}

			if (bean != null && bean instanceof Component) {
				log.debug("hfURLComponent bean founded");
				return (Component) bean;
			}
		} catch (NoSuchBeanDefinitionException e) {
			// bean not found - do not log exception, it's not needed.
		} catch (Exception e) {
			log.error("Some unknown error occur during instantiation of hfURLComponent component", e);
		}
		log.debug("hfURLComponent bean not found. Standard URL component is used");
		return new URL(stack, req, res);
	}

}
