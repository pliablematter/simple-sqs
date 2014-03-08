package com.pliablematter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TreeMap;
import static java.lang.String.format;


public class SimpleSQS {
	
	private final String accessKey;
	private final String secretKey;
	private final String region;

	public SimpleSQS(String accessKey, String secretKey, String region) {

		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.region = region;
	}

	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyyMMdd'T'HH:mm:ss");

	static {

		SimpleTimeZone timezone = new SimpleTimeZone(0, "UTC");

		DATE_TIME_FORMAT.setTimeZone(timezone);
	}

	public String send(final String queueName, final String messageBody)
			throws IOException {

		String baseUrl = format("https://sqs.%s.amazonaws.com/%s", region,
				queueName);

		StringBuilder stringToSign = new StringBuilder("GET\n");

		stringToSign.append(format("sqs.%s.amazonaws.com\n", region));

		stringToSign.append(format("/%s\n", queueName));

		final Map<String, String> args = new TreeMap<String, String>() {

			private static final long serialVersionUID = -6690441002938936185L;

			{
				put("Action", "SendMessage");
				put("MessageBody", messageBody);
				put("AWSAccessKeyId", accessKey);
				put("Version", "2012-11-05");
				put("SignatureMethod", "HmacSHA256");
				put("SignatureVersion", "2");
				put("Expires", DATE_TIME_FORMAT.format(new Date()));
			}
		};

		StringBuilder queryArgs = new StringBuilder();

		int index = 0;
		for (Map.Entry<String, String> pair : args.entrySet()) {
			if (index > 0)
				queryArgs.append("&");

			queryArgs.append(pair.getKey()).append("=")
					.append(encode(pair.getValue()));

			index++;
		}

		stringToSign.append(queryArgs);

		queryArgs.append("&Signature=").append(
				getSignature(stringToSign.toString()));

		return IOUtils.toString(new URL(format("%s?%s", baseUrl,
				queryArgs.toString())));
	}

	private String getSignature(String s) {

		try {
			SecretKeySpec spec = new SecretKeySpec(secretKey.getBytes("UTF-8"),
					"HmacSHA256");

			Mac mac = Mac.getInstance("HmacSHA256");

			mac.init(spec);

			byte[] rawHmac = mac.doFinal(s.getBytes("UTF-8"));

			return encode(new String(Base64.encodeBase64(rawHmac)));
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	public String encode(String s) {

		try {
			return URLEncoder.encode(s, "UTF-8").replaceAll("\\Q+\\E", "%20"); 
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}
}
