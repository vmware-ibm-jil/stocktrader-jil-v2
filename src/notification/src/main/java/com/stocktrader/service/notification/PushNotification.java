package com.stocktrader.service.notification;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.ibm.mobilefirstplatform.serversdk.java.push.Message;
import com.ibm.mobilefirstplatform.serversdk.java.push.Notification;
import com.ibm.mobilefirstplatform.serversdk.java.push.PushNotifications;
import com.ibm.mobilefirstplatform.serversdk.java.push.PushNotificationsResponseListener;
import com.ibm.mobilefirstplatform.serversdk.java.push.Target;

@Component
@PropertySource(value = { "classpath:application.properties" })
public class PushNotification {

	@Value("${push.tenantId}")
	String appGuuid;
	@Value("${push.apiKeyId}")
	String apiKeyId;
	@Value("${push.bluemixRegion}")
	String bluemixRegionn;
	@Value("${push.tagName}")
	String tag;
	@Value("${push.url:www.ibm.com}")
	String url;
	private Target target;

	@PostConstruct
	public void init() {
		PushNotifications.initWithApiKey(appGuuid, apiKeyId, PushNotifications.US_EAST_REGION);
		target = new Target.Builder().tagNames(new String[] { tag }).build();
		System.out.print("Done PushNotifications initWithApiKey " + toString());
	}

	public void sendPushNotificationMessage(String body) {

		Message message = new Message.Builder().alert(body).url(url).build();

		Notification notification = new Notification.Builder().message(message).target(target).build();
		PushNotifications.send(notification, new PushNotificationsResponseListener() {
			@Override
			public void onSuccess(int statusCode, String responseBody) {
				System.out.println("Successfully sent push notification Status code: " + statusCode + " Response body: "
						+ responseBody);
			}

			@Override
			public void onFailure(Integer statusCode, String responseBody, Throwable t) {
				System.out.println("Failed sent push notification. Status code: " + statusCode + " Response body: "
						+ responseBody);
				if (t != null) {
					t.printStackTrace();
				}
			}
		});

	}

	@Override
	public String toString() {
		return "PushNotification [appGuuid=" + appGuuid + ", apiKeyId=" + apiKeyId + ", bluemixRegionn="
				+ bluemixRegionn + ", tag=" + tag + ", url=" + url + ", target=" + target + "]";
	}

}
