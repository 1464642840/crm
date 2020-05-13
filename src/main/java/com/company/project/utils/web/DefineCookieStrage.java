package com.company.project.utils.web;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.NetscapeDraftSpec;
import org.apache.http.message.BasicHeader;

import java.util.List;

public class DefineCookieStrage extends NetscapeDraftSpec {

	@Override
	public List<Cookie> parse(Header head, CookieOrigin origin)
			throws MalformedCookieException {

		String value = head.getValue();
		String prefix = "Expires=";
		if (value.contains("Expires=") && value.contains("GMT")) {
			String expires = value.substring(value.indexOf(prefix)
					+ prefix.length());
			expires = expires.substring(0, expires.indexOf(";"));
			value = value.replace(prefix + expires + ";", "");
		}

		head = new BasicHeader(head.getName(), value);
		return super.parse(head, origin);
	}

}
