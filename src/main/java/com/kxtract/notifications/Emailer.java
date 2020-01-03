package com.kxtract.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest; 

public class Emailer {
private static final Logger logger = LoggerFactory.getLogger(Emailer.class);
  // This address must be verified with Amazon SES.
  private static final String FROM = "frank@frankkelly.us";
  
  private static String createHtmlBody(String textBody) {
	  return"<h1>A New Podcast Episode is available!</h1>"
		      + "<p>" + textBody + "</p>"
		      + "Visit <a href='https://demo.kxtract.com/'>KXtract</a>";
  }
  
	public static void sendNotificationEmail(String emailRecipient, String emailSubject, String textBody) {

		// TODO: Refactor to remove region
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.build();

		String htmlBody = createHtmlBody(textBody);
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(emailRecipient))
				.withMessage(new Message()
								.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody))
								.withText(new Content().withCharset("UTF-8").withData(textBody)))
						.withSubject(new Content().withCharset("UTF-8").withData(emailSubject)))
				.withSource(FROM);

		client.sendEmail(request);
		logger.info("Email sent!");
	}
}
