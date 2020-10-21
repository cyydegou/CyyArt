/**
 * @description 类描述
 * @author "caiyy"
 * @date 2018年7月3日
 */
package cyyGroup.cyyArt.until;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 类描述
 * @author "caiyy"
 * @date 2018年7月3日
 */
public class HttpUntil {
	/**
	 * get请求，参数拼接在地址上
	 * 
	 * @param url
	 *            请求地址加参数
	 * @return 响应
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpUntil.class);

	public static String get(String url, String cookie, String authorization) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);

		if (cookie != null) {
			get.setHeader("Cookie", cookie);
		}

		if (authorization != null) {
			get.setHeader("Authorization", authorization);
		}

		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(get);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * get请求，参数放在map里
	 * 
	 * @param url
	 *            请求地址
	 * @param map
	 *            参数map
	 * @return 响应
	 */
	public static String getMap(String url, Map<String, String> map) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		CloseableHttpResponse response = null;
		try {
			URIBuilder builder = new URIBuilder(url);
			builder.setParameters(pairs);
			HttpGet get = new HttpGet(builder.build());
			response = httpClient.execute(get);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			}
			return result;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 发送post请求，参数用map接收
	 * 
	 * @param url
	 *            地址
	 * @param map
	 *            参数
	 * @return 返回值
	 */
	public static String postMap(String url, Map<String, String> map, String authorization) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);

		if (authorization != null) {
			post.setHeader("Authorization", authorization);
		}

		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		CloseableHttpResponse response = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
			response = httpClient.execute(post);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			} else if (response != null && response.getStatusLine().getStatusCode() == 400) {
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			} else if (response != null && response.getStatusLine().getStatusCode() == 401) {
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			} else if (response != null && response.getStatusLine().getStatusCode() == 404) {
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	/**
	 * post请求，参数为json字符串
	 * 
	 * @param url
	 *            请求地址
	 * @param jsonString
	 *            json字符串
	 * @return 响应
	 */
	public static String postJson(String url, String jsonString, String cookies, String authorization) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
		if (cookies != null) {
			post.setHeader("Cookie", cookies);
		}

		if (authorization != null) {
			post.setHeader("Authorization", authorization);
		}

		CloseableHttpResponse response = null;
		try {
			post.setEntity(new ByteArrayEntity(jsonString.getBytes("UTF-8")));
			response = httpClient.execute(post);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			} else if (response != null && response.getStatusLine().getStatusCode() == 400) {
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			} else if (response != null && response.getStatusLine().getStatusCode() == 401) {
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String entityToString(HttpEntity entity) throws IOException {
		String result = null;
		if (entity != null) {
			long lenth = entity.getContentLength();
			if (lenth != -1 && lenth < 2048) {
				result = EntityUtils.toString(entity, "UTF-8");
			} else {
				InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
				CharArrayBuffer buffer = new CharArrayBuffer(2048);
				char[] tmp = new char[1024];
				int l;
				while ((l = reader1.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
				result = buffer.toString();
			}
		}
		return result;
	}

	public static String getLocalhost(Integer type) throws Exception {
		// 获得本机的所有网络接口
		Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();

		while (nifs.hasMoreElements()) {
			NetworkInterface nif = nifs.nextElement();

			// 获得与该网络接口绑定的 IP 地址，一般只有一个
			Enumeration<InetAddress> addresses = nif.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();

				if (addr instanceof Inet4Address) { // 只关心 IPv4 地址
					// logger.info("网卡接口名称：" + nif.getName());
					// logger.info("网卡接口地址：" + addr.getHostAddress());
					if (type == 1) {
						if (addr.isSiteLocalAddress()) {
							return addr.getHostAddress();
						}
					} else if (type == 2) {
						if (addr.isLinkLocalAddress()) {
							return addr.getHostAddress();
						}
					}

					// System.out.println(addr.getHostAddress());
					// System.out.println(addr.isSiteLocalAddress());
					// System.out.println(addr.isAnyLocalAddress());
					// System.out.println(addr.isLinkLocalAddress());
					// System.out.println(addr.isLoopbackAddress());
					// System.out.println(addr.isMulticastAddress());
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String string = get("http://backend.baiqiujie.cn/backend/member/0331f6c9-9498-7569-0502-e52880769ae4/address",
		        null,
		        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxODYwNjk3Mjc2MiIsInNjb3BlIjpbInNlcnZpY2UiXSwiaWQiOiIwMzMxZjZjOS05NDk4LTc1NjktMDUwMi1lNTI4ODA3NjlhZTQiLCJleHAiOjE1NzcyNTQ3ODcsInR5cGUiOiJtZW1iZXIiLCJhdXRob3JpdGllcyI6WyIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiXSwianRpIjoiYzZjZGIyYTItMGEwNC00MTMwLTgxZjYtNTFlOTdjYzIzNjVhIiwiY2xpZW50X2lkIjoiaW5uZXIifQ.UhRMIIQX0iHyJ4t3FKEHAjHBiTp9ib6uu6IVQxHczpRdA5NeDstplLgj4E2vQ9shc1Upsui65Z5P5DlLOS4pjdJrgcfsKfPSimJdQ5q8N8WFpiOTtid8U7xmAHO8C6WAFTeUaN5M-__O6BdR-KilsCRWOFoJpwidOeoxwdcEken__iYNgbC-KcMYINeEas0i-UJP2QyKIM0I3qacKSV5reTADn6mRrSifJKxo65xZtnshu_mE1Pym-safALb5Q0u100eOk-RwhvSzgA3iIGsdvB45lFkasHu0Ok3fFcUQFDlsax9mUAe6DTnUsE16BfFydCuCcBhmCbyE4JJEdmJYQ");
		System.out.println(string);
	}

}
