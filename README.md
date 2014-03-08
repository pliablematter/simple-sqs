#Simple SQS

Java class that can be used to send messages to AWS (Amazon Web Services) SQS (Simple Queue Service) without installing the full AWS SDK.

Originally created to send messages from Google App Engine to SQS, but can be used from any Java application.


## Usage
* Depends on commons-codec and commons-io. Copy those jars to your classpath.
* Either copy the com.pliablematter.SimpleSQS class into your project, or create a jar with mvn:install, then copy the jar from target/ into your project.
* Once that's done, you can send a message with:

```
SimpleSQS sqs = new SimpleSQS("ABCDEF", "123456", "region");
sqs.send("7890/queue", "{'hello':'world'}");

```
Where:
* ABCDEF is your AWS credentials key
* 123456 is your AWS credentials secret
* region is the AWS region (for example us-east-1)
* 7890/queue is the relative path to your SQS queue (everything after http://*.amazonaws.com/ in the URL)
* {'hello':'world'} is the message to send